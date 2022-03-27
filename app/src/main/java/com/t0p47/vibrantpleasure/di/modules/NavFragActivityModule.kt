package com.t0p47.vibrantpleasure.di.modules

import com.t0p47.vibrantpleasure.di.modules.fragment_modules.AdsFragmentModule
import com.t0p47.vibrantpleasure.di.modules.fragment_modules.AudioFragmentModule
import com.t0p47.vibrantpleasure.di.modules.fragment_modules.AutoModeFragmentModule
import com.t0p47.vibrantpleasure.di.modules.fragment_modules.ManualFragmentModule
import com.t0p47.vibrantpleasure.di.scopes.FragmentScope
import com.t0p47.vibrantpleasure.view.ads.AdsFragment
import com.t0p47.vibrantpleasure.view.audio.AudioFragment
import com.t0p47.vibrantpleasure.view.auto_mode.AutoModeFragment
import com.t0p47.vibrantpleasure.view.manual_mode.ManualFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NavFragActivityModule{

    @FragmentScope
    @ContributesAndroidInjector(modules = [AutoModeFragmentModule::class])
    abstract fun autoModeFragment(): AutoModeFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [ManualFragmentModule::class])
    abstract fun manualFragment(): ManualFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [AudioFragmentModule::class])
    abstract fun audioFragment(): AudioFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [AdsFragmentModule::class])
    abstract fun adsFragmentModule(): AdsFragment

}