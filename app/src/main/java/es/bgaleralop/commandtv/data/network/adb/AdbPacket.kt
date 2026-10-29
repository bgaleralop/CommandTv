package es.bgaleralop.commandtv.data.network.adb

import es.bgaleralop.commandtv.domain.model.AdbCommand
import es.bgaleralop.commandtv.domain.utils.HEADER_PACKAGE_SIZE
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Se encarga de empaquetar y desempaquetar las tramas de datos de ADB:
 * - Cabecera de 24 bytes con comandos como CNXN, AUTH, OPEN, WRTE, y un payload con el
 * comando de texto.
 */
class AdbPacket {
    companion object {
        /**
         * Crea el paquete para ser mandado.
         * @param command El comando que se quiere ejecutar
         * @param arg0 primer argumento del comando.
         * @param arg1 segundo argumento del comando.
         * @param payload La carga del mensaje que se envia.
         *
         * @return ByteArray con el paquete estructurado.
         */
        fun createAdbPacket(
            command: AdbCommand,
            arg0: Int,
            arg1: Int,
            payload: ByteArray
        ): ByteArray {
            val cmdBytes = command.value.toByteArray(Charsets.UTF_8)
            val dataLength = payload.size
            val dataCheck = payload.sumOf { it.toInt() and 0XFF }

            // El campo magic es el complemento bit a bit (~x) del comando entero.
            val cmdInt = ByteBuffer.wrap(cmdBytes).order(ByteOrder.LITTLE_ENDIAN).getInt()
            val magic = cmdInt.inv()

            val buffer =
                ByteBuffer.allocate(HEADER_PACKAGE_SIZE + dataLength).order(ByteOrder.LITTLE_ENDIAN)
            buffer.put(cmdBytes)
            buffer.putInt(arg0)
            buffer.putInt(arg1)
            buffer.putInt(dataLength)
            buffer.putInt(dataCheck)
            buffer.putInt(magic)
            buffer.put(payload)

            return buffer.array()
        }
    }
}