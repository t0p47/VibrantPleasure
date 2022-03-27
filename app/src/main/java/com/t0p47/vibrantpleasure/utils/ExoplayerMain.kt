package com.t0p47.vibrantpleasure.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class ExoplayerMain(private val context: Context, private val rawUri: Uri) {

    private var dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context, "exoplayer-codelab")
    private var player: ExoPlayer? = null

    var isPlayed = false

    init {

        initPlayer()
    }

    private fun initPlayer(){
        if(player == null){
            Log.d(LOG_TAG, "MyAppExoplayer: initPlayer")
            val trackSelector = DefaultTrackSelector()
            trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd())
            //player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
            player = ExoPlayer.Builder(context)
                .setTrackSelector(trackSelector)
                .build()

        }

        player?.playWhenReady = true
    }

    fun startPlaying(/*uri: Uri*/){

        Log.d(LOG_TAG, "MyAppExoplayer: startPlaying: $rawUri")
        if(player == null) initPlayer()

        val mediaSourceFactory = ProgressiveMediaSource.Factory(dataSourceFactory)

        //val mediaSource = mediaSourceFactory.createMediaSource(rawUri)
        val mediaSource = mediaSourceFactory.createMediaSource(MediaItem.fromUri(rawUri))

        val loopingMediaSource = LoopingMediaSource(mediaSource)

        player?.prepare(loopingMediaSource, false, false)
        isPlayed = true
    }

    fun stopPlaying(){
        if(player != null){
            player?.stop()
            isPlayed = false
        }
    }

    fun setVolume(volume: Float){
        player?.volume = volume
    }

}