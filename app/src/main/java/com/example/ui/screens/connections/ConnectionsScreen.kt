package com.example.ui.screens.connections

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ConnectionEntity
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
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ConnectionsScreen(
    currentUser: UserEntity?,
    searchQuery: String,
    searchResults: List<UserEntity>,
    connections: List<ConnectionEntity>,
    allUsers: List<UserEntity>,
    onSearchQueryChange: (String) -> Unit,
    onConnect: (String) -> Unit,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit,
    onCancel: (String) -> Unit,
    onRemove: (String) -> Unit,
    onBlock: (String) -> Unit,
    onReport: (UserEntity) -> Unit,
    onStartChat: (UserEntity) -> Unit,
    onViewUser: (UserEntity) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Discover", "Requests", "Connected")

    // calculate counts
    val incomingRequests = connections.filter { it.status == "PENDING" && it.receiverId == currentUser?.id }
    val outgoingRequests = connections.filter { it.status == "PENDING" && it.senderId == currentUser?.id }
    val acceptedConnections = connections.filter { it.status == "ACCEPTED" }

    AmbientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            // Screen Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Connections",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Social network of student peers",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }

            // Search bar (Name or Username)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search by name or @username...", color = TextMuted, fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = ElectricBlue)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = DarkSurfaceElevated.copy(alpha = 0.5f),
                    unfocusedContainerColor = DarkSurfaceElevated.copy(alpha = 0.3f),
                    focusedBorderColor = ElectricBlue,
                    unfocusedBorderColor = GlassBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("connections_search_input")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tab bar
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = TextPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = ElectricBlue,
                        height = 3.dp
                    )
                },
                divider = {
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.08f)))
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    val badge = when (index) {
                        1 -> incomingRequests.size
                        2 -> acceptedConnections.size
                        else -> 0
                    }

                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = title,
                                    fontSize = 14.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) TextPrimary else TextMuted
                                )
                                if (badge > 0) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(if (index == 1) CyberPurple else ElectricBlue)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = badge.toString(),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Content Area
            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp, top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                when (selectedTab) {
                    0 -> { // Discover / Search
                        if (searchResults.isEmpty()) {
                            item {
                                EmptyPlaceholder("No peers found matching \"$searchQuery\"")
                            }
                        } else {
                            items(searchResults) { peer ->
                                val connection = connections.find {
                                    (it.senderId == currentUser?.id && it.receiverId == peer.id) ||
                                    (it.receiverId == currentUser?.id && it.senderId == peer.id)
                                }
                                UserConnectionCard(
                                    currentUser = currentUser,
                                    peer = peer,
                                    connection = connection,
                                    onConnect = { onConnect(peer.id) },
                                    onAccept = { onAccept(peer.id) },
                                    onReject = { onReject(peer.id) },
                                    onCancel = { onCancel(peer.id) },
                                    onRemove = { onRemove(peer.id) },
                                    onBlock = { onBlock(peer.id) },
                                    onReport = { onReport(peer) },
                                    onStartChat = { onStartChat(peer) },
                                    onViewUser = { onViewUser(peer) }
                                )
                            }
                        }
                    }

                    1 -> { // Requests
                        if (incomingRequests.isEmpty() && outgoingRequests.isEmpty()) {
                            item {
                                EmptyPlaceholder("No pending connection requests")
                            }
                        } else {
                            if (incomingRequests.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "Received Requests",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberPurple,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                                items(incomingRequests) { req ->
                                    val sender = allUsers.find { it.id == req.senderId }
                                    if (sender != null) {
                                        UserConnectionCard(
                                            currentUser = currentUser,
                                            peer = sender,
                                            connection = req,
                                            onConnect = { onConnect(sender.id) },
                                            onAccept = { onAccept(sender.id) },
                                            onReject = { onReject(sender.id) },
                                            onCancel = { onCancel(sender.id) },
                                            onRemove = { onRemove(sender.id) },
                                            onBlock = { onBlock(sender.id) },
                                            onReport = { onReport(sender) },
                                            onStartChat = { onStartChat(sender) },
                                            onViewUser = { onViewUser(sender) }
                                        )
                                    }
                                }
                            }

                            if (outgoingRequests.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "Sent Requests",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ElectricBlue,
                                        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                                    )
                                }
                                items(outgoingRequests) { req ->
                                    val receiver = allUsers.find { it.id == req.receiverId }
                                    if (receiver != null) {
                                        UserConnectionCard(
                                            currentUser = currentUser,
                                            peer = receiver,
                                            connection = req,
                                            onConnect = { onConnect(receiver.id) },
                                            onAccept = { onAccept(receiver.id) },
                                            onReject = { onReject(receiver.id) },
                                            onCancel = { onCancel(receiver.id) },
                                            onRemove = { onRemove(receiver.id) },
                                            onBlock = { onBlock(receiver.id) },
                                            onReport = { onReport(receiver) },
                                            onStartChat = { onStartChat(receiver) },
                                            onViewUser = { onViewUser(receiver) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    2 -> { // Connected
                        val connectedPeers = allUsers.filter { user ->
                            connections.any {
                                ((it.senderId == currentUser?.id && it.receiverId == user.id) ||
                                 (it.receiverId == currentUser?.id && it.senderId == user.id)) &&
                                it.status == "ACCEPTED"
                            }
                        }

                        if (connectedPeers.isEmpty()) {
                            item {
                                EmptyPlaceholder("You haven't connected with any peers yet")
                            }
                        } else {
                            items(connectedPeers) { peer ->
                                val connection = connections.find {
                                    ((it.senderId == currentUser?.id && it.receiverId == peer.id) ||
                                     (it.receiverId == currentUser?.id && it.senderId == peer.id)) &&
                                    it.status == "ACCEPTED"
                                }
                                UserConnectionCard(
                                    currentUser = currentUser,
                                    peer = peer,
                                    connection = connection,
                                    onConnect = { onConnect(peer.id) },
                                    onAccept = { onAccept(peer.id) },
                                    onReject = { onReject(peer.id) },
                                    onCancel = { onCancel(peer.id) },
                                    onRemove = { onRemove(peer.id) },
                                    onBlock = { onBlock(peer.id) },
                                    onReport = { onReport(peer) },
                                    onStartChat = { onStartChat(peer) },
                                    onViewUser = { onViewUser(peer) }
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
fun UserConnectionCard(
    currentUser: UserEntity?,
    peer: UserEntity,
    connection: ConnectionEntity?,
    onConnect: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onCancel: () -> Unit,
    onRemove: () -> Unit,
    onBlock: () -> Unit,
    onReport: () -> Unit,
    onStartChat: () -> Unit,
    onViewUser: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewUser() }
            .testTag("user_connection_card_${peer.username}"),
        cornerRadius = 18.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarView(
                    name = peer.fullName,
                    avatarColor = peer.avatarColor,
                    size = 50.dp,
                    isOnline = peer.onlineStatus
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = peer.fullName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "@${peer.username}",
                        fontSize = 12.sp,
                        color = CyberPurple,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Options 3-dots menu for Block & Report
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(DarkSurfaceElevated)
                    ) {
                        if (connection?.status == "ACCEPTED") {
                            DropdownMenuItem(
                                text = {
                                    Row {
                                        Icon(Icons.Default.PersonRemove, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Remove Connection", color = ErrorRed)
                                    }
                                },
                                onClick = {
                                    showMenu = false
                                    onRemove()
                                }
                            )
                        }

                        DropdownMenuItem(
                            text = {
                                Row {
                                    Icon(Icons.Default.Block, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Block User", color = ErrorRed)
                                }
                            },
                            onClick = {
                                showMenu = false
                                onBlock()
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Row {
                                    Icon(Icons.Default.Flag, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Report User", color = TextPrimary)
                                }
                            },
                            onClick = {
                                showMenu = false
                                onReport()
                            }
                        )
                    }
                }
            }

            if (peer.bio.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = peer.bio,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action buttons according to connection state:
            // Connect, Pending, Accept, Reject, Cancel, Remove, Block, Report
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when {
                    connection == null -> {
                        Button(
                            onClick = onConnect,
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Connect", fontSize = 13.sp)
                        }
                    }

                    connection.status == "PENDING" && connection.senderId == currentUser?.id -> {
                        OutlinedButton(
                            onClick = onCancel,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Cancel Request", fontSize = 12.sp)
                        }
                    }

                    connection.status == "PENDING" && connection.receiverId == currentUser?.id -> {
                        OutlinedButton(
                            onClick = onReject,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Decline", fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = onAccept,
                            colors = ButtonDefaults.buttonColors(containerColor = OnlineGreen),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Accept", fontSize = 12.sp)
                        }
                    }

                    connection.status == "ACCEPTED" -> {
                        Button(
                            onClick = onStartChat,
                            colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Private Chat", fontSize = 12.sp)
                        }
                    }

                    connection.status == "BLOCKED" -> {
                        Text(
                            text = "Blocked",
                            fontSize = 12.sp,
                            color = ErrorRed,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyPlaceholder(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurfaceElevated.copy(alpha = 0.4f))
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = TextSecondary,
            fontSize = 14.sp
        )
    }
}
