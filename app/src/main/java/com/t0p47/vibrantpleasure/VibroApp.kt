package com.t0p47.vibrantpleasure

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.t0p47.vibrantpleasure.di.DaggerAppComponent
import com.t0p47.vibrantpleasure.extension.autoInject
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import com.yandex.mobile.ads.common.MobileAds
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class VibroApp: MultiDexApplication(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        // Инициализация AppMetrica SDK
        val configBuilder =
            YandexMetricaConfig.newConfigBuilder(getString(R.string.yandex_sdk_api_key))
        configBuilder.withLogs()

        YandexMetrica.activate(applicationContext, configBuilder.build())
        // Отслеживание активности пользователей
        YandexMetrica.enableActivityAutoTracking(this)

        DaggerAppComponent
            .builder()
            .context(this)
            .build()
            .inject(this)

        autoInject()
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}