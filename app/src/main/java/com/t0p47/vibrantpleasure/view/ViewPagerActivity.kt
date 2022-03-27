package com.t0p47.vibrantpleasure.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.t0p47.vibrantpleasure.R
import com.t0p47.vibrantpleasure.databinding.ActivityViewpagerBinding
import com.t0p47.vibrantpleasure.extension.DiActivity
import com.t0p47.vibrantpleasure.view.ads.AdsFragment
import com.t0p47.vibrantpleasure.view.audio.AudioFragment
import com.t0p47.vibrantpleasure.view.auto_mode.AutoModeFragment
import com.t0p47.vibrantpleasure.view.manual_mode.ManualFragment
import javax.inject.Inject

class ViewPagerActivity @Inject constructor() : DiActivity() {

    private lateinit var binding: ActivityViewpagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_viewpager)

        //binding.viewPager.adapter = MainViewPager(supportFragmentManager)
        binding.viewPager.adapter = createViewPagerAdapter()
    }

    fun createViewPagerAdapter(): RecyclerView.Adapter<*>{

        return object: FragmentStateAdapter(this){
            override fun getItemCount(): Int {
                return 4
            }

            override fun createFragment(position: Int): Fragment {
                var fragment: Fragment? = null
                when(position){
                    0 -> {
                        fragment = ManualFragment.newInstance()
                    }
                    1 -> {
                        fragment = AutoModeFragment.newInstance()
                    }
                    2 -> {
                        fragment = AudioFragment.newInstance()
                    }
                    3 -> {
                        fragment = AdsFragment.newInstance()
                    }
                }

                return fragment!!
            }

        }

    }
}
