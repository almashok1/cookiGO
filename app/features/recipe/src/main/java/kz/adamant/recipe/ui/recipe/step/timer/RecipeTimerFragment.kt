package kz.adamant.recipe.ui.recipe.step.timer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.SnackbarStyleType
import kz.adamant.common.extensions.makeSnackBar
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.recipe.databinding.FragmentRecipeTimerBinding
import kz.adamant.recipe.ui.recipe.step.timer.RecipeTimerViewModel.TimerState
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class RecipeTimerFragment : BindingFragment<FragmentRecipeTimerBinding>(
    FragmentRecipeTimerBinding::inflate
) {
    private val navArgs by navArgs<RecipeTimerFragmentArgs>()

    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(RecipeTimerViewModel::class) {
            { parametersOf(navArgs.duration) }
        }
    )

    private val vm get() = vm(RecipeTimerViewModel::class)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.timer.observe(viewLifecycleOwner) {
            when(it) {
                is TimerState.Finished -> showFinished()
                is TimerState.Paused -> setPlayButton()
                is TimerState.Ticking -> setTicking(it.time)
                is TimerState.Idle -> setTimerIdle()
            }
        }
        binding.btnPlayPause.onSafeClick {
            val value = vm.timer.value ?: return@onSafeClick
            when(value) {
                is TimerState.Idle, TimerState.Finished -> vm.startTimer()
                is TimerState.Paused -> vm.resumeTimer()
                else -> vm.pauseTimer()
            }
        }
        binding.btnReset.onSafeClick { vm.resetTimer() }
        binding.btnBack.onSafeClick { findNavController().navigateUp() }
    }

    @SuppressLint("SetTextI18n")
    fun Long.setTimerText() {
        binding.progressIndicator.setProgressCompat((this / 1000).toInt(), true)
        val minutes = this / 1000 / 60
        val seconds = this / 1000 % 60
        binding.tvTime.text = "$minutes m ${String.format("%02d", seconds)} s"
    }

    private fun showFinished() {
        requireActivity().makeSnackBar(
            "Timer is finished",
            styleType = SnackbarStyleType.SUCCESS
        )
        setPlayButton()
    }


    private fun setPauseButton() {
        setButtonSrc(kz.adamant.common.R.drawable.ic_timer_pause)
    }

    private fun setPlayButton() {
        setButtonSrc(kz.adamant.common.R.drawable.ic_timer_play)
    }

    private fun setButtonSrc(@DrawableRes src: Int) {
        binding.btnPlayPause.setImageResource(src)
    }

    private fun setTicking(time: Long) {
        time.setTimerText()
        setPauseButton()
    }

    private fun setTimerIdle() {
        binding.progressIndicator.max = 0
        binding.progressIndicator.max = (navArgs.duration).toInt()
        (navArgs.duration * 1000L).setTimerText()
        setPlayButton()
    }

}