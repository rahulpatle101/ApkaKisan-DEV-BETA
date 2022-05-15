package com.apkakisan.myapplication.helpers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class DefaultItemDecorator(
    private val horizontalSpacing: Int,
    private val verticalSpacing: Int
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = horizontalSpacing
        outRect.left = horizontalSpacing

        if (parent.getChildLayoutPosition(view) == 0)
            outRect.top = verticalSpacing

        outRect.bottom = verticalSpacing
    }
}