package com.example.ui.screens.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MessageEntity
import com.example.data.local.UserEntity
import com.example.ui.components.AmbientBackground
import com.example.ui.components.AvatarView
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatConversationScreen(
    currentUser: UserEntity?,
    peerUser: UserEntity,
    isPrivate: Boolean,
    messages: List<MessageEntity>,
    onBack: () -> Unit,
    onSendMessage: (peerId: String, text: String, messageType: String, mediaUrl: String?, replyToId: String?, replyToText: String?, isPrivate: Boolean) -> Unit,
    onDeleteMessage: (String) -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var inputText by remember { mutableStateOf("") }
    var replyingToMessage by remember { mutableStateOf<MessageEntity?>(null) }
    var selectedActionMessage by remember { mutableStateOf<MessageEntity?>(null) }
    var showAttachmentSheet by remember { mutableStateOf(false) }

    // Scroll to bottom when message arrives
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    AmbientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
        ) {
            // Chat Header
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 0.dp,
                borderGlow = isPrivate
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("chat_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }

                    AvatarView(
                        name = peerUser.fullName,
                        avatarColor = peerUser.avatarColor,
                        size = 40.dp,
                        isOnline = peerUser.onlineStatus
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = peerUser.fullName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            if (isPrivate) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(CyberPurple.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Private", fontSize = 10.sp, color = CyberPurple, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Text(
                            text = if (peerUser.onlineStatus) "Online" else "Offline",
                            fontSize = 12.sp,
                            color = if (peerUser.onlineStatus) NeonCyan else TextMuted
                        )
                    }

                    if (isPrivate) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(CyberPurple.copy(alpha = 0.2f))
                                .padding(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "End-to-end Vault",
                                tint = CyberPurple,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Messages List
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (messages.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isPrivate) CyberPurple.copy(alpha = 0.15f) else ElectricBlue.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPrivate) Icons.Default.Security else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (isPrivate) CyberPurple else ElectricBlue,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (isPrivate) "End-to-End Vault Conversation" else "Encrypted 1-to-1 Connection",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Send a message to start conversation with ${peerUser.fullName.split(" ").firstOrNull()}",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(messages) { msg ->
                            val isMe = msg.senderId == currentUser?.id
                            MessageBubble(
                                message = msg,
                                isMe = isMe,
                                onLongClick = { selectedActionMessage = msg }
                            )
                        }
                    }
                }
            }

            // Reply Preview Banner
            AnimatedVisibility(visible = replyingToMessage != null) {
                if (replyingToMessage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkSurfaceElevated)
                            .border(width = 1.dp, color = GlassBorder)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(32.dp)
                                    .background(ElectricBlue)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (replyingToMessage?.senderId == currentUser?.id) "Replying to yourself" else "Replying to ${peerUser.fullName}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricBlue
                                )
                                Text(
                                    text = replyingToMessage?.text ?: "",
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            IconButton(
                                onClick = { replyingToMessage = null },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Cancel reply", tint = TextMuted)
                            }
                        }
                    }
                }
            }

            // Chat Input Bar (Normal standard Android keyboard)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceGlass)
                    .border(width = 1.dp, color = GlassBorder)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Attachment button
                    IconButton(
                        onClick = { showAttachmentSheet = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = "Attach media",
                            tint = NeonCyan
                        )
                    }

                    // Input Text Field
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Write a message...", color = TextMuted, fontSize = 14.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = DarkSurfaceElevated.copy(alpha = 0.6f),
                            unfocusedContainerColor = DarkSurfaceElevated.copy(alpha = 0.4f),
                            focusedBorderColor = if (isPrivate) CyberPurple else ElectricBlue,
                            unfocusedBorderColor = GlassBorder
                        ),
                        shape = RoundedCornerShape(22.dp),
                        singleLine = false,
                        maxLines = 4,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (inputText.isNotBlank()) {
                                    onSendMessage(
                                        peerUser.id,
                                        inputText.trim(),
                                        "TEXT",
                                        null,
                                        replyingToMessage?.id,
                                        replyingToMessage?.text,
                                        isPrivate
                                    )
                                    inputText = ""
                                    replyingToMessage = null
                                }
                            }
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_message_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Send Button
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    if (isPrivate) listOf(CyberPurple, ElectricBlue) else listOf(ElectricBlue, NeonCyan)
                                )
                            )
                            .clickable {
                                if (inputText.isNotBlank()) {
                                    onSendMessage(
                                        peerUser.id,
                                        inputText.trim(),
                                        "TEXT",
                                        null,
                                        replyingToMessage?.id,
                                        replyingToMessage?.text,
                                        isPrivate
                                    )
                                    inputText = ""
                                    replyingToMessage = null
                                }
                            }
                            .testTag("chat_send_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    // Message Actions Dialog (Reply, Copy, Delete)
    if (selectedActionMessage != null) {
        val targetMsg = selectedActionMessage!!
        AlertDialog(
            onDismissRequest = { selectedActionMessage = null },
            title = { Text("Message Options", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Reply
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                replyingToMessage = targetMsg
                                selectedActionMessage = null
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Reply, contentDescription = null, tint = ElectricBlue)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Reply", color = TextPrimary, fontSize = 15.sp)
                    }

                    // Copy
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("chat_message", targetMsg.text)
                                clipboard.setPrimaryClip(clip)
                                selectedActionMessage = null
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = NeonCyan)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Copy to Clipboard", color = TextPrimary, fontSize = 15.sp)
                    }

                    // Delete
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDeleteMessage(targetMsg.id)
                                selectedActionMessage = null
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = ErrorRed)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Delete Message", color = ErrorRed, fontSize = 15.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedActionMessage = null }) {
                    Text("Close", color = TextSecondary)
                }
            },
            containerColor = DarkSurfaceElevated
        )
    }

    // Attachment Modal Sheet (Image, Voice, File)
    if (showAttachmentSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAttachmentSheet = false },
            containerColor = DarkSurfaceElevated
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Share Attachment",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Image
                    AttachmentOptionItem(
                        icon = Icons.Default.Image,
                        label = "Image",
                        color = ElectricBlue,
                        onClick = {
                            showAttachmentSheet = false
                            onSendMessage(
                                peerUser.id,
                                "📷 Shared study notes illustration",
                                "IMAGE",
                                "https://images.unsplash.com/photo-1532012164546-f432f2e37947",
                                null,
                                null,
                                isPrivate
                            )
                        }
                    )

                    // Voice Note
                    AttachmentOptionItem(
                        icon = Icons.Default.Mic,
                        label = "Voice Note",
                        color = CyberPurple,
                        onClick = {
                            showAttachmentSheet = false
                            onSendMessage(
                                peerUser.id,
                                "🎙️ Voice message (0:42)",
                                "VOICE",
                                null,
                                null,
                                null,
                                isPrivate
                            )
                        }
                    )

                    // File / PDF
                    AttachmentOptionItem(
                        icon = Icons.Default.PictureAsPdf,
                        label = "Document",
                        color = NeonCyan,
                        onClick = {
                            showAttachmentSheet = false
                            onSendMessage(
                                peerUser.id,
                                "📄 Physics_Formula_Sheet.pdf (1.4 MB)",
                                "FILE",
                                null,
                                null,
                                null,
                                isPrivate
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun AttachmentOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f))
                .border(1.dp, color.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = color, modifier = Modifier.size(26.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, fontSize = 12.sp, color = TextPrimary)
    }
}

