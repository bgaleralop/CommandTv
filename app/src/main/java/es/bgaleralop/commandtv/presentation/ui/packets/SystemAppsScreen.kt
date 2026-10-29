package es.bgaleralop.commandtv.presentation.ui.packets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.bgaleralop.commandtv.presentation.ui.packets.components.CheckItem
import es.bgaleralop.commandtv.presentation.ui.packets.components.MySearchBar
import es.bgaleralop.commandtv.presentation.ui.packets.components.SystemAppCard
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
            onQueryChange = { query = it },
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
