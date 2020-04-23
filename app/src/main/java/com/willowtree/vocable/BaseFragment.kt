package com.willowtree.vocable

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<out T : ViewBinding> : Fragment() {

    @Suppress("UNCHECKED_CAST")
    protected val binding: T
        get() = _binding as T

    private var _binding: ViewBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingInflater(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract val bindingInflater: (LayoutInflater) -> ViewBinding

    abstract fun getAllViews(): List<View>

    protected fun buildTextWithIcon(
        vararg strings: String,
        iconCharStart: Int,
        iconCharEnd: Int,
        view: TextView,
        @DrawableRes icon: Int
    ) {
        val sBuilder = SpannableStringBuilder()

        for (item in strings) {
            sBuilder.append(item)
        }
        val imageSpan = ImageSpan(
            requireContext(),
            icon,
            DynamicDrawableSpan.ALIGN_BASELINE
        )
        sBuilder.setSpan(imageSpan, iconCharStart, iconCharEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        view.setText(sBuilder, TextView.BufferType.SPANNABLE)
    }
}