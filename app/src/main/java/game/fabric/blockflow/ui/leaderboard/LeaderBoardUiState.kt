package game.fabric.blockflow.ui.leaderboard

import game.fabric.blockflow.gamelogic.EMPTY_STRING

data class LeaderBoardUiState(
    val list: List<LeaderBoardListItemUiModel> = emptyList()
)

data class LeaderBoardListItemUiModel(
    val name: String = EMPTY_STRING,
    val position: String = EMPTY_STRING,
    val score: String = EMPTY_STRING
)