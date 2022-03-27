package com.t0p47.vibrantpleasure.view

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.t0p47.vibrantpleasure.R
import com.t0p47.vibrantpleasure.ads.AdsController
import com.t0p47.vibrantpleasure.databinding.ActivityNavFragBinding
import com.t0p47.vibrantpleasure.extension.DiActivity
import com.t0p47.vibrantpleasure.inapp_purchase.BillingManager
import com.t0p47.vibrantpleasure.utils.LOG_TAG
import com.yandex.mobile.ads.banner.BannerAdView
import javax.inject.Inject

class NavFragActivity : DiActivity() {

    @Inject
    lateinit var billingManager: BillingManager

    @Inject
    lateinit var adsController: AdsController

    private lateinit var binding: ActivityNavFragBinding

    lateinit var downAdView: BannerAdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_nav_frag)
        downAdView = binding.adView

        lifecycle.addObserver(billingManager)

        lifecycle.addObserver(adsController)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        NavigationUI.setupWithNavController(binding.bottomNav, navHostFragment.navController)



        observeLiveData()
        adsController.enableAds(this)
    }

    private fun observeLiveData(){
        billingManager.purchaseUpdateEvent.observe(this, { purchase ->

            Log.d(LOG_TAG,"NavFragActivity: observeLiveData: purchase: $purchase")
            if(purchase.sku == "update_to_professional"){
                Log.d(LOG_TAG,"NavFragActivity: observeLiveData: disable ads")
                //TODO: Uncomment (CHECK BEFORE RELEASE)
                adsController.disableAds()
            }


        })
    }
}