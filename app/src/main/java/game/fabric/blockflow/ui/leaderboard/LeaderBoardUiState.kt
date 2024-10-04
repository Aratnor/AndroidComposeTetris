package game.fabric.blockflow.ui.leaderboard

import androidx.compose.ui.graphics.Color
import game.fabric.blockflow.gamelogic.EMPTY_STRING

data class LeaderBoardUiState(
    val firstPlaceList: List<LeaderBoardListItemUiModel> = emptyList(),
    val list: List<LeaderBoardListItemUiModel> = emptyList()
)

data class LeaderBoardListItemUiModel(
    val name: String = EMPTY_STRING,
    val position: String = EMPTY_STRING,
    val score: String = EMPTY_STRING,
    val textColorStart: Color = Color.Unspecified,
    val textColorEnd: Color = Color.Unspecified,
    val backgroundColor: Color = Color.Unspecified
)