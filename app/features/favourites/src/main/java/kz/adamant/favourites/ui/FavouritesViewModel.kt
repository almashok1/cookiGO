package kz.adamant.favourites.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kz.adamant.arch.utils.doOnSuccess
import kz.adamant.common.BaseViewModel
import kz.adamant.common.extensions.setMain
import kz.adamant.data.managers.SessionManager
import kz.adamant.recipe.RecipeNavigator
import kz.adamant.recipe.domain.RecipeInteractor
import kz.adamant.recipe.domain.RecipeModel
import kz.adamant.recipe.ui.models.RecipeDvo
import kz.adamant.recipe.ui.models.RecipeDvoToRecipeModelMapper
import kz.adamant.recipe.ui.models.RecipeToRecipeDvoMapper

class FavouritesViewModel(
    private val recipeInteractor: RecipeInteractor,
    private val recipeDvoMapper: RecipeToRecipeDvoMapper,
    private val recipeModelMapper: RecipeDvoToRecipeModelMapper,
    private val recipeNavigator: RecipeNavigator
) : BaseViewModel() {

    private val _favourites = MutableStateFlow<List<RecipeDvo>>(listOf())
    val favourites get() = _favourites.asLiveData(viewModelScope.coroutineContext)

    private val _refreshing = MutableLiveData<Boolean>()
    val refreshing: LiveData<Boolean> get() = _refreshing

    private var lastSearch : String? = null

    private val mutex = Mutex()

    private fun observe() = launchIO {
        recipeInteractor.observeFavourites().collect {
            _favourites.value = applySearch(it, lastSearch)
        }
    }

    private fun applySearch(list: List<RecipeModel>, searchText: String?): List<RecipeDvo> {
        val current = list.map { recipeDvoMapper.map(it, true) }
        return if (searchText.isNullOrEmpty()) current
        else current.filter {
            it.title.contains(searchText, ignoreCase = true)
                    || it.description.contains(searchText, ignoreCase = true)
                    || it.category.name.contains(searchText, ignoreCase = true)
        }
    }

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun loadFavourites(withRefreshing: Boolean = false) = launchIO {
        withContext(Dispatchers.Main) {
            _loading.value = true
            if (withRefreshing) _refreshing.value = true
        }
        recipeInteractor.getFavourites(SessionManager.userId).doOnSuccess {
            _loading.setMain(false)
        }.handleError()
        _refreshing.setMain(false)
    }

    fun searchFavourites(text: String? = null) = launchIO {
        mutex.withLock {
            lastSearch = text
            _favourites.value = applySearch(
                recipeInteractor.getCurrentFavourites(),
                text)
        }
    }

    fun addFavourite(recipeDvo: RecipeDvo) = launchNotBoundIO {
        recipeInteractor.addFavourite(
            SessionManager.userId,
            recipeModelMapper.map(recipeDvo)
        )
    }

    fun removeFavourite(recipeDvo: RecipeDvo) = launchNotBoundIO {
        recipeInteractor.removeFavourite(
            SessionManager.userId,
            recipeModelMapper.map(recipeDvo)
        )
    }

    fun openRecipeDetail(recipeDvo: RecipeDvo) = withFragment {
        recipeNavigator.openRecipeDetails(it, recipeDvo)
    }

    fun openHome() = withFragment {
        recipeNavigator.goToHome(it)
    }

    init {
        observe()
        loadFavourites()
    }

}