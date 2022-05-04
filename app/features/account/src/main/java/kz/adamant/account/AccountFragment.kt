package kz.adamant.account

import android.os.Bundle
import android.view.View
import kz.adamant.account.databinding.FragmentAccountBinding
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.onSafeClick
import kotlin.reflect.KClass

class AccountFragment : BindingFragment<FragmentAccountBinding>(
    FragmentAccountBinding::inflate
) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(AccountViewModel::class)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.onSafeClick {
            vm(AccountViewModel::class)?.logout()
        }
    }
}