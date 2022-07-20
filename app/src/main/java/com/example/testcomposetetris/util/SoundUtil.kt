package com.example.testcomposetetris.util

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import com.example.testcomposetetris.R

@SuppressLint("StaticFieldLeak")
object SoundUtil {

    private var _context: Context? = null
    private val sp: SoundPool by lazy {
        SoundPool.Builder().setMaxStreams(4).setMaxStreams(AudioManager.STREAM_MUSIC).build()
    }
    private lateinit var mediaPlayer: MediaPlayer
    private val _map = mutableMapOf<SoundType, Int>()

    fun init(context: Context) {
        _context = context
        mediaPlayer = MediaPlayer.create(context,R.raw.tetris_main_theme)
        mediaPlayer.isLooping = true
        Sounds.forEach {
            _map[it] = sp.load(_context, it.res, 1)
        }
    }

    fun release() {
        _context = null
        sp.release()
    }

    fun playGameTheme() {
        if(!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    fun stopGameTheme() {
        mediaPlayer.stop()
    }


    fun play(
        isMute: Boolean,
        sound: SoundType,
        loop: Boolean = false) {
        if (!isMute) {
            val streamId =
                sp.play(
                    requireNotNull(_map[sound]),
                    1f,
                    1f,
                    0,
                    0,
                    1f)
            val loopNumber = if(loop) { -1 } else { 0 }
            sp.setLoop(streamId,loopNumber)
        }
    }

}

sealed class SoundType(val res: Int) {
    object Move : SoundType(R.raw.move)
    object Rotate : SoundType(R.raw.rotate)
    object Start : SoundType(R.raw.start)
    object Drop : SoundType(R.raw.drop)
    object Clean : SoundType(R.raw.clean)
}

val Sounds =
    listOf(SoundType.Move, SoundType.Rotate, SoundType.Start, SoundType.Drop, SoundType.Clean)

