package com.example.testcomposetetris

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testcomposetetris.ui.Board
import com.example.testcomposetetris.ui.theme.TestComposeTetrisTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestComposeTetrisTheme {
                // A surface container using the 'background' color from the theme
                val viewModel: MainViewModel = viewModel()
                LaunchedEffect(key1 = Unit) {
                    viewModel.collect()
                }
                viewModel.start()
                Board()
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}



fun DrawScope.tile(
    topLeftPosition: Offset,
    width: Float,
    isNotEmpty: Boolean = false) {
    val color = if(isNotEmpty) {
        Color.Black
    } else {
        Color.Gray
    }
    val alpha = if(isNotEmpty) {
        1F
    } else {
        0.3F
    }
    drawRect(
        color,
        topLeftPosition,
        Size(width,width),
        alpha,
        Stroke(width = 2F)
    )
    val innerOffset = 8
    val innerTopLeftPosition = Offset(
        topLeftPosition.x + innerOffset,
        topLeftPosition.y + innerOffset
    )
    val innerWidth = width - 2 * innerOffset
    drawRect(
        color,
        innerTopLeftPosition,
        Size(innerWidth,innerWidth),
        alpha,
        Fill
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TestComposeTetrisTheme {
        Greeting("Android")
    }
}