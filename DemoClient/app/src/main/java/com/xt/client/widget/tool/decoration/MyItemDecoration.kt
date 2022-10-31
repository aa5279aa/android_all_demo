package com.xt.client.widget.tool.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

public class MyItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = 10
        outRect.bottom = 10
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.left = 0
        }
    }
}