@Composable
fun MessageBubble(
    message: MessageEntity,
    isMe: Boolean,
    onLongClick: () -> Unit
) {
    val bubbleColor = if (isMe) {
        Brush.linearGradient(listOf(ElectricBlue.copy(alpha = 0.9f), CyberPurple.copy(alpha = 0.85f)))
    } else {
        Brush.linearGradient(listOf(DarkSurfaceElevated.copy(alpha = 0.95f), DarkSurfaceGlass.copy(alpha = 0.9f)))
    }

    val bubbleShape = if (isMe) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    val timeString = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(message.timestamp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(bubbleShape)
                .background(bubbleColor)
                .border(
                    width = 1.dp,
                    color = if (isMe) ElectricBlue.copy(alpha = 0.4f) else GlassBorder,
                    shape = bubbleShape
                )
                .clickable { onLongClick() }
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column {
                // Reply Quote Preview
                if (!message.replyToText.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.25f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = message.replyToText,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // Message Text
                Text(
                    text = message.text,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Timestamp and Status (Sent, Delivered, Seen)
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timeString,
                        fontSize = 10.sp,
                        color = if (isMe) Color.White.copy(alpha = 0.7f) else TextMuted
                    )

                    if (isMe) {
                        Spacer(modifier = Modifier.width(4.dp))
                        when (message.status) {
                            "SENT" -> {
                                Icon(
                                    Icons.Default.Done,
                                    contentDescription = "Sent",
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            "DELIVERED" -> {
                                Icon(
                                    Icons.Default.DoneAll,
                                    contentDescription = "Delivered",
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            "SEEN" -> {
                                Icon(
                                    Icons.Default.DoneAll,
                                    contentDescription = "Seen",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
