package es.bgaleralop.commandtv.domain.model

/**
 * Recoge los distintos valores para el control remoto.
 */
enum class AdbKeyCommand(val value: String) {
    HOME("input keyevent 3"),
    BACK("input keyevent 4"),
    ENTER("input keyevent 66"),
    OK("input keyevent 66"),

    //Cruceta de direcciones.
    UP("input keyevent 19"),
    DOWN("input keyevent 20"),
    LEFT("input keyevent 21"),
    RIGHT("input keyevent 22"),

    //Volumen
    VOL_UP("input keyevent 24"),
    VOL_DOWN("input keyevent 25"),
    MUTE("input keyevent 164"),

    //Multimedia
    PLAY("input keyevent 85"),
    PAUSE("input keyevent 85"),
    FORWARD("input keyevent 87"),
    BACKWARD("input keyevent 88")
}