package kz.adamant.auth.ui.register

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import kz.adamant.auth.*
import kz.adamant.auth.databinding.FragmentAuthRegisterBinding
import kz.adamant.auth.ui.AuthViewModel
import kz.adamant.common.BaseViewModel
import kz.adamant.common.R
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.navigateWithAnim
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.common.extensions.setShowProgress
import kz.adamant.common.extensions.showInfoDialog
import kotlin.reflect.KClass

class AuthRegisterFragment: BindingFragment<FragmentAuthRegisterBinding>(FragmentAuthRegisterBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            etEmail.doAfterTextChanged {
                val isValid = isEmailValid()
                updateNextButtonState(emailValid = isValid)
                val text = if (!isValid) getString(R.string.auth_email_invalid) else null
                tilEmail.error = text
                tilEmail.setErrorIconOnClickListener { showInfoDialog(text ?: "") }
            }
            etName.doAfterTextChanged {
                val isValid = isNameValid()
                updateNextButtonState(nameValid = isValid)
                val text = if (!isValid) getString(R.string.auth_name_invalid) else null
                tilName.error = text
                tilName.setErrorIconOnClickListener { showInfoDialog(text ?: "") }
            }
            etPassword.doAfterTextChanged {
                updateNextButtonState()
            }

            btnNext.onSafeClick {
                if (!isPasswordValid()) {
                    showInfoDialog(getString(R.string.auth_password_invalid))
                    return@onSafeClick
                }
                vm?.signUp(
                    etEmail.text?.toString() ?: "",
                    etName.text?.toString() ?: "",
                    etPassword.text?.toString() ?: ""
                )
            }
            vm?.run {
                loading.observe(viewLifecycleOwner) {
                    btnNext.setShowProgress(it, getString(R.string.common_next))
                    if (!it) updateNextButtonState() else btnNext.isEnabled = false
                }
            }
            btnLogin.onSafeClick {
                findNavController().navigateWithAnim(kz.adamant.auth.R.id.authLoginFragment)
            }
        }

    }

    private val vm get() = vm(AuthViewModel::class)

    private fun isEmailValid() = binding.etEmail.text?.toString()?.isValidEmail() ?: false
    private fun isNameValid() = binding.etName.text?.toString()?.isValidName() ?: false
    private fun isPasswordValid() = binding.etPassword.text?.toString()?.isValidPassword() ?: false

    private fun updateNextButtonState(
        emailValid: Boolean = isEmailValid(),
        nameValid: Boolean = isNameValid(),
        passwordValid: Boolean = (binding.etPassword.text?.length ?: 0) > PASSWORD_AT_LEAST_SIZE
    ) = with(binding) {
        btnNext.isEnabled = emailValid && nameValid && passwordValid
    }

    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(AuthViewModel::class)
    )

}