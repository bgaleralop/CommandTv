package es.bgaleralop.commandtv.presentation.ui.remotecontrol.component

import androidx.compose.ui.graphics.vector.ImageVector
import es.bgaleralop.commandtv.domain.model.InnerApp

/**
 * Modelo de datos para representar una aplicación en la cuadrícula.
 */
data class GridApp(
    val app: InnerApp,
    val image: ImageVector
)