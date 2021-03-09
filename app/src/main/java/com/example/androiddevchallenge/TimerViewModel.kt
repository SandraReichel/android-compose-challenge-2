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

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Integer.parseInt

class TimerViewModel : ViewModel() {

    var currentTimerInput = "10"
    var startTime = 10
    private val interval = 1000L
    var countDownTimer: CountDownTimer

    private var _timeLeft = MutableLiveData(startTime)
    val timeLeft: LiveData<Int> = _timeLeft

    private var _isTimerRunning = MutableLiveData(false)
    val isTimerRunning: LiveData<Boolean> = _isTimerRunning

    private var _isInputValid = MutableLiveData(true)
    val isInputValid: LiveData<Boolean> = _isInputValid

    init {
        countDownTimer = object : CountDownTimer(startTime * 1000L, interval) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.postValue((millisUntilFinished / interval).toInt())
            }

            override fun onFinish() {}
        }
    }

    fun setCurrentInput(input: String) {
        currentTimerInput = input
        try {
            val num = parseInt(input)
            _isInputValid.postValue(true)
            startTime = num
        } catch (e: NumberFormatException) {
            _isInputValid.postValue(false)
        }
    }

    fun switchTimerRunning() {
        if (_isTimerRunning.value == true) {
            countDownTimer.cancel()
            _isTimerRunning.postValue(false)
        } else {
            countDownTimer.start()
            _isTimerRunning.postValue(true)
        }
    }
}
