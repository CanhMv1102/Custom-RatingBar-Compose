package azuraglobal.vn.myapplication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import azuraglobal.vn.clap3.ui.theme.Color4F46E5
import azuraglobal.vn.clap3.ui.theme.ColorD9D9D9
import azuraglobal.vn.clap3.ui.theme.ColorFFFFFF

private val TAG = "RatingViewModel"

@Composable
fun ShowRatingDialog(
    canShow: Boolean,
    onDismissRequest: () -> Unit,
    onDisableRate: () -> Unit
) {
    val ratingViewModel: RatingViewModel = remember {
        RatingViewModel()
    }

    val currentStar = ratingViewModel.currentStar.collectAsStateWithLifecycle().value

    fun onClickRate() {
        if (currentStar == RatingViewModel.RatingStarState.COMPLETED) {
            ratingViewModel.resetDialog()
            onDismissRequest()
        }else{
            ratingViewModel.onRatingCompleted()
        }
    }

    val ratingModel = currentStar.toRatingModel()
    if (canShow) {
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = true
            ),
            onDismissRequest = {
                ratingViewModel.resetDialog()
                onDismissRequest()
            }
        ) {

            var parentWidth by remember { mutableIntStateOf(0) }

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent)
                    .padding(horizontal = 15.dp)
            ) {
                val (dialog, emoji) = createRefs()


                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(color = ColorFFFFFF, shape = RoundedCornerShape(24.dp))
                        .padding(15.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .onGloballyPositioned { layoutCoordinates ->
                            parentWidth = layoutCoordinates.size.width
                        }
                        .constrainAs(dialog) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }) {

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = stringResource(id = ratingModel.title),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(id = ratingModel.description),
                        minLines = 2,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 5.dp, start = 15.dp, end = 15.dp)
                    )

                   if (ratingModel.starCount != RatingViewModel.RatingStarState.COMPLETED.numberOfStar) {
                       Spacer(modifier = Modifier.height(20.dp))
                       CustomRatingBar(
                           ratingModel = ratingModel,
                           parentWidth = parentWidth,
                           onStarClick = {
                               ratingViewModel.onStarChange(it)
                           }
                       )
                   }

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        enabled = ratingModel.buttonEnable,
                        colors = ButtonColors(
                            containerColor = Color4F46E5,
                            contentColor = ColorFFFFFF,
                            disabledContentColor = ColorD9D9D9,
                            disabledContainerColor = ColorD9D9D9
                        ),

                        onClick = {
                            onClickRate()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = ratingModel.buttonText),
                            modifier = Modifier.padding(vertical = 9.dp)
                        )
                    }
                }

                Image(
                    painter = painterResource(id = ratingModel.emoji),
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .constrainAs(emoji) {
                            top.linkTo(dialog.top)
                            bottom.linkTo(dialog.top)
                            start.linkTo(dialog.start)
                            end.linkTo(dialog.end)
                        }
                )

            }
        }
    }
}


@Composable
@Preview(
    device = "spec:width=1080px,height=2408px,dpi=440,isRound=true", name = "RealDevice",
    backgroundColor = 0xFFFFFFFF, wallpaper = Wallpapers.NONE
)
fun PreviewStatelessRatingDialog() {
    ShowRatingDialog(
        canShow = true,
        onDismissRequest = {},
        onDisableRate = {}
    )
}