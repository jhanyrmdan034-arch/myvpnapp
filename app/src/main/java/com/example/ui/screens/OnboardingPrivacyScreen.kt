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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppStrings
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
fun OnboardingPrivacyScreen(
    langCode: String,
    onBackClick: () -> Unit,
    onAcceptAndContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isAgreed by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val policyText = if (langCode == "fa") {
        AppStrings.samplePrivacyPolicyPersian
    } else {
        AppStrings.samplePrivacyPolicyEnglish
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VpnDarkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
            .testTag("onboarding_privacy_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Row: Back button + Step indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("privacy_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = VpnTextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                JumpJumpShieldIcon(size = 30)
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

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(VpnSurfaceElevated)
                    .border(1.dp, VpnCardBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "2 / 2",
                    color = VpnNeonGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Title and Subtitle
        Text(
            text = AppStrings.get("privacy_title", langCode),
            color = VpnTextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = AppStrings.get("privacy_subtitle", langCode),
            color = VpnTextSecondary,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Scrollable Box with Privacy Policy text
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VpnSurface)
                .border(1.dp, VpnCardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
                .testTag("privacy_policy_text_box")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = VpnNeonGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Encrypted & Zero-Log Architecture",
                        color = VpnNeonGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = policyText,
                    color = VpnTextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox Agreement Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { isAgreed = !isAgreed }
                .padding(vertical = 4.dp)
                .testTag("privacy_agreement_checkbox_row"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isAgreed,
                onCheckedChange = { isAgreed = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = VpnNeonGreen,
                    uncheckedColor = VpnTextMuted,
                    checkmarkColor = VpnDarkBg
                ),
                modifier = Modifier.testTag("privacy_checkbox")
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = AppStrings.get("privacy_agreement", langCode),
                color = if (isAgreed) VpnTextPrimary else VpnTextSecondary,
                fontSize = 13.sp,
                fontWeight = if (isAgreed) FontWeight.Medium else FontWeight.Normal,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Accept and Continue Button
        Button(
            onClick = onAcceptAndContinue,
            enabled = isAgreed,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("privacy_accept_continue_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = VpnNeonGreen,
                contentColor = VpnDarkBg,
                disabledContainerColor = VpnSurfaceElevated,
                disabledContentColor = VpnTextMuted
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = AppStrings.get("accept_and_continue", langCode),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
