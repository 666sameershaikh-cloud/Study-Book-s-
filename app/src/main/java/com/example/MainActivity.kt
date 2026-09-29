package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.UserEntity
import com.example.ui.components.AppTab
import com.example.ui.components.BooksStudyBottomNav
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.books.BooksScreen
import com.example.ui.screens.books.ClassDetailScreen
import com.example.ui.screens.books.SubjectDetailScreen
import com.example.ui.screens.chat.ChatConversationScreen
import com.example.ui.screens.chat.ChatListScreen
import com.example.ui.screens.connections.ConnectionsScreen
import com.example.ui.screens.connections.UserProfileDetailScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.privacy.PrivacyPasswordDialog
import com.example.ui.screens.privacy.PrivateChatVaultScreen
import com.example.ui.screens.profile.EditProfileDialog
import com.example.ui.screens.profile.PrivacySettingsDialog
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.profile.ReportUserDialog
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AuthMode
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                BooksStudyApp()
            }
        }
    }
}

@Composable
fun BooksStudyApp(viewModel: MainViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val authError by viewModel.authError.collectAsState()
    val authSuccess by viewModel.authSuccessMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchUsersResult.collectAsState()
    val connections by viewModel.userConnections.collectAsState()
    val allUsers by viewModel.allOtherUsers.collectAsState()

    val showPrivacyModal by viewModel.showPrivacyPasswordModal.collectAsState()
    val privacyError by viewModel.privacyError.collectAsState()
    val showEditProfileModal by viewModel.showEditProfileModal.collectAsState()
    val showPrivacySettingsModal by viewModel.showPrivacySettingsModal.collectAsState()
    val reportingUser by viewModel.reportingUser.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    var showPrivacyOverviewDialog by remember { mutableStateOf(false) }
    var showReaderSettingsDialog by remember { mutableStateOf(false) }

    // Auto dismiss toast
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            kotlinx.coroutines.delay(3200)
            viewModel.dismissToast()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val screen = currentScreen) {
            is Screen.Splash -> {
                SplashScreen(
                    onSplashFinished = {
                        if (currentUser != null) {
                            viewModel.navigateTo(Screen.Main(AppTab.HOME))
                        } else {
                            viewModel.navigateTo(Screen.Auth(AuthMode.LOGIN))
                        }
                    }
                )
            }

            is Screen.Auth -> {
                AuthScreen(
                    initialMode = screen.initialMode,
                    isLoading = isLoading,
                    errorMessage = authError,
                    successMessage = authSuccess,
                    onLogin = { email, pass -> viewModel.login(email, pass) },
                    onRegister = { name, user, email, pass, confirm ->
                        viewModel.register(name, user, email, pass, confirm)
                    },
                    onForgotPassword = { email, newPass -> viewModel.forgotPassword(email, newPass) }
                )
            }

            is Screen.Main -> {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DeepBlack,
                    bottomBar = {
                        BooksStudyBottomNav(
                            currentTab = screen.activeTab,
                            unreadChatCount = 0,
                            pendingConnectionCount = connections.count { it.status == "PENDING" && it.receiverId == currentUser?.id },
                            onTabSelected = { newTab -> viewModel.setTab(newTab) }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        when (screen.activeTab) {
                            AppTab.HOME -> {
                                HomeScreen(
                                    currentUser = currentUser,
                                    allUsers = allUsers,
                                    connections = connections,
                                    onNavigateToSearch = { viewModel.setTab(AppTab.CONNECTIONS) },
                                    onNavigateToChat = { peer ->
                                        viewModel.navigateTo(Screen.ChatConversation(peerUser = peer, isPrivate = false))
                                    },
                                    onNavigateToProfile = { viewModel.setTab(AppTab.PROFILE) },
                                    onConnectUser = { peerId -> viewModel.sendConnectionRequest(peerId) }
                                )
                            }

                            AppTab.BOOKS -> {
                                BooksScreen(
                                    classes = viewModel.curriculumClasses,
                                    onClassClick = { schoolClass ->
                                        viewModel.navigateTo(Screen.ClassDetail(schoolClass))
                                    },
                                    onEyeMenuHideClick = {
                                        viewModel.openPrivacyPasswordModal()
                                    },
                                    onEyeMenuPrivacyClick = {
                                        showPrivacyOverviewDialog = true
                                    },
                                    onEyeMenuSettingsClick = {
                                        showReaderSettingsDialog = true
                                    }
                                )
                            }

                            AppTab.CONNECTIONS -> {
                                ConnectionsScreen(
                                    currentUser = currentUser,
                                    searchQuery = searchQuery,
                                    searchResults = searchResults,
                                    connections = connections,
                                    allUsers = allUsers,
                                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                    onConnect = { viewModel.sendConnectionRequest(it) },
                                    onAccept = { viewModel.acceptConnection(it) },
                                    onReject = { viewModel.rejectConnection(it) },
                                    onCancel = { viewModel.cancelConnection(it) },
                                    onRemove = { viewModel.removeConnection(it) },
                                    onBlock = { viewModel.blockUser(it) },
                                    onReport = { viewModel.openReportDialog(it) },
                                    onStartChat = { peer ->
                                        viewModel.navigateTo(Screen.ChatConversation(peerUser = peer, isPrivate = false))
                                    },
                                    onViewUser = { peer ->
                                        viewModel.navigateTo(Screen.UserProfileDetail(peer))
                                    }
                                )
                            }

                            AppTab.CHAT -> {
                                ChatListScreen(
                                    currentUser = currentUser,
                                    allUsers = allUsers,
                                    connections = connections,
                                    onOpenChat = { peer ->
                                        viewModel.navigateTo(Screen.ChatConversation(peerUser = peer, isPrivate = false))
                                    },
                                    onNavigateToConnections = { viewModel.setTab(AppTab.CONNECTIONS) }
                                )
                            }

                            AppTab.PROFILE -> {
                                ProfileScreen(
                                    currentUser = currentUser,
                                    onEditProfileClick = { viewModel.openEditProfileModal() },
                                    onPrivacySettingsClick = { viewModel.openPrivacySettingsModal() },
                                    onPrivacyPasswordResetClick = { viewModel.openPrivacyPasswordModal() },
                                    onLogoutClick = { viewModel.logout() }
                                )
                            }
                        }
                    }
                }
            }

            is Screen.ClassDetail -> {
                ClassDetailScreen(
                    schoolClass = screen.schoolClass,
                    onBack = { viewModel.navigateTo(Screen.Main(AppTab.BOOKS)) },
                    onSelectSubject = { subject ->
                        viewModel.navigateTo(Screen.SubjectDetail(screen.schoolClass, subject))
                    }
                )
            }

            is Screen.SubjectDetail -> {
                SubjectDetailScreen(
                    schoolClass = screen.schoolClass,
                    subject = screen.subject,
                    onBack = { viewModel.navigateTo(Screen.ClassDetail(screen.schoolClass)) }
                )
            }

            is Screen.ChatConversation -> {
                val messages by viewModel.getChatMessages(screen.peerUser.id, screen.isPrivate).collectAsState()
                ChatConversationScreen(
                    currentUser = currentUser,
                    peerUser = screen.peerUser,
                    isPrivate = screen.isPrivate,
                    messages = messages,
                    onBack = {
                        if (screen.isPrivate) {
                            viewModel.navigateTo(Screen.PrivateChatVault)
                        } else {
                            viewModel.navigateTo(Screen.Main(AppTab.CHAT))
                        }
                    },
                    onSendMessage = { peerId, text, type, mediaUrl, replyId, replyText, isPriv ->
                        viewModel.sendMessage(peerId, text, type, mediaUrl, replyId, replyText, isPriv)
                    },
                    onDeleteMessage = { messageId ->
                        viewModel.deleteMessage(messageId)
                    }
                )
            }

            is Screen.PrivateChatVault -> {
                PrivateChatVaultScreen(
                    currentUser = currentUser,
                    allUsers = allUsers,
                    connections = connections,
                    onBackAndLock = { viewModel.lockPrivateArea() },
                    onOpenPrivateChat = { peer ->
                        viewModel.navigateTo(Screen.ChatConversation(peerUser = peer, isPrivate = true))
                    }
                )
            }

            is Screen.UserProfileDetail -> {
                val connection = connections.find {
                    (it.senderId == currentUser?.id && it.receiverId == screen.user.id) ||
                    (it.receiverId == currentUser?.id && it.senderId == screen.user.id)
                }

                UserProfileDetailScreen(
                    currentUser = currentUser,
                    user = screen.user,
                    connection = connection,
                    onBack = { viewModel.navigateTo(Screen.Main(AppTab.CONNECTIONS)) },
                    onConnect = { viewModel.sendConnectionRequest(screen.user.id) },
                    onAccept = { viewModel.acceptConnection(screen.user.id) },
                    onReject = { viewModel.rejectConnection(screen.user.id) },
                    onCancel = { viewModel.cancelConnection(screen.user.id) },
                    onRemove = { viewModel.removeConnection(screen.user.id) },
                    onBlock = { viewModel.blockUser(screen.user.id) },
                    onReport = { viewModel.openReportDialog(screen.user) },
                    onStartChat = {
                        viewModel.navigateTo(Screen.ChatConversation(peerUser = screen.user, isPrivate = false))
                    }
                )
            }
        }

        // In-app Notification / Toast Bar
        AnimatedVisibility(
            visible = !toastMessage.isNullOrEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 16.dp, start = 20.dp, end = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurfaceElevated.copy(alpha = 0.95f))
                    .border(1.dp, ElectricBlue.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        text = toastMessage ?: "",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // Privacy Password Dialog (Eye -> Hide -> Privacy Password -> Private Chats)
    if (showPrivacyModal) {
        val hasPass = viewModel.checkHasPrivacyPassword()
        PrivacyPasswordDialog(
            hasPasswordSet = hasPass,
            errorMessage = privacyError,
            isLoading = isLoading,
            onDismiss = { viewModel.closePrivacyPasswordModal() },
            onSubmitPassword = { pin -> viewModel.submitPrivacyPassword(pin) },
            onSetupPassword = { pin, confirm -> viewModel.setInitialPrivacyPassword(pin, confirm) },
            onResetPassword = { accountPass, newPin -> viewModel.resetPrivacyPassword(accountPass, newPin) }
        )
    }

    // Privacy Overview Dialog (from Eye -> Privacy)
    if (showPrivacyOverviewDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyOverviewDialog = false },
            title = { Text("Zero-Knowledge Privacy", color = TextPrimary) },
            text = {
                Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "BOOKS STUDY is designed with student confidentiality in mind.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "• Private Chats are isolated in a separate encrypted vault.\n• The Privacy Password is never displayed or saved in plain text.\n• Only accepted, mutual connections can message each other.\n• You can lock and hide your conversations instantly.",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyOverviewDialog = false }) {
                    Text("Got it", color = CyberPurple)
                }
            },
            containerColor = DarkSurfaceElevated
        )
    }

    // Reader Settings Dialog (from Eye -> Settings)
    if (showReaderSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showReaderSettingsDialog = false },
            title = { Text("Curriculum Reader Settings", color = TextPrimary) },
            text = {
                Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
                    Text("• Font Scale: Standard Responsive SP", color = TextSecondary, fontSize = 13.sp)
                    Text("• Class 1–12 Reference Mode: Offline SQLite Persistence", color = TextSecondary, fontSize = 13.sp)
                    Text("• Theme: OLED Deep Space & Blue/Purple Ambient Glow", color = TextSecondary, fontSize = 13.sp)
                    Text("• Keyboard: Standard Android System IME", color = TextSecondary, fontSize = 13.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showReaderSettingsDialog = false }) {
                    Text("Done", color = ElectricBlue)
                }
            },
            containerColor = DarkSurfaceElevated
        )
    }

    // Edit Profile Modal
    if (showEditProfileModal && currentUser != null) {
        EditProfileDialog(
            currentUser = currentUser!!,
            onDismiss = { viewModel.closeEditProfileModal() },
            onSaveProfile = { name, bio, color -> viewModel.updateProfile(name, bio, color) }
        )
    }

    // Privacy Settings Modal
    if (showPrivacySettingsModal && currentUser != null) {
        PrivacySettingsDialog(
            currentUser = currentUser!!,
            onDismiss = { viewModel.closePrivacySettingsModal() },
            onSavePrivacy = { online, lastSeen, readRec, typing, preview ->
                viewModel.updatePrivacySettings(online, lastSeen, readRec, typing, preview)
            }
        )
    }

    // Report User Dialog
    if (reportingUser != null) {
        ReportUserDialog(
            targetUser = reportingUser!!,
            onDismiss = { viewModel.closeReportDialog() },
            onSubmitReport = { category, details ->
                viewModel.reportUser(reportingUser!!.id, category, details)
            }
        )
    }
}
