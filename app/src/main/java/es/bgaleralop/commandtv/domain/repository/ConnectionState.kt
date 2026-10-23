package es.bgaleralop.commandtv.domain.repository

sealed interface ConnectionState {
    object Disconnected : ConnectionState
    object Connecting : ConnectionState
    data class Connected(val ip: String) : ConnectionState
    data class Error(val message: String) : ConnectionState
}