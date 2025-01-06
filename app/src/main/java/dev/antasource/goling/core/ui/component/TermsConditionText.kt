package dev.antasource.goling.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import dev.antasource.goling.ui.feature.ui.theme.Typography
import dev.antasource.goling.ui.feature.ui.theme.bluePrimary
import dev.antasource.goling.ui.feature.ui.theme.darkBlue

@Composable
fun TermsConditionText(
    onTermsClick:()-> Unit,
    onPrivacyClick:()-> Unit,
    modifier: Modifier
) {
    val annotedText = buildAnnotatedString {
        append("Dengan mendaftar saya telah menyetujui\n")
        pushStringAnnotation(tag = "Syarat", annotation = "syarat" )
        withStyle(style = SpanStyle(color = bluePrimary, textDecoration = TextDecoration.Underline)){
            append("Syarat & Ketentuan")
        }
        pop()
        append(" dan ")
        pushStringAnnotation(tag = "Kebijakan", annotation = "kebijakan")
        withStyle(style = SpanStyle(color = bluePrimary, textDecoration = TextDecoration.Underline)){
            append("Kebijakan Privasi")
        }
        pop()
        append(" GoLing")
    }

    Text(
        text = annotedText,
        style = Typography.bodySmall,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clickable(onClick = {
                annotedText.getStringAnnotations(tag = "Syarat", start = 0, end = annotedText.length).firstOrNull().let { onTermsClick }
            }
        ).clickable(onClick = {
                annotedText.getStringAnnotations(tag = "Kebijakan", start = 0, end = annotedText.length).firstOrNull().let { onPrivacyClick}
            })
    )
}