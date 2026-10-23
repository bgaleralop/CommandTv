package es.bgaleralop.commandtv.data.repository

import es.bgaleralop.commandtv.data.network.AdbCommandManager
import es.bgaleralop.commandtv.data.network.AdbSocketClient
import es.bgaleralop.commandtv.domain.repository.AdbRepository
import es.bgaleralop.commandtv.domain.repository.ConnectionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class AdbRepositoryImpl(
    private val socketClient: AdbSocketClient
) : AdbRepository {

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private var socket: Socket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    private var commandManager: AdbCommandManager? = null

    /**
     * Conecta con el dispositivo.
     */
    override suspend fun connect(ip: String, port: Int): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                _connectionState.value = ConnectionState.Connecting

                socket = socketClient.connect(ip, port).getOrThrow()
                outputStream = socket?.getOutputStream()
                inputStream = socket?.getInputStream()

                commandManager = AdbCommandManager(inputStream!!, outputStream!!)
                _connectionState.value = ConnectionState.Connected(ip)
                Result.success(Unit)

            } catch (e: Exception) {
                disconnectInternal()
                _connectionState.value =
                    ConnectionState.Error(e.localizedMessage ?: "Error de conexión")
                Result.failure(e)
            }
        }

    override suspend fun disconnect() = withContext(Dispatchers.IO) {
        disconnectInternal()
        _connectionState.value = ConnectionState.Disconnected
    }

    override suspend fun executeShell(command: String): Result<String> =
        withContext(Dispatchers.IO) {
            if (commandManager == null)
                return@withContext Result.failure(IllegalStateException("Fallo durante la conexion"))

            try {
                val response = commandManager!!.executeShell(command)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Limpia los flujos y el socket tras las desconexión.
     */
    private fun disconnectInternal() {
        try {
            outputStream?.close()
            inputStream?.close()
            socket?.close()
        } catch (_: Exception) {
        } finally {
            outputStream = null
            inputStream = null
            socket = null
            commandManager = null
        }
    }

}