package kz.adamant.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object AppMessages {

    private val _snackbarMessages = MutableLiveData<Event<Pair<String, (()->Unit)?>>>()
    val snackbarMessages: LiveData<Event<Pair<String, (() -> Unit)?>>> = _snackbarMessages

    fun sendSnackbarMessage(message: String, action: (()->Unit)? = null) {
        _snackbarMessages.postValue(Event(message to action))
    }

}
