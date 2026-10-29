package es.bgaleralop.commandtv.presentation.ui.packets.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Composable que muestra un elemento de checkbox con texto.
 */
@Composable
fun CheckItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        Text(text = text)
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}