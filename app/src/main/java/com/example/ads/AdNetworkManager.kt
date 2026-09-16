package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * AdNetworkManager: Unified Manager for Google AdMob Interstitial Ads.
 * Uses official test unit ID: "ca-app-pub-3940256099942544/1033173712".
 * Handles:
 * 1. Step 2 in Connection Flow: Load and display full-screen Interstitial Ad programmatically.
 * 2. Step 3 in Connection Flow: The exact millisecond the user taps 'X' (close), fires callback to establish VPN tunnel.
 * 3. Step 3/4 in Disconnection Flow: Load and display Interstitial Ad after 12s loading, then terminate VpnService on close.
 * 4. Resilient timeout safety: if network is offline or ad fails to load, gracefully triggers callback so user is never stuck.
 */
object AdNetworkManager {
    private const val TAG = "AdNetworkManager"

    // Default AdMob Test Interstitial Unit ID
    const val ADMOB_TEST_INTERSTITIAL_ID: String = "ca-app-pub-3940256099942544/1033173712"
    var admobInterstitialUnitId: String = ADMOB_TEST_INTERSTITIAL_ID

    private var isInitialized = AtomicBoolean(false)
    private var preloadedConnectAd: InterstitialAd? = null
    private var preloadedDisconnectAd: InterstitialAd? = null
    private val mainScope = CoroutineScope(Dispatchers.Main)

    enum class AdType {
        CONNECT,
        DISCONNECT
    }

    /**
     * Initializes Google Mobile Ads SDK once at startup.
     */
    fun initialize(context: Context) {
        if (isInitialized.compareAndSet(false, true)) {
            try {
                MobileAds.initialize(context) { status ->
                    Log.i(TAG, "Google Mobile Ads initialized: $status")
                    preloadAd(context, AdType.CONNECT)
                }
            } catch (e: Throwable) {
                Log.w(TAG, "Failed initializing MobileAds: ${e.message}")
            }
        }
    }

    /**
     * Preloads an interstitial ad to ensure instantaneous display when needed.
     */
    fun preloadAd(context: Context, adType: AdType) {
        try {
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                context,
                admobInterstitialUnitId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d(TAG, "Preloaded Interstitial Ad for $adType")
                        if (adType == AdType.CONNECT) {
                            preloadedConnectAd = interstitialAd
                        } else {
                            preloadedDisconnectAd = interstitialAd
                        }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        Log.w(TAG, "Failed to preload Interstitial Ad for $adType: ${loadAdError.message}")
                    }
                }
            )
        } catch (e: Throwable) {
            Log.w(TAG, "Error in preloadAd: ${e.message}")
        }
    }

    /**
     * Shows full-screen Google Interstitial Test Ad programmatically.
     * Covers entire screen programmatically without any placeholder boxes or containers.
     * The callback [onAdClosed] is fired the exact millisecond the user closes the ad ('X' button),
     * or immediately if the ad fails / times out.
     */
    fun showInterstitial(
        activity: Activity,
        adType: AdType,
        onAdClosed: () -> Unit
    ) {
        Log.i(TAG, "Requesting Programmatic Google Interstitial Ad for $adType")

        val isHandled = AtomicBoolean(false)
        val triggerCloseOnce: () -> Unit = {
            if (isHandled.compareAndSet(false, true)) {
                Log.i(TAG, "Interstitial Ad closed/completed event dispatched for $adType")
                onAdClosed()
            }
        }

        // Check if preloaded ad is available
        val cachedAd = if (adType == AdType.CONNECT) {
            val ad = preloadedConnectAd
            preloadedConnectAd = null
            ad
        } else {
            val ad = preloadedDisconnectAd
            preloadedDisconnectAd = null
            ad
        }

        if (cachedAd != null) {
            presentInterstitialAd(activity, cachedAd, adType, triggerCloseOnce)
            return
        }

        // If not preloaded, load and display with safety timeout (max 4 seconds)
        var timeoutJob: Job? = null
        timeoutJob = mainScope.launch {
            delay(4000)
            if (!isHandled.get()) {
                Log.w(TAG, "Ad load timeout (4s) reached, proceeding with $adType")
                triggerCloseOnce()
            }
        }

        try {
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                activity,
                admobInterstitialUnitId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        timeoutJob?.cancel()
                        if (!isHandled.get()) {
                            presentInterstitialAd(activity, interstitialAd, adType, triggerCloseOnce)
                        }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        timeoutJob?.cancel()
                        Log.w(TAG, "Ad load failed: ${loadAdError.message}, proceeding immediately")
                        triggerCloseOnce()
                    }
                }
            )
        } catch (e: Throwable) {
            timeoutJob?.cancel()
            Log.w(TAG, "Exception loading Interstitial Ad: ${e.message}")
            triggerCloseOnce()
        }
    }

    private fun presentInterstitialAd(
        activity: Activity,
        interstitialAd: InterstitialAd,
        adType: AdType,
        onComplete: () -> Unit
    ) {
        interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed by user tapping 'X'. Immediately proceeding.")
                onComplete()
                // Preload next ad in background
                preloadAd(activity, if (adType == AdType.CONNECT) AdType.DISCONNECT else AdType.CONNECT)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.w(TAG, "Ad failed to show: ${adError.message}. Proceeding.")
                onComplete()
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Interstitial Ad displayed on screen")
            }
        }

        interstitialAd.show(activity)
    }
}
