package com.t0p47.vibrantpleasure.di

import com.t0p47.vibrantpleasure.VibroApp
import com.t0p47.vibrantpleasure.di.modules.AppModule
import com.t0p47.vibrantpleasure.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun context(app: VibroApp): Builder

        fun build(): AppComponent
    }

    fun inject(app: VibroApp)

}