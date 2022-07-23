package com.example.testcomposetetris.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.orZero
import com.example.testcomposetetris.ui.theme.BACKGROUND
import com.example.testcomposetetris.util.SoundType
import com.example.testcomposetetris.util.SoundUtil
import kotlin.math.absoluteValue

@SuppressLint("Recycle")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameScreen(navController: NavHostController) {
    val viewModel: MainViewModel = viewModel()

    SoundUtil.init(LocalContext.current,viewModel)


    SoundUtil.playGameTheme()

    LaunchedEffect(key1 = 2) {
        viewModel.collect()
    }

    LaunchedEffect(key1 = 1) {
        viewModel.startGame()
    }
    var initialDragPosX by remember { mutableStateOf(0F) }
    var initialDragPosY by remember { mutableStateOf(0F) }
    var currentDragPosX by remember { mutableStateOf(0F) }
    var currentDragPosY by remember { mutableStateOf(0F) }
    var isHorizontalDragStarted by remember { mutableStateOf(false) }
    var clickTime by remember { mutableStateOf(0L) }
    var clickCount by remember { mutableStateOf(0) }
    var mVelocityTracker: VelocityTracker? = null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BACKGROUND)
            ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter {

                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialDragPosX = it.x
                            initialDragPosY = it.y
                            currentDragPosX = it.x
                            currentDragPosY = it.y
                            clickTime = System.currentTimeMillis()
                            mVelocityTracker?.clear()
                            mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()

                            mVelocityTracker?.addMovement(it)

                            return@pointerInteropFilter true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val xDifference = currentDragPosX - it.x
                            val yDifference = currentDragPosY - it.y
                            mVelocityTracker?.addMovement(it)
                            mVelocityTracker?.computeCurrentVelocity(1000)
                            val velocityY =
                                mVelocityTracker?.getYVelocity(it.getPointerId(it.actionIndex))
                            val velocityX =
                                mVelocityTracker?.getXVelocity(it.getPointerId(it.actionIndex))
                            Log.i("Velocity X", "x $velocityX")
                            Log.i("Velocity Y", "y $velocityY")
                            if (xDifference > viewModel.rectangleWidth) {
                                SoundUtil.play(SoundType.Move)
                                viewModel.moveLeft((xDifference/viewModel.rectangleWidth).toInt())
                                isHorizontalDragStarted = true
                                currentDragPosX = it.x
                                currentDragPosY = it.y
                                clickCount = 0
                            } else if (xDifference < -viewModel.rectangleWidth) {
                                SoundUtil.play(SoundType.Move)
                                viewModel.moveRight((xDifference/viewModel.rectangleWidth).absoluteValue.toInt())
                                isHorizontalDragStarted = true
                                currentDragPosX = it.x
                                currentDragPosY = it.y
                                clickCount = 0
                            } else if (
                                yDifference.absoluteValue < viewModel.rectangleWidth &&
                                velocityY?.absoluteValue?.compareTo(2000).orZero() > 0 ||
                                yDifference.absoluteValue > viewModel.rectangleWidth &&
                                velocityY?.compareTo(1600).orZero() > 0 &&
                                !isHorizontalDragStarted
                            ) {
                                SoundUtil.play(SoundType.Drop)
                                viewModel.moveUp()
                                currentDragPosX = it.x
                                currentDragPosY = it.y
                                clickCount = 0

                            } else if (
                                yDifference < -viewModel.rectangleWidth * 1.4
                            ) {
                                viewModel.moveDown()
                                SoundUtil.play(SoundType.Move)
                                currentDragPosX = it.x
                                currentDragPosY = it.y
                                clickCount = 0
                            }
                            return@pointerInteropFilter true
                        }
                        MotionEvent.ACTION_UP -> {
                            mVelocityTracker?.addMovement(it)
                            isHorizontalDragStarted = false
                            val diff = System.currentTimeMillis() - clickTime

                            if(diff < 250) {
                                when {
                                    currentDragPosX  >= viewModel.muteButtonOffset.x + 20 &&
                                            currentDragPosX <= viewModel.muteButtonOffset.x + viewModel.muteButtonSize.width + 20 &&
                                            currentDragPosY >= viewModel.muteButtonOffset.y&&
                                            currentDragPosY <= viewModel.muteButtonOffset.y + viewModel.muteButtonSize.height + 20 -> {
                                        viewModel.isMuted = !viewModel.isMuted
                                        if(viewModel.isMuted) {
                                            SoundUtil.stopGameTheme()
                                        } else {
                                            SoundUtil.playGameTheme()
                                        }
                                            }
                                    (currentDragPosX - initialDragPosX).absoluteValue == 0F &&
                                            (currentDragPosY - initialDragPosY).absoluteValue  == 0F -> {
                                        clickCount++
                                            }

                                }

                                if (clickCount == 1) {
                                    SoundUtil.play( SoundType.Rotate)
                                    viewModel.rotate()
                                    clickCount = 0
                                }
                            }

                            mVelocityTracker?.recycle()
                            mVelocityTracker = null
                            return@pointerInteropFilter true

                        }
                    }
                    false
                },
            horizontalArrangement  =  Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                Board(viewModel = viewModel, navController = navController)
            }
        }
    }

}