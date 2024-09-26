package azuraglobal.vn.myapplication

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import azuraglobal.vn.clap3.ui.theme.Color4F46E5


@Composable
fun ColumnScope.CustomRatingBar(
    parentWidth: Int,
    ratingModel: RatingModel,
    onStarClick: (Int) -> Unit
) {

    val starCount = 5
    val stars = mutableListOf<Star>()

    //calculate notice text
    val text = stringResource(id = R.string.best_rating_for_us)
    val paint = android.graphics.Paint().apply {
        textSize = dpToPx( 12.dp)
        isAntiAlias = true
        color = android.graphics.Color.BLACK
    }
    val noticeText = NoticeText(
        text = stringResource(id = R.string.best_rating_for_us),
        size = dpToPx(dp = 12.dp),
        width = paint.measureText(text),
        height = paint.fontMetrics.run { bottom - top }
    )

    val parentW = parentWidth.toFloat()
    val cornerRadius = dpToPx(dp = 100.dp)
    val boxHorizontalPadding = dpToPx(dp = 12.dp)
    val boxVerticalPadding = dpToPx(dp = 6.dp)

    //calculate ratingbar
    val imageBitmap: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.ic_rating_star_normal)
    val starWidth = imageBitmap.width.toFloat()
    val starHeight = imageBitmap.height.toFloat()
    val totalStarWidth = starWidth * 5
    val totalStarSpace = (starWidth / 2) * 4
    val ratingBarWidth = totalStarWidth + totalStarSpace
    val leftSpace: Float = (parentW - ratingBarWidth) / 2

    (1..starCount).forEach { index ->
        val star = when {
            index <= ratingModel.starCount -> Star(imageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_rating_star_fill))
            index == starCount -> Star(imageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_rating_star_normal_5_star))
            else -> Star(imageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_rating_star_normal))
        }
        stars.add(star)
    }

    val parentHeight = pxToDp(px = starHeight) + pxToDp(px = starHeight / 2) + 10.dp

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(parentHeight)
        .pointerInput(Unit) {
            detectTapGestures { tapOffset ->
                Log.i("Canvas", "CustomRatingBar: tab offset = $tapOffset")
                stars.forEachIndexed { index, star ->
                    Log.i("Canvas", "CustomRatingBar: forEachIndexed index = $index , star =$star  ")
                    if (tapOffset.x in star.xRange && tapOffset.y in star.yRange) {
                        Log.i("Canvas", "CustomRatingBar: index = $index , star =$star  ")
                        onStarClick.invoke(index + 1)
                    }
                }
            }
        }
    ) {

        (0 until starCount).forEach { i ->
            val xOffset = leftSpace + (i * (starWidth + starWidth / 2))
            val yOffset = size.height - starHeight

            stars[i].xOffset = xOffset
            stars[i].yOffset = yOffset
            stars[i].xRange = xOffset..xOffset + starWidth
            stars[i].yRange = yOffset..yOffset + starHeight

            drawImage(
                image = stars[i].imageBitmap,
                topLeft = Offset(xOffset, yOffset)
            )
        }


        if (ratingModel.starCount < RatingViewModel.MAX_STAR) {
            val toolBoxRightOffset = Offset(
                x = leftSpace + (4 * (starWidth + starWidth / 2) + (starWidth + starWidth / 4)),
                y = size.height - starHeight
            )

            val toolBoxTrianglePathPartOffset = Offset(
                x = leftSpace + ((4) * (starWidth + starWidth / 2) + starWidth / 2),
                y = size.height - starHeight
            )


            val boxWidth = noticeText.width + boxHorizontalPadding
            val boxHeight = noticeText.height + boxVerticalPadding
            val boxBottom = toolBoxRightOffset.y - starHeight / 3
            val boxTop = boxBottom - boxHeight


            val boxRight = toolBoxRightOffset.x
            val boxLeft = boxRight - boxWidth

            // calculate notice box 's small triangle offset
            val triangleBottomOffset = Offset(
                x = toolBoxTrianglePathPartOffset.x,
                y = toolBoxRightOffset.y - 15f
            )

            val triangleLeftOffset = Offset(
                x = toolBoxTrianglePathPartOffset.x - 20f,
                y = toolBoxRightOffset.y - starHeight / 3
            )

            val triangleRightOffset = Offset(
                x = toolBoxTrianglePathPartOffset.x + 20f,
                y = toolBoxRightOffset.y - starHeight / 3
            )

            val noticeBox = NoticeBox(
                radius = cornerRadius,
                width = boxWidth,
                height = boxHeight,
                leftOffset = boxLeft,
                rightOffset = boxRight,
                topOffset = boxTop,
                bottomOffset = boxBottom
            )

            val triangle = NoticeTriangle(
                bottomOffset = triangleBottomOffset,
                leftOffset = triangleLeftOffset,
                rightOffset = triangleRightOffset
            )

            drawNoticeBox(
                noticeBox = noticeBox,
                noticeTriangle = triangle,
                text = noticeText
            )
        }

    }
}


fun androidx.compose.ui.graphics.drawscope.DrawScope.drawNoticeBox(
    noticeBox: NoticeBox,
    text: NoticeText,
    noticeTriangle: NoticeTriangle
) {


    drawRoundRect(
        color = Color4F46E5,
        topLeft = Offset(noticeBox.leftOffset, noticeBox.topOffset),
        size = androidx.compose.ui.geometry.Size(noticeBox.width, noticeBox.height),
        cornerRadius = CornerRadius(noticeBox.radius, noticeBox.radius)
    )


    val trianglePath = Path().apply {
        moveTo(noticeTriangle.leftOffset.x, noticeTriangle.leftOffset.y)
        lineTo(noticeTriangle.bottomOffset.x, noticeTriangle.bottomOffset.y)
        lineTo(noticeTriangle.rightOffset.x, noticeTriangle.rightOffset.y)
        close()
    }

    drawPath(trianglePath, color = Color4F46E5)

    val textHorizontalSpacing = (noticeBox.width - text.width) / 2

    val textLeftOffset = (noticeBox.leftOffset + textHorizontalSpacing)
    val textBottomOffset = (noticeBox.bottomOffset - text.height / 2)

    drawContext.canvas.nativeCanvas.drawText(
        text.text,
        textLeftOffset,
        textBottomOffset,
        androidx.compose.ui.graphics.Paint().asFrameworkPaint().apply {
            color = android.graphics.Color.WHITE
            textSize = text.size
            isAntiAlias = true
        }
    )
}
