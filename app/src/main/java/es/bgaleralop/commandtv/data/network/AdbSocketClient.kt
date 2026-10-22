package es.bgaleralop.commandtv.data.network

import es.bgaleralop.commandtv.data.crypto.AdbKeyManager
import es.bgaleralop.commandtv.domain.model.AdbCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Abre y gestiona el socket TCP hacia la IP y el puerto del televisor. Maneja la lectura y escritura
 * de flujos de bytes.
 */
class AdbSocketClient(
    private val keyManager: AdbKeyManager
) {
    private var socket: Socket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null

    /**
     * Realiza la conexión con el host objetivo
     *
     * @param ip La ip del host.
     * @param port El puerto en el que escucha. Por defecto 5555.
     *
     * @return true si la conexión es exitosa o false en caso contrario.
     */
    suspend fun connect(ip: String, port: Int = 5555): Boolean = withContext(Dispatchers.IO) {
        try {
            socket = Socket(ip, port).apply {
                soTimeout = 10000
            }
            outputStream = socket?.getOutputStream()
            inputStream = socket?.getInputStream()

            //1. Enviamos paquete de conexión inicial.
            sendConnectionPacket()

            //2. Leemos la respuesta del servidor (Loop de handshake)
            return@withContext handleHandshake()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Envía el paquete inicial de conexión.
     */
    private fun sendConnectionPacket() {
        val payload = "host::\u0000".toByteArray(Charsets.UTF_8)
        val packet = createAdbPacket(
            command = AdbCommand.CONNECT,
            arg0 = 0x01000000, // Version ADB
            arg1 = 1048576, // Max payload
            payload = payload
        )

        outputStream?.write(packet)
        outputStream?.flush()
    }

    /**
     * Maneja el handshake con el host.
     */
    private fun handleHandshake(): Boolean {
        val header = ByteArray(24)
        val bytesRead = inputStream?.read(header) ?: -1
        if (bytesRead < 24) return false

        val command = AdbCommand.valueOf(String(header.copyOfRange(0, 4), Charsets.UTF_8))
        val arg0 = ByteBuffer.wrap(header, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getInt()
        val dataLength = ByteBuffer.wrap(header, 12, 4).order(ByteOrder.LITTLE_ENDIAN).getInt()
        val payload = ByteArray(dataLength)
        if (dataLength > 0) {
            inputStream?.read(payload)
        }

        when (command) {
            AdbCommand.CONNECT -> return true // Hemos conectado directamente
            AdbCommand.AUTH -> {
                if (arg0 == 1) {
                    // El servidor pide firmar el token
                    val signature = keyManager.signToken(payload)
                    sendAuthSignature(signature)
                    //Volvemos a leeer para confirmar el CONNECT tras la firma.
                    return handleHandshake()
                }
            }
            else -> return false
        }
        return false
    }

    /**
     * Envía paquete con la firma del token si el host la solicita.
     */
    private fun sendAuthSignature(signature: ByteArray) {
        val packet = createAdbPacket(
            command = AdbCommand.AUTH,
            arg0 = 2,
            arg1 = 0,
            payload = signature
        )
        outputStream?.write(packet)
        outputStream?.flush()
    }

    /**
     * Crea el paquete para ser mandado.
     * @param command El comando que se quiere ejecutar
     * @param arg0 primer argumento del comando.
     * @param arg1 segundo argumento del comando.
     * @param payload La carga del mensaje que se envia.
     *
     * @return ByteArray con el paquete estructurado.
     */
    private fun createAdbPacket(
        command: AdbCommand,
        arg0: Int,
        arg1: Int,
        payload: ByteArray
    ): ByteArray {
        val cmdBytes = command.value.toByteArray(Charsets.UTF_8)
        val dataLength = payload.size
        val dataCheck = payload.sumOf { it.toInt() and 0XFF }

        // El campo magic es el complemento bit a bit (~x) del comando entero.
        val cmdInt = ByteBuffer.wrap(cmdBytes).order(ByteOrder.LITTLE_ENDIAN).getInt()
        val magic = cmdInt.inv()

        val buffer = ByteBuffer.allocate(24 + dataLength).order(ByteOrder.LITTLE_ENDIAN)
        buffer.put(cmdBytes)
        buffer.putInt(arg0)
        buffer.putInt(arg1)
        buffer.putInt(dataLength)
        buffer.putInt(dataCheck)
        buffer.putInt(magic)
        buffer.put(payload)

        return buffer.array()
    }
}