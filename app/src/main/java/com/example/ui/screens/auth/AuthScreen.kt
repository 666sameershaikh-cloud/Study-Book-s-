package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AmbientBackground
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AuthMode

@Composable
fun AuthScreen(
    initialMode: AuthMode = AuthMode.LOGIN,
    isLoading: Boolean,
    errorMessage: String?,
    successMessage: String?,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String, String, String) -> Unit,
    onForgotPassword: (String, String) -> Unit
) {
    var mode by remember { mutableStateOf(initialMode) }

    // Form inputs
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var emailOrUsername by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    AmbientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Badge
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ElectricBlue.copy(alpha = 0.15f))
                    .border(1.dp, ElectricBlue.copy(alpha = 0.35f), CircleShape)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "BOOKS STUDY",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = when (mode) {
                    AuthMode.LOGIN -> "Welcome Back"
                    AuthMode.REGISTER -> "Create Account"
                    AuthMode.FORGOT_PASSWORD -> "Reset Password"
                },
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = when (mode) {
                    AuthMode.LOGIN -> "Connect with peers & access private chats"
                    AuthMode.REGISTER -> "Join the verified community of learners"
                    AuthMode.FORGOT_PASSWORD -> "Enter your email to set a new password"
                },
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Error & Success Feedback
            AnimatedVisibility(visible = !errorMessage.isNullOrEmpty(), enter = fadeIn(), exit = fadeOut()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ErrorRed.copy(alpha = 0.15f))
                        .border(1.dp, ErrorRed.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = ErrorRed,
                        fontSize = 13.sp
                    )
                }
            }

            AnimatedVisibility(visible = !successMessage.isNullOrEmpty(), enter = fadeIn(), exit = fadeOut()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(OnlineGreen.copy(alpha = 0.15f))
                        .border(1.dp, OnlineGreen.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = successMessage ?: "",
                        color = OnlineGreen,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Glassmorphism Form Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    when (mode) {
                        AuthMode.LOGIN -> {
                            // EMAIL OR USERNAME
                            OutlinedTextField(
                                value = emailOrUsername,
                                onValueChange = { emailOrUsername = it },
                                label = { Text("Email or Username", color = TextSecondary) },
                                leadingIcon = {
                                    Icon(Icons.Default.Email, contentDescription = "Email", tint = ElectricBlue)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                                colors = outlinedFieldColors(),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_email_input")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // PASSWORD WITH EYE ICON
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Password", color = TextSecondary) },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = "Password", tint = ElectricBlue)
                                },
                                trailingIcon = {
                                    IconButton(
                                        onClick = { passwordVisible = !passwordVisible },
                                        modifier = Modifier.testTag("toggle_login_password_visibility")
                                    ) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                            tint = TextMuted
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = {
                                    focusManager.clearFocus()
                                    onLogin(emailOrUsername, password)
                                }),
                                colors = outlinedFieldColors(),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_password_input")
                            )

                            // Forgot Password Link
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "Forgot Password?",
                                    color = NeonCyan,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .clickable { mode = AuthMode.FORGOT_PASSWORD }
                                        .testTag("forgot_password_button")
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // LOGIN BUTTON
                            Button(
                                onClick = { onLogin(emailOrUsername, password) },
                                enabled = !isLoading && emailOrUsername.isNotBlank() && password.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ElectricBlue,
                                    disabledContainerColor = ElectricBlue.copy(alpha = 0.4f)
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("login_submit_button")
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(color = TextPrimary, modifier = Modifier.size(22.dp))
                                } else {
                                    Text(
                                        text = "Login",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Switch to Register
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Don't have an account? ", color = TextSecondary, fontSize = 14.sp)
                                Text(
                                    text = "Create Account",
                                    color = CyberPurple,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { mode = AuthMode.REGISTER }
                                        .testTag("switch_to_register_button")
                                )
                            }
                        }

                        AuthMode.REGISTER -> {
                            // FULL NAME
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                label = { Text("Full Name", color = TextSecondary) },
                                leadingIcon = {
                                    Icon(Icons.Default.Badge, contentDescription = "Name", tint = CyberPurple)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = FocusDirection.Down.let { ImeAction.Next }),
                                colors = outlinedFieldColors(),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("register_fullname_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // USERNAME
                            OutlinedTextField(
                                value = username,
                                onValueChange = { username = it },
                                label = { Text("Username", color = TextSecondary) },
                                leadingIcon = {
                                    Icon(Icons.Default.AlternateEmail, contentDescription = "Username", tint = CyberPurple)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                colors = outlinedFieldColors(),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("register_username_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // EMAIL
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email", color = TextSecondary) },
                                leadingIcon = {
                                    Icon(Icons.Default.Email, contentDescription = "Email", tint = CyberPurple)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                                colors = outlinedFieldColors(),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("register_email_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // PASSWORD
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Password (min 6 chars)", color = TextSecondary) },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = "Password", tint = CyberPurple)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = TextMuted
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                                colors = outlinedFieldColors(),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("register_password_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // CONFIRM PASSWORD
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                label = { Text("Confirm Password", color = TextSecondary) },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = "Confirm", tint = CyberPurple)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                        Icon(
                                            imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = TextMuted
                                        )
                                    }
                                },
                                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                                colors = outlinedFieldColors(),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("register_confirm_password_input")
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // CREATE ACCOUNT BUTTON
                            Button(
                                onClick = {
                                    onRegister(fullName, username, email, password, confirmPassword)
                                },
                                enabled = !isLoading && fullName.isNotBlank() && username.isNotBlank() && email.isNotBlank() && password.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyberPurple,
                                    disabledContainerColor = CyberPurple.copy(alpha = 0.4f)
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("register_submit_button")
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(color = TextPrimary, modifier = Modifier.size(22.dp))
                                } else {
                                    Text(
                                        text = "Create Account",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Switch back to Login
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Already have an account? ", color = TextSecondary, fontSize = 14.sp)
                                Text(
                                    text = "Login",
                                    color = ElectricBlue,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { mode = AuthMode.LOGIN }
                                        .testTag("switch_to_login_button")
                                )
                            }
                        }

                        AuthMode.FORGOT_PASSWORD -> {
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Account Email", color = TextSecondary) },
                                leadingIcon = {
                                    Icon(Icons.Default.Email, contentDescription = "Email", tint = NeonCyan)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                                colors = outlinedFieldColors(),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("forgot_email_input")
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = newPassword,
                                onValueChange = { newPassword = it },
                                label = { Text("New Password (min 6 chars)", color = TextSecondary) },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = "New Password", tint = NeonCyan)
                                },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                                colors = outlinedFieldColors(),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("forgot_new_password_input")
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { onForgotPassword(email, newPassword) },
                                enabled = !isLoading && email.isNotBlank() && newPassword.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("forgot_submit_button")
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(22.dp))
                                } else {
                                    Text(
                                        text = "Reset Password",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Back to Login",
                                    color = ElectricBlue,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { mode = AuthMode.LOGIN }
                                        .testTag("forgot_back_to_login_button")
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedContainerColor = DarkSurfaceElevated.copy(alpha = 0.5f),
    unfocusedContainerColor = DarkSurfaceElevated.copy(alpha = 0.3f),
    focusedBorderColor = ElectricBlue,
    unfocusedBorderColor = GlassBorder,
    cursorColor = ElectricBlue
)
