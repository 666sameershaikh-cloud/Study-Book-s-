package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserEntity
import com.example.ui.components.AvatarView
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun EditProfileDialog(
    currentUser: UserEntity,
    onDismiss: () -> Unit,
    onSaveProfile: (String, String, Long) -> Unit
) {
    var fullName by remember { mutableStateOf(currentUser.fullName) }
    var bio by remember { mutableStateOf(currentUser.bio) }
    var selectedColor by remember { mutableLongStateOf(currentUser.avatarColor) }

    val colorOptions = listOf(
        0xFF3B82F6, 0xFF8B5CF6, 0xFF10B981, 0xFFF59E0B, 0xFFEC4899,
        0xFF06B6D4, 0xFF6366F1, 0xFF14B8A6, 0xFFD946EF
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Edit Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Avatar preview with selected color
                AvatarView(
                    name = fullName.ifBlank { "User" },
                    avatarColor = selectedColor,
                    size = 64.dp,
                    isOnline = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Color picker row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    colorOptions.forEach { colorVal ->
                        val isSelected = selectedColor == colorVal
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(colorVal))
                                .border(
                                    width = if (isSelected) 2.5.dp else 0.dp,
                                    color = if (isSelected) Color.White else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = colorVal }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Full Name field
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name", color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = ElectricBlue) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = GlassBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_profile_fullname_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Bio field
                OutlinedTextField(
                    value = bio,
                    onValueChange = { if (it.length <= 160) bio = it },
                    label = { Text("Bio / Academic Interests", color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = CyberPurple) },
                    maxLines = 3,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = CyberPurple,
                        unfocusedBorderColor = GlassBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_profile_bio_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onSaveProfile(fullName, bio, selectedColor) },
                    enabled = fullName.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("edit_profile_save_button")
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {},
        containerColor = DarkSurfaceElevated,
        shape = RoundedCornerShape(24.dp)
    )
}
