package es.bgaleralop.commandtv.domain.model

import es.bgaleralop.commandtv.domain.utils.DEFAULT_EMULATOR_PORT

/**
 * Modelo de datos para representar un dispositivo ADB.
 */
data class AdbDevice(
    val ip: String,
    val name: String, //Nombre del host
    val port: Int = DEFAULT_EMULATOR_PORT
)