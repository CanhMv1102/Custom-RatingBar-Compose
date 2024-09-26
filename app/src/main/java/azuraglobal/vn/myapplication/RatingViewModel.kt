package azuraglobal.vn.myapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RatingViewModel(
) : ViewModel() {
    private val TAG = "RatingViewModel"
    companion object{
        val MAX_STAR : Int = 5
    }
    private val _currentStar = MutableStateFlow(RatingStarState.START)
    val currentStar = _currentStar.asStateFlow()


    fun onStarChange(starIndex: Int) {
        val starState = RatingStarState.fromInt(starIndex)
        Log.i(TAG, "onStarChange: starIndex = $starIndex, startState = $starState")
        _currentStar.value = starState
    }

    fun resetDialog ( ) {
        _currentStar.value = RatingStarState.START
    }

    fun onRatingCompleted ( ) {
        _currentStar.value = RatingStarState.COMPLETED
    }

    enum class RatingStarState(val numberOfStar : Int){
        COMPLETED (-1),
        START (0),
        ONE_STAR (1),
        TWO_STAR (2),
        THREE_STAR (3),
        FOUR_STAR (4),
        FIVE_STAR (5);
        companion object{
            fun fromInt(value: Int) = entries.firstOrNull { it.numberOfStar == value } ?: START
        }
    }

}