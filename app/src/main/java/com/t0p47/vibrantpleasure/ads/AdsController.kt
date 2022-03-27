package com.t0p47.vibrantpleasure.ads

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.t0p47.vibrantpleasure.view.NavFragActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdsController @Inject constructor(): LifecycleObserver {

    companion object{
        internal val DOWN_BANNER_PLACE_INTERVAL = 30000L //30 seconds
    }

    private var upDownAds: UpDownBanner? = null
    private lateinit var activity: NavFragActivity

    //@OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun enableAds(activity: NavFragActivity){
        this.activity = activity
        upDownAds = UpDownBanner(activity)
        Log.d("ADS_TAG", "AdsController: enableAds")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onUnbind(owner: LifecycleOwner){
        Log.d("ADS_TAG", "AdsController: onUnbind")
        //disableAds()
    }

    fun disableAds(){
        upDownAds?.disableUpDownAds()

        upDownAds = null
    }

}