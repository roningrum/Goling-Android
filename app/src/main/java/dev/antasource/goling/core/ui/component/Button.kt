package dev.antasource.goling.core.ui.component

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.antasource.goling.ui.feature.ui.theme.Typography
import dev.antasource.goling.ui.feature.ui.theme.bluePrimary
import dev.antasource.goling.ui.feature.ui.theme.whiteBackground

@Composable
fun ButtonPrimary(textButton:String, modifier: Modifier, onClick: ()->Unit){
    Button(
        colors = ButtonDefaults.buttonColors(bluePrimary),
        onClick = onClick,
        modifier = modifier,
        content = {
            Text(
                text = textButton,
                color = whiteBackground
            )
        }
    )
}

@Composable
fun OutlineButton(textButton:String, modifier: Modifier, onClick: ()->Unit){
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = ButtonDefaults.outlinedShape,
        content = {
            Text(
                text = textButton,
                color = bluePrimary
            )
        },
        border = BorderStroke(1.dp, bluePrimary),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor =whiteBackground
        )
    )
}

@Composable
fun TextOnlyButton(textButton: String, modifier: Modifier, onClick: () -> Unit){
    TextButton(
        onClick = onClick,
        modifier = modifier,
        shape = ButtonDefaults.textShape,
    ){
        Text(text = textButton, style = Typography.bodySmall)
    }
}

