package com.t0p47.vibrantpleasure.di.modules

import androidx.lifecycle.ViewModel
import com.t0p47.vibrantpleasure.di.ViewModelKey
import com.t0p47.vibrantpleasure.view.ads.AdsViewModel
import com.t0p47.vibrantpleasure.view.audio.AudioViewModel
import com.t0p47.vibrantpleasure.view.auto_mode.AutoModeViewModel
import com.t0p47.vibrantpleasure.view.manual_mode.ManualViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AutoModeViewModel::class)
    abstract fun autoModeViewModel(autoModeViewModel: AutoModeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ManualViewModel::class)
    abstract fun manualViewModel(manualViewModel: ManualViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AdsViewModel::class)
    abstract fun adsViewModel(adsViewModel: AdsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AudioViewModel::class)
    abstract fun audioViewModel(audioViewModel: AudioViewModel): ViewModel

}