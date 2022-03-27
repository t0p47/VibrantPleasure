package com.t0p47.vibrantpleasure.view.auto_mode

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil

import com.t0p47.vibrantpleasure.R
import com.t0p47.vibrantpleasure.databinding.AutoModeFragmentBinding
import com.t0p47.vibrantpleasure.extension.FragmentInjectable
import com.t0p47.vibrantpleasure.extension.ViewModelFactory
import com.t0p47.vibrantpleasure.extension.injectViewModel
import com.t0p47.vibrantpleasure.model.VibratePattern
import com.t0p47.vibrantpleasure.utils.LOG_TAG
import javax.inject.Inject
import kotlin.random.Random

class AutoModeFragment : Fragment(), FragmentInjectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AutoModeViewModel
    private lateinit var binding: AutoModeFragmentBinding

    private lateinit var vibrator: Vibrator

    private var maxTime = 1000
    private var vibratePattern = VibratePattern.Normal

    private var vibroTime = 1500
    private var pauseTime = 1500

    //TODO: select something one(List of views OR activeView)
    private val btns = mutableListOf<View>()
    private var activeBtn: View? = null

    companion object {
        fun newInstance() =
            AutoModeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.auto_mode_fragment, container, false)
        viewModel = injectViewModel(viewModelFactory)

        Log.d(LOG_TAG,"AutoModeFragment: onCreateView")

        vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        initAuto()
        initSeekbar()

        return binding.root
    }

    private fun initSeekbar(){

        Log.d(LOG_TAG,"AutoModeFragment: initSeekbar")

        binding.seekBarVibroTime.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(binding.btnStartStop.isActivated){
                    vibrator.cancel()
                    vibroTime = progress
                    Log.d(LOG_TAG,"AutoModeFragment: seekVibroTime: setted $progress")
                    binding.tvVibroTime.text = "Длительность вибрации: $progress мс"
                    playNormal()
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.seekBarWaitTime.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(binding.btnStartStop.isActivated){
                    vibrator.cancel()
                    pauseTime = progress
                    binding.tvWaitTime.text = "Длительность паузы: $progress мс"
                    playNormal()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        binding.seekBarForce.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(binding.btnStartStop.isActivated){
                    vibrator.cancel()
                    vibroTime = 3000 - progress
                    pauseTime = 3000 - progress
                    binding.tvForce.text = "Интенсивность: $progress"
                    playNormal()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        binding.sliderVibroTime.addOnChangeListener { slider, value, fromUser ->
            Log.d(LOG_TAG,"AutoModeFragment: sliderVibroTime: value $value")
            if(binding.btnStartStop.isActivated){
                vibrator.cancel()
                vibroTime = value.toInt()

                //binding.tvVibroTimeSlider.text = "Длительность вибрации: $vibroTime мс"
                binding.tvVibroTimeSlider.text = context?.getString(R.string.auto_vibro_time, vibroTime)

                playNormal()
            }else{
                vibroTime = value.toInt()
                binding.tvVibroTimeSlider.text = context?.getString(R.string.auto_vibro_time, vibroTime)
            }
        }

        binding.sliderPauseTime.addOnChangeListener { slider, value, fromUser ->
            Log.d(LOG_TAG,"AutoModeFragment: sliderVibroTime: value $value")
            if(binding.btnStartStop.isActivated){
                vibrator.cancel()
                pauseTime = value.toInt()

                //binding.tvPauseTimeSlider.text = "Длительность паузы: $pauseTime мс"
                binding.tvPauseTimeSlider.text = context?.getString(R.string.auto_pause_time, pauseTime)

                playNormal()
            }else{
                pauseTime = value.toInt()
                binding.tvPauseTimeSlider.text = context?.getString(R.string.auto_pause_time, pauseTime)
            }
        }

    }

    private fun initAuto(){

        binding.btnRandom.setOnClickListener{

            binding.btnRandom.isActivated = !binding.btnRandom.isActivated
            if(binding.btnRandom.isActivated){
                binding.isManual = false


                activeBtn?.isActivated = false
                activeBtn = binding.btnRandom
                vibratePattern = VibratePattern.Random
                if(binding.btnStartStop.isActivated){
                    vibrator.cancel()
                    startVibrate()
                }
            }
        }

        binding.btnNormal.setOnClickListener{

            binding.btnNormal.isActivated = !binding.btnNormal.isActivated
            if(binding.btnNormal.isActivated){
                binding.isManual = true



                activeBtn?.isActivated = false
                activeBtn = binding.btnNormal
                vibratePattern = VibratePattern.Normal
                if(binding.btnStartStop.isActivated){
                    vibrator.cancel()
                    startVibrate()
                }
            }
        }

        binding.btnStrongToWeak.setOnClickListener {

            binding.btnStrongToWeak.isActivated = !binding.btnStrongToWeak.isActivated
            if(binding.btnStrongToWeak.isActivated){
                binding.isManual = false


                activeBtn?.isActivated = false
                activeBtn = binding.btnStrongToWeak
                vibratePattern = VibratePattern.StrongToWeak
                if(binding.btnStartStop.isActivated){
                    vibrator.cancel()
                    startVibrate()
                }
            }
        }

        binding.btnWeakToStrong.setOnClickListener{

            binding.btnWeakToStrong.isActivated = !binding.btnWeakToStrong.isActivated
            if(binding.btnWeakToStrong.isActivated){
                binding.isManual = false


                activeBtn?.isActivated = false
                activeBtn = binding.btnWeakToStrong
                vibratePattern = VibratePattern.WeakToStrong
                if(binding.btnStartStop.isActivated){
                    vibrator.cancel()
                    startVibrate()
                }
            }
        }

        binding.btnStartStop.setOnClickListener{

            binding.btnStartStop.isActivated = !binding.btnStartStop.isActivated

            if(binding.btnStartStop.isActivated){
                if(binding.isManual == true){
                    vibroTime = binding.seekBarVibroTime.progress
                    pauseTime = binding.seekBarWaitTime.progress
                }
                startVibrate()
            }else{
                vibrator.cancel()
            }
        }


    }

    private fun startVibrate(){
        when(vibratePattern){
            VibratePattern.Normal -> {

                playNormal()
            }
            VibratePattern.WeakToStrong -> {
                playWeakToStrong()
            }
            VibratePattern.StrongToWeak -> {
                playStrongToWeak()
            }
            VibratePattern.Random -> {
                playRandomWave()
            }
        }
    }

    private fun playNormal(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0,vibroTime.toLong(),pauseTime.toLong()),0))
        }else{
            vibrator.vibrate(longArrayOf(0,vibroTime.toLong(),pauseTime.toLong()),0)
        }
    }

    private fun playRandomWave(){
        val timingList = ArrayList<Long>()
        val amplitudeList = ArrayList<Int>()

        var x = 25

        for(i in 1 until 100){

            x = Random.nextInt(200)
            timingList.add((x+10).toLong())

            if(x % 2 == 0) amplitudeList.add(115)
            else amplitudeList.add(0)
        }

        val vibratePattern = timingList.toLongArray()
        val amplitudes = amplitudeList.toIntArray()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val effect = VibrationEffect.createWaveform(vibratePattern, amplitudes, 0)
            vibrator.vibrate(effect)
        }else{
            vibrator.vibrate(vibratePattern, 0)
        }
    }

    private fun playWeakToStrong(){
        //val timing = longArrayOf(0, 400, 800, 600, 800, 800, 800, 1000)
        //val timing = longArrayOf(0, 10, 20, 30, 40, 50, 60, 70)

        //val amplitude = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255)



        val vibrateTimingsList = ArrayList<Long>()
        val amplitudeList = ArrayList<Int>()

        var x = 25

        while(x < 200){
            vibrateTimingsList.add((x+10).toLong())

            if(x % 2 == 0) amplitudeList.add(115)
            else amplitudeList.add(0)
            x+=5
        }

        /*for(x in 1..100){
            vibrateTimingsList.add((x*10).toLong())

            if(x % 2 == 0) amplitudeList.add(255)
            else amplitudeList.add(0)


        }*/
        val vibratePattern = vibrateTimingsList.toLongArray()
        val amplitudes = amplitudeList.toIntArray()


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val effect = VibrationEffect.createWaveform(vibratePattern, amplitudes, 0)
            vibrator.vibrate(effect)
        }else{
            vibrator.vibrate(vibratePattern, 0)
        }
    }

    private fun playStrongToWeak(){
        val timingList = ArrayList<Long>()
        val amplitudeList = ArrayList<Int>()

        var x = 200
        while(x > 25){
            timingList.add((x-10).toLong())

            if(x % 2 == 0) amplitudeList.add(115)
            else amplitudeList.add(0)
            x-=5
        }

        val timing = timingList.toLongArray()
        val amplitude = amplitudeList.toIntArray()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val effect = VibrationEffect.createWaveform(timing, amplitude, 0)
            vibrator.vibrate(effect)
        }else{
            vibrator.vibrate(timing, 0)
        }
    }

    private fun vibratePercentToTime(percent: Int): Long{

        val percentDiv: Float = percent.toFloat() / 100f
        val result = (maxTime * percentDiv)
        Log.d(LOG_TAG,"AutoModeFragment: vibratePercentToTime: percent: $percent, resultTime: $result")

        return result.toLong()

    }

    //Old
    //Timing Two
    /*vibroTime = 10
    pauseTime = 10

    var increment = 1

    val timingsTwo = arrayListOf<Long>(0)

    //TODO: Every ten index need to make longer fast vibration(maybe decrement increment?)
    for(index in 1..100){

        increment = index
        /*vibroTime += 5+increment
        pauseTime += 7+increment*2*/

        //Not bad
        //vibroTime += 2+increment*2
        //pauseTime += 3+increment

        //Better
        vibroTime += 3+increment
        pauseTime += 2+increment*(1/2)

        timingsTwo.add(vibroTime.toLong())
        timingsTwo.add(pauseTime.toLong())
    }

    timingsTwo.forEach{
        Log.d(TAG,"AutoModeFragment: weakToStrong: timing: $it")
    }

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        //vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0,vibroTime.toLong(),pauseTime.toLong()),0))
        vibrator.vibrate(VibrationEffect.createWaveform(timingsTwo.toLongArray(),0))
    }else{
        vibrator.vibrate(longArrayOf(0,vibroTime.toLong(),pauseTime.toLong()),0)
    }
    isVibrate = true*/

}
