package kz.adamant.recipe.data

import kz.adamant.arch.api.SerializedName
import kz.adamant.arch.models.Outcome
import kz.adamant.recipe.domain.CookingStep
import kz.adamant.recipe.domain.IngredientListDto
import kz.adamant.recipe.domain.LeaveReviewRequest
import kz.adamant.recipe.domain.RecipeModel
import kz.adamant.recipe.domain.Recommendations
import kz.adamant.recipe.domain.Review
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeApi {

    @POST("api/getRecommendationsList")
    suspend fun getRecommendationList(@Body request: UserIdRequest): Outcome<Recommendations>

    @POST("api/getIngredientsById")
    suspend fun getIngredientsById(
        @Body request: RecipeIdRequest
    ): Outcome<IngredientListDto>
}

interface RecipeSecondaryApi {

    @GET("recipe/review/{recipeId}")
    suspend fun getReviews(
        @Path("recipeId") recipeId: Long
    ): Outcome<List<Review>>

    @GET("recipe/steps/{recipeId}")
    suspend fun getRecipeSteps(
        @Path("recipeId") recipeId: Long
    ): Outcome<List<CookingStep>>

    @POST("leave/review")
    suspend fun leaveReview(
        @Body request: LeaveReviewRequest
    ): Outcome<Any>

    @GET("user/favourite/{userId}")
    suspend fun getFavourites(
        @Path("userId") userId: Long,
    ): Outcome<List<RecipeModel>?>

    @POST("user/favourite")
    suspend fun addFavourite(
        @Body request: UserAndRecipeIdRequest
    ): Outcome<Any>

    @DELETE("user/favourite/{userId}/recipe/{recipeId}")
    suspend fun removeFavourite(
        @Path("userId") userId: Long,
        @Path("recipeId") recipeId: Long
    ): Outcome<Void>
}

class UserIdRequest(val userId: String)
class RecipeIdRequest(val recipeId: String)
class UserAndRecipeIdRequest(
    @SerializedName("user_id") val userId: Long,
    @SerializedName("recipe_id") val recipeId: Long
)