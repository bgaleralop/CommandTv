package es.bgaleralop.commandtv.domain.usecase

/**
 * Interfaz que recoge los distintos comandos que puede ejecutar el servicio.
 */
interface CommandTvServiceActions {
    /** Incrementa el volumen del dispositivo */
    suspend fun increaseVolume(): Result<Unit>
    /** Decrementa el volumen del dispositivo */
    suspend fun decreaseVolume(): Result<Unit>
    /** Mutea el dispositivo */
    suspend fun muted(): Result<Unit>
    /** Desmutea el dispositivo */
    suspend fun unmuted(): Result<Unit>
    /** Cambia el canal hacia arriba */
    suspend fun channelUp(): Result<Unit>
    /** Cambia el canal hacia abajo */
    suspend fun channelDown(): Result<Unit>
    /** Apaga el dispositivo */
    suspend fun powerOff(): Result<Unit>
    /** Enciende el dispositivo */
    suspend fun powerOn(): Result<Unit>
    /** Navega al menu principal */
    suspend fun goHome(): Result<Unit>
    /** Navega hacia atras */
    suspend fun goBack(): Result<Unit>
    /** Selecciona la opción */
    suspend fun select(): Result<Unit>
    /** Navega hacia arriba */
    suspend fun goUp(): Result<Unit>
    /** Navega hacia abajo */
    suspend fun goDown(): Result<Unit>
    /** Navega hacia la izquierda */
    suspend fun goLeft(): Result<Unit>
    /** Navega hacia la derecha */
    suspend fun goRight(): Result<Unit>

}