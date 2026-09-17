package com.example

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.example.data.PreferencesManager
import com.example.data.ServerModel
import com.example.data.VpnRemoteConfigRepository
import com.example.data.VpnStatus
import com.example.data.WireGuardConfig
import com.example.service.VpnHealthCheck
import com.example.ui.VpnViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class ExampleRobolectricTest {

  @Test
  fun `verify onboarding complete transitions and persistence`() {
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
  fun `verify session limit parameters`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = VpnViewModel(context)
    assertEquals(5400L, VpnViewModel.MAX_SESSION_SECONDS)
    assertEquals(5400L, viewModel.stats.value.remainingLimitSeconds)
  }

  @Test
  fun `verify permission denied updates state to DISCONNECTED and shows toast`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = VpnViewModel(context)
    assertEquals(VpnStatus.DISCONNECTED, viewModel.vpnStatus.value)

    viewModel.onVpnPermissionDenied()
    // As requested: in case of error, button reverts to Disconnected and toast is shown
    assertEquals(VpnStatus.DISCONNECTED, viewModel.vpnStatus.value)
    assertTrue(viewModel.toastMessage.value != null)
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
    val testServer = com.example.data.ServerModel(
      server_name = "US-1",
      country = "United States",
      country_code = "US",
      config = "[Interface]\nPrivateKey=123\nAddress=10.0.0.2/32\n[Peer]\nPublicKey=abc\nEndpoint=1.1.1.1:51820"
    )
    viewModel.selectServer(testServer, context)

    var permissionRequested = false
    viewModel.startConnectionFlow(context) {
      permissionRequested = true
    }
    if (permissionRequested) {
      viewModel.onVpnPermissionGranted(context)
    }

    assertTrue(viewModel.isRocketLoading.value)
    assertEquals(VpnStatus.CONNECTING, viewModel.vpnStatus.value)

    viewModel.cancelConnecting()
    assertFalse(viewModel.isRocketLoading.value)
    assertEquals(VpnStatus.DISCONNECTED, viewModel.vpnStatus.value)
  }

  @Test
  fun `verify JSON server parsing from remote repository`() {
    val repo = VpnRemoteConfigRepository()
    val sampleJson = """
      [
        {
          "server_name": "US-Fast-1",
          "country": "United States",
          "country_code": "US",
          "config": "[Interface]\nPrivateKey = aaaa\nAddress = 10.0.0.2/32\n[Peer]\nPublicKey = bbbb\nEndpoint = 198.51.100.1:51820\nAllowedIPs = 0.0.0.0/0"
        },
        {
          "server_name": "DE-Secure-1",
          "country": "Germany",
          "country_code": "DE",
          "config": "[Interface]\nPrivateKey = cccc\nAddress = 10.0.0.3/32\n[Peer]\nPublicKey = dddd\nEndpoint = 198.51.100.2:51820\nAllowedIPs = 0.0.0.0/0"
        }
      ]
    """.trimIndent()

    val servers = repo.parseServersJson(sampleJson)
    assertEquals(2, servers.size)
    assertEquals("US-Fast-1", servers[0].server_name)
    assertEquals("United States", servers[0].country)
    assertEquals("US", servers[0].country_code)
    assertEquals("198.51.100.1", servers[0].ipAddress)

    assertEquals("DE-Secure-1", servers[1].server_name)
    assertEquals("Germany", servers[1].country)
    assertEquals("DE", servers[1].country_code)
    assertEquals("198.51.100.2", servers[1].ipAddress)
  }

  @Test
  fun `verify live traffic statistics reset on disconnect`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = VpnViewModel(context)
    viewModel.cancelDisconnecting()

    val stats = viewModel.stats.value
    assertEquals(0f, stats.downloadSpeedMbps, 0.001f)
    assertEquals(0f, stats.uploadSpeedMbps, 0.001f)
    assertEquals(0f, stats.bytesReceivedMb, 0.001f)
    assertEquals(0f, stats.bytesSentMb, 0.001f)
    assertEquals(VpnViewModel.MAX_SESSION_SECONDS, stats.remainingLimitSeconds)
  }

  @Test
  fun `verify splash screen launch state and completion flow`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = VpnViewModel(context)
    assertFalse(viewModel.isSplashScreenCompleted.value)

    viewModel.completeSplashScreen()
    assertTrue(viewModel.isSplashScreenCompleted.value)
  }

  @Test
  fun `verify wireguard config parsing extracts all fields accurately`() {
    val rawConfig = """
      [Interface]
      PrivateKey = aSamplePrivateKeyBase64String=
      Address = 10.8.0.5/24
      DNS = 1.1.1.1, 8.8.8.8
      MTU = 1420

      [Peer]
      PublicKey = aSamplePublicKeyBase64String=
      Endpoint = 198.51.100.5:51820
      AllowedIPs = 0.0.0.0/0
      PersistentKeepalive = 25
    """.trimIndent()

    val parsed = WireGuardConfig.parse(rawConfig)
    assertNotNull(parsed)
    assertEquals("aSamplePrivateKeyBase64String=", parsed!!.privateKey)
    assertEquals("10.8.0.5", parsed.address)
    assertEquals(24, parsed.prefix)
    assertEquals(listOf("1.1.1.1", "8.8.8.8"), parsed.dnsList)
    assertEquals(1420, parsed.mtu)
    assertEquals("aSamplePublicKeyBase64String=", parsed.publicKey)
    assertEquals("198.51.100.5", parsed.endpointHost)
    assertEquals(51820, parsed.endpointPort)
    assertEquals("198.51.100.5:51820", parsed.endpoint)
    assertTrue(parsed.isValid)
  }

  @Test
  fun `verify health check immediately rejects dummy 1_2_3_4 IP as offline`() = runBlocking {
    val testServer = ServerModel(
      server_name = "Dummy-Server",
      country = "Test",
      country_code = "TT",
      config = """
        [Interface]
        PrivateKey = someKey=
        Address = 10.0.0.2/32
        DNS = 1.1.1.1
        [Peer]
        PublicKey = pubKey=
        Endpoint = 1.2.3.4:51820
      """.trimIndent()
    )

    val result = VpnHealthCheck.verifyServerHealth(
      server = testServer,
      wgConfig = testServer.wireGuardConfig,
      timeoutMs = 3000L
    )

    assertTrue("Result must be failure for dummy IP", result is VpnHealthCheck.HealthResult.Failure)
    val failure = result as VpnHealthCheck.HealthResult.Failure
    assertEquals("سرور پاسخ نمی‌دهد یا آفلاین است. لطفاً سرور دیگری را انتخاب کنید.", failure.reason)
  }
}
