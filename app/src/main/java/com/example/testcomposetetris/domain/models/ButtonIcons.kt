package com.example.testcomposetetris.domain.models

import androidx.annotation.DrawableRes
import com.example.testcomposetetris.R

enum class ButtonIcons(
    @DrawableRes val resId: Int
) {
    MUTE_SOUND(R.drawable.ic_baseline_volume_off_24),
    UNMUTE_SOUND(R.drawable.ic_baseline_volume_up_24),
    MUTE_MUSIC(R.drawable.ic_baseline_music_off_24),
    UNMUTE_MUSIC(R.drawable.ic_baseline_music_up_24),
    PLAY(R.drawable.ic_baseline_play_arrow_24),
    PAUSE(R.drawable.ic_baseline_pause_24)
}