package com.t0p47.vibrantpleasure.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import com.t0p47.vibrantpleasure.R
import com.t0p47.vibrantpleasure.databinding.ActivitySplashBinding
import com.t0p47.vibrantpleasure.extension.DiActivity
import com.t0p47.vibrantpleasure.view.NavFragActivity
import javax.inject.Inject

class SplashActivity @Inject constructor() : DiActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val SPLASH_SHOW_TIME: Long = 3500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            //startActivity(Intent(this,ViewPagerActivity::class.java))
            startActivity(Intent(this, NavFragActivity::class.java))

            finish()
        }, SPLASH_SHOW_TIME)

    }
}
