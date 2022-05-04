package kz.adamant.recipe.domain

import kz.adamant.arch.models.Outcome
import kz.adamant.arch.utils.doOnSuccess
import kz.adamant.arch.utils.map
import kz.adamant.recipe.data.RecipeApi
import kz.adamant.recipe.data.RecipeIdRequest
import kz.adamant.recipe.data.RecipeLocalDataSource
import kz.adamant.recipe.data.RecipeSecondaryApi
import kz.adamant.recipe.data.UserAndRecipeIdRequest
import kz.adamant.recipe.data.UserIdRequest

class RecipeInteractor(
    private val api: RecipeApi,
    private val apiSecond: RecipeSecondaryApi,
    private val recipeLocalDataSource: RecipeLocalDataSource
) {
    suspend fun getRecommendationList(userId: Long): Outcome<RecommendationsModel> {
        return api.getRecommendationList(UserIdRequest(userId.toString())).map {
            RecommendationsModel(
                recommendations = it?.recommendations ?: listOf(),
                dailyRecommendation = it?.dailyRecommendation
            )
        }
    }

    suspend fun getIngredients(recipeId: Long) =
        api.getIngredientsById(RecipeIdRequest(recipeId.toString())).map {
            it?.toModel()
        }

    suspend fun getRecipeSteps(recipeId: Long) = apiSecond.getRecipeSteps(recipeId)

    suspend fun getRecipeReviews(recipeId: Long) = apiSecond.getReviews(recipeId).map {
        it?.sortedBy { review -> review.createdDate }
    }

    suspend fun leaveReview(request: LeaveReviewRequest) = apiSecond.leaveReview(request)

    suspend fun getFavourites(userId: Long): Outcome<List<RecipeModel>> {
        return apiSecond.getFavourites(userId).map {
            it ?: listOf()
        }.doOnSuccess {
            recipeLocalDataSource.updateFavourites(it ?: listOf())
        }
    }

    fun observeFavourites() = recipeLocalDataSource.favourites
    fun getCurrentFavourites() = recipeLocalDataSource.getCurrentFavourites()

    suspend fun addFavourite(userId: Long, recipeModel: RecipeModel) = apiSecond.addFavourite(
        UserAndRecipeIdRequest(userId, recipeModel.id)
    ).doOnSuccess {
        recipeLocalDataSource.addFavourite(recipeModel)
    }

    suspend fun removeFavourite(userId: Long, recipeModel: RecipeModel): Outcome<Void> {
        recipeLocalDataSource.removeFavourite(recipeModel)
        return apiSecond.removeFavourite(userId, recipeModel.id)
    }
}
