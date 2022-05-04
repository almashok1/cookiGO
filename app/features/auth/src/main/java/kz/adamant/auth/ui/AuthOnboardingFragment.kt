package kz.adamant.auth.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.adamant.auth.R
import kz.adamant.auth.databinding.FragmentAuthOnboardingBinding
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.getSlideAnimNavBuilder
import kz.adamant.common.extensions.navigateWithAnim
import kz.adamant.common.extensions.onSafeClick
import kotlin.reflect.KClass

class AuthOnboardingFragment: BindingFragment<FragmentAuthOnboardingBinding>(FragmentAuthOnboardingBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            viewPager2.adapter = ViewPagerAdapter(this@AuthOnboardingFragment)
            dotsIndicator.setViewPager2(binding.viewPager2)

            btnAction.onSafeClick {
                findNavController().navigateWithAnim(R.id.authRegisterFragment)
            }
        }
    }

    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf()

}

private class ViewPagerAdapter(
    fragment: Fragment
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = OnboardingSectionFragment.getSize()

    override fun createFragment(position: Int): Fragment {
        return OnboardingSectionFragment.newInstance(position)
    }

}