package es.bgaleralop.commandtv.presentation.ui.remotecontrol.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp


/**Represtación de un botón clickable con un icono. */
@Composable
fun SimpleIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String = ""
){
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors().copy(
            containerColor = Color.Gray.copy(alpha = 0.3f)
        ),
        modifier = modifier.size(48.dp)
    ) {
        Icon(
            rememberVectorPainter(icon),
            contentDescription = description
        )
    }
}