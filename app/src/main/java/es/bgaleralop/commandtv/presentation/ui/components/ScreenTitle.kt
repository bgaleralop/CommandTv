package es.bgaleralop.commandtv.presentation.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** Titulo para las pantallas */
@Composable
fun ScreenTitle(
    title: String,
    modifier: Modifier = Modifier
){
    Text(
        text = title,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}