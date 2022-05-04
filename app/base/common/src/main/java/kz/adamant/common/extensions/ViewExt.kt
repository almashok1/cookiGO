package kz.adamant.common.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kz.adamant.common.CooldownAction
import kz.adamant.common.R
import kz.adamant.common.databinding.LayoutDialogAlertTextBinding

internal const val MIN_CLICK_REFRESH_INTERVAL = 300L

/* View */

fun View.onSafeClick(refreshInterval: Long = MIN_CLICK_REFRESH_INTERVAL, action: () -> Unit){
    val cooldown = CooldownAction(refreshInterval, action)
    setOnClickListener {
        cooldown.tryAction()
    }
}

/* ViewBinding */

inline var ViewBinding.isVisible: Boolean
    get() = root.isVisible
    set(value) {
        root.isVisible = value
    }

fun ViewBinding.beginChangeBoundsTransition() {
    if (root is ViewGroup) {
        (root as ViewGroup).beginChangeBoundsTransition()
    }
}

fun ViewGroup.beginChangeBoundsTransition(){
    TransitionManager.endTransitions(this)
    TransitionManager.beginDelayedTransition(this, ChangeBounds())
}


fun Context.showInfoDialog(message: String) {
    val view = LayoutDialogAlertTextBinding.inflate(LayoutInflater.from(this))
    view.root.text = message
    MaterialAlertDialogBuilder(this)
        .setCustomTitle(view.root)
        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        .create()
        .show()
}

fun Fragment.showInfoDialog(message: String) = requireContext().showInfoDialog(message)

fun MaterialButton.setShowProgress(
    showProgress: Boolean = false,
    textSource: String?,
    disableWhenLoading: Boolean = true,
    @DrawableRes iconSource: Int? = null,
    @ColorRes progressColor: Int = R.color.black
) {
    iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
    icon = if (showProgress) {
        CircularProgressDrawable(context).apply {
            setStyle(CircularProgressDrawable.DEFAULT)
            setColorSchemeColors(context.getColorCompat(progressColor))
            start()
        }
    } else iconSource?.let { context.getDrawableCompat(it) }
    text = if (showProgress) "" else textSource
    if (disableWhenLoading) {
        isEnabled = !showProgress
    }
    if (icon != null) {
        icon.callback = object : Drawable.Callback {
            override fun unscheduleDrawable(who: Drawable, what: Runnable) {}
            override fun invalidateDrawable(who: Drawable) { this@setShowProgress.invalidate() }
            override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {}
        }
    }
}

@DrawableRes
fun getIngredientDrawableFromPosition(position: Int, itemCount: Int): Int = when (position) {
    0 -> R.drawable.bg_item_grey_top_round_5
    itemCount - 1 -> R.drawable.bg_item_grey_bottom_round_5
    else -> R.drawable.bg_item_grey
}