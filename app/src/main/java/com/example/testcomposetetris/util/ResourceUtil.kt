package com.example.testcomposetetris.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap

object ResourceUtil {

    var resources: Resources? = null

    fun init(
        resources: Resources
    ) {
        this.resources = resources
    }

    fun getDrawable(
        @DrawableRes resId: Int
    ): Drawable? = resources?.let {
            ResourcesCompat.getDrawable(
                it,
                resId,
                null
            )
        }

    fun getBitmap(
        @DrawableRes resId: Int
    ): Bitmap? = resources?.let {
        getDrawable(resId)?.toBitmap()
    }
}