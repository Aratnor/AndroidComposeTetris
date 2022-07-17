package com.example.testcomposetetris.ui

import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.util.SoundType
import com.example.testcomposetetris.util.SoundUtil
import kotlin.math.absoluteValue

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameScreen(navController: NavHostController) {
    val viewModel: MainViewModel = viewModel()

    LaunchedEffect(key1 = 2) {
        viewModel.collect()
    }

    LaunchedEffect(key1 = 1) {
        viewModel.startGame()
    }
    var initialDragPosX by remember { mutableStateOf(0F) }
    var initialDragPosY by remember { mutableStateOf(0F) }
    var clickTime by remember { mutableStateOf(0L) }
    var clickCount by remember { mutableStateOf(0) }
    lateinit var mVelocityTracker: VelocityTracker
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth()
                .pointerInteropFilter {

                    when(it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialDragPosX = it.x
                            initialDragPosY = it.y
                            clickTime = System.currentTimeMillis()
                            mVelocityTracker = VelocityTracker.obtain()

                            mVelocityTracker.addMovement(it)

                            return@pointerInteropFilter true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            mVelocityTracker.addMovement(it)
                            mVelocityTracker.computeCurrentVelocity(1000)
                            val xDifference =initialDragPosX - it.x
                            val yDifference = initialDragPosY - it.y
                            val velocityY = mVelocityTracker.getYVelocity(it.getPointerId(it.actionIndex))
                            val velocityX = mVelocityTracker.getXVelocity(it.getPointerId(it.actionIndex))
                            Log.i("Velocity","$velocityY")
                            if(xDifference > viewModel.rectangleWidth || velocityX.compareTo(-1800) < 0 && velocityY.compareTo(500) < 0) {
                                SoundUtil.play(false, SoundType.Move)
                                viewModel.moveLeft()
                                initialDragPosX = it.x
                                initialDragPosY = it.y
                                clickCount = 0
                            } else if(xDifference < - viewModel.rectangleWidth || velocityX.compareTo(1800) > 0 && velocityY.compareTo(500) < 0) {
                                SoundUtil.play(false, SoundType.Move)
                                viewModel.moveRight()
                                initialDragPosX = it.x
                                initialDragPosY = it.y
                                clickCount = 0
                            } else if(yDifference > viewModel.rectangleWidth * 3 || velocityY.compareTo(3000) > 0) {
                                SoundUtil.play(false, SoundType.Drop)
                                viewModel.moveUp()
                                initialDragPosX = it.x
                                initialDragPosY = it.y
                                clickCount = 0

                            } else if(yDifference < -viewModel.rectangleWidth * 1.4) {
                                viewModel.moveDown()
                                SoundUtil.play(false, SoundType.Move)
                                initialDragPosX = it.x
                                initialDragPosY = it.y
                                clickCount = 0
                            }
                            return@pointerInteropFilter true
                        }
                        MotionEvent.ACTION_UP -> {
                            mVelocityTracker.recycle()

                            val diff = System.currentTimeMillis() - clickTime
                            if(diff < 250){
                                clickCount++
                            } else {
                                clickCount = 0
                            }
                            if(clickCount == 2) {
                                SoundUtil.play(false,SoundType.Rotate)
                                viewModel.rotate()
                                clickCount = 0
                            }
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
                    .padding(
                        top = 40.dp
                    )
                    .fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                Board(viewModel = viewModel, navController = navController)
            }
        }
    }

}