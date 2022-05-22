package kz.adamant.recipe.ui.recipe.step.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.adamant.common.BaseViewModel
import kz.adamant.common.ExtendedCountdownTimer

class RecipeTimerViewModel(
    private val timeSeconds: Long,
) : BaseViewModel() {

    private val _timer = MutableLiveData<TimerState>(TimerState.Idle)
    val timer: LiveData<TimerState> get() = _timer

    private var countDownTimer = createCountdownTimer()

    private fun createCountdownTimer() = object: ExtendedCountdownTimer(timeSeconds * 1000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            _timer.value = TimerState.Ticking(millisUntilFinished)
        }

        override fun onFinish() {
            _timer.value = TimerState.Finished
        }

    }

    fun startTimer() {
        countDownTimer.start()
    }

    fun resumeTimer() {
        countDownTimer.resume()
    }

    fun pauseTimer() {
        countDownTimer.pause()
        _timer.value = TimerState.Paused
    }

    fun resetTimer() {
        countDownTimer.pause()
        countDownTimer.cancel()
        countDownTimer = createCountdownTimer()
        _timer.value = TimerState.Idle
    }


    sealed class TimerState {
        class Ticking(val time: Long): TimerState()
        object Finished : TimerState()
        object Paused: TimerState()
        object Idle: TimerState()
    }

}