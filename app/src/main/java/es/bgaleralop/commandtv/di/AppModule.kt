package es.bgaleralop.commandtv.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.bgaleralop.commandtv.data.crypto.AdbKeyManager
import es.bgaleralop.commandtv.data.network.common.DeviceDiscoveryManager
import es.bgaleralop.commandtv.data.repository.AdbRepositoryImpl
import es.bgaleralop.commandtv.domain.repository.AdbRepository
import es.bgaleralop.commandtv.domain.usecase.AndroidCommandTvServiceActions
import es.bgaleralop.commandtv.domain.usecase.AndroidTvServiceImpl
import es.bgaleralop.commandtv.domain.usecase.CommandTvService
import javax.inject.Singleton

/**
 * Objeto contenedor de dependencias.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAdbKeyManager(@ApplicationContext context: Context): AdbKeyManager {
        return AdbKeyManager(context)
    }

    @Provides
    @Singleton
    fun provideDeviceDiscoveryManager(@ApplicationContext context: Context): DeviceDiscoveryManager {
        return DeviceDiscoveryManager(context)
    }

    @Provides
    @Singleton
    fun provideAdbRepository(adbKeyManager: AdbKeyManager): AdbRepository {
        return AdbRepositoryImpl(adbKeyManager)
    }

    @Provides
    @Singleton
    fun provideCommandTvService(adbRepository: AdbRepository, discoveryManager: DeviceDiscoveryManager): CommandTvService {
        return AndroidTvServiceImpl(adbRepository, discoveryManager)
    }

    @Provides
    @Singleton
    fun provideAndroidTvServiceImpl(adbRepository: AdbRepository, discoveryManager: DeviceDiscoveryManager): AndroidTvServiceImpl {
        return AndroidTvServiceImpl(adbRepository, discoveryManager)
    }

    @Provides
    @Singleton
    fun provideAndroidCommandActions(commandTvService: AndroidTvServiceImpl) : AndroidCommandTvServiceActions {
        return AndroidCommandTvServiceActions(commandTvService)
    }

}