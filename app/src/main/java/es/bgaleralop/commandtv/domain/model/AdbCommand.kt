package es.bgaleralop.commandtv.domain.model

enum class AdbCommand(val value: String) {
    CONNECT("CNXN"),
    AUTH("AUTH"),
    OPEN("OPEN"),
    WRITE("WRTE"),
    CLOSE("CLSE"),
    OK("OKAY")
}