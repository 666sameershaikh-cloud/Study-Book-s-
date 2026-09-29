package com.example.ui.screens.profile

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserEntity
import com.example.ui.components.AmbientBackground
import com.example.ui.components.AvatarView
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    currentUser: UserEntity?,
    onEditProfileClick: () -> Unit,
    onPrivacySettingsClick: () -> Unit,
    onPrivacyPasswordResetClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var showLogoutConfirm by remember { mutableStateOf(false) }
    var showAccountInfoDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    AmbientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile & Settings",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp, top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Profile Card
                item {
                    if (currentUser != null) {
                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("profile_user_card"),
                            cornerRadius = 24.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AvatarView(
                                    name = currentUser.fullName,
                                    avatarColor = currentUser.avatarColor,
                                    size = 76.dp,
                                    isOnline = true
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = currentUser.fullName,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )

                                Text(
                                    text = "@${currentUser.username}",
                                    fontSize = 13.sp,
                                    color = CyberPurple,
                                    fontWeight = FontWeight.Medium
                                )

                                if (currentUser.bio.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = currentUser.bio,
                                        fontSize = 13.sp,
                                        color = TextSecondary,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        lineHeight = 17.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Edit Profile Button
                                Button(
                                    onClick = onEditProfileClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("profile_edit_button")
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Edit Profile", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }

                // Settings Group
                item {
                    Text(
                        text = "Preferences & Security",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }

                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 20.dp,
                        borderGlow = false
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            // Privacy Settings
                            SettingsItemRow(
                                icon = Icons.Default.Security,
                                title = "Privacy Settings",
                                subtitle = "Online status, last seen, read receipts",
                                iconColor = CyberPurple,
                                onClick = onPrivacySettingsClick,
                                tag = "settings_privacy_item"
                            )

                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))

                            // Privacy Password / Vault Key
                            SettingsItemRow(
                                icon = Icons.Default.Lock,
                                title = "Privacy Password Management",
                                subtitle = "Reset or configure your secret chats PIN",
                                iconColor = ElectricBlue,
                                onClick = onPrivacyPasswordResetClick,
                                tag = "settings_privacy_password_item"
                            )

                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))

                            // Notification Settings
                            SettingsItemRow(
                                icon = Icons.Default.Notifications,
                                title = "Notification Settings",
                                subtitle = "Message alerts and connection requests",
                                iconColor = NeonCyan,
                                onClick = { showNotificationsDialog = true },
                                tag = "settings_notifications_item"
                            )

                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))

                            // Account Information
                            SettingsItemRow(
                                icon = Icons.Default.Info,
                                title = "Account Information",
                                subtitle = currentUser?.email ?: "Registered User",
                                iconColor = TextSecondary,
                                onClick = { showAccountInfoDialog = true },
                                tag = "settings_account_item"
                            )
                        }
                    }
                }

                // Logout
                item {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showLogoutConfirm = true }
                            .testTag("profile_logout_card"),
                        cornerRadius = 16.dp,
                        borderGlow = false
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(ErrorRed.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Logout",
                                    tint = ErrorRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Text(
                                text = "Logout",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ErrorRed
                            )
                        }
                    }
                }
            }
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Log Out", color = TextPrimary) },
            text = { Text("Are you sure you want to log out of BOOKS STUDY?", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirm = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Logout", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = DarkSurfaceElevated
        )
    }

    // Account Details Dialog
    if (showAccountInfoDialog && currentUser != null) {
        AlertDialog(
            onDismissRequest = { showAccountInfoDialog = false },
            title = { Text("Account Details", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("User ID: ${currentUser.id.take(12)}...", color = TextSecondary, fontSize = 12.sp)
                    Text("Email: ${currentUser.email}", color = TextPrimary, fontSize = 14.sp)
                    Text("Username: @${currentUser.username}", color = TextPrimary, fontSize = 14.sp)
                    Text("Member Since: Standard Student Verification", color = TextMuted, fontSize = 12.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showAccountInfoDialog = false }) {
                    Text("Close", color = ElectricBlue)
                }
            },
            containerColor = DarkSurfaceElevated
        )
    }

    // Notification Details Dialog
    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            title = { Text("Notification Preferences", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("✓ In-app unread badges enabled", color = TextSecondary, fontSize = 13.sp)
                    Text("✓ Private message alerts enabled", color = TextSecondary, fontSize = 13.sp)
                    Text("✓ Connection request alerts enabled", color = TextSecondary, fontSize = 13.sp)
                    Text("System notifications will vibrate and display when permitted.", color = TextMuted, fontSize = 12.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) {
                    Text("Done", color = NeonCyan)
                }
            },
            containerColor = DarkSurfaceElevated
        )
    }
}

@Composable
private fun SettingsItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    onClick: () -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(16.dp)
        )
    }
}
