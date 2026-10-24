package es.bgaleralop.commandtv.data.network

import android.content.Context
import android.net.wifi.WifiManager
import es.bgaleralop.commandtv.domain.model.AdbDevice
import es.bgaleralop.commandtv.domain.utils.DEFAULT_EMULATOR_PORT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Clase encargada de escanear toda la red buscando dispositivos a la escucha.
 * Realiza un escaneo por fuerza bruta sobre el puerto (por defecto 5555) y obtiene la ip y el nombre del target.
 */
class NetworkScanner(private val context: Context) {

    var port: Int = DEFAULT_EMULATOR_PORT
        private set
    var timeoutMs: Int = 150
        private set

    suspend fun scanLocalNetwork(): List<AdbDevice> = withContext(Dispatchers.IO) {
        val subnet = getLocalSubnet() ?: return@withContext emptyList()
        val activeDevices = mutableListOf<AdbDevice>()

        // Lanzamos 254 corrutinas en paralelo para escanear toda la subred de golpe
        val deferred = (1..254).map { i ->
            async {
                val ip = "$subnet$i"
                if (isAdbPortOpen(ip, port, timeoutMs)) {
                    AdbDevice(
                        ip = ip,
                        name = InetAddress.getByName(ip).hostName,
                        port = port
                    )
                } else {
                    null
                }
            }
        }
        // Esperamos a que terminen todas las peticiones y filtramos las válidas.
        activeDevices.addAll(deferred.awaitAll().filterNotNull())

        activeDevices
    }

    private fun getLocalSubnet(): String? {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipInt = wifiManager.connectionInfo.ipAddress

        if (ipInt == 0) return null

        //Convertimos la IP entera a formato legible por humanos
        val ipBytes = ByteArray(4) { (ipInt shr (it * 8) and 0xFF).toByte() }
        val ipString = InetAddress.getByAddress(ipBytes).hostAddress ?: return null

        // Extraemos los tres primeros octetos.
        val lastDotIndex = ipString.lastIndexOf('.')
        return if (lastDotIndex != -1) ipString.substring(0, lastDotIndex + 1) else null
    }

    private fun isAdbPortOpen(ip: String, port: Int, timeoutMs: Int): Boolean {
        return try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(ip, port), timeoutMs)
                true // El puerto ADB está abierto
            }
        } catch (e: Exception) {
            false // No hay host o el puerto esta cerrado
        }
    }

    fun setSearchPort(newPort: Int) {
        port = newPort
    }

    fun setTimeoutMs(timeout: Int) {
        timeoutMs = timeout
    }
}

