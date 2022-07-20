package com.example.testcomposetetris.ext

fun Boolean?.orFalse(): Boolean {
    return this ?: false
}