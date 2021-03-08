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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
        TimerScreen(viewModel = viewModel)
        Text(text = "Ready... Set... GO!")
    }
}

@Composable
fun TimerScreen(viewModel: TimerViewModel) {
    val timeLeftInMillis by viewModel.timeLeftInMills.observeAsState(
        TimerStatus(
            viewModel.startTime,
            false
        )
    )

    TimerControlView(
        timeLeftInMillis = timeLeftInMillis,
        changeRunningState = { viewModel.isTimerRunning = it },
        changeStartTime = { viewModel.startTime = it }
    )
}

@Composable
fun TimerControlView(
    timeLeftInMillis: TimerStatus,
    changeRunningState: (Boolean) -> Unit,
    changeStartTime: (Long) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Time left is: , $timeLeftInMillis",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = timeLeftInMillis.toString(),
            onValueChange = { changeStartTime(10000L) },
            label = { Text("Change Time") }
        )
        Button(onClick = {}) {
            Row() {
                val icon =
                    Icon(
                        if (timeLeftInMillis.isTimerRunning) Icons.Filled.PauseCircleOutline else Icons.Filled.PlayCircleOutline,
                        contentDescription = null
                    )
                Text(" Adopt Now")
            }
        }
    }
}


@Composable
fun HelloContent2(name: String, onNameChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello, $name",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = name,
            onValueChange = { onNameChange(it) },
            label = { Text("Name") }
        )
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

    Row {
        Card(
            elevation = 8.dp, shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                //.clickable(onClick = onClick)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                //NameTag(cat = cat)}}}
            }
        }
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
