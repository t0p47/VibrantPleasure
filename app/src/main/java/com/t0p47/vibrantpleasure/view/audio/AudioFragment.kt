package com.t0p47.vibrantpleasure.view.audio

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.t0p47.vibrantpleasure.R
import com.t0p47.vibrantpleasure.databinding.AudioFragmentBinding
import com.t0p47.vibrantpleasure.extension.FragmentInjectable
import com.t0p47.vibrantpleasure.extension.ViewModelFactory
import com.t0p47.vibrantpleasure.extension.injectViewModel
import com.t0p47.vibrantpleasure.utils.ExoplayerMain
import com.t0p47.vibrantpleasure.utils.LOG_TAG
import javax.inject.Inject
import kotlin.math.roundToInt

class AudioFragment : Fragment(), FragmentInjectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AudioViewModel

    private lateinit var binding: AudioFragmentBinding

    private var player: SimpleExoPlayer? = null

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var enabledSound = 0
    private lateinit var audioManager: AudioManager

    private var myAppExoplayerBirdOne: ExoplayerMain? = null
    private var myAppExoplayerCicada: ExoplayerMain? = null
    private var myAppExoplayerRoofRain: ExoplayerMain? = null
    private var myAppExoplayerVacuum: ExoplayerMain? = null
    private var myAppExoplayerFrog: ExoplayerMain? = null
    private var myAppExoplayerTrain: ExoplayerMain? = null

    private var myAppExoplayerAirplane: ExoplayerMain? = null
    private var myAppExoplayerBirdTwo: ExoplayerMain? = null
    private var myAppExoplayerBirdThree: ExoplayerMain? = null
    private var myAppExoplayerCar: ExoplayerMain? = null
    private var myAppExoplayerFan: ExoplayerMain? = null
    private var myAppExoplayerHairDryer: ExoplayerMain? = null
    private var myAppExoplayerLightRain: ExoplayerMain? = null
    private var myAppExoplayerOcean: ExoplayerMain? = null
    private var myAppExoplayerOwl: ExoplayerMain? = null
    private var myAppExoplayerRainInForest: ExoplayerMain? = null
    private var myAppExoplayerRainInWindow: ExoplayerMain? = null
    private var myAppExoplayerSleet: ExoplayerMain? = null
    private var myAppExoplayerStream: ExoplayerMain? = null
    private var myAppExoplayerThunder: ExoplayerMain? = null
    private var myAppExoplayerWind: ExoplayerMain? = null

    private var exoplayersList = mutableListOf<ExoplayerMain?>()

    private var isPlayed = false

    //private var soundBtns = mutableListOf<Button>()

    private lateinit var playbackStateListener: PlaybackStateListener

    companion object {
        fun newInstance() = AudioFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d(LOG_TAG, "AudioFragment: onCreateView")

        playbackStateListener = PlaybackStateListener()

        binding = DataBindingUtil.inflate(inflater, R.layout.audio_fragment, container, false)
        viewModel = injectViewModel(viewModelFactory)

        /*soundBtns.add(binding.btnCicada)
        soundBtns.add(binding.btnRoofRain)
        soundBtns.add(binding.btnBirdOne)
        soundBtns.add(binding.btnVacuum)
        soundBtns.add(binding.btnFrog)
        soundBtns.add(binding.btnTrain)*/

        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager

        binding.btnPlayStop.setOnClickListener{

            if(isPlayed){
                stopSound()

                isPlayed = !isPlayed
                binding.btnPlayStop.text = getString(R.string.play)
            }else{
                playSound()
                isPlayed = !isPlayed
                binding.btnPlayStop.text = getString(R.string.stop_en)
            }
        }

        binding.sbAllVolume.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                val maxVolumeForStream = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                val onePercent = maxVolumeForStream / 100f

                Log.d(LOG_TAG,"MainActivity: sbAllVolume: value: ${(onePercent * p1).roundToInt()}, maxVolumeForStream: $maxVolumeForStream")

                //TODO: Make volume setting correct, take 'getStreamMaxVolume' and divide by 100 and get one percent volume value and then multiply by seekbar value
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (onePercent * p1).roundToInt(), AudioManager.ADJUST_SAME)
                //myAppExoplayerRoofRain?.getStreamType()?.let { audioManager.setStreamVolume(it, p1, 0) }
                //myAppExoplayerRoofRain?.setVolume((p1.toFloat())/100)

                //audioManager.setStreamVolume()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        /*binding.sbCicada.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                myAppExoplayerCicada?.setVolume(p1.toFloat()/100)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })*/

        /*binding.sbRoofRain.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                myAppExoplayerRoofRain?.setVolume(p1.toFloat()/100)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })*/

        /*binding.sbBirdOne.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                myAppExoplayerBird?.setVolume(p1.toFloat()/100)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })*/

        /*binding.sbVacuum.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                myAppExoplayerVacuum?.setVolume(p1.toFloat()/100)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })*/

        /*binding.sbFrog.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                myAppExoplayerFrog?.setVolume(p1.toFloat()/100)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })*/

        /*binding.sbTrain.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                myAppExoplayerTrain?.setVolume(p1.toFloat()/100)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })*/

        /*binding.btnBirdOne.setOnClickListener{
            if(myAppExoplayerBird == null && enabledSound < 5) {

                binding.btnBirdOne.isActivated = true

                myAppExoplayerBird = ExoplayerMain(requireContext(), RawResourceDataSource.buildRawResourceUri(R.raw.bird_sing_1))

                if(isPlayed) myAppExoplayerBird?.startPlaying()
                enabledSound++
                Log.d(TAG, "MainActivity: birdOne enable: $enabledSound")
            }else if(myAppExoplayerBird == null && enabledSound >= 5){
                Toast.makeText(requireContext(), "Нельзя включать более 5 звуков", Toast.LENGTH_LONG).show()
            }else {

                binding.btnBirdOne.isActivated = false
                myAppExoplayerBird?.stopPlaying()
                myAppExoplayerBird = null
                enabledSound--
                Log.d(TAG,"MainActivity: birdOne disable: $enabledSound")
            }
        }*/

        /*binding.btnCicada.setOnClickListener{
            if(myAppExoplayerCicada == null){

                binding.btnCicada.isActivated = true

                myAppExoplayerCicada = ExoplayerMain(requireContext(), RawResourceDataSource.buildRawResourceUri(R.raw.cicada))
                if(isPlayed)myAppExoplayerCicada?.startPlaying()
                enabledSound++
                Log.d(TAG,"MainActivity: cicada enable: $enabledSound")
            }else if(myAppExoplayerCicada == null && enabledSound >= 5){
                Toast.makeText(requireContext(), "Нельзя включать более 5 звуков", Toast.LENGTH_LONG).show()
            }else{

                binding.btnCicada.isActivated = false

                myAppExoplayerCicada?.stopPlaying()
                myAppExoplayerCicada = null
                enabledSound--
                Log.d(TAG,"MainActivity: cicada disable: $enabledSound")
            }
        }*/

        /*binding.btnRoofRain.setOnClickListener{
            if(myAppExoplayerRoofRain == null){

                binding.btnRoofRain.isActivated = true

                myAppExoplayerRoofRain = ExoplayerMain(requireContext(), RawResourceDataSource.buildRawResourceUri(R.raw.rain_on_roof))
                if(isPlayed) myAppExoplayerRoofRain?.startPlaying()
                enabledSound++
                Log.d(TAG,"MainActivity: roofRain enable: $enabledSound")
            }else if(myAppExoplayerRoofRain == null && enabledSound >= 5){
                Toast.makeText(requireContext(), "Нельзя включать более 5 звуков", Toast.LENGTH_LONG).show()
            }else{

                binding.btnRoofRain.isActivated = false

                myAppExoplayerRoofRain?.stopPlaying()
                myAppExoplayerRoofRain = null
                enabledSound--
                Log.d(TAG,"MainActivity: roofRain disable: $enabledSound")
            }
        }*/

        /*binding.btnVacuum.setOnClickListener{
            if(myAppExoplayerVacuum == null){

                binding.btnVacuum.isActivated = true

                myAppExoplayerVacuum = ExoplayerMain(requireContext(), RawResourceDataSource.buildRawResourceUri(R.raw.vacuum_cleaner))
                if(isPlayed) myAppExoplayerVacuum?.startPlaying()
                enabledSound++
                Log.d(TAG,"MainActivity: vacuum enable: $enabledSound")
            }else if(myAppExoplayerVacuum == null && enabledSound >= 5){
                Toast.makeText(requireContext(), "Нельзя включать более 5 звуков", Toast.LENGTH_LONG).show()
            }else{

                binding.btnVacuum.isActivated = false

                myAppExoplayerVacuum?.stopPlaying()
                myAppExoplayerVacuum = null
                enabledSound--
                Log.d(TAG,"MainActivity: vacuum disable: $enabledSound")
            }
        }*/

        /*binding.btnFrog.setOnClickListener{
            if(myAppExoplayerFrog == null){

                binding.btnFrog.isActivated = true

                myAppExoplayerFrog = ExoplayerMain(requireContext(), RawResourceDataSource.buildRawResourceUri(R.raw.frog))
                if(isPlayed) myAppExoplayerFrog?.startPlaying()
                enabledSound++
                Log.d(TAG,"MainActivity: frog enable: $enabledSound")
            }else if(myAppExoplayerFrog == null && enabledSound >= 5){
                Toast.makeText(requireContext(), "Нельзя включать более 5 звуков", Toast.LENGTH_LONG).show()
            }else{

                binding.btnFrog.isActivated = false

                myAppExoplayerFrog?.stopPlaying()
                myAppExoplayerFrog = null
                enabledSound--
                Log.d(TAG,"MainActivity: frog disable: $enabledSound")
            }
        }*/

        /*binding.btnTrain.setOnClickListener{
            if(myAppExoplayerTrain == null){

                binding.btnTrain.isActivated = true

                myAppExoplayerTrain = ExoplayerMain(requireContext(), RawResourceDataSource.buildRawResourceUri(R.raw.train))
                if(isPlayed) myAppExoplayerTrain?.startPlaying()
                enabledSound++
                Log.d(TAG,"MainActivity: train enable: $enabledSound")
            }else if(myAppExoplayerTrain == null && enabledSound >= 5){
                Toast.makeText(requireContext(), "Нельзя включать более 5 звуков", Toast.LENGTH_LONG).show()
            }else{

                binding.btnTrain.isActivated = false

                myAppExoplayerTrain?.stopPlaying()
                myAppExoplayerTrain = null
                enabledSound--
                Log.d(TAG,"MainActivity: train disable: $enabledSound")
            }
        }*/


        exoplayersList.add(myAppExoplayerCicada)
        exoplayersList.add(myAppExoplayerRoofRain)
        exoplayersList.add(myAppExoplayerBirdOne)
        exoplayersList.add(myAppExoplayerVacuum)
        exoplayersList.add(myAppExoplayerFrog)
        exoplayersList.add(myAppExoplayerTrain)

        exoplayersList.add(myAppExoplayerAirplane)
        exoplayersList.add(myAppExoplayerBirdTwo)
        exoplayersList.add(myAppExoplayerBirdThree)
        exoplayersList.add(myAppExoplayerCar)
        exoplayersList.add(myAppExoplayerWind)
        exoplayersList.add(myAppExoplayerFan)
        exoplayersList.add(myAppExoplayerHairDryer)
        exoplayersList.add(myAppExoplayerLightRain)
        exoplayersList.add(myAppExoplayerOcean)
        exoplayersList.add(myAppExoplayerOwl)
        exoplayersList.add(myAppExoplayerRainInForest)
        exoplayersList.add(myAppExoplayerRainInWindow)
        exoplayersList.add(myAppExoplayerSleet)
        exoplayersList.add(myAppExoplayerStream)
        exoplayersList.add(myAppExoplayerThunder)

        setupMusicType(binding.sbCicada, 0, binding.btnCicada, R.raw.cicada)
        setupMusicType(binding.sbRoofRain, 1, binding.btnRoofRain, R.raw.rain_on_roof)
        setupMusicType(binding.sbBirdOne, 2, binding.btnBirdOne, R.raw.bird_sing_1)
        setupMusicType(binding.sbVacuum, 3, binding.btnVacuum, R.raw.vacuum_cleaner)
        setupMusicType(binding.sbFrog, 4, binding.btnFrog, R.raw.frog)
        setupMusicType(binding.sbTrain, 5, binding.btnTrain, R.raw.train)

        setupMusicType(binding.sbAirplane ,6, binding.btnAirplane, R.raw.airplane)
        setupMusicType(binding.sbBirdTwo ,7, binding.btnBirdTwo, R.raw.bird_sing_2)
        setupMusicType(binding.sbBirdThree ,8, binding.btnBirdThree, R.raw.bird_sing_3)
        setupMusicType(binding.sbCar ,9, binding.btnCar, R.raw.car)
        setupMusicType(binding.sbWind ,10, binding.btnWind, R.raw.wind)
        setupMusicType(binding.sbFan ,11, binding.btnFan, R.raw.fan)
        setupMusicType(binding.sbHairDryer ,12, binding.btnHairDryer, R.raw.hairdryer)
        setupMusicType(binding.sbLightRain ,13, binding.btnLightRain, R.raw.light_rain)
        setupMusicType(binding.sbOcean ,14, binding.btnOcean, R.raw.ocean)
        setupMusicType(binding.sbOwl ,15, binding.btnOwl, R.raw.owl)
        setupMusicType(binding.sbRainInForest ,16, binding.btnRainInForest, R.raw.rain_in_forest)
        setupMusicType(binding.sbRainInWindow ,17, binding.btnRainOnWindow, R.raw.rain_on_window)
        setupMusicType(binding.sbSleet ,18, binding.btnSleet, R.raw.sleet)
        setupMusicType(binding.sbStream ,19, binding.btnStream, R.raw.stream)
        setupMusicType(binding.sbThunder ,20, binding.btnThunder, R.raw.thunder)



        //setupMusicType(seekBar: SeekBar, exoPlayer: ExoplayerMain?, button: Button, musicResId: Int)


        /*val myAppExoplayer1 = MyAppExoplayer(this)
        myAppExoplayer1.startPlaying(RawResourceDataSource.buildRawResourceUri(R.raw.cicada))

        val myAppExoplayer2 = MyAppExoplayer(this)
        myAppExoplayer2.startPlaying(RawResourceDataSource.buildRawResourceUri(R.raw.rain_on_roof))

        val myAppExoplayer3 = MyAppExoplayer(this)
        myAppExoplayer3.startPlaying(RawResourceDataSource.buildRawResourceUri(R.raw.bird_sing_1))*/

        return binding.root
    }

    private fun setupMusicType(seekBar: SeekBar, exoPlayerIndex: Int, button: ImageButton, musicResId: Int){
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //myAppExoplayerCicada?.setVolume(p1.toFloat()/100)
                exoplayersList[exoPlayerIndex]?.setVolume(progress.toFloat()/100)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        button.setOnClickListener {
            if(exoplayersList[exoPlayerIndex] == null && enabledSound < 5){
                button.isActivated = true

                //myAppExoplayerBird = ExoplayerMain(requireContext(), RawResourceDataSource.buildRawResourceUri(R.raw.bird_sing_1))
                exoplayersList[exoPlayerIndex] = ExoplayerMain(requireContext(), RawResourceDataSource.buildRawResourceUri(musicResId))

                if(isPlayed) exoplayersList[exoPlayerIndex]?.startPlaying()
                enabledSound++
            }else if(exoplayersList[exoPlayerIndex] == null && enabledSound >=5 ){
                Toast.makeText(requireContext(), "Нельзя включать более 5 звуков", Toast.LENGTH_LONG).show()
            }else{
                button.isActivated = false
                exoplayersList[exoPlayerIndex]?.stopPlaying()
                exoplayersList[exoPlayerIndex] = null
                enabledSound--
            }
        }
    }

    private fun playSound(){
        Log.d(LOG_TAG,"MainActivity: playSound")
        /*exoPlayers.forEach{
            it?.startPlaying()
        }*/

        exoplayersList[0]?.startPlaying()
        exoplayersList[1]?.startPlaying()
        exoplayersList[2]?.startPlaying()
        exoplayersList[3]?.startPlaying()
        exoplayersList[4]?.startPlaying()
        exoplayersList[5]?.startPlaying()
        exoplayersList[6]?.startPlaying()
        exoplayersList[7]?.startPlaying()
        exoplayersList[8]?.startPlaying()
        exoplayersList[9]?.startPlaying()
        exoplayersList[10]?.startPlaying()
        exoplayersList[11]?.startPlaying()
        exoplayersList[12]?.startPlaying()
        exoplayersList[13]?.startPlaying()
        exoplayersList[14]?.startPlaying()
        exoplayersList[15]?.startPlaying()
        exoplayersList[16]?.startPlaying()
        exoplayersList[17]?.startPlaying()
        exoplayersList[18]?.startPlaying()
        exoplayersList[19]?.startPlaying()
        exoplayersList[20]?.startPlaying()
        //myAppExoplayerBird?.startPlaying()
        //myAppExoplayerCicada?.startPlaying()
        //myAppExoplayerRoofRain?.startPlaying()
        //myAppExoplayerVacuum?.startPlaying()
        //myAppExoplayerFrog?.startPlaying()
        //myAppExoplayerTrain?.startPlaying()
    }

    private fun stopSound(){
        Log.d(LOG_TAG,"MainActivity: stopSound")
        /*exoPlayers.forEach{
            it?.stopPlaying()
        }*/

        exoplayersList[0]?.stopPlaying()
        exoplayersList[1]?.stopPlaying()
        exoplayersList[2]?.stopPlaying()
        exoplayersList[3]?.stopPlaying()
        exoplayersList[4]?.stopPlaying()
        exoplayersList[5]?.stopPlaying()
        exoplayersList[6]?.stopPlaying()
        exoplayersList[7]?.stopPlaying()
        exoplayersList[8]?.stopPlaying()
        exoplayersList[9]?.stopPlaying()
        exoplayersList[10]?.stopPlaying()
        exoplayersList[11]?.stopPlaying()
        exoplayersList[12]?.stopPlaying()
        exoplayersList[13]?.stopPlaying()
        exoplayersList[14]?.stopPlaying()
        exoplayersList[15]?.stopPlaying()
        exoplayersList[16]?.stopPlaying()
        exoplayersList[17]?.stopPlaying()
        exoplayersList[18]?.stopPlaying()
        exoplayersList[19]?.stopPlaying()
        exoplayersList[20]?.stopPlaying()

        //myAppExoplayerBird?.stopPlaying()
        //myAppExoplayerBird = null

        //myAppExoplayerCicada?.stopPlaying()
        //myAppExoplayerCicada = null

        //myAppExoplayerRoofRain?.stopPlaying()
        //myAppExoplayerRoofRain = null


        //myAppExoplayerVacuum?.stopPlaying()
        //myAppExoplayerVacuum = null

        //myAppExoplayerFrog?.stopPlaying()
        //myAppExoplayerFrog = null

        //myAppExoplayerTrain?.stopPlaying()
        //myAppExoplayerTrain = null

        /*private var myAppExoplayerBird: MyAppExoplayer? = null
        private var myAppExoplayerCicada: MyAppExoplayer? = null
        private var myAppExoplayerRoofRain: MyAppExoplayer? = null
        private var myAppExoplayerVacuum: MyAppExoplayer? = null
        private var myAppExoplayerFrog: MyAppExoplayer? = null
        private var myAppExoplayerTrain: MyAppExoplayer? = null*/
    }

}

class PlaybackStateListener: Player.EventListener {

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        var stateString = ""
        when(playbackState){
            ExoPlayer.STATE_IDLE -> {
                stateString = "ExoPlayer.STATE_IDLE     -"
            }
            ExoPlayer.STATE_BUFFERING -> {
                stateString = "ExoPlayer.STATE_BUFFERING     -"
            }
            ExoPlayer.STATE_READY -> {
                stateString = "ExoPlayer.STATE_READY     -"
            }
            ExoPlayer.STATE_ENDED -> {
                stateString = "ExoPlayer.STATE_ENDED     -"
            }
        }

        Log.d(LOG_TAG,"PlaybackStateListener: changed state to $stateString, playWhenReady: $playWhenReady")
    }
}