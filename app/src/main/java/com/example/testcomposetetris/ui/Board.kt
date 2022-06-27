package com.example.testcomposetetris.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.tile

@Composable
fun Board() {
    val viewModel: MainViewModel = viewModel()
    val viewState = viewModel.viewState.value
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .weight(1F)
                .padding(
                    start = 100.dp,
                    end = 100.dp,
                    top = 100.dp
                ),
            contentAlignment = Alignment.TopCenter) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                val totalWidth = size.width
                val numberOfRectInRow = 12
                val padding = 12
                val totalWidthWithoutMargin = totalWidth - (numberOfRectInRow - 1) * padding
                val widthOfRectangle = totalWidthWithoutMargin / numberOfRectInRow

                viewState.tiles.forEachIndexed { positionY, row ->
                    row.forEachIndexed { positionX, isOccupied ->
                        val startPositionX = (positionX) * (padding + widthOfRectangle)
                        val startPositionY = (positionY) * (padding + widthOfRectangle)
                        tile(Offset(startPositionX,startPositionY),widthOfRectangle,isOccupied)
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            RotateButton(
                modifier = Modifier.padding(end = 40.dp),
                text = "Left"
            ) {
                viewModel.moveLeft()
            }

            RotateButton(
                modifier = Modifier.padding(start = 40.dp),
                text = "Right"
            ) {
                viewModel.moveRight()
            }
        }

        RotateButton(modifier = Modifier.padding(0.dp), text = "Down") {
            viewModel.moveDown()
        }


        Button(
            onClick = {
                viewModel.rotate()
            }
        ) {
            Text("Rotate")
        }

    }
}

@Composable
fun RotateButton(modifier: Modifier, text: String, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = { onClick.invoke()}
    ) {
        Text(text)
    }
}
