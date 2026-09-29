package com.example.ui.screens.books

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SchoolClass
import com.example.ui.components.AmbientBackground
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VioletGlow

@Composable
fun BooksScreen(
    classes: List<SchoolClass>,
    onClassClick: (SchoolClass) -> Unit,
    onEyeMenuHideClick: () -> Unit,
    onEyeMenuPrivacyClick: () -> Unit,
    onEyeMenuSettingsClick: () -> Unit
) {
    var showEyeMenu by remember { mutableStateOf(false) }

    AmbientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            // Top Bar with ONLY the Eye Icon at the top-right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Curriculum Books",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Standard Class 1 to 12 Reference",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                // ONLY EYE ICON ON TOP-RIGHT
                Box {
                    IconButton(
                        onClick = { showEyeMenu = true },
                        modifier = Modifier
                            .testTag("books_eye_icon_button")
                            .clip(CircleShape)
                            .background(DarkSurfaceElevated.copy(alpha = 0.8f))
                            .border(1.dp, GlassBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Options",
                            tint = ElectricBlue,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Eye Dropdown Menu: Hide, Privacy, Settings
                    DropdownMenu(
                        expanded = showEyeMenu,
                        onDismissRequest = { showEyeMenu = false },
                        modifier = Modifier
                            .background(DarkSurfaceElevated)
                            .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                    ) {
                        // 1. Hide
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = CyberPurple,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Hide", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                }
                            },
                            onClick = {
                                showEyeMenu = false
                                onEyeMenuHideClick()
                            },
                            colors = MenuDefaults.itemColors()
                        )

                        // 2. Privacy
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Security,
                                        contentDescription = null,
                                        tint = ElectricBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Privacy", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                }
                            },
                            onClick = {
                                showEyeMenu = false
                                onEyeMenuPrivacyClick()
                            }
                        )

                        // 3. Settings
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Settings,
                                        contentDescription = null,
                                        tint = NeonCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Settings", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                }
                            },
                            onClick = {
                                showEyeMenu = false
                                onEyeMenuSettingsClick()
                            }
                        )
                    }
                }
            }

            // Grid of Classes 1 to 12
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 100.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(classes) { schoolClass ->
                    val color = Color(schoolClass.badgeColor)
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClassClick(schoolClass) }
                            .testTag("class_card_${schoolClass.classNumber}"),
                        cornerRadius = 20.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.radialGradient(
                                                listOf(color.copy(alpha = 0.35f), color.copy(alpha = 0.1f))
                                            )
                                        )
                                        .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoStories,
                                        contentDescription = null,
                                        tint = color,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(DarkSurfaceElevated.copy(alpha = 0.8f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${schoolClass.subjects.size} Sub",
                                        fontSize = 11.sp,
                                        color = TextMuted,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = schoolClass.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = schoolClass.description,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
