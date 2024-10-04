package game.fabric.blockflow.ui.leaderboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun LeaderBoardScreenRoute(
    viewModel: LeaderBoardScreenViewModel = hiltViewModel()
) {
    LeaderBoardScreen()
}
@Composable
fun LeaderBoardScreen() {
}

@Preview
@Composable
fun PreviewLeaderBoardScreen() {
    LeaderBoardScreen()
}