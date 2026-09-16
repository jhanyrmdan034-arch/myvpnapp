package com.example.ui.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.DefaultData
import com.example.data.Language
import com.example.ui.components.CountryFlag
import com.example.ui.components.JumpJumpShieldIcon
import com.example.ui.theme.VpnCardBorder
import com.example.ui.theme.VpnDarkBg
import com.example.ui.theme.VpnNeonGreen
import com.example.ui.theme.VpnSurface
import com.example.ui.theme.VpnSurfaceElevated
import com.example.ui.theme.VpnTextMuted
import com.example.ui.theme.VpnTextPrimary
import com.example.ui.theme.VpnTextSecondary

@Composable
fun OnboardingLanguageScreen(
    selectedLanguageCode: String,
    onLanguageSelected: (Language) -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VpnDarkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
            .testTag("onboarding_language_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header with Logo and Step indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                JumpJumpShieldIcon(size = 32)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Vecta",
                    color = VpnTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "VPN",
                    color = Color(0xFF3A82F7),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // Step Indicator Badge (1 of 2)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(VpnSurfaceElevated)
                    .border(1.dp, VpnCardBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "1 / 2",
                    color = VpnNeonGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Title and Subtitle
        Text(
            text = AppStrings.get("select_language_title", selectedLanguageCode),
            color = VpnTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = AppStrings.get("select_language_subtitle", selectedLanguageCode),
            color = VpnTextSecondary,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Languages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(DefaultData.languages, key = { it.code }) { language ->
                val isSelected = language.code == selectedLanguageCode
                LanguageListItem(
                    language = language,
                    isSelected = isSelected,
                    onClick = { onLanguageSelected(language) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Continue Button
        Button(
            onClick = onContinueClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("onboarding_language_continue_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = VpnNeonGreen,
                contentColor = VpnDarkBg
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = AppStrings.get("continue_btn", selectedLanguageCode),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun LanguageListItem(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) VpnSurfaceElevated else VpnSurface)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) VpnNeonGreen else VpnCardBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("lang_item_${language.code}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CountryFlag(countryCode = language.countryCode, size = 34.dp)
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = language.nativeName,
                    color = VpnTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
                Text(
                    text = language.name,
                    color = VpnTextMuted,
                    fontSize = 12.sp
                )
            }
        }

        // Custom Radio indicator
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = if (isSelected) VpnNeonGreen else VpnTextMuted,
                    shape = CircleShape
                )
                .background(if (isSelected) VpnNeonGreen else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = VpnDarkBg,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
