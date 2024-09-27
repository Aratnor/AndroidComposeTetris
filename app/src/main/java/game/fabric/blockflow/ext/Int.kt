package game.fabric.blockflow.ext

fun Int.convertToMinute(): String {
    val minute = this / 60
    return if(minute < 10) {
        "0$minute"
    } else {
        minute.toString()
    }
}

fun Int.convertToRemainingSecond(): String {
    val second = this % 60
    return if(second < 10) {
        "0$second"
    } else {
        second.toString()
    }
}