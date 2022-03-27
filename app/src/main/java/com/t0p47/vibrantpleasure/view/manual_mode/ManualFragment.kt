package com.t0p47.vibrantpleasure.view.manual_mode

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.agilie.circularpicker.presenter.CircularPickerContract

import com.t0p47.vibrantpleasure.R
import com.t0p47.vibrantpleasure.databinding.ManualFragmentBinding
import com.t0p47.vibrantpleasure.extension.FragmentInjectable
import com.t0p47.vibrantpleasure.extension.ViewModelFactory
import com.t0p47.vibrantpleasure.extension.injectViewModel
import com.t0p47.vibrantpleasure.utils.LOG_TAG
import javax.inject.Inject

class ManualFragment : Fragment(), FragmentInjectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: ManualFragmentBinding

    private lateinit var viewModel: ManualViewModel

    private var isVibrate = false

    private lateinit var vibrator: Vibrator
    private var vibroTime = 1500
    private var pauseTime = 1500

    companion object {
        fun newInstance() =
            ManualFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.manual_fragment, container, false)
        viewModel = injectViewModel(viewModelFactory)

        vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        initManual()

        return binding.root
    }

    private fun initManual(){
        binding.btnStartStop.setOnClickListener{

            if(isVibrate){
                binding.btnStartStop.text = context?.getText(R.string.start)
                vibrator.cancel()
                isVibrate = false
            }else{
                binding.btnStartStop.text = context?.getText(R.string.stop)

                Log.d(LOG_TAG,"ManualFragment: btnStart: vibroTime: $vibroTime, pauseTime: $pauseTime")

                startVibrate()
                isVibrate = true
            }
        }

        val typeface = Typeface.createFromAsset(requireActivity().assets, "OpenSans-ExtraBold.ttf")

        binding.cpPauseTime.apply {
            colors = (intArrayOf(
                Color.parseColor("#00EDE9"),
                Color.parseColor("#0087D9"),
                Color.parseColor("#8A1CC3")))
            gradientAngle = 220
            maxLapCount = 1
            centeredTypeFace = typeface
            currentValue = 1500
            maxValue = 3000
            centeredTextSize = 60f
            centeredText = "Wait"
            valueChangedListener = (object : CircularPickerContract.Behavior.ValueChangedListener {
                override fun onValueChanged(value: Int) {

                    pauseTime = value
                    Log.d(LOG_TAG,"ManualFragment: seekVibroTime: setted $value")

                    //binding.tvWaitTime.text = "Пауза: $value мс"
                    binding.tvWaitTime.text = context?.getString(R.string.manual_pause_time, value)

                    vibrator.cancel()
                    if(isVibrate){
                        startVibrate()
                    }
                }
            })
            colorChangedListener = (object : CircularPickerContract.Behavior.ColorChangedListener {
                override fun onColorChanged(r: Int, g: Int, b: Int) {
                    binding.tvWaitTime.setTextColor(Color.rgb(r, g, b))
                }
            })
        }

        binding.cpVibroTime.apply {
            colors = (intArrayOf(
                Color.parseColor("#FF8D00"),
                Color.parseColor("#FF0058"),
                Color.parseColor("#920084")))
            gradientAngle = 150
            maxValue = 3000
            currentValue = 1500
            centeredTypeFace = typeface
            maxLapCount = 1
            centeredTextSize = 60f
            centeredText = "Vibro"
            valueChangedListener = object : CircularPickerContract.Behavior.ValueChangedListener {
                override fun onValueChanged(value: Int) {

                    //binding.tvVibroTime.text = "Вибрация: $value мс"
                    binding.tvVibroTime.text = context?.getString(R.string.manual_vibro_time, value)

                    vibroTime = value
                    Log.d(LOG_TAG,"ManualFragment: seekVibroTime: setted $value")
                    vibrator.cancel()
                    if(isVibrate){
                        startVibrate()
                    }
                }
            }
            colorChangedListener = (object : CircularPickerContract.Behavior.ColorChangedListener {
                override fun onColorChanged(r: Int, g: Int, b: Int) {
                    binding.tvVibroTime.setTextColor(Color.rgb(r, g, b))
                }
            })
        }
    }

    private fun startVibrate(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0,vibroTime.toLong(),pauseTime.toLong()),0))
        }else{
            vibrator.vibrate(longArrayOf(0,vibroTime.toLong(),pauseTime.toLong()),0)
        }
    }

}
