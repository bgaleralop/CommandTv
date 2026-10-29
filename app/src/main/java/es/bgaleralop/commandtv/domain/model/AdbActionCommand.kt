package es.bgaleralop.commandtv.domain.model

sealed class AdbActionCommand(val command: String) {
    data class DisablePacket(val packet: String) :
        AdbActionCommand("pm disable-user --user 0 $packet")

    data class EnablePacket(val packet: String) : AdbActionCommand("pm enable $packet")
    data class UninstallPacket(val packet: String) :
        AdbActionCommand("pm uninstall --user 0 $packet")

    data class LaunchApp(val packet: String) :
        AdbActionCommand("monkey -p $packet -c android.intent.category.LAUNCHER 1")

    object ListPackages : AdbActionCommand("pm list packages")
    object ListPackagesState : AdbActionCommand("pm list packages -f")
    object ListPackagesDisabled : AdbActionCommand("pm list packages -d")
    object ListThirdPartPackages : AdbActionCommand("pm list packages -3")
    object ListSystemPackages : AdbActionCommand("pm list packages -s")
}