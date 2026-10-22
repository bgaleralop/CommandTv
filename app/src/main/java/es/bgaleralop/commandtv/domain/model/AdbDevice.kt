package es.bgaleralop.commandtv.domain.model

data class AdbDevice(
    val ip: String,
    val name: String, //Nombre del host
    val port: Int = 5555
)