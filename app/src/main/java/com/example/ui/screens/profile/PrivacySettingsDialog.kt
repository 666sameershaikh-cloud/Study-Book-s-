package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserEntity
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PrivacySettingsDialog(
    currentUser: UserEntity,
    onDismiss: () -> Unit,
    onSavePrivacy: (Boolean, Boolean, Boolean, Boolean, Boolean) -> Unit
) {
    var showOnlineStatus by remember { mutableStateOf(currentUser.showOnlineStatus) }
    var showLastSeen by remember { mutableStateOf(currentUser.showLastSeen) }
    var readReceipts by remember { mutableStateOf(currentUser.readReceipts) }
    var showTyping by remember { mutableStateOf(currentUser.showTyping) }
    var showMessagePreview by remember { mutableStateOf(currentUser.showMessagePreview) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Privacy Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Online Status
                PrivacyToggleRow(
                    title = "Online Status",
                    description = "Allow accepted peers to see when you're online",
                    checked = showOnlineStatus,
                    onCheckedChange = { showOnlineStatus = it },
                    tag = "privacy_toggle_online_status"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Last Seen
                PrivacyToggleRow(
                    title = "Last Seen",
                    description = "Show time of your last active session",
                    checked = showLastSeen,
                    onCheckedChange = { showLastSeen = it },
                    tag = "privacy_toggle_last_seen"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Read Receipts
                PrivacyToggleRow(
                    title = "Read Receipts",
                    description = "Show double check marks when messages are read",
                    checked = readReceipts,
                    onCheckedChange = { readReceipts = it },
                    tag = "privacy_toggle_read_receipts"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Typing Indicator
                PrivacyToggleRow(
                    title = "Typing Indicator",
                    description = "Show when you are composing a message",
                    checked = showTyping,
                    onCheckedChange = { showTyping = it },
                    tag = "privacy_toggle_typing_indicator"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Message Preview
                PrivacyToggleRow(
                    title = "Message Preview",
                    description = "Display snippet of new messages on home screen",
                    checked = showMessagePreview,
                    onCheckedChange = { showMessagePreview = it },
                    tag = "privacy_toggle_message_preview"
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onSavePrivacy(
                            showOnlineStatus,
                            showLastSeen,
                            readReceipts,
                            showTyping,
                            showMessagePreview
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("privacy_settings_save_button")
                ) {
                    Text("Save Privacy Preferences", fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {},
        containerColor = DarkSurfaceElevated,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
private fun PrivacyToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = description,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextPrimary,
                checkedTrackColor = ElectricBlue,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = DarkSurfaceElevated
            ),
            modifier = Modifier.testTag(tag)
        )
    }
}
