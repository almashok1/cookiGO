package kz.adamant.recipe.ui.recipe.review

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import kz.adamant.common.BaseViewModel
import kz.adamant.common.EventObserver
import kz.adamant.common.R
import kz.adamant.common.binding.BindingBottomSheetFragment
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.common.extensions.setShowProgress
import kz.adamant.common.extensions.setUpRoundedCorners
import kz.adamant.data.managers.SessionManager
import kz.adamant.recipe.databinding.FragmentLeaveFeedbackBinding
import kotlin.reflect.KClass

class RecipeLeaveFeedbackFragment : BindingBottomSheetFragment<FragmentLeaveFeedbackBinding>(
    FragmentLeaveFeedbackBinding::inflate
) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(RecipeReviewViewModel::class)
    )

    override fun getTheme() = R.style.CustomBottomSheetDialog

    private val args by navArgs<RecipeLeaveFeedbackFragmentArgs>()
    
    private val vm get() = vm(RecipeReviewViewModel::class)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as BottomSheetDialog).setUpRoundedCorners()
        binding.tvName.text = SessionManager.username
        binding.ivClose.onSafeClick { dismiss() }
        binding.tvInitials.text = SessionManager.username.split(" ").run {
            (first().getOrNull(0)?.toString() ?: "") + (last().getOrNull(0)?.toString() ?: "")
        }
        binding.rateView.setClicks()
        binding.rateView.setRate(binding.rateView.inRate)
        vm.loadingLeaveReview.observe(viewLifecycleOwner, EventObserver {
            binding.btnPublish.setShowProgress(it, getString(R.string.common_publish))
        })
        binding.btnPublish.onSafeClick {
            vm.submitReview(
                args.recipeId,
                binding.rateView.inRate,
                binding.etText.text?.toString()
            )
        }
    }
}