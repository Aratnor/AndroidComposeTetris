package game.fabric.blockflow.ui.leaderboard

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import game.fabric.blockflow.ui.theme.BACKGROUND
import game.fabric.blockflow.ui.theme.FIRST_PLACE_TEXT_END
import game.fabric.blockflow.ui.theme.FIRST_PLACE_TEXT_START
import game.fabric.blockflow.ui.theme.NORMAL_PLACE_TEXT
import game.fabric.blockflow.ui.theme.SECOND_PLACE_TEXT_END
import game.fabric.blockflow.ui.theme.SECOND_PLACE_TEXT_START
import game.fabric.blockflow.ui.theme.THIRD_PLACE_TEXT_END
import game.fabric.blockflow.ui.theme.THIRD_PLACE_TEXT_START
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LeaderBoardScreenViewModel @Inject constructor() : ViewModel() {
    val uiState: StateFlow<LeaderBoardUiState> get() = _uiState
    private val _uiState: MutableStateFlow<LeaderBoardUiState> = MutableStateFlow(
        LeaderBoardUiState(
            firstPlaceList = listOf(LeaderBoardListItemUiModel(
                name = "Player1", score = "500", textColorStart = FIRST_PLACE_TEXT_START,
                textColorEnd = FIRST_PLACE_TEXT_END,
            ),
                LeaderBoardListItemUiModel(
                    name = "Player2", score = "400", textColorStart = SECOND_PLACE_TEXT_START,
                    textColorEnd = SECOND_PLACE_TEXT_END, backgroundColor = BACKGROUND
                ),
                LeaderBoardListItemUiModel(
                    name = "Player3", score = "300", textColorStart = THIRD_PLACE_TEXT_START,
                    textColorEnd = THIRD_PLACE_TEXT_END, backgroundColor = BACKGROUND
                )),
            list = listOf(
                LeaderBoardListItemUiModel(
                    name = "Player4", score = "200", textColorStart = NORMAL_PLACE_TEXT,
                    textColorEnd = NORMAL_PLACE_TEXT, backgroundColor = BACKGROUND
                ),
                LeaderBoardListItemUiModel(
                    name = "Player4", score = "200", textColorStart = NORMAL_PLACE_TEXT,
                    textColorEnd = NORMAL_PLACE_TEXT, backgroundColor = BACKGROUND
                ),
                LeaderBoardListItemUiModel(
                    name = "Player4", score = "200", textColorStart = NORMAL_PLACE_TEXT,
                    textColorEnd = NORMAL_PLACE_TEXT, backgroundColor = BACKGROUND
                ),
                LeaderBoardListItemUiModel(
                    name = "Player4", score = "200", textColorStart = NORMAL_PLACE_TEXT,
                    textColorEnd = NORMAL_PLACE_TEXT, backgroundColor = BACKGROUND
                ),LeaderBoardListItemUiModel(
                    name = "Player4", score = "200", textColorStart = NORMAL_PLACE_TEXT,
                    textColorEnd = NORMAL_PLACE_TEXT, backgroundColor = BACKGROUND
                ),
                LeaderBoardListItemUiModel(
                    name = "Player4", score = "200", textColorStart = NORMAL_PLACE_TEXT,
                    textColorEnd = NORMAL_PLACE_TEXT, backgroundColor = BACKGROUND
                ),
                LeaderBoardListItemUiModel(
                    name = "Player4", score = "200", textColorStart = NORMAL_PLACE_TEXT,
                    textColorEnd = NORMAL_PLACE_TEXT, backgroundColor = BACKGROUND
                ),
                LeaderBoardListItemUiModel(
                    name = "Player4", score = "200", textColorStart = NORMAL_PLACE_TEXT,
                    textColorEnd = NORMAL_PLACE_TEXT, backgroundColor = BACKGROUND
                ),LeaderBoardListItemUiModel(
                    name = "Player4", score = "200", textColorStart = NORMAL_PLACE_TEXT,
                    textColorEnd = NORMAL_PLACE_TEXT, backgroundColor = BACKGROUND
                ),
                LeaderBoardListItemUiModel(
                    name = "Player4", score = "200", textColorStart = NORMAL_PLACE_TEXT,
                    textColorEnd = NORMAL_PLACE_TEXT, backgroundColor = BACKGROUND
                ),
                LeaderBoardListItemUiModel(
                    name = "Player4", score = "200", textColorStart = NORMAL_PLACE_TEXT,
                    textColorEnd = NORMAL_PLACE_TEXT, backgroundColor = BACKGROUND
                ),
                LeaderBoardListItemUiModel(
                    name = "Player4", score = "200", textColorStart = NORMAL_PLACE_TEXT,
                    textColorEnd = NORMAL_PLACE_TEXT, backgroundColor = BACKGROUND
                )
            )
        )
    )
}