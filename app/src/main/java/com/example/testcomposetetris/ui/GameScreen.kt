package com.example.testcomposetetris.ui

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.R

@Composable
fun GameScreen() {
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
                        change.consumeAllChanges()
                        when {
                            dragAmount.y > 20 -> viewModel.moveDown()
                            dragAmount.y < -20 -> viewModel.moveUp()
                            dragAmount.x > 20 -> viewModel.moveRight()
                            dragAmount.x < -20 -> viewModel.moveLeft()
                        }
                        Log.i("Drag Amount","X : ${dragAmount.x} Y: ${dragAmount.y}")
                    }
                },
            horizontalArrangement  =  Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        top = 40.dp
                    ),
                contentAlignment = Alignment.TopStart
            ) {
                Board(viewModel = viewModel)
            }
            NextPieceLayout(viewModel = viewModel)
        }

        RotateButton(
            modifier = Modifier.padding(),
            text = stringResource(R.string.up_button_text)) {
            viewModel.moveUp()
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            RotateButton(
                modifier = Modifier.padding(end = 40.dp),
                text = stringResource(R.string.left_button_text)
            ) {
                viewModel.moveLeft()
            }

            RotateButton(
                modifier = Modifier.padding(start = 40.dp),
                text = stringResource(R.string.right_button_text)
            ) {
                viewModel.moveRight()
            }
        }

        RotateButton(
            modifier = Modifier.padding(bottom = 20.dp),
            text = stringResource(R.string.down_button_text)
        ) {
            viewModel.moveDown()
        }


        Button(
            onClick = {
                viewModel.rotate()
            }
        ) {
            Text(stringResource(R.string.rotate_button_text))
        }
    }

}