package com.example.testcomposetetris.ui

import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.R
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
    var currentDragPosX by remember { mutableStateOf(0F) }
    var currentDragPosY by remember { mutableStateOf(0F) }
    var isHorizontalDragStarted by remember { mutableStateOf(false) }
    var clickTime by remember { mutableStateOf(0L) }
    var clickCount by remember { mutableStateOf(0) }
    lateinit var mVelocityTracker: VelocityTracker
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.dark_blue)),
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
                            mVelocityTracker = VelocityTracker.obtain()

                            mVelocityTracker.addMovement(it)

                            return@pointerInteropFilter true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val xBaseDifference = it.x - initialDragPosX
                            val yBaseDifference = it.y - initialDragPosY
                            val xDifference = currentDragPosX - it.x
                            val yDifference = currentDragPosY - it.y
                            val velocityY =
                                mVelocityTracker.getYVelocity(it.getPointerId(it.actionIndex))
                            val velocityX =
                                mVelocityTracker.getXVelocity(it.getPointerId(it.actionIndex))
                            Log.i("Velocity X", "x $velocityX")
                            Log.i("Velocity Y", "y $velocityY")
                            if (xDifference > viewModel.rectangleWidth || velocityX.compareTo(-1200) < 0 && velocityY.compareTo(
                                    500
                                ) < 0
                            ) {
                                SoundUtil.play(false, SoundType.Move)
                                viewModel.moveLeft()
                                isHorizontalDragStarted = true
                                currentDragPosX = it.x
                                currentDragPosY = it.y
                                clickCount = 0
                            } else if (xDifference < -viewModel.rectangleWidth || velocityX.compareTo(
                                    1200
                                ) > 0 && velocityY.compareTo(500) < 0
                            ) {
                                SoundUtil.play(false, SoundType.Move)
                                viewModel.moveRight()
                                isHorizontalDragStarted = true
                                currentDragPosX = it.x
                                currentDragPosY = it.y
                                clickCount = 0
                            } else if (
                                yDifference > viewModel.rectangleWidth * 3 ||
                                velocityY.compareTo(1500) > 0 &&
                                !isHorizontalDragStarted
                            ) {
                                SoundUtil.play(false, SoundType.Drop)
                                viewModel.moveUp()
                                currentDragPosX = it.x
                                currentDragPosY = it.y
                                clickCount = 0

                            } else if (
                                yDifference < -viewModel.rectangleWidth * 1.4
                            ) {
                                viewModel.moveDown()
                                SoundUtil.play(false, SoundType.Move)
                                currentDragPosX = it.x
                                currentDragPosY = it.y
                                clickCount = 0
                            }
                            it.offsetLocation(xBaseDifference, yBaseDifference)
                            mVelocityTracker.addMovement(it)
                            mVelocityTracker.computeCurrentVelocity(1000)
                            return@pointerInteropFilter true
                        }
                        MotionEvent.ACTION_UP -> {
                            mVelocityTracker.recycle()
                            isHorizontalDragStarted = false
                            val diff = System.currentTimeMillis() - clickTime
                            if (diff < 250) {
                                clickCount++
                            } else {
                                clickCount = 0
                            }
                            if (clickCount == 2) {
                                SoundUtil.play(false, SoundType.Rotate)
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
                    .fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                Board(viewModel = viewModel, navController = navController)
            }
        }
    }

}