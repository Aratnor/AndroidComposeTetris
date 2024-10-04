package game.fabric.blockflow.design

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import game.fabric.blockflow.ext.coloredShadow
import game.fabric.blockflow.ui.theme.BUTTON_BACKGROUND

@Composable
fun PrimaryButton(
    text: String,
    style: TextStyle = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.SemiBold),
    fillMaxWidth: Boolean = false,
    buttonPadding: PaddingValues = PaddingValues(
        horizontal = 20.dp
    ),
    textPadding: PaddingValues = PaddingValues(
        top = 16.dp,
        bottom = 16.dp,
        start = 36.dp,
        end = 36.dp
    ),
    onClick: () -> Unit
) {
    var modifier = Modifier.coloredShadow(Color.Black)
    if (fillMaxWidth) {
        modifier = modifier
            .fillMaxWidth()
            .padding(buttonPadding)
    }
    OutlinedButton(
        modifier = modifier,
        shape = RoundedCornerShape(12),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = BUTTON_BACKGROUND,
            contentColor = Color.White
        ),
        onClick = onClick
    ) {
        val textModifier = Modifier.padding(
            textPadding
        )
        Text(
            modifier = textModifier,
            text = text,
            style = style
        )
    }
}

@Preview
@Composable
fun PreviewPrimaryButton() {
    PrimaryButton(
        text = "Start Game",
        fillMaxWidth = true
    ) {}
}