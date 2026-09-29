package com.example.ui.screens.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.local.ConnectionEntity
import com.example.data.local.UserEntity
import com.example.ui.components.AmbientBackground
import com.example.ui.components.AvatarView
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VioletGlow

@Composable
fun HomeScreen(
    currentUser: UserEntity?,
    allUsers: List<UserEntity>,
    connections: List<ConnectionEntity>,
    onNavigateToSearch: () -> Unit,
    onNavigateToChat: (UserEntity) -> Unit,
    onNavigateToProfile: () -> Unit,
    onConnectUser: (String) -> Unit
) {
    AmbientBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 100.dp, top = 16.dp)
        ) {
            // Header / Greeting
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Welcome back ",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "👋",
                                fontSize = 22.sp
                            )
                        }
                        Text(
                            text = currentUser?.fullName ?: "Learner",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NeonCyan
                        )
                    }

                    if (currentUser != null) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { onNavigateToProfile() }
                        ) {
                            AvatarView(
                                name = currentUser.fullName,
                                avatarColor = currentUser.avatarColor,
                                size = 46.dp,
                                isOnline = true
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // User Mini Profile Card
            item {
                if (currentUser != null) {
                    val acceptedConnectionsCount = connections.count { it.status == "ACCEPTED" }
                    val pendingRequestsCount = connections.count { it.status == "PENDING" && it.receiverId == currentUser.id }

                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_user_mini_profile"),
                        cornerRadius = 22.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AvatarView(
                                    name = currentUser.fullName,
                                    avatarColor = currentUser.avatarColor,
                                    size = 56.dp,
                                    isOnline = true
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = currentUser.fullName,
                                        fontSize = 17.sp,
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
                                        Text(
                                            text = currentUser.bio,
                                            fontSize = 12.sp,
                                            color = TextSecondary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Mini Stats Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(DarkSurfaceElevated.copy(alpha = 0.6f))
                                    .padding(vertical = 10.dp, horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = acceptedConnectionsCount.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = ElectricBlue
                                    )
                                    Text(
                                        text = "Connections",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(26.dp)
                                        .background(Color.White.copy(alpha = 0.1f))
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = pendingRequestsCount.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = CyberPurple
                                    )
                                    Text(
                                        text = "Requests",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(26.dp)
                                        .background(Color.White.copy(alpha = 0.1f))
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "12",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = NeonCyan
                                    )
                                    Text(
                                        text = "Curriculum Ref",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Recent Connections
            item {
                val acceptedConnectionUserIds = connections
                    .filter { it.status == "ACCEPTED" }
                    .map { if (it.senderId == currentUser?.id) it.receiverId else it.senderId }
                    .toSet()

                val recentConnectedUsers = allUsers.filter { it.id in acceptedConnectionUserIds }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Connections",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    if (recentConnectedUsers.isNotEmpty()) {
                        Text(
                            text = "${recentConnectedUsers.size} peers",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (recentConnectedUsers.isEmpty()) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 16.dp,
                        borderGlow = false
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No active connections yet",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onNavigateToSearch,
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue.copy(alpha = 0.8f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Find Peers to Connect", fontSize = 13.sp)
                            }
                        }
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(recentConnectedUsers) { peer ->
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onNavigateToChat(peer) }
                                    .background(DarkSurfaceElevated.copy(alpha = 0.5f))
                                    .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AvatarView(
                                    name = peer.fullName,
                                    avatarColor = peer.avatarColor,
                                    size = 50.dp,
                                    isOnline = peer.onlineStatus
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = peer.fullName.split(" ").firstOrNull() ?: peer.fullName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Chat",
                                    fontSize = 11.sp,
                                    color = NeonCyan,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // People You May Know
            item {
                val nonConnectedUsers = allUsers.filter { user ->
                    val conn = connections.find {
                        (it.senderId == currentUser?.id && it.receiverId == user.id) ||
                        (it.receiverId == currentUser?.id && it.senderId == user.id)
                    }
                    conn == null || conn.status != "ACCEPTED"
                }.take(5)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "People you may know",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "Explore all",
                        color = ElectricBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToSearch() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    nonConnectedUsers.forEach { user ->
                        val connection = connections.find {
                            (it.senderId == currentUser?.id && it.receiverId == user.id) ||
                            (it.receiverId == currentUser?.id && it.senderId == user.id)
                        }

                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            cornerRadius = 16.dp,
                            borderGlow = false
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AvatarView(
                                    name = user.fullName,
                                    avatarColor = user.avatarColor,
                                    size = 46.dp,
                                    isOnline = user.onlineStatus
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = user.fullName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "@${user.username}",
                                        fontSize = 12.sp,
                                        color = TextMuted
                                    )
                                    if (user.bio.isNotEmpty()) {
                                        Text(
                                            text = user.bio,
                                            fontSize = 11.sp,
                                            color = TextSecondary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                when {
                                    connection == null -> {
                                        Button(
                                            onClick = { onConnectUser(user.id) },
                                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(15.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Connect", fontSize = 12.sp)
                                        }
                                    }
                                    connection.status == "PENDING" && connection.senderId == currentUser?.id -> {
                                        OutlinedButton(
                                            onClick = {},
                                            enabled = false,
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.HourglassTop, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextMuted)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Pending", fontSize = 12.sp, color = TextMuted)
                                        }
                                    }
                                    connection.status == "ACCEPTED" -> {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(ElectricBlue.copy(alpha = 0.2f))
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text("Connected", fontSize = 12.sp, color = ElectricBlue, fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Recent Chats Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Chats",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Go to chats",
                        tint = ElectricBlue,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onNavigateToSearch() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                val connectedUsers = allUsers.filter { user ->
                    connections.any {
                        ((it.senderId == currentUser?.id && it.receiverId == user.id) ||
                         (it.receiverId == currentUser?.id && it.senderId == user.id)) &&
                        it.status == "ACCEPTED"
                    }
                }

                if (connectedUsers.isEmpty()) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 16.dp,
                        borderGlow = false
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ChatBubble, contentDescription = null, tint = CyberPurple, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("No conversations yet", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("Connect with peers to start messaging", color = TextSecondary, fontSize = 12.sp)
                            }
                        }
                    }
                } else {
                    connectedUsers.take(3).forEach { peer ->
                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                                .clickable { onNavigateToChat(peer) },
                            cornerRadius = 16.dp,
                            borderGlow = false
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AvatarView(
                                    name = peer.fullName,
                                    avatarColor = peer.avatarColor,
                                    size = 44.dp,
                                    isOnline = peer.onlineStatus
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = peer.fullName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Tap to open chat",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(ElectricBlue.copy(alpha = 0.15f))
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.ChatBubble,
                                        contentDescription = "Chat",
                                        tint = ElectricBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
