package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElectricBlue

@Composable
fun AmbientBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        // Ambient glow canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Top-right blue glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ElectricBlue.copy(alpha = 0.16f),
                        DeepNavy.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.85f, height * 0.15f),
                    radius = width * 0.7f
                )
            )

            // Middle-left purple glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        CyberPurple.copy(alpha = 0.14f),
                        DeepNavy.copy(alpha = 0.06f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.1f, height * 0.55f),
                    radius = width * 0.65f
                )
            )

            // Bottom-right subtle violet glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ElectricBlue.copy(alpha = 0.10f),
                        CyberPurple.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.75f, height * 0.90f),
                    radius = width * 0.6f
                )
            )
        }

        content()
    }
}
