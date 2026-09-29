package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

enum class AppTab(val title: String) {
    HOME("Home"),
    BOOKS("Books"),
    CONNECTIONS("Connections"),
    CHAT("Chat"),
    PROFILE("Profile")
}

@Composable
fun BooksStudyBottomNav(
    currentTab: AppTab,
    unreadChatCount: Int = 0,
    pendingConnectionCount: Int = 0,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            DarkSurfaceElevated.copy(alpha = 0.92f),
                            DarkSurfaceGlass.copy(alpha = 0.95f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            ElectricBlue.copy(alpha = 0.35f),
                            CyberPurple.copy(alpha = 0.25f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTab.values().forEach { tab ->
                    val isSelected = tab == currentTab
                    val iconFilled: ImageVector
                    val iconOutlined: ImageVector

                    when (tab) {
                        AppTab.HOME -> {
                            iconFilled = Icons.Filled.Home
                            iconOutlined = Icons.Outlined.Home
                        }
                        AppTab.BOOKS -> {
                            iconFilled = Icons.Filled.AutoStories
                            iconOutlined = Icons.Outlined.AutoStories
                        }
                        AppTab.CONNECTIONS -> {
                            iconFilled = Icons.Filled.Groups
                            iconOutlined = Icons.Outlined.Groups
                        }
                        AppTab.CHAT -> {
                            iconFilled = Icons.Filled.ChatBubble
                            iconOutlined = Icons.Outlined.ChatBubbleOutline
                        }
                        AppTab.PROFILE -> {
                            iconFilled = Icons.Filled.Person
                            iconOutlined = Icons.Outlined.Person
                        }
                    }

                    val badgeCount = when (tab) {
                        AppTab.CHAT -> unreadChatCount
                        AppTab.CONNECTIONS -> pendingConnectionCount
                        else -> 0
                    }

                    BottomNavItem(
                        tab = tab,
                        isSelected = isSelected,
                        icon = if (isSelected) iconFilled else iconOutlined,
                        badgeCount = badgeCount,
                        onClick = { onTabSelected(tab) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    tab: AppTab,
    isSelected: Boolean,
    icon: ImageVector,
    badgeCount: Int,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) TextPrimary else TextMuted,
        animationSpec = spring(),
        label = "contentColor"
    )

    val backgroundModifier = if (isSelected) {
        Modifier.background(
            Brush.radialGradient(
                listOf(
                    ElectricBlue.copy(alpha = 0.28f),
                    CyberPurple.copy(alpha = 0.15f),
                    Color.Transparent
                )
            )
        )
    } else {
        Modifier
    }

    Column(
        modifier = Modifier
            .testTag("nav_tab_${tab.name.lowercase()}")
            .clip(RoundedCornerShape(18.dp))
            .then(backgroundModifier)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = tab.title,
                tint = if (isSelected) ElectricBlue else TextMuted,
                modifier = Modifier.size(24.dp)
            )

            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(x = 10.dp, y = (-8).dp)
                        .clip(CircleShape)
                        .background(ErrorRed)
                )
            }
        }

        Text(
            text = tab.title,
            color = contentColor,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
