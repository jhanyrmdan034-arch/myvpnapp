package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.PreferencesManager
import com.example.ui.VpnViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("VectaVPN", appName)
  }

  @Test
  fun `verify onboarding persistence flow`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = PreferencesManager(context)
    prefs.resetOnboarding()
    assertFalse(prefs.isOnboardingCompleted)

    val viewModel = VpnViewModel(context)
    assertEquals(1, viewModel.onboardingStep.value)

    viewModel.goToPrivacyScreen()
    assertEquals(2, viewModel.onboardingStep.value)

    viewModel.completeOnboarding()
    assertTrue(viewModel.isOnboardingCompleted.value)
    assertTrue(prefs.isOnboardingCompleted)
  }

  @Test
  fun `verify session limit and connecting countdown parameters`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = VpnViewModel(context)
    assertEquals(5400L, VpnViewModel.MAX_SESSION_SECONDS)
    assertEquals(15, viewModel.connectingRemainingSeconds.value)
    assertEquals(5400L, viewModel.stats.value.remainingLimitSeconds)
  }

  @Test
  fun `verify permission denied updates state to ERROR`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = VpnViewModel(context)
    assertEquals(com.example.data.VpnStatus.DISCONNECTED, viewModel.vpnStatus.value)

    viewModel.onVpnPermissionDenied()
    assertEquals(com.example.data.VpnStatus.ERROR, viewModel.vpnStatus.value)
    assertTrue(viewModel.errorMessage.value != null)

    viewModel.clearError()
    assertEquals(com.example.data.VpnStatus.DISCONNECTED, viewModel.vpnStatus.value)
  }

  @Test
  fun `verify disconnect confirm dialog prompt and cancel dismiss`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = VpnViewModel(context)
    assertFalse(viewModel.showDisconnectConfirm.value)

    viewModel.promptDisconnect()
    assertTrue(viewModel.showDisconnectConfirm.value)

    // User clicks Cancel - simply dismiss without taking action
    viewModel.dismissDisconnectConfirm()
    assertFalse(viewModel.showDisconnectConfirm.value)
    assertFalse(viewModel.isDisconnecting.value)
  }

  @Test
  fun `verify connection flow cancel resets rocket loading and status`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = VpnViewModel(context)

    viewModel.startConnectionFlow(context) { /* vpn launcher */ }
    assertTrue(viewModel.isRocketLoading.value)
    assertEquals(com.example.data.VpnStatus.CONNECTING, viewModel.vpnStatus.value)

    viewModel.cancelConnecting()
    assertFalse(viewModel.isRocketLoading.value)
    assertEquals(com.example.data.VpnStatus.DISCONNECTED, viewModel.vpnStatus.value)
  }

  @Test
  fun `verify subscription payload parser extracts unique countries`() {
    val repo = com.example.data.VpnRemoteConfigRepository()
    val samplePayload = """
      vless://uuid-1@de1.example.com:443?type=tcp#Germany
      vless://uuid-2@de2.example.com:443?type=tcp#🇩🇪 - DE2 | Direct
      vless://uuid-3@us1.example.com:443?type=tcp#UnitedStates
      vless://uuid-4@at1.example.com:443?type=tcp#🇦🇹 - AT | Direct
      vless://uuid-5@fi1.example.com:443?type=tcp#🇫🇮 - FL | All net
    """.trimIndent()

    val servers = repo.parseSubscriptionPayload(samplePayload)
    // Should contain Germany, United States, Austria, Finland uniquely
    assertEquals(4, servers.size)
    val names = servers.map { it.name }
    assertTrue(names.contains("Germany"))
    assertTrue(names.contains("United States"))
    assertTrue(names.contains("Austria"))
    assertTrue(names.contains("Finland"))
    assertTrue(servers.all { it.configProfile != null && it.configProfile!!.startsWith("vless://") })
  }

  @Test
  fun `verify live traffic statistics reset on disconnect`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = VpnViewModel(context)
    viewModel.disconnectVpn(context)

    val stats = viewModel.stats.value
    assertEquals(0f, stats.downloadSpeedMbps, 0.001f)
    assertEquals(0f, stats.uploadSpeedMbps, 0.001f)
    assertEquals(0f, stats.bytesReceivedMb, 0.001f)
    assertEquals(0f, stats.bytesSentMb, 0.001f)
    assertEquals(VpnViewModel.MAX_SESSION_SECONDS, stats.remainingLimitSeconds)
  }
}
