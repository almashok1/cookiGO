package kz.adamant.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.adamant.arch.models.Outcome
import kz.adamant.arch.utils.doOnComplete
import kz.adamant.arch.utils.onSuccess
import kz.adamant.common.BaseViewModel
import kz.adamant.common.extensions.getState
import kz.adamant.common.extensions.liveData
import kz.adamant.common.extensions.setMain
import kz.adamant.preferences.domain.Preferences
import kz.adamant.preferences.domain.UserPreferencesInteractor

class UserPreferencesViewModel(
    private val interactor: UserPreferencesInteractor,
    private val userPreferencesNavigator: UserPreferencesNavigator
) : BaseViewModel() {

    private val _state = getState<Preferences>()
    val state = _state.liveData

    private val _loadingSubmit = MutableLiveData<Boolean>()
    val loadingSubmit: LiveData<Boolean> = _loadingSubmit

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        launchIO {
            _state.request(action = ::getPreferences)
        }
    }

    private suspend fun getPreferences(): Outcome<Preferences> {
        return interactor.getPreferences()
    }

    fun submit(
        cuisines: List<Preferences.Item>,
        categories: List<Preferences.Item>
    ) {
        _loadingSubmit.value = true
        launchIO {
            interactor.submitPreferences(Preferences(
                cuisines = cuisines,
                categories = categories
            )).onSuccess {
                withFragment {
                    userPreferencesNavigator.openMain(it)
                }
            }.doOnComplete {
                _loadingSubmit.setMain(false)
            }.handleError()
        }
    }
}