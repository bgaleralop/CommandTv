package es.bgaleralop.commandtv.data.network.adb

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import es.bgaleralop.commandtv.domain.model.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TvDiscoveryManager(context: Context) {

    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    private var discoveryListener: NsdManager.DiscoveryListener? = null

    // Tipo de servicio que emiten los dispositivos AndroidTV / ADB en la red local.
    private val SERVICE_TYPE_ADB = "_adb._tcp." // Podría ser también "_googlecast._tcp."
    private val SERVICE_TYPE_CAST = "_googlecast._tcp."

    suspend fun startDiscovery(onDeviceFound: (Device) -> Unit) = withContext(Dispatchers.IO) {
        discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(serviceType: String?) {}

            override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
                // Cuando se encuentra un servicio, necesitamos resolver sus datos (IP y puerto)
                nsdManager.resolveService(serviceInfo, object : NsdManager.ResolveListener {
                    override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
                        val ip = serviceInfo?.host?.hostAddress ?: return
                        val name = serviceInfo.serviceName // Obtenemos el nombre del target

                        onDeviceFound(Device(ip = ip, name = name, port = serviceInfo.port))
                    }

                    override fun onResolveFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {}
                })
            }

            override fun onDiscoveryStopped(serviceType: String?) {}
            override fun onServiceLost(serviceInfo: NsdServiceInfo?) {}
            override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {}
            override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {}
        }

        nsdManager.discoverServices(SERVICE_TYPE_ADB, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
        nsdManager.discoverServices(
            SERVICE_TYPE_CAST,
            NsdManager.PROTOCOL_DNS_SD,
            discoveryListener
        )
    }

    fun stopDiscovery() {
        discoveryListener?.let {
            nsdManager.stopServiceDiscovery(it)
        }
    }
}