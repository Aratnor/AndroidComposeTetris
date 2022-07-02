package com.example.testcomposetetris.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.R

@Composable
fun GameScreen() {
    val viewModel: MainViewModel = viewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth()

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