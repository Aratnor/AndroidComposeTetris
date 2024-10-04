package game.fabric.blockflow.ui.leaderboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import game.fabric.blockflow.ui.gameoverscreen.ScoreBoardItemCard
import game.fabric.blockflow.ui.theme.BACKGROUND
import game.fabric.blockflow.ui.theme.FIRST_PLACE_TEXT_END
import game.fabric.blockflow.ui.theme.FIRST_PLACE_TEXT_START
import game.fabric.blockflow.ui.theme.NORMAL_PLACE_TEXT
import game.fabric.blockflow.ui.theme.SECOND_PLACE_TEXT_END
import game.fabric.blockflow.ui.theme.SECOND_PLACE_TEXT_START
import game.fabric.blockflow.ui.theme.THIRD_PLACE_TEXT_END
import game.fabric.blockflow.ui.theme.THIRD_PLACE_TEXT_START


@Composable
fun LeaderBoardScreenRoute(
    viewModel: LeaderBoardScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    LeaderBoardScreen(uiState.value)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LeaderBoardScreen(uiState: LeaderBoardUiState) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .background(BACKGROUND)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        stickyHeader {
            Column(Modifier.background(BACKGROUND)) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BACKGROUND)
                        .padding(vertical = 16.dp),
                    text = "ScoreBoard",
                    color = Color.White,
                    style = MaterialTheme.typography.h3,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(24.dp))
                uiState.firstPlaceList.forEach {
                    ScoreBoardItemCard(item = it)
                }
            }
        }
        items(uiState.list) {
            ScoreBoardItemCard(item = it)
        }
    }
}

@Preview
@Composable
fun PreviewLeaderBoardScreen() {
    LeaderBoardScreen(
        LeaderBoardUiState(
            listOf(
                LeaderBoardListItemUiModel(
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