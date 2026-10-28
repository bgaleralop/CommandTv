package es.bgaleralop.commandtv.domain.usecase

import es.bgaleralop.commandtv.domain.model.AdbDevice
import es.bgaleralop.commandtv.domain.model.AdbKeyEvents
import es.bgaleralop.commandtv.domain.model.AppPackage
import es.bgaleralop.commandtv.domain.repository.ConnectionState
import kotlinx.coroutines.flow.StateFlow

/**
 * Servicio que recoge las funcionalidades de la aplicación a la hora de
 * interactuar con el target.
 */
interface CommandTvService {
    val connectionState: StateFlow<ConnectionState>
    suspend fun discoverDevices(): StateFlow<List<AdbDevice>>
    suspend fun connect(device: AdbDevice): Result<Unit>
    suspend fun disconnect()
    suspend fun getAllPackages(): List<AppPackage>
    suspend fun getEnabledPackages(): List<AppPackage>
    suspend fun getDisabledPackages(): List<AppPackage>
    suspend fun getSystemPackages(): List<AppPackage>
    suspend fun getThirdPartPackages(): List<AppPackage>
    suspend fun getApps(): List<AppPackage>

    suspend fun enablePackage(appPackage: AppPackage): Result<Unit>
    suspend fun disablePackage(appPackage: AppPackage): Result<Unit>
    suspend fun launchApp(appPackage: AppPackage): Result<Unit>
    suspend fun executeKeyEvent(keyEvent: AdbKeyEvents): Result<Unit>
    suspend fun executeShellCommand(command: String): Result<String>

}