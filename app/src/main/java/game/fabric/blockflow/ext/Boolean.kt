package game.fabric.blockflow.ext

fun Boolean?.orFalse(): Boolean {
    return this ?: false
}