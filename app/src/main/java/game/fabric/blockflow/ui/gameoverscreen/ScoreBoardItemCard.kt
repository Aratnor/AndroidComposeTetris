package game.fabric.blockflow.ui.gameoverscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import game.fabric.blockflow.design.effect.ShimmerEffect
import game.fabric.blockflow.ui.leaderboard.LeaderBoardListItemUiModel
import game.fabric.blockflow.ui.theme.BACKGROUND
import game.fabric.blockflow.ui.theme.FIRST_PLACE_TEXT_END
import game.fabric.blockflow.ui.theme.FIRST_PLACE_TEXT_START
import game.fabric.blockflow.ui.theme.SCOREBOARD_SCREEN_BACKGROUND

@Composable
fun ScoreBoardItemCard(
    item: LeaderBoardListItemUiModel,
    cardPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    contentPadding: PaddingValues = PaddingValues(
        start = 8.dp,
        end = 8.dp,
        top = 16.dp,
        bottom = 16.dp
    )
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(cardPadding),
        backgroundColor = item.backgroundColor,
        border = BorderStroke(width = 3.dp,item.backgroundColor),
        elevation = 0.dp
    ) {
        Row {
            Text(
                modifier = Modifier.padding(contentPadding),
                text = item.name,
                style = MaterialTheme.typography.h4.copy(
                    brush = ShimmerEffect(
                        item.textColorStart,
                        item.textColorEnd
                    ),
                    fontWeight = FontWeight.Bold
                ),
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                modifier = Modifier.padding(contentPadding),
                text = item.score,
                style = MaterialTheme.typography.h4.copy(
                    brush = ShimmerEffect(
                        item.textColorStart,
                        item.textColorEnd
                    ),
                    fontWeight = FontWeight.Bold
                ),
            )
        }
    }
}

@Preview
@Composable
fun PreviewGameOverText() {
    Box(
        modifier = Modifier
            .background(SCOREBOARD_SCREEN_BACKGROUND)
            .fillMaxSize()
    ) {
        ScoreBoardItemCard(
            item = LeaderBoardListItemUiModel(
                name = "1. Player1",
                score = "4000",
                textColorStart = FIRST_PLACE_TEXT_START,
                textColorEnd = FIRST_PLACE_TEXT_END,
                backgroundColor = BACKGROUND
            )
        )
    }
}