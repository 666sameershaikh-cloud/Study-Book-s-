package com.example.ui.screens.privacy

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class PrivacyDialogMode {
    ENTER,
    SETUP,
    RESET
}

@Composable
fun PrivacyPasswordDialog(
    hasPasswordSet: Boolean,
    errorMessage: String?,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSubmitPassword: (String) -> Unit,
    onSetupPassword: (String, String) -> Unit,
    onResetPassword: (String, String) -> Unit
) {
    var mode by remember {
        mutableStateOf(if (hasPasswordSet) PrivacyDialogMode.ENTER else PrivacyDialogMode.SETUP)
    }

    var enterPin by remember { mutableStateOf("") }
    var setupPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var accountPassword by remember { mutableStateOf("") }
    var newPin by remember { mutableStateOf("") }

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
                // Top close button & Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(CyberPurple.copy(alpha = 0.2f))
                            .border(1.dp, CyberPurple.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = CyberPurple,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = when (mode) {
                        PrivacyDialogMode.ENTER -> "Enter Privacy Password"
                        PrivacyDialogMode.SETUP -> "Set Privacy Password"
                        PrivacyDialogMode.RESET -> "Reset Privacy Password"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = when (mode) {
                        PrivacyDialogMode.ENTER -> "Unlock your secret private chats vault"
                        PrivacyDialogMode.SETUP -> "Create a dedicated secret PIN (different from login password)"
                        PrivacyDialogMode.RESET -> "Verify your login password to set a new Privacy PIN"
                    },
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Error message
                AnimatedVisibility(visible = !errorMessage.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ErrorRed.copy(alpha = 0.15f))
                            .border(1.dp, ErrorRed.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = ErrorRed,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                when (mode) {
                    PrivacyDialogMode.ENTER -> {
                        OutlinedTextField(
                            value = enterPin,
                            onValueChange = { if (it.length <= 12) enterPin = it },
                            label = { Text("Privacy Password", color = TextSecondary) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CyberPurple) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                if (enterPin.isNotBlank()) onSubmitPassword(enterPin)
                            }),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = CyberPurple,
                                unfocusedBorderColor = GlassBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("privacy_enter_password_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Forgot Privacy Password?",
                                fontSize = 12.sp,
                                color = NeonCyan,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .clickable { mode = PrivacyDialogMode.RESET }
                                    .testTag("privacy_forgot_password_button")
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = { onSubmitPassword(enterPin) },
                            enabled = !isLoading && enterPin.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("privacy_enter_submit_button")
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = TextPrimary, modifier = Modifier.size(20.dp))
                            } else {
                                Text("Unlock Private Chats", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    PrivacyDialogMode.SETUP -> {
                        OutlinedTextField(
                            value = setupPin,
                            onValueChange = { if (it.length <= 12) setupPin = it },
                            label = { Text("New Privacy Password (min 4 chars)", color = TextSecondary) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CyberPurple) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = CyberPurple,
                                unfocusedBorderColor = GlassBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("privacy_setup_pin_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = confirmPin,
                            onValueChange = { if (it.length <= 12) confirmPin = it },
                            label = { Text("Confirm Privacy Password", color = TextSecondary) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CyberPurple) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                if (setupPin.isNotBlank() && confirmPin.isNotBlank()) onSetupPassword(setupPin, confirmPin)
                            }),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = CyberPurple,
                                unfocusedBorderColor = GlassBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("privacy_confirm_pin_input")
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { onSetupPassword(setupPin, confirmPin) },
                            enabled = !isLoading && setupPin.length >= 4 && confirmPin.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("privacy_setup_submit_button")
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = TextPrimary, modifier = Modifier.size(20.dp))
                            } else {
                                Text("Save & Open Vault", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    PrivacyDialogMode.RESET -> {
                        OutlinedTextField(
                            value = accountPassword,
                            onValueChange = { accountPassword = it },
                            label = { Text("Account Login Password", color = TextSecondary) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NeonCyan) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = GlassBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("privacy_reset_account_password_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = newPin,
                            onValueChange = { if (it.length <= 12) newPin = it },
                            label = { Text("New Privacy Password", color = TextSecondary) },
                            leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null, tint = NeonCyan) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                if (accountPassword.isNotBlank() && newPin.length >= 4) onResetPassword(accountPassword, newPin)
                            }),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = GlassBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("privacy_reset_new_pin_input")
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { onResetPassword(accountPassword, newPin) },
                            enabled = !isLoading && accountPassword.isNotBlank() && newPin.length >= 4,
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("privacy_reset_submit_button")
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp))
                            } else {
                                Text("Reset & Unlock", fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        TextButton(
                            onClick = { mode = if (hasPasswordSet) PrivacyDialogMode.ENTER else PrivacyDialogMode.SETUP }
                        ) {
                            Text("Back", color = TextMuted, fontSize = 13.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        containerColor = DarkSurfaceElevated,
        shape = RoundedCornerShape(24.dp)
    )
}
