package kz.adamant.common.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import kz.adamant.common.R
import kz.adamant.common.databinding.ViewEmptyStateBinding
import kz.adamant.common.extensions.onSafeClick

class EmptyStateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayoutCompat(context, attrs) {

    val binding = ViewEmptyStateBinding.inflate(LayoutInflater.from(context), this, true)

    fun setOnActionBtnClick(action: () -> Unit) {
        binding.btnAction.onSafeClick { action() }
    }

    init {
        val typedAttrs = context.obtainStyledAttributes(attrs, R.styleable.EmptyStateView)
        binding.btnAction.text = typedAttrs.getString(R.styleable.EmptyStateView_btnTitle)
        binding.tvTitle.text = typedAttrs.getString(R.styleable.EmptyStateView_emptyTitle)
        binding.tvDescription.text = typedAttrs.getString(R.styleable.EmptyStateView_emptyDescription)
        binding.ivImage.setImageResource(typedAttrs.getResourceId(R.styleable.EmptyStateView_image, R.drawable.ic_pasta_amigo))
        binding.btnAction.isVisible = typedAttrs.getBoolean(R.styleable.EmptyStateView_isBtnVisible, true)
        typedAttrs.recycle()
    }

}