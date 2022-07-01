package com.example.testcomposetetris.domain

fun generateRandomNumber(
    min: Int = 0,
    max: Int = 11
): Int = (min..max).random()
