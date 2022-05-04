package kz.adamant.common.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.IntRange
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import kz.adamant.common.R
import kz.adamant.common.databinding.ViewRateStarBigBinding
import kz.adamant.common.extensions.getColorCompat

class RateStarViewBig @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayoutCompat(context, attrs) {

    private val binding = ViewRateStarBigBinding.inflate(LayoutInflater.from(context), this, true)

    var inRate = 5
        private set

    fun setRate(@IntRange(from = 0, to = 5) rate: Int) {
        inRate = rate
        for (i in 1..5) {
            (binding.root.getChildAt(i - 1) as AppCompatImageView).imageTintList =
                ColorStateList.valueOf(
                    context.getColorCompat(if (i <= rate) R.color.main else R.color.item_disabled)
                )
        }
    }

    fun setClicks() {
        for (i in 0 until 5) {
            binding.root.getChildAt(i).setOnClickListener {
                setRate(i + 1)
            }
        }
    }

}