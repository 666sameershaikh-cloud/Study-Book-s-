package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ConnectionEntity
import com.example.data.local.MessageEntity
import com.example.data.local.UserEntity
import com.example.data.model.SchoolClass
import com.example.data.model.Subject
import com.example.data.repository.BooksStudyRepository
import com.example.data.repository.CurriculumData
import com.example.ui.components.AppTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Splash : Screen()
    data class Auth(val initialMode: AuthMode = AuthMode.LOGIN) : Screen()
    data class Main(val activeTab: AppTab = AppTab.HOME) : Screen()
    data class ClassDetail(val schoolClass: SchoolClass) : Screen()
    data class SubjectDetail(val schoolClass: SchoolClass, val subject: Subject) : Screen()
    data class ChatConversation(val peerUser: UserEntity, val isPrivate: Boolean = false) : Screen()
    object PrivateChatVault : Screen()
    data class UserProfileDetail(val user: UserEntity) : Screen()
}

enum class AuthMode {
    LOGIN,
    REGISTER,
    FORGOT_PASSWORD
}

data class ChatSummary(
    val peerUser: UserEntity,
    val lastMessage: MessageEntity?,
    val unreadCount: Int,
    val isPrivate: Boolean
)

data class ConnectionWithUser(
    val connection: ConnectionEntity,
    val peerUser: UserEntity,
    val isIncomingRequest: Boolean,
    val isOutgoingRequest: Boolean,
    val isConnected: Boolean,
    val isBlocked: Boolean
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BooksStudyRepository(AppDatabase.getDatabase(application))

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _authSuccessMessage = MutableStateFlow<String?>(null)
    val authSuccessMessage: StateFlow<String?> = _authSuccessMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Privacy lock
    private val _isPrivacyUnlocked = MutableStateFlow(false)
    val isPrivacyUnlocked: StateFlow<Boolean> = _isPrivacyUnlocked.asStateFlow()

    private val _privacyError = MutableStateFlow<String?>(null)
    val privacyError: StateFlow<String?> = _privacyError.asStateFlow()

    // Show privacy modal / eye action
    private val _showEyeMenu = MutableStateFlow(false)
    val showEyeMenu: StateFlow<Boolean> = _showEyeMenu.asStateFlow()

    private val _showPrivacyPasswordModal = MutableStateFlow(false)
    val showPrivacyPasswordModal: StateFlow<Boolean> = _showPrivacyPasswordModal.asStateFlow()

    // User profile edit and settings modals
    private val _showEditProfileModal = MutableStateFlow(false)
    val showEditProfileModal: StateFlow<Boolean> = _showEditProfileModal.asStateFlow()

    private val _showPrivacySettingsModal = MutableStateFlow(false)
    val showPrivacySettingsModal: StateFlow<Boolean> = _showPrivacySettingsModal.asStateFlow()

    private val _reportingUser = MutableStateFlow<UserEntity?>(null)
    val reportingUser: StateFlow<UserEntity?> = _reportingUser.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeDatabase()
        }
    }

    fun dismissToast() {
        _toastMessage.value = null
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setTab(tab: AppTab) {
        _currentScreen.value = Screen.Main(activeTab = tab)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleEyeMenu(show: Boolean) {
        _showEyeMenu.value = show
    }

    fun openPrivacyPasswordModal() {
        _showEyeMenu.value = false
        _privacyError.value = null
        _showPrivacyPasswordModal.value = true
    }

    fun closePrivacyPasswordModal() {
        _showPrivacyPasswordModal.value = false
        _privacyError.value = null
    }

    fun openEditProfileModal() {
        _showEditProfileModal.value = true
    }

    fun closeEditProfileModal() {
        _showEditProfileModal.value = false
    }

    fun openPrivacySettingsModal() {
        _showPrivacySettingsModal.value = true
    }

    fun closePrivacySettingsModal() {
        _showPrivacySettingsModal.value = false
    }

    fun openReportDialog(user: UserEntity) {
        _reportingUser.value = user
    }

    fun closeReportDialog() {
        _reportingUser.value = null
    }

    // AUTH
    fun login(emailOrUsername: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            _authSuccessMessage.value = null

            val result = repository.login(emailOrUsername, pass)
            _isLoading.value = false
            result.fold(
                onSuccess = { user ->
                    _currentUser.value = user
                    _currentScreen.value = Screen.Main(AppTab.HOME)
                },
                onFailure = { error ->
                    _authError.value = error.message ?: "Authentication failed"
                }
            )
        }
    }

    fun register(fullName: String, username: String, email: String, pass: String, confirmPass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            _authSuccessMessage.value = null

            if (pass != confirmPass) {
                _isLoading.value = false
                _authError.value = "Passwords do not match"
                return@launch
            }

            val result = repository.register(fullName, username, email, pass)
            _isLoading.value = false
            result.fold(
                onSuccess = {
                    _authSuccessMessage.value = "Account created successfully! Please sign in."
                    _currentScreen.value = Screen.Auth(AuthMode.LOGIN)
                },
                onFailure = { error ->
                    _authError.value = error.message ?: "Registration failed"
                }
            )
        }
    }

    fun forgotPassword(email: String, newPass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            _authSuccessMessage.value = null

            val result = repository.resetPassword(email, newPass)
            _isLoading.value = false
            result.fold(
                onSuccess = {
                    _authSuccessMessage.value = "Password reset successfully. Sign in with your new password."
                    _currentScreen.value = Screen.Auth(AuthMode.LOGIN)
                },
                onFailure = { error ->
                    _authError.value = error.message ?: "Password reset failed"
                }
            )
        }
    }

    fun logout() {
        val user = _currentUser.value
        if (user != null) {
            viewModelScope.launch {
                repository.setUserOffline(user.id)
            }
        }
        _currentUser.value = null
        _isPrivacyUnlocked.value = false
        _currentScreen.value = Screen.Auth(AuthMode.LOGIN)
    }

    // PRIVACY PASSWORD
    fun checkHasPrivacyPassword(): Boolean {
        val user = _currentUser.value ?: return false
        return !user.privacyPasswordHash.isNullOrEmpty()
    }

    fun submitPrivacyPassword(pin: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _privacyError.value = null

            val isCorrect = repository.verifyPrivacyPassword(user.id, pin)
            _isLoading.value = false
            if (isCorrect) {
                _isPrivacyUnlocked.value = true
                _showPrivacyPasswordModal.value = false
                _currentScreen.value = Screen.PrivateChatVault
            } else {
                _privacyError.value = "Incorrect Privacy Password. Try again."
            }
        }
    }

    fun setInitialPrivacyPassword(pin: String, confirmPin: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            if (pin != confirmPin) {
                _privacyError.value = "Privacy Passwords do not match"
                return@launch
            }
            if (pin.length < 4) {
                _privacyError.value = "Password must be at least 4 digits"
                return@launch
            }

            _isLoading.value = true
            _privacyError.value = null
            val result = repository.setPrivacyPassword(user.id, pin)
            _isLoading.value = false
            result.fold(
                onSuccess = {
                    // refresh current user entity
                    _currentUser.value = repository.getUserSync(user.id)
                    _isPrivacyUnlocked.value = true
                    _showPrivacyPasswordModal.value = false
                    _currentScreen.value = Screen.PrivateChatVault
                    showToast("Privacy Password set successfully")
                },
                onFailure = {
                    _privacyError.value = it.message ?: "Failed to set privacy password"
                }
            )
        }
    }

    fun resetPrivacyPassword(accountPassword: String, newPin: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _privacyError.value = null

            val result = repository.resetPrivacyPassword(user.id, accountPassword, newPin)
            _isLoading.value = false
            result.fold(
                onSuccess = {
                    _currentUser.value = repository.getUserSync(user.id)
                    _isPrivacyUnlocked.value = true
                    _showPrivacyPasswordModal.value = false
                    _currentScreen.value = Screen.PrivateChatVault
                    showToast("Privacy Password reset successfully")
                },
                onFailure = {
                    _privacyError.value = it.message ?: "Reset failed. Verify your account password."
                }
            )
        }
    }

    fun lockPrivateArea() {
        _isPrivacyUnlocked.value = false
        if (_currentScreen.value is Screen.PrivateChatVault ||
            (_currentScreen.value is Screen.ChatConversation && (_currentScreen.value as Screen.ChatConversation).isPrivate)
        ) {
            _currentScreen.value = Screen.Main(AppTab.BOOKS)
        }
    }

    // SEARCH & CONNECTIONS
    val searchUsersResult: StateFlow<List<UserEntity>> = combine(_searchQuery, _currentUser) { query, user ->
        Pair(query, user)
    }.flatMapLatest { (query, user) ->
        if (user == null) flowOf(emptyList())
        else repository.searchUsers(query, user.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userConnections: StateFlow<List<ConnectionEntity>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else repository.getConnectionsForUser(user.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOtherUsers: StateFlow<List<UserEntity>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else repository.getAllOtherUsers(user.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun sendConnectionRequest(targetUserId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.sendConnectionRequest(user.id, targetUserId)
            showToast("Connection request sent")
        }
    }

    fun acceptConnection(targetUserId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.acceptConnectionBetween(user.id, targetUserId)
            showToast("Connection accepted! You can now start private chats.")
        }
    }

    fun rejectConnection(targetUserId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.rejectConnectionBetween(user.id, targetUserId)
            showToast("Connection declined")
        }
    }

    fun cancelConnection(targetUserId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.cancelConnectionBetween(user.id, targetUserId)
            showToast("Connection request cancelled")
        }
    }

    fun removeConnection(targetUserId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.removeConnectionBetween(user.id, targetUserId)
            showToast("Connection removed")
        }
    }

    fun blockUser(targetUserId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.blockUser(user.id, targetUserId)
            showToast("User blocked")
        }
    }

    fun reportUser(reportedUserId: String, category: String, description: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.reportUser(user.id, reportedUserId, category, description)
            _reportingUser.value = null
            showToast("Report submitted. Thank you for keeping BOOKS STUDY safe.")
        }
    }

    // MESSAGES
    fun getChatMessages(peerUserId: String, isPrivate: Boolean): StateFlow<List<MessageEntity>> {
        val user = _currentUser.value
        val chatId = if (user != null) repository.getChatId(user.id, peerUserId) else ""
        return repository.getMessagesForChat(chatId, isPrivate)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun sendMessage(
        peerUserId: String,
        text: String,
        messageType: String = "TEXT",
        mediaUrl: String? = null,
        replyToId: String? = null,
        replyToText: String? = null,
        isPrivate: Boolean = false
    ) {
        val user = _currentUser.value ?: return
        if (text.isBlank() && mediaUrl.isNullOrBlank()) return

        viewModelScope.launch {
            val result = repository.sendMessage(
                senderId = user.id,
                receiverId = peerUserId,
                text = text.trim(),
                messageType = messageType,
                mediaUrl = mediaUrl,
                replyToId = replyToId,
                replyToText = replyToText,
                isPrivate = isPrivate
            )
            result.onFailure {
                showToast(it.message ?: "Failed to send message")
            }
        }
    }

    fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            repository.deleteMessage(messageId)
        }
    }

    fun updateProfile(fullName: String, bio: String, avatarColor: Long) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateProfile(user.id, fullName, bio, avatarColor)
            _currentUser.value = repository.getUserSync(user.id)
            _showEditProfileModal.value = false
            showToast("Profile updated successfully")
        }
    }

    fun updatePrivacySettings(
        showOnlineStatus: Boolean,
        showLastSeen: Boolean,
        readReceipts: Boolean,
        showTyping: Boolean,
        showMessagePreview: Boolean
    ) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updatePrivacySettings(
                user.id,
                showOnlineStatus,
                showLastSeen,
                readReceipts,
                showTyping,
                showMessagePreview
            )
            _currentUser.value = repository.getUserSync(user.id)
            _showPrivacySettingsModal.value = false
            showToast("Privacy settings saved")
        }
    }

    // Classes & Curriculum
    val curriculumClasses = CurriculumData.classes
}
