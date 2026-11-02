package es.bgaleralop.commandtv.data.network.webos

import android.content.Context
import es.bgaleralop.commandtv.data.network.common.NetworkScanner
import es.bgaleralop.commandtv.domain.model.Device
import es.bgaleralop.commandtv.domain.model.DeviceOS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Scanner de red buscando dispositivos WebOS
 */
class WebOsScanner(private val context: Context) {
    private val scanner = NetworkScanner(context)

    suspend fun scan(port: Int): List<Device> = withContext(Dispatchers.IO) {
        scanner.setSearchPort(port)

        return@withContext scanner.scanLocalNetwork().map { it.copy(os = DeviceOS.WEBOS) }
    }

}