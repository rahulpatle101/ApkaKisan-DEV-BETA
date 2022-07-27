package com.apkakisan.myapplication.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

object TimerUtil {

    fun incrementalTimer(start: Long = 0) = flow {
        var counter = start
        while (true) {
            emit(counter)
            counter++
            delay(1000)
        }
    }

    fun countDownTimer(start: Long, end: Long = 0L) = flow {
        for (i in start downTo end) {
            emit(i)
            delay(1000)
        }
    }
}