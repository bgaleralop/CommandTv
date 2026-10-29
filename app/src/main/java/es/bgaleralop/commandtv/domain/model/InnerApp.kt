package es.bgaleralop.commandtv.domain.model

data class InnerApp(
    val appName: String,
    val appPackage: String,
    var isEnabled: Boolean
)