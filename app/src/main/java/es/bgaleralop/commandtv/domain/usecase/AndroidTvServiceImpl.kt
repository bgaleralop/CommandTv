package es.bgaleralop.commandtv.domain.usecase

import es.bgaleralop.commandtv.data.network.DeviceDiscoveryManager
import es.bgaleralop.commandtv.domain.model.AdbActionCommand
import es.bgaleralop.commandtv.domain.model.AdbDevice
import es.bgaleralop.commandtv.domain.model.AdbKeyEvents
import es.bgaleralop.commandtv.domain.model.AppPackage
import es.bgaleralop.commandtv.domain.repository.AdbRepository
import es.bgaleralop.commandtv.domain.repository.ConnectionState
import es.bgaleralop.commandtv.domain.utils.isSystemPackage
import es.bgaleralop.commandtv.domain.utils.parsePackage
import kotlinx.coroutines.flow.StateFlow

/**
 * Servicio que implementa las funcionalidades de la aplicación.
 *
 * @param repository la implementación de AdbRepository para el acceso a los datos.
 */
class AndroidTvServiceImpl(
    private val repository: AdbRepository,
    private val discoveryManager: DeviceDiscoveryManager
) : CommandTvService {
    override val connectionState: StateFlow<ConnectionState> = repository.connectionState

    override suspend fun discoverDevices(): StateFlow<List<AdbDevice>> {
        discoveryManager.startSmartDiscovery()
        return discoveryManager.devices
    }

    override suspend fun connect(device: AdbDevice): Result<Unit> {
        return repository.connect(device.ip, device.port)
    }

    override suspend fun disconnect() {
        return repository.disconnect()
    }

    override suspend fun getAllPackages(): List<AppPackage> {
        val response = repository.executeShell(AdbActionCommand.ListPackages.command)
        if (response.isSuccess) {
            val fullString = response.getOrElse { return emptyList() }
            val packages = fullString.split("\n").map { parsePackage(it) }
            return packages.map {
                AppPackage(
                    packageName = it,
                    isSystemApp = isSystemPackage(it),
                    isEnabled = false
                )
            }
        } else {
            return emptyList()
        }
    }

    override suspend fun getEnabledPackages(): List<AppPackage> {
        TODO("Not yet implemented")
    }

    override suspend fun getDisabledPackages(): List<AppPackage> {
        val response = repository.executeShell(AdbActionCommand.ListPackagesDisabled.command)
        if (response.isSuccess) {
            val fullString = response.getOrElse { return emptyList() }
            val packages = fullString.split("\n").map { parsePackage(it) }
            return packages.map {
                AppPackage(
                    packageName = it,
                    isSystemApp = isSystemPackage(it),
                    isEnabled = false
                )
            }
        } else {
            return emptyList()
        }
    }

    override suspend fun getSystemPackages(): List<AppPackage> {
        val response = repository.executeShell(AdbActionCommand.ListSystemPackages.command)
        if (response.isSuccess) {
            val fullString = response.getOrElse { return emptyList() }
            val packages = parseResponseToPackages(fullString)
            return packages.map {
                AppPackage(
                    packageName = it,
                    isSystemApp = true,
                    isEnabled = false
                )
            }
        } else {
            return emptyList()
        }
    }

    override suspend fun getThirdPartPackages(): List<AppPackage> {
        val response = repository.executeShell(AdbActionCommand.ListThirdPartPackages.command)
        if (response.isSuccess) {
            val fullString = response.getOrElse { return emptyList() }
            val packages = parseResponseToPackages(fullString)
            return packages.map {
                AppPackage(
                    packageName = it,
                    isSystemApp = false,
                    isEnabled = false
                )
            }
        } else {
            return emptyList()
        }
    }

    override suspend fun getApps(): List<AppPackage> {
        TODO("Not yet implemented")
    }

    override suspend fun enablePackage(appPackage: AppPackage): Result<Unit> {
        val response =
            repository.executeShell(AdbActionCommand.EnablePacket(appPackage.packageName).command)

        return if (response.isSuccess) Result.success(Unit) else Result.failure(Exception("Fallo al activar paquete."))
    }

    override suspend fun disablePackage(appPackage: AppPackage): Result<Unit> {
        val response =
            repository.executeShell(AdbActionCommand.DisablePacket(appPackage.packageName).command)

        return if (response.isSuccess) Result.success(Unit) else Result.failure(Exception("Fallo al desactivar paquete."))
    }

    override suspend fun launchApp(appPackage: AppPackage): Result<Unit> {
        val response =
            repository.executeShell(AdbActionCommand.LaunchApp(appPackage.packageName).command)

        return if (response.isSuccess) Result.success(Unit) else Result.failure(Exception("Fallo al lanzar app"))
    }

    override suspend fun executeKeyEvent(keyEvent: AdbKeyEvents): Result<Unit> {
        val response = repository.executeShell(keyEvent.value)

        return if (response.isSuccess) Result.success(Unit) else Result.failure(Exception("Fallo al ejecutar keyEvent"))
    }

    override suspend fun executeShellCommand(command: String): Result<String> {
        return repository.executeShell(command)
    }

    private fun parseResponseToPackages(response: String): List<String> {
        return response.split("\n").map { parsePackage(it) }
    }
}