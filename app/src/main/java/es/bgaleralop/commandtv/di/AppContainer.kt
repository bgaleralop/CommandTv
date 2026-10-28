package es.bgaleralop.commandtv.di

import android.content.Context
import es.bgaleralop.commandtv.data.crypto.AdbKeyManager
import es.bgaleralop.commandtv.data.network.DeviceDiscoveryManager
import es.bgaleralop.commandtv.data.repository.AdbRepositoryImpl
import es.bgaleralop.commandtv.domain.repository.AdbRepository
import es.bgaleralop.commandtv.domain.usecase.AndroidTvServiceImpl

/**
 * Clase contenedor de dependencias.
 */
class AppContainer(context: Context) {
    val adbKeyManager = AdbKeyManager(context)
    val discoveryManager = DeviceDiscoveryManager(context)

    val adbRepository: AdbRepository = AdbRepositoryImpl(adbKeyManager)

    val commandTvService = AndroidTvServiceImpl(adbRepository, discoveryManager)
}