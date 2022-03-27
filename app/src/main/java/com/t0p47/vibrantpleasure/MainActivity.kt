package com.t0p47.vibrantpleasure

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.t0p47.vibrantpleasure.databinding.ActivityMainBinding
import com.t0p47.vibrantpleasure.utils.LOG_TAG
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var vibrator: Vibrator
    private var vibroStrength: Int = abs(255*0.5).toInt()

    private var vibroTime = 1500
    private var pauseTime = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        binding.btnVibrateOnce.setOnClickListener{

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                Log.d(LOG_TAG,"MainActivity: haveAmplitude control: ${vibrator.hasAmplitudeControl()}")
                vibrator.vibrate(VibrationEffect.createOneShot(500, vibroStrength))
                Log.d(LOG_TAG,"MainActivity: vibrateOnce(26 & above) with strength: $vibroStrength")
            }else{
                //Deprecated in API 26
                vibrator.vibrate(500)
                Log.d(LOG_TAG,"MainActivity: vibrateOnce(below 26)")
            }
        }

        binding.btnVibrateLong.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                vibrator.vibrate(VibrationEffect.createOneShot(5000, vibroStrength))
            }else{
                vibrator.vibrate(5000)
            }
        }

        binding.btnVibrateWave.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                playWaveFormVibration()
            }else{
                vibrator.vibrate(5000)
            }
        }

        binding.btnStop.setOnClickListener{
            vibrator.cancel()
        }

        binding.btnShare.setOnClickListener{
            shareApp()
        }

        binding.seekBarStrength.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //vibroStrength: Int = abs(255*0.5).toInt()

                Log.d(LOG_TAG,"MainActivity: onProgressChanged, progress: $progress," +
                        " progress/100: ${abs((255*(progress/100f)).toInt())}")

                vibroStrength = if(abs((255*(progress/100f)).toInt())>=1){

                    abs((255*(progress/100f)).toInt())
                }else{
                    1
                }

                binding.tvStrength.text = "Сила вибрации: $progress%"
                Log.d(LOG_TAG,"MainActivity: vibroStrength: $vibroStrength")

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d(LOG_TAG,"MainActivity: onStartTrackingTouch")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d(LOG_TAG,"MainActivity: onStopTrackingTouch")
            }

        })


        initSampleAppBtn()

        initRegimeButtons()

    }

    private fun initSampleAppBtn(){

        binding.btnUserMode.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0,vibroTime.toLong(),pauseTime.toLong()),0))
            }else{
                vibrator.vibrate(longArrayOf(0,vibroTime.toLong(),pauseTime.toLong()),0)
            }
        }

        binding.btnSimpleMode.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0,vibroTime.toLong(),pauseTime.toLong()), 0))
            }else{
                vibrator.vibrate(longArrayOf(0,vibroTime.toLong(),pauseTime.toLong()), 0)
            }
        }

        //binding.seekBarStrength.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener

        binding.seekBarVibroTime.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                vibroTime = progress
                Log.d(LOG_TAG,"MainActivity: seekVibroTime: setted $progress")
                binding.tvVibroTime.text = "Длительность вибрации: $progress мс"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        binding.seekBarWaitTime.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pauseTime = progress
                Log.d(LOG_TAG,"MainActivity: seekPauseTime: setted $progress")
                binding.tvWaitTime.text = "Длительность паузы: $progress мс"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

    }

    private fun initRegimeButtons(){
        binding.btnHigher.setOnClickListener {
            /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                vibrator.vibrate(Vi)
            }*/
            playHigherWaveVibration()
        }
        binding.btnLower.setOnClickListener {
            playLowerWaveVibration()
        }

        binding.btnRandom.setOnClickListener {
            playRandomWave()
        }

        binding.btnEqual.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0,vibroTime.toLong(),pauseTime.toLong()),0))
            }else{
                vibrator.vibrate(longArrayOf(0,vibroTime.toLong(),pauseTime.toLong()),0)
            }
        }
    }

    /*
    * if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createOneShot(5000, vibroStrength))
        }else{
            vibrator.vibrate(5000)
        }*/
    //@RequiresApi(api = Build.VERSION_CODES.O)

    private fun shareApp(){
        Log.d(LOG_TAG,"MainActivity: shareApp")
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        startActivity(Intent.createChooser(shareIntent,getString(R.string.send_to)))
    }

    private fun playHigherWaveVibration(){
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
        }

        /*
        * for(x in 10..3000){
            vibrateTimingsList.add(x.toLong())
            val amplitudeVal = (x/3000)*255

            if(x % 2 == 0) amplitudeList.add(110)
            else amplitudeList.add(0)

            Log.d(TAG,"MainActivity: iter: $x, ampVal: $amplitudeVal ")
        }
        * */
    }

    private fun playLowerWaveVibration(){
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
        }
    }

    private fun playWaveFormVibration(){
        //val vibratePattern = longArrayOf(0, 400, 800, 600, 800, 800, 800, 1000)
        //val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255)

        val vibrateTimingsList = ArrayList<Long>()
        val amplitudeList = ArrayList<Int>()

        for(x in 10..3000){
            vibrateTimingsList.add(x.toLong())
            val amplitudeVal = (x/3000)*255

            if(x % 2 == 0) amplitudeList.add(110)
            else amplitudeList.add(0)

            Log.d(LOG_TAG,"MainActivity: iter: $x, ampVal: $amplitudeVal ")
        }

        val vibratePattern = vibrateTimingsList.toLongArray()
        val amplitudes = amplitudeList.toIntArray()


        // -1 Play exactly once

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val effect = VibrationEffect.createWaveform(vibratePattern, amplitudes, -1)
                vibrator.vibrate(effect)
        }

    }
}
