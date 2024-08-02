package com.example.flashonoff

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashlightApp(this)
        }
    }
}

@Composable
fun FlashlightApp(context: Context) {
    var isFlashOn by remember { mutableStateOf(false) }

    val purpleColor = Color(0xFF7D3C98)
    val darkPurpleColor = Color(0xFF4A235A)
    val backgroundColor = Color(0xFF1C1C1C)
    val whiteColor = Color(0xFFFFFFFF)
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(darkPurpleColor, purpleColor)
    )

    val cameraManager = ContextCompat.getSystemService(context, CameraManager::class.java)
    val cameraId = cameraManager?.cameraIdList?.find { id ->
        cameraManager.getCameraCharacteristics(id).get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
    }

    LaunchedEffect(isFlashOn) {
        cameraId?.let {
            cameraManager?.setTorchMode(it, isFlashOn)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Flashlight",
                color = whiteColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
            ) {
                FlashButton(text = "SOS", gradientBrush = gradientBrush, textColor = whiteColor)
                FlashButton(text = "Morse", gradientBrush = gradientBrush, textColor = whiteColor)
            }

            PowerButton(isFlashOn, onClick = { isFlashOn = !isFlashOn }, gradientBrush)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FlashButton(text: String, gradientBrush: Brush, textColor: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(brush = gradientBrush)
            .border(2.dp, textColor, CircleShape)
            .clickable { /* Handle click */ }
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PowerButton(isFlashOn: Boolean, onClick: () -> Unit, gradientBrush: Brush) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(brush = gradientBrush)
            .border(4.dp, Color.White, CircleShape)
            .clickable { onClick() }
    ) {
        Text(
            text = if (isFlashOn) "Off" else "On",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
