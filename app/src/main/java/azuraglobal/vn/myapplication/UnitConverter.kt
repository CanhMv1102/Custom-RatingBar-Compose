package azuraglobal.vn.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun dpToPx(dp: Dp): Float {
    val density = LocalDensity.current.density
    return dp.value * density
}

@Composable
fun pxToDp(px: Float): Dp {
    val density = LocalDensity.current.density // Get the screen density
    return Dp(px / density) // Convert px to dp
}

fun RatingViewModel.RatingStarState.toRatingModel(): RatingModel {
    return when (this) {

        RatingViewModel.RatingStarState.COMPLETED -> RatingModel(
            title = R.string.feedback_title,
            description = R.string.feedback_description,
            emoji = R.drawable.ic_emoji_rating_5_star,
            starCount = -1,
            buttonText = R.string.ok,
            buttonEnable = true
        )

        RatingViewModel.RatingStarState.START -> RatingModel(
            title = R.string.rating_0_star_title,
            description = R.string.rating_0_star_description,
            emoji = R.drawable.ic_emoji_rating_0_star,
            starCount = 0,
            buttonText = R.string.rate_us,
            buttonEnable = false
        )

        RatingViewModel.RatingStarState.ONE_STAR -> RatingModel(
            title = R.string.rating_1_star_title,
            description = R.string.rating_1_star_description,
            emoji = R.drawable.ic_emoji_rating_1_star,
            starCount = 1,
            buttonText = R.string.rate_us,
            buttonEnable = true
        )

        RatingViewModel.RatingStarState.TWO_STAR -> RatingModel(
            title = R.string.rating_2_star_title,
            description = R.string.rating_2_star_description,
            emoji = R.drawable.ic_emoji_rating_2_star,
            starCount = 2,
            buttonText = R.string.rate_us,
            buttonEnable = true
        )

        RatingViewModel.RatingStarState.THREE_STAR -> RatingModel(
            title = R.string.rating_3_star_title,
            description = R.string.rating_3_star_description,
            emoji = R.drawable.ic_emoji_rating_3_star,
            starCount = 3,
            buttonText = R.string.rate_us,
            buttonEnable = true
        )

        RatingViewModel.RatingStarState.FOUR_STAR -> RatingModel(
            title = R.string.rating_4_star_title,
            description = R.string.rating_4_star_description,
            emoji = R.drawable.ic_emoji_rating_4_star,
            starCount = 4,
            buttonText = R.string.rate_us,
            buttonEnable = true
        )

        RatingViewModel.RatingStarState.FIVE_STAR -> RatingModel(
            title = R.string.rating_5_star_title,
            description = R.string.rating_5_star_description,
            emoji = R.drawable.ic_emoji_rating_5_star,
            starCount = 5,
            buttonText = R.string.rate_us,
            buttonEnable = true
        )

    }
}
