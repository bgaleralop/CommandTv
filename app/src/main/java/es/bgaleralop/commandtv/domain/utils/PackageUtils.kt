package es.bgaleralop.commandtv.domain.utils

/**
 * Parsea a una forma mas amigable el paquete devuelto.
 *
 * @param responsePackage el nombre crudo del packete.
 * @return el nombre del packete parseado.
 */
fun parsePackage(responsePackage: String): String {
    return responsePackage
        .trim()
        .removePrefix("package:")
}

/**
 * Verifica si un paquete es de sistema.
 *
 * @param pkgName El nombre del packete.
 * @return True si el packete es de sistema o False en caso contrario.
 */
fun isSystemPackage(pkgName: String): Boolean {
    return pkgName.startsWith("com.google.") ||
            pkgName.startsWith("com.android.") ||
            pkgName.startsWith("com.sony.") ||
            pkgName.startsWith("com.tcl.") ||
            pkgName.startsWith("com.xiaomi.")
}