package kz.adamant.common.extensions

import android.graphics.Color
import android.graphics.Paint
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.color

fun TextView.markWithRequiredAsterisk() {
    text = text.toString().markWithRequiredAsterisk()
}

fun String.markWithRequiredAsterisk() = buildSpannedString {
    color(Color.RED) { append("* ") }
    append(this@markWithRequiredAsterisk)
}

fun TextView.strikeThrough(shouldStrike: Boolean) {
    paintFlags = if (shouldStrike) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}