package game.fabric.blockflow.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import game.fabric.blockflow.MainViewModel
import game.fabric.blockflow.orZero
import game.fabric.blockflow.ui.theme.BACKGROUND
import game.fabric.blockflow.util.SoundType
import game.fabric.blockflow.util.SoundUtil
import kotlin.math.absoluteValue

@SuppressLint("Recycle")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameScreen(navController: NavHostController) {
    val viewModel: MainViewModel = viewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        SoundUtil.init(context,viewModel)
    }

    SoundUtil.playGameTheme(true)

    LaunchedEffect(key1 = 2) {
        viewModel.collect()
    }

    LaunchedEffect(Unit) {
        viewModel.collectGameSoundActions()
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
            .background(BACKGROUND),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        var movementStartTime by remember {
            mutableStateOf(0L)
        }

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
                            movementStartTime = System.currentTimeMillis()
                            mVelocityTracker?.clear()
                            mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
                            mVelocityTracker?.addMovement(it)

                            return@pointerInteropFilter true
                        }

                        MotionEvent.ACTION_MOVE -> {
                            val xDifference = currentDragPosX - it.x
                            val yDifference = currentDragPosY - it.y
                            val passedTimeAsSec = (System.currentTimeMillis() - movementStartTime) / 1000.0
                            val velocityXAsSec = xDifference / passedTimeAsSec
                            val velocityYAsSec = yDifference / passedTimeAsSec
                            Log.i("Passed Time", "second $$passedTimeAsSec velocityX $velocityXAsSec velocityY $velocityYAsSec")
                            Log.i("Movement difference", "xDifference $xDifference y Difference $yDifference")
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
                                viewModel.moveLeft((xDifference / viewModel.rectangleWidth).toInt())
                                isHorizontalDragStarted = true
                                currentDragPosX = it.x
                                currentDragPosY = it.y
                                clickCount = 0
                            } else if (xDifference < -viewModel.rectangleWidth) {
                                SoundUtil.play(SoundType.Move)
                                viewModel.moveRight((xDifference / viewModel.rectangleWidth).absoluteValue.toInt())
                                isHorizontalDragStarted = true
                                currentDragPosX = it.x
                                currentDragPosY = it.y
                                clickCount = 0
                            } else if (
                                yDifference > -viewModel.rectangleWidth &&
                                velocityY
                                    ?.compareTo(-2000)
                                    .orZero() < 0 ||
                                velocityY
                                    ?.compareTo(3000)
                                    .orZero() > 0 &&
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

                            if (diff < 250) {
                                when {
                                    (currentDragPosX - initialDragPosX).absoluteValue == 0F &&
                                            (currentDragPosY - initialDragPosY).absoluteValue == 0F -> {
                                        clickCount++
                                    }

                                }

                                if (clickCount == 1) {
                                    SoundUtil.play(SoundType.Rotate)
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