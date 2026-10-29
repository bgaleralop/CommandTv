package es.bgaleralop.commandtv.presentation.ui.packets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.bgaleralop.commandtv.presentation.ui.remotecontrol.InnerApp
import es.bgaleralop.commandtv.presentation.ui.theme.CommandTvTheme

/**
 * Composable que muestra la pantalla de la lista de aplicaciones del sistema.
 */
@Composable
fun SystemAppsScreen(
    systemApps: List<InnerApp>,
    modifier: Modifier = Modifier
){
    var query by remember { mutableStateOf("") }
    var checkDisabledOnly by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        MySearchBar(
            query = "",
            onQueryChange = {query = it},
            modifier = Modifier.fillMaxWidth(0.98f).padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier.align(Alignment.End)
        ) {
            CheckItem(
                text = "Solo deshabilitados",
                checked = checkDisabledOnly,
                onCheckedChange = { checkDisabledOnly = it }
            )
        }
        LazyColumn(

        ) {
            items(filterApps(systemApps, query)) { app ->
                SystemAppCard(app, {})
            }
        }


    }

}

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

private fun filterApps(apps: List<InnerApp>, query: String): List<InnerApp> {
    return apps.filter { app ->
        app.appName.contains(query, ignoreCase = true) || app.appPackage.contains(query, ignoreCase = true)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SystemAppsScreenPreview() {
    CommandTvTheme() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SystemAppsScreen(
                systemApps = listOf(
                    InnerApp("youtube", "com.google.android.youtube", true),
                    InnerApp("youtube music", "com.google.android.apps.youtube.music", true),
                    InnerApp("netflix", "com.netflix.mediaclient", false),
                    InnerApp("youtube", "com.google.android.youtube", true),
                    InnerApp("youtube music", "com.google.android.apps.youtube.music", true),
                    InnerApp("netflix", "com.netflix.mediaclient", false),
                    InnerApp("youtube", "com.google.android.youtube", true),
                    InnerApp("youtube music", "com.google.android.apps.youtube.music", true),
                    InnerApp("netflix", "com.netflix.mediaclient", false),
                    InnerApp("youtube", "com.google.android.youtube", true),
                    InnerApp("youtube music", "com.google.android.apps.youtube.music", true),
                    InnerApp("netflix", "com.netflix.mediaclient", false),
                ),
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
