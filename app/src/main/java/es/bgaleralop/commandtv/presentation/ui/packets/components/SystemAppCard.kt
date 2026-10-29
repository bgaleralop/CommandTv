package es.bgaleralop.commandtv.presentation.ui.packets.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.bgaleralop.commandtv.domain.model.InnerApp

/**
 * Composable que muestra una tarjeta para representar un paquete de aplicación del sistema.
 */
@Composable
fun SystemAppCard(
    systemApp: InnerApp,
    onToggleEnabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(0.95f).padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Apps,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.padding(end = 4.dp))
            Column(
            ) {
                Text(
                    systemApp.appName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    systemApp.appPackage,
                    fontSize = 8.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = systemApp.isEnabled,
                onCheckedChange = onToggleEnabled
            )
        }
    }
}