package azuraglobal.vn.myapplication

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import azuraglobal.vn.clap3.ui.theme.Color4F46E5

private val TAG = "CustomRatingBar"

@Composable
fun ColumnScope.CustomRatingBar(
    parentWidth: Int,
    ratingModel: RatingModel,
    onStarClick: (Int) -> Unit
) {
    val starCount = 5
    val stars = remember {
        mutableListOf<Star>(
            Star(),
            Star(),
            Star(),
            Star(),
            Star(),
        )
    }

    //calculate notice text
    val text = stringResource(id = R.string.best_rating_for_us)
    val paint = android.graphics.Paint().apply {
        textSize = dpToPx(12.dp)
        isAntiAlias = true
        color = android.graphics.Color.BLACK
    }
    val noticeText =  NoticeText(
        text = stringResource(id = R.string.best_rating_for_us),
        sizeInDp = dpToPx(dp = 12.dp),
        width = paint.measureText(text),
        height = paint.fontMetrics.run { bottom - top }
    )


    //  notice box properties
    val cornerRadius = dpToPx(dp = 100.dp)
    val boxHorizontalPadding = dpToPx(dp = 12.dp)
    val boxVerticalPadding = dpToPx(dp = 6.dp)

    //calculate star size
    val imageBitmap: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.ic_rating_star_normal)
    val starWidth = imageBitmap.width.toFloat()
    val starHeight = imageBitmap.height.toFloat()

    //space between each star
    val totalStarSpace = (starWidth / 2) * 4

    //calculate rating bar width
    val ratingBarWidth = (starWidth * 5) + totalStarSpace

    // rating bar padding left
    val leftSpace: Float = (parentWidth.toFloat() - ratingBarWidth) / 2

    // calculate parent height which allow enough space to display the child views
    val parentHeight = pxToDp(px = starHeight * 2)

    (stars).forEachIndexed { index, star ->
        when {
            index + 1 <= ratingModel.starCount -> star.imageResource =
                ImageBitmap.imageResource(id = R.drawable.ic_rating_star_fill)

            index + 1 == starCount -> star.imageResource =
                ImageBitmap.imageResource(id = R.drawable.ic_rating_star_normal_5_star)

            else -> star.imageResource =
                ImageBitmap.imageResource(id = R.drawable.ic_rating_star_normal)
        }
    }

    
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(parentHeight)
        .pointerInput(Unit) {
            detectTapGestures { tapOffset ->
                val index =
                    stars.indexOfFirst { tapOffset.x in it.xRange && tapOffset.y in it.yRange }

                onStarClick.invoke(index + 1)
            }
        }
    ) {

        stars.forEachIndexed { index, star ->
            val xOffset = leftSpace + (index * (starWidth + starWidth / 2))
            val yOffset = size.height - starHeight

            star.xOffset = xOffset
            star.yOffset = yOffset
            star.xRange = xOffset..xOffset + starWidth
            star.yRange = yOffset..yOffset + starHeight

            star.imageResource?.let {
                drawImage(
                    image = it,
                    topLeft = Offset(xOffset, yOffset)
                )
            }
        }


        if (ratingModel.starCount < RatingViewModel.MAX_STAR) {

            val noticeBoxRightOffset = Offset(
                x = leftSpace + (4 * (starWidth + starWidth / 2) + (starWidth + starWidth / 3)),
                y = size.height - starHeight
            )

            //small triangle below notice box
            val noticeBoxTriangleOffset = Offset(
                x = leftSpace + ((4) * (starWidth + starWidth / 2) + starWidth / 2),
                y = size.height - starHeight
            )

            // calculate notice box properties
            val boxWidth = noticeText.width + boxHorizontalPadding
            val boxHeight = noticeText.height + boxVerticalPadding
            val boxBottom = noticeBoxRightOffset.y - starHeight / 3
            val boxTop = boxBottom - boxHeight
            val boxRight = noticeBoxRightOffset.x
            val boxLeft = boxRight - boxWidth
            val noticeBox = NoticeBox(
                radius = cornerRadius,
                width = boxWidth,
                height = boxHeight,
                leftOffset = boxLeft,
                rightOffset = boxRight,
                topOffset = boxTop,
                bottomOffset = boxBottom
            )

            noticeText.startOffset =
                noticeBoxRightOffset.x - noticeText.width - boxHorizontalPadding / 2
            noticeText.topOffset = boxBottom - boxVerticalPadding


            // calculate notice box 's small triangle offset

            val triangleBottomOffset = Offset(
                x = noticeBoxTriangleOffset.x,
                y = noticeBoxRightOffset.y - 15f
            )

            val triangleLeftOffset = Offset(
                x = noticeBoxTriangleOffset.x - 20f,
                y = noticeBoxRightOffset.y - starHeight / 3
            )

            val triangleRightOffset = Offset(
                x = noticeBoxTriangleOffset.x + 20f,
                y = noticeBoxRightOffset.y - starHeight / 3
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

    drawContext.canvas.nativeCanvas.drawText(
        text.text,
        text.startOffset,
        text.topOffset,
        androidx.compose.ui.graphics.Paint().asFrameworkPaint().apply {
            color = android.graphics.Color.WHITE
            textSize = text.sizeInDp
            isAntiAlias = true
        }
    )
}


@Composable
@Preview(locale = "es")
fun PreviewCustomRatingBar() {
    Column {
        CustomRatingBar(
            parentWidth = 1080,
            ratingModel = RatingModel(
                title = R.string.rating_0_star_title,
                description = R.string.rating_0_star_description,
                emoji = R.drawable.ic_emoji_rating_0_star,
                starCount = 0
            ),
            onStarClick = {}
        )
    }
}