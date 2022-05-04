package kz.adamant.common.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import kz.adamant.common.databinding.LayoutFullProgressBinding

class FullProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding =
        LayoutFullProgressBinding.inflate(LayoutInflater.from(context), this, true)

    @SuppressLint("ClickableViewAccessibility")
    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnTouchListener { _, _ -> true }
    }

    val progress get() = binding.progressBar
}
