package kz.adamant.recipe.ui.recipe.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kz.adamant.arch.utils.doOnComplete
import kz.adamant.arch.utils.doOnError
import kz.adamant.arch.utils.doOnSuccess
import kz.adamant.arch.utils.onSuccess
import kz.adamant.common.BaseViewModel
import kz.adamant.common.Event
import kz.adamant.common.extensions.getState
import kz.adamant.common.extensions.liveData
import kz.adamant.common.extensions.setMain
import kz.adamant.common.extensions.setToPrevSavedState
import kz.adamant.data.managers.SessionManager
import kz.adamant.recipe.domain.RecipeInteractor
import kz.adamant.recipe.domain.LeaveReviewRequest
import kz.adamant.recipe.domain.Review
import kz.adamant.recipe.ui.recipe.review.RecipeReviewFragment.Companion.KEY_OBSERVE_ADDED
import java.util.Date

class RecipeReviewViewModel(
    private val recipeInteractor: RecipeInteractor
) : BaseViewModel() {

    private val _reviews = getState<List<Review>>(Dispatchers.IO)
    val reviews get() = _reviews.liveData

    private val _loadingLeaveReview = MutableLiveData<Event<Boolean>>()
    val loadingLeaveReview: LiveData<Event<Boolean>> get() = _loadingLeaveReview

    fun getReviews(recipeId: Long) {
        _reviews.request {
            recipeInteractor.getRecipeReviews(recipeId).apply {
                handleError()
            }
//                .doOnError {
//                    _reviews.setMain(listOf(
//                        Review(-1, "Almas Tanayev", 4, "Great pancakes! I don't know why more people don't make from scratch, they were so easy to make and tasted so much better! did add a bit of water as it was pretty thick, but after about 1/4 c it was perfect. Great pancakes! I don't know why more people don't make from scratch, they were so easy to make and tasted so much better! did add a bit of water as it was pretty thick, but after about 1/4 c it was perfect. Great pancakes! I don't know why more people don't make from scratch, they were so easy to make and tasted so much better! did add a bit of water as it was pretty thick, but after about 1/4 c it was perfect", Date()),
//                        Review(-2, "Erasyl Yediluly", 5, "Great pancakes!", Date()),
//                    ))
//                }
        }
    }

    fun submitReview(
        recipeId: Long,
        star: Int,
        text: String?
    ) = launchNotBoundIO {
        _loadingLeaveReview.setMain(Event(true))
        recipeInteractor.leaveReview(
            LeaveReviewRequest(
                SessionManager.userId,
                recipeId,
                text,
                star
            )
        ).onSuccess {
            withFragment {
                it.setToPrevSavedState(KEY_OBSERVE_ADDED, true)
                it.findNavController().navigateUp()
            }
        }.doOnComplete {
            _loadingLeaveReview.setMain(Event(false))
        }.handleError()
    }

}