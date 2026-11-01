package es.bgaleralop.commandtv.data.network.adb

import android.content.Context
import es.bgaleralop.commandtv.data.network.common.NetworkScanner
import es.bgaleralop.commandtv.domain.model.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Maneja la lógica de descubrimiento de dispositivos.
 */
class DeviceDiscoveryManager(
    private val context: Context,
) {
    private val networkScanner: NetworkScanner = NetworkScanner(context)
    private val nsdDiscoveryManager: TvDiscoveryManager = TvDiscoveryManager(context)


    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private var discoveryJob: Job? = null

    private val DELAY = 4000

    /**
     * Realiza una busqueda automática por mDNS, si no encuentra dispositivos inicia busqueda
     * por fuerza bruta.
     */
    fun startSmartDiscovery() {
        discoveryJob?.cancel()
        discoveryJob = CoroutineScope(Dispatchers.IO).launch {
            _isScanning.value = true
            _devices.value = emptyList()

            val foundDevicesMap = mutableMapOf<String, Device>()

            //1. Iniciamos mDNS
            nsdDiscoveryManager.startDiscovery { device ->
                foundDevicesMap[device.ip] = device
                _devices.value = foundDevicesMap.values.toList()
            }

            //2. Damos un margen para que mDNS responda
            delay(DELAY.milliseconds)

            //3. Si tras el margen mDNS no ha encontrado NADA, activamos busqueda fuerza bruta
            if (foundDevicesMap.isEmpty()) {
                nsdDiscoveryManager.stopDiscovery()
                runTcpSubnetScan(foundDevicesMap)
            } else {
                // Si mDNS encontró dispositivos, paramos el listener tras un poco más de tiempo
                delay(2000.milliseconds)
                nsdDiscoveryManager.stopDiscovery()
            }

            _isScanning.value = false
        }
    }

    /**
     * Fuerza un escaneo profundo de la red buscando dispositivos.
     */
    fun forceDeepScan() {
        discoveryJob?.cancel()
        discoveryJob = CoroutineScope(Dispatchers.IO).launch {
            _isScanning.value = true
            _devices.value = emptyList()
            nsdDiscoveryManager.stopDiscovery()

            val foundDevicesMap = mutableMapOf<String, Device>()
            runTcpSubnetScan(foundDevicesMap)

            _isScanning.value = false
        }
    }

    /**
     * Realiza una búsqueda de dispositivos por fuerza bruta.
     */
    private suspend fun runTcpSubnetScan(destinationMap: MutableMap<String, Device>) {
        val activeIps = networkScanner.scanLocalNetwork()

        activeIps.forEach {
            destinationMap[it.ip] = it
            _devices.value = destinationMap.values.toList()
        }
    }
}