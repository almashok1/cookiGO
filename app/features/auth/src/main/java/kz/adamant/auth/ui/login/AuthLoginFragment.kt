package kz.adamant.auth.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import kz.adamant.auth.PASSWORD_AT_LEAST_SIZE
import kz.adamant.auth.USERNAME_AT_LEAST_SIZE
import kz.adamant.auth.databinding.FragmentAuthLoginBinding
import kz.adamant.auth.isValidName
import kz.adamant.auth.ui.AuthViewModel
import kz.adamant.common.BaseViewModel
import kz.adamant.common.R
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.common.extensions.setShowProgress
import kotlin.reflect.KClass

class AuthLoginFragment: BindingFragment<FragmentAuthLoginBinding>(FragmentAuthLoginBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            etName.doAfterTextChanged {
                val isValid = isNameValid()
                updateNextButtonState(nameValid = isValid)
            }
            etPassword.doAfterTextChanged {
                updateNextButtonState()
            }
            btnNext.onSafeClick {
                vm?.signIn(
                    etName.text?.toString() ?: "",
                    etPassword.text?.toString() ?: ""
                )
            }
            vm?.run {
                loading.observe(viewLifecycleOwner) {
                    btnNext.setShowProgress(it, getString(R.string.common_login))
                    if (!it) updateNextButtonState() else btnNext.isEnabled = false
                }
            }
        }
    }

    private val vm get() = vm(AuthViewModel::class)

    private fun isNameValid() = binding.etName.text?.toString()?.isValidName() ?: false

    private fun updateNextButtonState(
        nameValid: Boolean = isNameValid(),
        passwordValid: Boolean = (binding.etPassword.text?.length ?: 0) > PASSWORD_AT_LEAST_SIZE
    ) = with(binding) {
        btnNext.isEnabled = nameValid && passwordValid
    }

    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(AuthViewModel::class)
    )

}