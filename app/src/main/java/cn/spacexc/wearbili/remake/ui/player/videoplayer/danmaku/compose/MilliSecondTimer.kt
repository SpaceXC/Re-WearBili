package cn.spacexc.wearbili.remake.ui.player.videoplayer.danmaku.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import java.util.Timer
import java.util.TimerTask

class MillisecondTimer {
    private var timer: Timer? = null
    var elapsedTime by mutableLongStateOf(0L)
    private var isRunning: Boolean = false
    private var speedMultiplier: Float = 1f

    init {
        timer = Timer()
    }

    fun start() {
        if (!isRunning) {
            timer = Timer()
            timer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    elapsedTime += (1 * speedMultiplier).toLong()   //为了更好理解代码，非画蛇添足
                    //println("Elapsed Time: $elapsedTime milliseconds")
                }
            }, 0, 1)
            isRunning = true
        }
    }

    fun setSpeed(speed: Float) {
        speedMultiplier = speed
    }

    fun pause() {
        timer?.cancel()
        isRunning = false
    }

    fun seekTo(time: Long) {
        elapsedTime = time
    }

    fun reset() {
        timer?.cancel()
        elapsedTime = 0
        isRunning = false
        println("Timer Reset")
    }
}
