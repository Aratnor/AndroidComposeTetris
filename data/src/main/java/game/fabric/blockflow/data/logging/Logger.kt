package game.fabric.blockflow.data.logging

import android.util.Log

data class Logger(val tag: String) {
    fun log(message: String) {
        Log.i(tag,message)
    }
}