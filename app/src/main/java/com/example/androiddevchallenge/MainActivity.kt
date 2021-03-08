/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevchallenge.ui.theme.MyTheme


class MainActivity : AppCompatActivity() {

    private lateinit var timerViewModel: TimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)

        setContent {
            MyTheme {
                MyApp(timerViewModel)
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(viewModel: TimerViewModel) {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()
        ) {
            TopAppBar(
                title = {
                    Row {
                        Text(text = "Flippy, Sandy Timer")
                        Icon(Icons.Filled.Timelapse, contentDescription = null)
                    }
                }
            )
            TimerScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun TimerScreen(viewModel: TimerViewModel) {
    val timeLeftInMillis by viewModel.timeLeftInMills.observeAsState(

            viewModel.startTime

        )

    val running by viewModel.isTimerRunning.observeAsState(true)

    TimerControlView(
        timeLeftInMillis = timeLeftInMillis,
        changeRunningState = { viewModel.switchTimerRunning() },
        changeStartTime = { viewModel.startTime = it },
        running
    )
}

@Composable
fun TimerControlView(
    timeLeftInMillis: Long,
    changeRunningState: () -> Unit,
    changeStartTime: (Long) -> Unit,
    running: Boolean
) {
    Card(
        elevation = 8.dp, shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            //.clickable(onClick = onClick)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Time left is: , ${timeLeftInMillis}",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
            Spacer(Modifier.padding(8.dp))
            OutlinedTextField(
                value = "10000",
                onValueChange = { changeStartTime(10000L) },
                label = { Row() {
                    Text("Change Time")
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null
                    )
                } }
            )
            Spacer(Modifier.padding(8.dp))
            Button(onClick = { changeRunningState() }) {
                Row {
                        Icon(
                            if (running) Icons.Filled.PauseCircleOutline else Icons.Filled.PlayCircleOutline,
                            contentDescription = null
                        )
                    Text(if (running) "Pause" else "Start")
                }
            }
        }
    }
}

@Composable
fun SandClock() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        /* drawCircle(
             color = Color.Blue,
             center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
             radius = size.minDimension / 4
         )*/

        drawLine(
            start = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
            end = Offset(
                x = (canvasWidth / 2) - (canvasWidth / 3),
                y = ((canvasHeight / 2) + (canvasHeight / 3))
            ),
            color = Color.Blue,
            strokeWidth = 5F
        )

        drawLine(
            start = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
            end = Offset(
                x = ((canvasWidth / 2) + (canvasWidth / 3)),
                y = ((canvasHeight / 2) + (canvasHeight / 3))
            ),
            color = Color.Blue,
            strokeWidth = 5F
        )
    }
}


@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(TimerViewModel())
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(TimerViewModel())
    }
}
