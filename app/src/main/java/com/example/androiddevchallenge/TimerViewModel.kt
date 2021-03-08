package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    var startTime = 10000L
    var interval = 1000L
    var isTimerRunning = false
    var countDownTimer: CountDownTimer

    private var _timeLeftInMilli = MutableLiveData(startTime)
    val timeLeftInMills: LiveData<Long> = _timeLeftInMilli

    init {
        countDownTimer = object : CountDownTimer(startTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeftInMilli.postValue(millisUntilFinished / interval)
            }

            override fun onFinish() {
                //mTextField.setText("done!")
            }
        }
    }
}