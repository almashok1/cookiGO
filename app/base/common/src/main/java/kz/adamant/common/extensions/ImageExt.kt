package kz.adamant.common.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kz.adamant.common.R

const val DRIVE_BASE_URL = "https://docs.google.com/uc?id="

fun ImageView.loadWithDriveUrl(id: String?, @DrawableRes placeholder: Int = R.drawable.placeholder_image) {
    loadUrl(DRIVE_BASE_URL+id, placeholder)
}

fun ImageView.loadUrl(url: String?, @DrawableRes placeholder: Int = R.drawable.placeholder_image) {
    if (url.isNullOrEmpty()) {
        loadDrawable(placeholder)
    } else {
        Glide.with(this)
            .load(url)
            .placeholder(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }
}

fun ImageView.loadUrl(url: String?, placeholder: Drawable) {
    if (url.isNullOrEmpty()) {
        loadDrawable(placeholder)
    } else {
        Glide.with(this)
            .load(url)
            .placeholder(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }
}

fun ImageView.loadDrawable(@DrawableRes resId: Int) {
    Glide.with(this)
        .load(resId)
        .into(this)
}

fun ImageView.loadDrawable(drawable: Drawable) {
    Glide.with(this)
        .load(drawable)
        .into(this)
}