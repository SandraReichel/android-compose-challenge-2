package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class TimerStatus(
    var currentTime: Long,
    var isTimerRunning: Boolean
)

class TimerViewModel : ViewModel() {

    var startTime = 10000L
    private val interval = 1000L
    var countDownTimer: CountDownTimer

    private var _timeLeftInMilli = MutableLiveData(startTime)
    val timeLeftInMills: LiveData<Long> = _timeLeftInMilli

    private var _isTimerRunning = MutableLiveData(false)
    val isTimerRunning: LiveData<Boolean> = _isTimerRunning

    init {
        countDownTimer = object : CountDownTimer(startTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                //_isTimerRunning.postValue((millisUntilFinished / interval).toInt() % 2 == 0)
                _timeLeftInMilli.postValue(millisUntilFinished / interval)
            }

            override fun onFinish() {
                //mTextField.setText("done!")
            }
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