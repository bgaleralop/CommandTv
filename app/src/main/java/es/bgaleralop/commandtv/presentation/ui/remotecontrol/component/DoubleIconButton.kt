package es.bgaleralop.commandtv.presentation.ui.remotecontrol.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Composable que muestra un botón con dos iconos y un texto.
 */
@Composable
fun DoubleIconButton(
    text: String,
    onTopClick: () -> Unit,
    onBottomClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.Gray.copy(alpha = 0.3f),
        shape = ShapeDefaults.ExtraLarge,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(
                onClick = onTopClick,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = ""
                )
            }
            Text(text = text)
            IconButton(
                onClick = onBottomClick,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Remove,
                    contentDescription = ""
                )
            }
        }
    }
}