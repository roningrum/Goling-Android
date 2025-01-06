package dev.antasource.goling.core.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.antasource.goling.ui.feature.ui.theme.bluePrimary
import dev.antasource.goling.ui.feature.ui.theme.disableGrey


@Composable
fun TextInputForm(value: TextFieldValue, text: String, onValueChange:(TextFieldValue)-> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text) }, // Label di dalam border
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Padding di sekitar TextField
        shape = RoundedCornerShape(12.dp), // Bentuk border custom
        colors = TextFieldDefaults.colors(
            focusedContainerColor = bluePrimary,
            unfocusedContainerColor = disableGrey,
            errorContainerColor = androidx.compose.ui.graphics.Color.Red
            // Background custom
        )
    )
}