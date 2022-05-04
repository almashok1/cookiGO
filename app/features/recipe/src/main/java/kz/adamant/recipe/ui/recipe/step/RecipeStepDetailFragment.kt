package kz.adamant.recipe.ui.recipe.step

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import kz.adamant.common.BaseViewModel
import kz.adamant.common.R
import kz.adamant.common.binding.BindingBottomSheetFragment
import kz.adamant.common.extensions.loadWithDriveUrl
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.common.extensions.setUpRoundedCorners
import kz.adamant.recipe.databinding.FragmentRecipeStepDetailBinding
import kotlin.reflect.KClass

class RecipeStepDetailFragment : BindingBottomSheetFragment<FragmentRecipeStepDetailBinding>(
    FragmentRecipeStepDetailBinding::inflate
) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf()

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    private val args by navArgs<RecipeStepDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as BottomSheetDialog).setUpRoundedCorners()
        binding.run {
            tvDescription.text = args.step.text
            tvName.text = root.context.getString(R.string.recipe_stepNumber, args.step.stepNumber)
            ivPhoto.loadWithDriveUrl(args.step.image)
            ivClose.onSafeClick { dismiss() }
        }
    }

}