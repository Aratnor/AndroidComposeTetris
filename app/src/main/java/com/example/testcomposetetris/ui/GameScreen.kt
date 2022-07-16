package com.example.testcomposetetris.ui

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.testcomposetetris.MainViewModel

@Composable
fun GameScreen(navController: NavHostController) {
    val viewModel: MainViewModel = viewModel()

    LaunchedEffect(key1 = 2) {
        viewModel.collect()
    }

    LaunchedEffect(key1 = 1) {
        viewModel.startGame()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        Log.i("Drag Gesture","$dragAmount")
                        change.consumeAllChanges()
                        when {
                            dragAmount.y > 12-> viewModel.moveDown()
                            dragAmount.y < -35  -> viewModel.moveUp()
                            dragAmount.x > 8  -> viewModel.moveRight()
                            dragAmount.x < -8 -> viewModel.moveLeft()
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            viewModel.rotate()
                        }
                    )
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