package game.fabric.blockflow.util

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import game.fabric.blockflow.MainViewModel
import game.fabric.blockflow.R
import game.fabric.blockflow.ext.orFalse

@SuppressLint("StaticFieldLeak")
object SoundUtil {

    private var _context: Context? = null
    private var viewModel: MainViewModel? = null
    private val sp: SoundPool by lazy {
        SoundPool.Builder().setMaxStreams(4).setMaxStreams(AudioManager.STREAM_MUSIC).build()
    }
    private lateinit var mediaPlayer: MediaPlayer
    private val _map = mutableMapOf<SoundType, Int>()

    fun init(context: Context,viewModel: MainViewModel) {
        if(_context == null) {
            SoundUtil.viewModel = viewModel
            _context = context
            mediaPlayer = MediaPlayer.create(context,R.raw.tetris_main_theme)
            mediaPlayer.isLooping = true
            Sounds.forEach {
                _map[it] = sp.load(_context, it.res, 1)
            }
        }
    }

    fun release() {
        _context = null
        viewModel = null
        sp.release()
    }

    fun playGameTheme(reset: Boolean = false) {
        if(SoundUtil::mediaPlayer.isInitialized) {
            if(mediaPlayer.isPlaying) return
            _context?.let {
                if(reset) {
                    mediaPlayer = MediaPlayer.create(it,R.raw.tetris_main_theme)
                }
                mediaPlayer.isLooping = true
            }
            mediaPlayer.start()
        }
    }
    fun stopGameTheme() {
        if(this::mediaPlayer.isInitialized) {
            mediaPlayer.pause()
        }
    }


    fun play(
        sound: SoundType,
        loop: Boolean = false) {
        if (!viewModel?.isMuted.orFalse()) {
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

