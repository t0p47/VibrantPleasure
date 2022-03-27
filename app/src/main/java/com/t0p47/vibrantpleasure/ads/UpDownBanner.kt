package com.t0p47.vibrantpleasure.ads

import android.util.Log
import com.t0p47.vibrantpleasure.ads.AdsController.Companion.DOWN_BANNER_PLACE_INTERVAL
import com.t0p47.vibrantpleasure.view.NavFragActivity
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import java.util.*
import kotlin.concurrent.timer

class UpDownBanner(private val activity: NavFragActivity) {

    private var downRefreshTimer: Timer? = null

    init {
        enableDownTimer()
    }

    private fun enableDownTimer(){
        //activity.downAdView.setBlockId("R-M-DEMO-320x50")
        activity.downAdView.setBlockId("R-M-930268-1")

        activity.downAdView.setAdSize(AdSize.BANNER_320x50)


        val adRequest = AdRequest.Builder().build()

        activity.downAdView.setBannerAdEventListener(bannerAdEventListener)

        downRefreshTimer = timer(
            name = "down_refresh",
            daemon = false,
            initialDelay = 10000,
            period = DOWN_BANNER_PLACE_INTERVAL
        ){
            Log.d("ADS_TAG", "UpDownBanner: downRefreshTimer: tick")
            activity.downAdView.loadAd(adRequest)
        }
    }

    private val bannerAdEventListener = object: BannerAdEventListener{
        override fun onAdLoaded() {
            Log.d("ADS_TAG", "UpDownBanner: enableDownTimer: onAdLoaded")
        }

        override fun onAdFailedToLoad(error: AdRequestError) {
            Log.d("ADS_TAG", "UpDownBanner: enableDownTimer: onAdFailedToLoad code: ${error.code}, description: ${error.description}")
        }

        override fun onLeftApplication() {
            Log.d("ADS_TAG", "UpDownBanner: enableDownTimer: onAdLeftApplication")
        }

        override fun onReturnedToApplication() {
            Log.d("ADS_TAG", "UpDownBanner: enableDownTimer: onReturnedToApplication")
        }

    }

    fun disableUpDownAds(){
        downRefreshTimer?.cancel()
        downRefreshTimer = null
    }

}