package kz.adamant.auth.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import kz.adamant.common.R
import kz.adamant.auth.databinding.FragmentOnboardingSectionBinding
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kotlin.reflect.KClass

class OnboardingSectionFragment : BindingFragment<FragmentOnboardingSectionBinding>(FragmentOnboardingSectionBinding::inflate) {

    private val index get() = requireArguments().getInt(ARGS_INDEX, 0)

    companion object {
        private val ONBOARDINGS = listOf(
            OnboardingSection(R.drawable.ic_onboarding1, R.string.auth_onboarding_title1),
            OnboardingSection(R.drawable.ic_onboarding2, R.string.auth_onboarding_title2),
            OnboardingSection(R.drawable.ic_onboarding3, R.string.auth_onboarding_title3),
        )

        private const val ARGS_INDEX = "index"

        fun newInstance(index: Int): OnboardingSectionFragment {
            val fragment = OnboardingSectionFragment()
            fragment.arguments = bundleOf(ARGS_INDEX to index)
            return fragment
        }

        fun getSize() = ONBOARDINGS.size
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onboarding = ONBOARDINGS[index]
        binding.run {
            ivImage.setImageResource(onboarding.image)
            tvTitle.text = getString(onboarding.title)
        }
    }

    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf()

}

data class OnboardingSection(
    @DrawableRes val image: Int,
    @StringRes val title: Int
)