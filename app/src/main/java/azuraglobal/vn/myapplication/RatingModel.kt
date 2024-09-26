package azuraglobal.vn.myapplication

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap

data class RatingModel(
    val title: Int,
    val description: Int,
    val emoji: Int,
    val starCount: Int,
    val buttonText: Int = R.string.rate_us,
    val buttonEnable: Boolean = true,
)

data class NoticeBox(
    val radius: Float,
    val width: Float,
    val height: Float,
    val leftOffset: Float,
    val rightOffset: Float,
    val topOffset: Float,
    val bottomOffset: Float
)

data class NoticeText(
    val text: String,
    val sizeInDp: Float,
    val width: Float,
    val height: Float,
    var startOffset: Float = 0f,
    var topOffset: Float = 0f
)

data class Star(
    var xOffset: Float = 0f,
    var yOffset: Float = 0f,
    var imageResource: ImageBitmap ? = null,
    var xRange: ClosedFloatingPointRange<Float> = 0f..0f,
    var yRange: ClosedFloatingPointRange<Float> = 0f..0f
)


data class NoticeTriangle(
    val bottomOffset: Offset,
    val leftOffset: Offset,
    val rightOffset: Offset
)