package kz.adamant.account

import android.os.Bundle
import android.view.View
import kz.adamant.account.databinding.FragmentAccountBinding
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.data.managers.SessionManager
import kotlin.reflect.KClass

class AccountFragment : BindingFragment<FragmentAccountBinding>(
    FragmentAccountBinding::inflate
) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(AccountViewModel::class)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvName.text = SessionManager.username
        binding.tvInitials.text = SessionManager.username.split(" ").run {
            (first().getOrNull(0)?.toString() ?: "") + (last().getOrNull(0)?.toString() ?: "")
        }
        binding.btnLogout.onSafeClick {
            vm(AccountViewModel::class)?.logout()
        }
    }
}