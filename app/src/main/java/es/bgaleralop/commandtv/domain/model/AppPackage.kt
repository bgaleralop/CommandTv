package es.bgaleralop.commandtv.domain.model

data class AppPackage(
    val packageName: String,
    val isEnabled: Boolean,
    val isSystemApp: Boolean
)
