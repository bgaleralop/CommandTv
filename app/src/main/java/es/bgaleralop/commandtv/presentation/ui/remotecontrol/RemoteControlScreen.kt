package es.bgaleralop.commandtv.presentation.ui.remotecontrol

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Input
import androidx.compose.material.icons.filled.MusicVideo
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.rounded.Assistant
import androidx.compose.material.icons.rounded.ConnectedTv
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Subtitles
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.bgaleralop.commandtv.domain.model.InnerApp
import es.bgaleralop.commandtv.presentation.ui.remotecontrol.component.AppsGrid
import es.bgaleralop.commandtv.presentation.ui.remotecontrol.component.DoubleIconButton
import es.bgaleralop.commandtv.presentation.ui.remotecontrol.component.GridApp
import es.bgaleralop.commandtv.presentation.ui.remotecontrol.component.SimpleIconButton
import es.bgaleralop.commandtv.presentation.ui.theme.CommandTvTheme

/**
 * Composable que muestra la pantalla de control remoto.
 */
@Composable
fun RemoteControlScreen(
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(0.9f).padding(bottom = 32.dp)
        ){
            Text(
                text = "Remote Control View",
                fontSize = 28.sp,
            )
            Icon(
                imageVector = Icons.Rounded.ConnectedTv,
                contentDescription = ""
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            SimpleIconButton(
                icon = Icons.Default.PowerSettingsNew,
                onClick = {}
            )
            SimpleIconButton(
                icon = Icons.AutoMirrored.Filled.VolumeOff,
                onClick = {}
            )
        }

        // Aqui iria el touchpad
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            SimpleIconButton(
                icon = Icons.Rounded.Home,
                onClick = {}
            )
            SimpleIconButton(
                icon = Icons.Rounded.Assistant,
                onClick = {}
            )
            SimpleIconButton(
                icon = Icons.AutoMirrored.Rounded.ArrowBack,
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.height(28.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            DoubleIconButton(
                text = "VOL",
                onTopClick = {},
                onBottomClick = {}
            )
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(vertical = 8.dp)
                ) {
                SimpleIconButton(
                    icon = Icons.AutoMirrored.Rounded.Input,
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(8.dp))
                SimpleIconButton(
                    icon = Icons.Rounded.Subtitles,
                    onClick = {}
                )
            }
            DoubleIconButton(
                text = "CH",
                onTopClick = {},
                onBottomClick = {}
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        // Aqui van las apps para iniciar.
        AppsGrid(apps = appsList)
    }
}



private val appsList = listOf<GridApp>(
    GridApp(
        app = InnerApp("Youtube", "com.google.android.youtube", true),
        image = Icons.Default.VideoLibrary
    ),
    GridApp(
        app = InnerApp("Youtube Music", "com.google.android.youtube", true),
        image = Icons.Default.MusicVideo
    ),
    GridApp(
        app = InnerApp("Youtube", "com.google.android.youtube", true),
        image = Icons.Default.SentimentDissatisfied
    ),
    GridApp(
        app = InnerApp("Youtube", "com.google.android.youtube", true),
        image = Icons.Default.SentimentSatisfied
    ),
    GridApp(
        app = InnerApp("Youtube Music", "com.google.android.youtube", true),
        image = Icons.Default.MusicVideo
    ),
    GridApp(
        app = InnerApp("Youtube", "com.google.android.youtube", true),
        image = Icons.Default.SentimentDissatisfied
    ),
    GridApp(
        app = InnerApp("Youtube", "com.google.android.youtube", true),
        image = Icons.Default.SentimentSatisfied
    ),
)







@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RemoteControlScreenPreview() {
    CommandTvTheme() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            RemoteControlScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}