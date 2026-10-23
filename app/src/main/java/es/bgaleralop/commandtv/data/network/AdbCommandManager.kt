package es.bgaleralop.commandtv.data.network

import es.bgaleralop.commandtv.data.network.AdbPacket.Companion.createAdbPacket
import es.bgaleralop.commandtv.domain.model.AdbCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

/**
 * Clase encargada de manejar el envío de comandos al dispositivo, escuchar el flujo de entrada y
 * devolverlo limpio cuando el canal se cierra.
 */
class AdbCommandManager(
    private val inputStream: InputStream,
    private val outputStream: OutputStream
) {
    /**
     * Ejecuta un comando shell de forma síncrona dentro de una corrutina y devuelve la salida
     * completa en texto (stdout/stderr combinado).
     */
    suspend fun executeShell(command: String, localId: Int = 1): String =
        withContext(Dispatchers.IO) {
            val outputBuilder = StringBuilder()

            //1. Enviar paquete OPEN con el comando shell
            sendOpenCommand(command, localId)

            //2. Bucle de lectura de respuesta del socet
            var remoteId = 0
            var channelOpen = true

            val headerBuffer = ByteArray(24)

            while (channelOpen) {
                val bytesRead = inputStream.read(headerBuffer)
                if (bytesRead < 24) break //Conexión interrumpida o final de stream.

                // Desempaquetar la cabecera ADB de 24 Bytes.
                val commandStr = String(headerBuffer, 0, 4, StandardCharsets.UTF_8)
                val arg0 =
                    ByteBuffer.wrap(headerBuffer, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getInt()
                val arg1 =
                    ByteBuffer.wrap(headerBuffer, 8, 4).order(ByteOrder.LITTLE_ENDIAN).getInt()
                val dataLength =
                    ByteBuffer.wrap(headerBuffer, 12, 4).order(ByteOrder.LITTLE_ENDIAN).getInt()

                // Leer el payload de datos si lo hay
                val payload = ByteArray(dataLength)
                if (dataLength > 0) {
                    inputStream.read(payload)
                }

                // Procesar según el tipo de trama que envía el dispositivo.
                when (AdbCommand.valueOf(commandStr)) {
                    AdbCommand.OK -> {
                        // El servidor acepta el canal o confirma recepción. Guardamos el remoteId si viene en arg0
                        remoteId = arg0
                    }

                    AdbCommand.WRITE -> {
                        //Tenemos datos de respuesta del terminal.
                        val text = String(payload, StandardCharsets.UTF_8)
                        outputBuilder.append(text)

                        // Debemos responder obligatoriamente con un OK para confirmar el WRTE
                        sendAckPacket(localId = arg1, remoteId = arg0)
                    }

                    AdbCommand.CLOSE -> {
                        // El comando ha terminado y el dispositivo cierra el canal
                        channelOpen = false
                        // Respondemos cerrando también por nuestra parte.
                        sendClosePacket(localId = arg1, remoteId = arg0)
                    }

                    else -> continue
                }
            }

            return@withContext outputBuilder.toString()
        }

    /**
     * Envia el comando al target con OPEN para abrir la conexión
     */
    private fun sendOpenCommand(command: String, localId: Int) {
        val payloadBytes = "shell:$command\u0000".toByteArray(StandardCharsets.UTF_8)
        val packet =
            createAdbPacket(AdbCommand.OPEN, arg0 = localId, arg1 = 0, payload = payloadBytes)
        outputStream.write(packet)
        outputStream.flush()
    }

    /**
     * Envía confirmacion de recibido al target
     */
    private fun sendAckPacket(localId: Int, remoteId: Int) {
        //Enviar OK en respuesta a WRTE
        val packet =
            createAdbPacket(AdbCommand.OK, arg0 = localId, arg1 = remoteId, payload = ByteArray(0))
        outputStream.write(packet)
        outputStream.flush()
    }

    /**
     * Envía comando de cierre de conexión al target
     */
    private fun sendClosePacket(localId: Int, remoteId: Int) {
        //Enviar CLOSE para cerrar definitivamente.
        val packet = createAdbPacket(
            command = AdbCommand.CLOSE,
            arg0 = localId,
            arg1 = remoteId,
            payload = ByteArray(0)
        )
        outputStream.write(packet)
        outputStream.flush()
    }
}