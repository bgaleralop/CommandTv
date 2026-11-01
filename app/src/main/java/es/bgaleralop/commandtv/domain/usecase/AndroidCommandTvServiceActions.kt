package es.bgaleralop.commandtv.domain.usecase

import es.bgaleralop.commandtv.domain.model.AdbKeyEvents

class AndroidCommandTvServiceActions(
    private val commandTvService: AndroidTvServiceImpl
) : CommandTvServiceActions {

    override suspend fun increaseVolume(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_VOLUME_UP)
    }

    override suspend fun decreaseVolume(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_VOLUME_DOWN)
    }

    override suspend fun muted(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_VOLUME_MUTE)
    }

    override suspend fun unmuted(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_VOLUME_MUTE)
    }

    override suspend fun channelUp(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_CHANNEL_UP)
    }

    override suspend fun channelDown(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_CHANNEL_DOWN)
    }

    override suspend fun powerOff(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_TV_POWER)
    }

    override suspend fun powerOn(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_TV_POWER)
    }

    override suspend fun goHome(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_HOME)
    }

    override suspend fun goBack(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_BACK)
    }

    override suspend fun select(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_ENTER)
    }

    override suspend fun goUp(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_DPAD_UP)
    }

    override suspend fun goDown(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_DPAD_DOWN)
    }

    override suspend fun goLeft(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_DPAD_LEFT)
    }

    override suspend fun goRight(): Result<Unit> {
        return commandTvService.executeKeyEvent(AdbKeyEvents.KEY_DPAD_RIGHT)
    }
}