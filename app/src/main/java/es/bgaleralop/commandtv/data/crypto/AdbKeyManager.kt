package es.bgaleralop.commandtv.data.crypto

import android.content.Context
import java.io.File
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec

/**
 * Geneera un par de claves RSA de 2048 bits la primera vez que se instala la app y las guarda en el
 * almacenamiento seguro (Android Keystore).
 *
 * Cuando el televisor responda con un paquete de autenticación(AUTH), esta clase firmará el token
 * usando la clave privada para superar el emparejamiento.
 */
class AdbKeyManager(private val context: Context) {
    private val privateKeyFile = File(context.filesDir, "adb_private.key")

    init {
        //Si no existen las llaves previas, las generamos al iniciar la app.
        if (!privateKeyFile.exists()) {
            generateAndSaveKeyPair()
        }
    }

    /**
     * Genera y guarda el keyPair
     */
    private fun generateAndSaveKeyPair() {
        val keyGen = KeyPairGenerator.getInstance("RSA")
        keyGen.initialize(2048) // Clave RSA de 2048 bits requerida por ADB
        val keyPair = keyGen.generateKeyPair()

        //Guardamos la clave privada en formato codificado (PKCS#8)
        privateKeyFile.writeBytes(keyPair.private.encoded)
    }

    /**
     * Carga la clave almacenada en el dispositivo.
     */
    private fun loadPrivateKey(): PrivateKey {
        val bytes = privateKeyFile.readBytes()
        val keySpec = PKCS8EncodedKeySpec(bytes)
        val keyFactory = KeyFactory.getInstance("RSA")

        return keyFactory.generatePrivate(keySpec)
    }

    /**
     * Firma digitalmente el token de 20 bytes enviado por el host objetivo en el paquete AUTH.
     * @param token El token enviado por el host.
     *
     * @return ByteArray con el token firmado.
     */
    fun signToken(token: ByteArray): ByteArray {
        val privateKey = loadPrivateKey()

        //ADB utiliza SHA1withRSA para firmar el reto de autenticación.
        val signature = Signature.getInstance("SHA1withRSA")
        signature.initSign(privateKey)
        signature.update(token)

        return signature.sign()
    }
}