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
import android.view.Gravity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import kotlin.time.seconds


class MainActivity : AppCompatActivity() {

    private lateinit var timerViewModel: TimerViewModel

    @ExperimentalTime
    @ExperimentalAnimationApi
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
@ExperimentalTime
@ExperimentalAnimationApi
@Composable
fun MyApp(viewModel: TimerViewModel) {
    Surface(color = MaterialTheme.colors.background) {

        var panelVisible by rememberSaveable { mutableStateOf(false) }
        val timeLeftInMillis by viewModel.timeLeftInMills.observeAsState(viewModel.startTime)
        val running by viewModel.isTimerRunning.observeAsState(true)


        Column(
            horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()
        ) {
            TopAppBar(
                title = {
                    Row {
                        Text(text = "Sandy Timer ")
                        Icon(Icons.Filled.Timelapse, contentDescription = null)
                    }
                }
            )

            val clockRotation: Float by animateFloatAsState(if (running) 1f else 0.5f,
            animationSpec = tween(3000))
            Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                SandClock(rotateValue = clockRotation)
            }
        }

        AnimatedVisibility(
            visible = panelVisible,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
            )
        ) {

            TimerControlView(
                timeLeftInMillis = timeLeftInMillis,
                changeRunningState = { viewModel.switchTimerRunning() },
                changeStartTime = { viewModel.startTime = it },
                running
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(onClick = { panelVisible = !panelVisible }) {
                Row {
                    Icon(Icons.Filled.Settings, contentDescription = null)
                    Text("Setup the Timer")
                }
            }
        }
    }
}

@ExperimentalTime
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
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            val seconds = (timeLeftInMillis / 1000).rem(60)
            val minutes = ((timeLeftInMillis / 1000) / 60).toInt()

            val timeString = "$minutes:$seconds"

            Text(
                text = "Time left is:",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
            Text(
                text = timeString,
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h1
            )
            Spacer(Modifier.padding(8.dp))
            OutlinedTextField(
                value = "10000",
                onValueChange = { changeStartTime(10000L) },
                label = {
                    Row() {
                        Text("Change Time")
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null
                        )
                    }
                }
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
fun SandClock(rotateValue: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val pointMiddle = Offset(x = canvasWidth / 2, y = canvasHeight / 2)
        val pointTopLeft = Offset(
            x = ((canvasWidth / 2) + (canvasWidth / 3)),
            y = ((canvasHeight / 2) - (canvasHeight / 3))
        )
        val pointTopRight = Offset(
            x = ((canvasWidth / 2) - (canvasWidth / 3)),
            y = ((canvasHeight / 2) - (canvasHeight / 3))
        )
        val pointBottomLeft = Offset(
            x = ((canvasWidth / 2) + (canvasWidth / 3)),
            y = ((canvasHeight / 2) + (canvasHeight / 3))
        )
        val pointBottomRight = Offset(
            x = (canvasWidth / 2) - (canvasWidth / 3),
            y = ((canvasHeight / 2) + (canvasHeight / 3)
                    )
        )

        withTransform(
            {
                rotate(rotateValue * 360, pointMiddle)
                scale(0.5F, 0.5F)
            }, {

                drawLine(
                    start = pointMiddle,
                    end = pointBottomLeft,
                    color = Color.Blue,
                    strokeWidth = 10F
                )

                drawLine(
                    start = pointMiddle,
                    end = pointBottomRight,
                    color = Color.Blue,
                    strokeWidth = 10F
                )

                drawLine(
                    start = pointMiddle,
                    end = pointTopLeft,
                    color = Color.Blue,
                    strokeWidth = 10F
                )

                drawLine(
                    start = pointMiddle,
                    end = pointTopRight,
                    color = Color.Blue,
                    strokeWidth = 10F
                )

                drawLine(
                    start = pointTopRight,
                    end = pointTopLeft,
                    color = Color.Blue,
                    strokeWidth = 10F
                )

                drawLine(
                    start = pointBottomLeft,
                    end = pointBottomRight,
                    color = Color.Blue,
                    strokeWidth = 10F
                )
            }
        )
    }
}


@ExperimentalTime
@ExperimentalAnimationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(TimerViewModel())
    }
}

@ExperimentalTime
@ExperimentalAnimationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(TimerViewModel())
    }
}
