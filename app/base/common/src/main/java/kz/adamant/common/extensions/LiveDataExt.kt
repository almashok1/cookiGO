package kz.adamant.common.extensions

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> MutableLiveData<T>.setMain(value: T) {
    withContext(Dispatchers.Main) { this@setMain.value = value }
}