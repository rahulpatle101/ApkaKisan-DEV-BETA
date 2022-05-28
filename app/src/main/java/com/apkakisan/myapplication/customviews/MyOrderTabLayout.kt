package com.apkakisan.myapplication.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.apkakisan.myapplication.R
import com.google.android.material.tabs.TabLayout

class MyOrderTabLayout : TabLayout {

    private val tabTitles = arrayOf(
        R.string.active,
        R.string.history
    )

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        addTab(newTab())
        addTab(newTab())

        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            tab?.customView = getTabView(i)

//            val tabView = (getChildAt(0) as ViewGroup).getChildAt(i)
//            val p = tabView.layoutParams as MarginLayoutParams
//
//            if (i == tabCount - 1) {
//                p.marginStart = 40
//                p.marginEnd = 40
//                p.bottomMargin = 0
//                p.topMargin = 0
//            } else {
//                p.marginStart = 40
//                p.marginEnd = 0
//                p.bottomMargin = 0
//                p.topMargin = 0
//            }
//
//            tabView.requestLayout()
        }
    }

    private fun getTabView(position: Int): View {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_tab_my_orders, null)

        val text = v.findViewById<AppCompatTextView>(R.id.tvTabName)
        text.setText(tabTitles[position])
//        if (LanguageUtil.isArabic())
//            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen._10sp))
//        if (LanguageUtil.isEnglish())
//            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen._16sp))

        return v
    }
}