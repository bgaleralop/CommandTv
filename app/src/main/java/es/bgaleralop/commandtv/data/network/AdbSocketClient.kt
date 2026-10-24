package es.bgaleralop.commandtv.data.network

import es.bgaleralop.commandtv.data.crypto.AdbKeyManager
import es.bgaleralop.commandtv.data.network.AdbPacket.Companion.createAdbPacket
import es.bgaleralop.commandtv.domain.model.AdbCommand
import es.bgaleralop.commandtv.domain.utils.ADB_VERSION
import es.bgaleralop.commandtv.domain.utils.ARG0_LENGTH
import es.bgaleralop.commandtv.domain.utils.ARG0_OFFSET
import es.bgaleralop.commandtv.domain.utils.COMMAND_LENGTH
import es.bgaleralop.commandtv.domain.utils.DATA_LENGTH_LENGTH
import es.bgaleralop.commandtv.domain.utils.DATA_LENGTH_OFFSET
import es.bgaleralop.commandtv.domain.utils.DEFAULT_EMULATOR_PORT
import es.bgaleralop.commandtv.domain.utils.DEFAULT_TIMEOUT
import es.bgaleralop.commandtv.domain.utils.HEADER_PACKAGE_SIZE
import es.bgaleralop.commandtv.domain.utils.MAX_PAYLOAD
import es.bgaleralop.commandtv.domain.utils.SIGN_TOKEN_REQUESTED
import es.bgaleralop.commandtv.domain.utils.SIGN_TOKEN_RESPONSE
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
    var socket: Socket? = null
        private set
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
    suspend fun connect(ip: String, port: Int = DEFAULT_EMULATOR_PORT): Result<Socket> =
        withContext(Dispatchers.IO) {
        try {
            socket = Socket(ip, port).apply {
                soTimeout = DEFAULT_TIMEOUT
            }
            outputStream = socket?.getOutputStream()
            inputStream = socket?.getInputStream()

            //1. Enviamos paquete de conexión inicial.
            sendConnectionPacket()

            //2. Leemos la respuesta del servidor (Loop de handshake)
            if (handleHandshake()) {
                Result.success(socket!!)
            } else {
                Result.failure(Exception("Handshake failed"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.failure(e)
        }
    }

    /**
     * Envía el paquete inicial de conexión.
     */
    private fun sendConnectionPacket() {
        val payload = "host::\u0000".toByteArray(Charsets.UTF_8)
        val packet = createAdbPacket(
            command = AdbCommand.CONNECT,
            arg0 = ADB_VERSION, // Version ADB
            arg1 = MAX_PAYLOAD, // Max payload
            payload = payload
        )

        outputStream?.write(packet)
        outputStream?.flush()
    }

    /**
     * Maneja el handshake con el host.
     */
    private fun handleHandshake(): Boolean {
        val header = ByteArray(HEADER_PACKAGE_SIZE)
        val bytesRead = inputStream?.read(header) ?: -1
        if (bytesRead < HEADER_PACKAGE_SIZE) return false

        val command =
            AdbCommand.valueOf(String(header.copyOfRange(0, COMMAND_LENGTH), Charsets.UTF_8))
        val arg0 = ByteBuffer.wrap(header, ARG0_OFFSET, ARG0_LENGTH).order(ByteOrder.LITTLE_ENDIAN)
            .getInt()
        val dataLength = ByteBuffer.wrap(header, DATA_LENGTH_OFFSET, DATA_LENGTH_LENGTH)
            .order(ByteOrder.LITTLE_ENDIAN).getInt()
        val payload = ByteArray(dataLength)
        if (dataLength > 0) {
            inputStream?.read(payload)
        }

        when (command) {
            AdbCommand.CONNECT -> return true // Hemos conectado directamente
            AdbCommand.AUTH -> {
                if (arg0 == SIGN_TOKEN_REQUESTED) {
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
            arg0 = SIGN_TOKEN_RESPONSE,
            arg1 = 0,
            payload = signature
        )
        outputStream?.write(packet)
        outputStream?.flush()
    }
}