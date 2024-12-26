package dev.antasource.goling.util

import java.text.DecimalFormat

object Util {
    fun formatCurrency(amount: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount)
    }
}