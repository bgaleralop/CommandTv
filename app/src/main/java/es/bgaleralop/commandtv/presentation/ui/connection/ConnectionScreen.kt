package es.bgaleralop.commandtv.presentation.ui.connection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.bgaleralop.commandtv.domain.model.AdbDevice
import es.bgaleralop.commandtv.presentation.ui.components.ScreenTitle
import es.bgaleralop.commandtv.presentation.ui.theme.CommandTvTheme

@Composable
fun ConnectionScreen(
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        ScreenTitle(title = "AuraTv Bridge", modifier.align(Alignment.Start))
        SearchingInfo()
        Spacer(modifier = Modifier.height(32.dp))
        DiscoveredDevices(devices = devicesList, modifier = Modifier.weight(10f))
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton (
            onClick = {},
            modifier = Modifier.fillMaxWidth(.7f)
        ) {
            Text("Add Ip Manually")
        }
    }
}

/**
 * Composable que muestra la información de busqueda
 */
@Composable
fun SearchingInfo(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                "Buscando dispositivos...",
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = Color.Blue,
                trackColor = Color.LightGray
            )
        }
    }
}

/**
 * Composable encargado de mostrar los dispositivos encontrados y si estan conectados.
 */
@Composable
fun DiscoveredDevices(
    devices: List<AdbDevice>,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text("Dispositivos encontrados:")
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(devices) { device ->
                DeviceCard(device)
            }
        }
    }
}

/**
 * Composable que representa la tarjeta de un dispositivo.
 */
@Composable
fun DeviceCard(device: AdbDevice, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(0.9f)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Tv,
                contentDescription = ""
            )
            Column() {
                Text(
                    text = device.name,
                )

                Text(
                    text = "(${device.ip})"
                )
            }
        }
    }
}

private val devicesList = listOf<AdbDevice>(
    AdbDevice("192.168.1.1", "Host1"),
    AdbDevice("192.168.1.2", "Host2"), AdbDevice("192.168.1.1", "Host1"),
    AdbDevice("192.168.1.2", "Host2"), AdbDevice("192.168.1.1", "Host1"),
    AdbDevice("192.168.1.2", "Host2"), AdbDevice("192.168.1.1", "Host1"),
    AdbDevice("192.168.1.2", "Host2"), AdbDevice("192.168.1.1", "Host1"),
    AdbDevice("192.168.1.2", "Host2"),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConnectionScreenPreview(){
    CommandTvTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ConnectionScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}