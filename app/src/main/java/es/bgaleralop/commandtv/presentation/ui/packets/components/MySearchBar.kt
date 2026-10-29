package es.bgaleralop.commandtv.presentation.ui.packets.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

/**
 * Composable que muestra una barra de búsqueda.
 */
@Composable
fun MySearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
){
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.clip(ShapeDefaults.Medium),
        placeholder = { Text("Filtrar Paquetes")},
        maxLines = 1,
        singleLine = true,
        trailingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "")},
        enabled = enabled
    )
}