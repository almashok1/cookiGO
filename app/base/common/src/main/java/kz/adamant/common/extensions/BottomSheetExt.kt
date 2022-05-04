package kz.adamant.common.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kz.adamant.common.R

fun BottomSheetDialog.setDimBehind() {
    val dialog = this
    dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
}

fun BottomSheetDialog.setFullScreen(context: Context) {
    val behavior = getBottomSheetBehavior()
    val targetHeight = context.screenDimension().y
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
    behavior.peekHeight = targetHeight

    getBottomSheetView().run {
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams = layoutParams
    }
}

fun BottomSheetDialog.setUpRoundedCorners() {
    setOnShowListener {
        val view = getBottomSheetView()
        view.backgroundTintMode = PorterDuff.Mode.CLEAR
        view.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        view.setBackgroundColor(Color.TRANSPARENT)
    }
}

fun BottomSheetDialog.disableCollapsing() {
    val behavior = getBottomSheetBehavior()
    behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                behavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    })
}

private fun BottomSheetDialog.getBottomSheetView() : FrameLayout {
    return findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
}

private fun BottomSheetDialog.getBottomSheetBehavior() : BottomSheetBehavior<View> {
    val bottomSheet = getBottomSheetView()
    return BottomSheetBehavior.from(bottomSheet)
}
