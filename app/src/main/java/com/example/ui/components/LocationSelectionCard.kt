package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppStrings
import com.example.data.ServerModel
import com.example.ui.theme.VpnNeonGreen
import com.example.ui.theme.VpnTextMuted
import com.example.ui.theme.VpnTextPrimary
import com.example.ui.theme.VpnTextSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelectionCard(
    servers: List<ServerModel>,
    selectedServer: ServerModel?,
    langCode: String,
    isFetching: Boolean = false,
    fetchError: String? = null,
    onRetry: () -> Unit = {},
    onSelectServer: (ServerModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val isFa = langCode == "fa"

    val currentServer = selectedServer ?: servers.firstOrNull()

    // 1. Sleek Server Selection Card on the Home Screen (Without Ping)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A2133),
                        Color(0xFF131926)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF2E3D5C),
                        Color(0xFF1F2B44)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                if (servers.isNotEmpty()) {
                    showSheet = true
                } else {
                    onRetry()
                }
            }
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("location_selection_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("toggle_servers_card"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Server details (Flag, Name)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (isFetching && servers.isEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp,
                        color = Color(0xFF3A82F7)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = if (isFa) "در حال دریافت سرورها..." else "Loading servers...",
                        color = VpnTextSecondary,
                        fontSize = 14.sp
                    )
                } else if (servers.isEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Retry",
                        tint = Color(0xFFFF6B6B),
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isFa) "خطا در دریافت سرورها" else "Server list offline",
                            color = Color(0xFFFF6B6B),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (isFa) "برای تلاش مجدد کلیک کنید" else "Tap to retry",
                            color = VpnTextMuted,
                            fontSize = 11.sp
                        )
                    }
                } else if (currentServer != null) {
                    val countryCode = currentServer.country_code
                    val countryDisplayName = if (currentServer.country.isNotBlank()) {
                        currentServer.country
                    } else {
                        AppStrings.getServerName(countryCode, langCode)
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0F1522))
                            .border(1.5.dp, Color(0xFF3A82F7).copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        CountryFlag(countryCode = countryCode, size = 28.dp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = AppStrings.get("selected_server_location", langCode),
                            color = VpnTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = countryDisplayName,
                            color = VpnTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // "Change" or "Retry" pill button on the right
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF222F47))
                    .border(1.dp, Color(0xFF3A82F7).copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .clickable {
                        if (servers.isNotEmpty()) {
                            showSheet = true
                        } else {
                            onRetry()
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (servers.isEmpty()) {
                            if (isFa) "تلاش مجدد" else "Retry"
                        } else {
                            AppStrings.get("change_server", langCode)
                        },
                        color = Color(0xFF60A5FA),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (servers.isEmpty()) Icons.Default.Refresh else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Action",
                        tint = Color(0xFF60A5FA),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }

    // 2. Modal Bottom Sheet for Server Selection
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
            },
            sheetState = sheetState,
            containerColor = Color(0xFF121724),
            scrimColor = Color.Black.copy(alpha = 0.7f),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    color = Color(0xFF3A82F7).copy(alpha = 0.4f)
                )
            },
            modifier = Modifier.testTag("server_selection_modal")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .padding(horizontal = 20.dp)
            ) {
                // Header Row with Title, Server Count, and Prominent Close (X) Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = AppStrings.get("select_location", langCode),
                            color = VpnTextPrimary,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${servers.size} ${AppStrings.get("high_speed_locations", langCode)}",
                            color = VpnTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    // Prominent Close (X) Button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF222B3D))
                            .border(1.dp, Color(0xFF3A82F7).copy(alpha = 0.3f), CircleShape)
                        .clickable {
                            coroutineScope.launch {
                                sheetState.hide()
                                showSheet = false
                            }
                        }
                        .testTag("close_server_modal"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close server selection",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Section Label
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.get("available_locations", langCode),
                        color = VpnTextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "WireGuard Protocol",
                        color = Color(0xFF3A82F7),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Scrollable List of Servers from GitHub
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 28.dp)
                ) {
                    items(servers, key = { it.id }) { server ->
                        val isSelected = server.id == (currentServer?.id ?: "") ||
                            (server.country_code.equals(currentServer?.country_code, ignoreCase = true) && server.server_name.equals(currentServer?.server_name, ignoreCase = true))

                        val countryDisplayName = if (server.country.isNotBlank()) {
                            server.country
                        } else {
                            AppStrings.getServerName(server.country_code, langCode)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isSelected)
                                        Color(0xFF142426)
                                    else
                                        Color(0xFF182030)
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) VpnNeonGreen else Color(0xFF253047),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    onSelectServer(server)
                                    coroutineScope.launch {
                                        sheetState.hide()
                                        showSheet = false
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                                .testTag("server_item_${server.id}")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Flag & Server Details
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF0F1522)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CountryFlag(
                                            countryCode = server.country_code,
                                            size = 28.dp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column {
                                        Text(
                                            text = countryDisplayName,
                                            color = if (isSelected) VpnNeonGreen else VpnTextPrimary,
                                            fontSize = 15.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${server.server_name} • WireGuard",
                                            color = VpnTextMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                // Selection Indicator Checkmark
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(VpnNeonGreen),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color(0xFF0D1821),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .border(1.5.dp, Color(0xFF3A4B6E), CircleShape)
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
