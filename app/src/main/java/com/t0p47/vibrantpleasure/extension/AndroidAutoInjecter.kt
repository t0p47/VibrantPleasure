package com.t0p47.vibrantpleasure.extension

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.t0p47.vibrantpleasure.VibroApp
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

interface FragmentInjectable

open class DiActivity: AppCompatActivity(), HasAndroidInjector{

    @Inject
    internal lateinit var fragmentInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = fragmentInjector
}

fun VibroApp.autoInject(){
    this.registerActivityLifecycleCallbacks(object: Application.ActivityLifecycleCallbacks{

        override fun onActivityCreated(activity: Activity, p1: Bundle?){

            activity.also {

                if(it is HasAndroidInjector)  AndroidInjection.inject(it)

                if(it is FragmentActivity){
                    it.supportFragmentManager.registerFragmentLifecycleCallbacks(object: FragmentManager.FragmentLifecycleCallbacks(){
                        override fun onFragmentCreated(
                            fm: FragmentManager,
                            f: Fragment,
                            savedInstanceState: Bundle?
                        ) {
                            if(f is FragmentInjectable) AndroidSupportInjection.inject(f)
                        }
                    }, true)
                }
            }
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }

    })
}