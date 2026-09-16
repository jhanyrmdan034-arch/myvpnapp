package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppStrings
import com.example.data.BottomTab
import com.example.ui.theme.VpnDarkBg
import com.example.ui.theme.VpnNavInactive
import com.example.ui.theme.VpnNeonGreen

@Composable
fun JumpJumpBottomBar(
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    langCode: String,
    modifier: Modifier = Modifier
) {
    val isFa = langCode == "fa"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(VpnDarkBg)
            .navigationBarsPadding()
            .height(64.dp)
            .padding(horizontal = 48.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = Icons.Filled.Home,
            label = if (isFa) "خانه" else "Home",
            isSelected = currentTab == BottomTab.HOME,
            testTag = "nav_home",
            onClick = { onTabSelected(BottomTab.HOME) }
        )

        BottomNavItem(
            icon = Icons.Outlined.Settings,
            label = if (isFa) "تنظیمات" else "Settings",
            isSelected = currentTab == BottomTab.SETTINGS,
            testTag = "nav_settings",
            onClick = { onTabSelected(BottomTab.SETTINGS) }
        )
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val tint = if (isSelected) VpnNeonGreen else VpnNavInactive

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
