package com.zj.easyfloat

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.imuxuan.floatingview.FloatingMagnetView

class EasyFloatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FloatingMagnetView(context, attrs, defStyleAttr) {

    constructor(context: Context, view: View) : this(context) {
        addView(view)
    }
}