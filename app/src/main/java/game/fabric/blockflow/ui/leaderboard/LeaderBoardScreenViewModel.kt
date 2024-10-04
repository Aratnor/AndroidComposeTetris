package game.fabric.blockflow.ui.leaderboard

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class LeaderBoardScreenViewModel: ViewModel() {
    val uiState: StateFlow<LeaderBoardUiState> get() = _uiState
    private val _uiState: MutableStateFlow<LeaderBoardUiState> = MutableStateFlow(LeaderBoardUiState())
}