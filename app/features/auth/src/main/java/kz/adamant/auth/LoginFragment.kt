package kz.adamant.auth

import kz.adamant.auth.databinding.FragmentLoginBinding
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kotlin.reflect.KClass

class LoginFragment : BindingFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(LoginViewModel::class)
    )
}
