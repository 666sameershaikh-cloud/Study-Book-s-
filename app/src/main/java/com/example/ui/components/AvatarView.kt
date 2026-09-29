package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.TextPrimary

@Composable
fun AvatarView(
    name: String,
    avatarColor: Long,
    size: Dp = 48.dp,
    isOnline: Boolean = false,
    showOnlineBadge: Boolean = true,
    modifier: Modifier = Modifier
) {
    val initials = name.trim().split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .map { it.first().uppercaseChar() }
        .joinToString("")
        .ifEmpty { "?" }

    val baseColor = Color(avatarColor)

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow border
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(ElectricBlue.copy(alpha = 0.6f), CyberPurple.copy(alpha = 0.6f))
                    ),
                    shape = CircleShape
                )
                .background(
                    Brush.radialGradient(
                        listOf(baseColor.copy(alpha = 0.85f), baseColor.copy(alpha = 0.45f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.38f).sp
            )
        }

        if (showOnlineBadge && isOnline) {
            val badgeSize = (size * 0.28f).coerceAtLeast(10.dp)
            Box(
                modifier = Modifier
                    .size(badgeSize)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-1).dp, y = (-1).dp)
                    .clip(CircleShape)
                    .background(OnlineGreen)
                    .border(2.dp, DeepBlack, CircleShape)
            )
        }
    }
}
