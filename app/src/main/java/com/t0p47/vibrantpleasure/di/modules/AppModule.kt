package com.t0p47.vibrantpleasure.di.modules

import com.t0p47.vibrantpleasure.VibroApp
import com.t0p47.vibrantpleasure.di.scopes.ActivityScope
import com.t0p47.vibrantpleasure.utils.PreferenceHelper
import com.t0p47.vibrantpleasure.view.NavFragActivity
import com.t0p47.vibrantpleasure.view.ViewPagerActivity
import com.t0p47.vibrantpleasure.view.splash.SplashActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
abstract class AppModule{

    @ActivityScope
    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    abstract fun splashActivityInjector(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [NavFragActivityModule::class])
    abstract fun navFragActivityInjector(): NavFragActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ViewPagerActivityModule::class])
    abstract fun viewPagerActivityInjector(): ViewPagerActivity

    @Module
    companion object{

        @JvmStatic
        @Provides
        fun provideSharedPreferences(app: VibroApp): PreferenceHelper {
            return PreferenceHelper(app)
        }

        /*@JvmStatic
        @Provides
        fun provideBillingManager(app: VibroApp, activity: NavFragActivity?): BillingManager {
            return BillingManager(app, activity)
        }*/

    }

}