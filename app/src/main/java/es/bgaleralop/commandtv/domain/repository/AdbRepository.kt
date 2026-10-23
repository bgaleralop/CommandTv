package es.bgaleralop.commandtv.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface AdbRepository {
    /** Indica el estado de la conexión. */
    val connectionState: StateFlow<ConnectionState>

    /**
     * Maneja la conexión con el dispositivo.
     *
     * @param ip la ip del dispositivo objetivo.
     * @param port el puerto en el que escucha por defecto 5555
     *
     * @return resultado de la conexión.
     */
    suspend fun connect(ip: String, port: Int = 5555): Result<Unit>

    /**
     * Maneja la desconexión con el dispositivo.
     */
    suspend fun disconnect()

    /**
     * Maneja la ejecución de comandos en el dispositivo objetivo.
     *
     * @param command La cadena con el comando que se quiere ejecutar.
     *
     * @return La respuesta del dispositivo o fallo en caso de que no se pueda realizar.
     */
    suspend fun executeShell(command: String): Result<String>
}