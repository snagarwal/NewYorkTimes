package com.satya.newyorktimes.util

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import java.lang.Exception

/**
 * Adds interior dividers to a RecyclerView with a GridLayoutManager.
 */
class NewsItemDividerDecoration
/**
 * Sole constructor. Takes in [Drawable] objects to be used as
 * horizontal and vertical dividers.
 *
 * @param horizontalDivider A divider `Drawable` to be drawn on the
 * rows of the grid of the RecyclerView
 * @param verticalDivider A divider `Drawable` to be drawn on the
 * columns of the grid of the RecyclerView
 * @param numColumns The number of columns in the grid of the RecyclerView
 */
    (
    private val mHorizontalDivider: Drawable,
    private val mVerticalDivider: Drawable,
    private val mNumColumns: Int
) : RecyclerView.ItemDecoration() {

    /**
     * Draws horizontal and/or vertical dividers onto the parent RecyclerView.
     *
     * @param canvas The [Canvas] onto which dividers will be drawn
     * @param parent The RecyclerView onto which dividers are being added
     * @param state The current RecyclerView.State of the RecyclerView
     */
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        try {
            drawHorizontalDividers(canvas, parent)
        } catch (e: Exception) {
        }
        try {
            drawVerticalDividers(canvas, parent)
        } catch (e: Exception) {
        }
    }

    /**
     * Determines the size and location of offsets between items in the parent
     * RecyclerView.
     *
     * @param outRect The [Rect] of offsets to be added around the child view
     * @param view The child view to be decorated with an offset
     * @param parent The RecyclerView onto which dividers are being added
     * @param state The current RecyclerView.State of the RecyclerView
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val childIsInLeftmostColumn = parent.getChildAdapterPosition(view) % mNumColumns == 0
        if (!childIsInLeftmostColumn) {
            outRect.left = mHorizontalDivider.intrinsicWidth
        }

        val childIsInFirstRow = parent.getChildAdapterPosition(view) < mNumColumns
        if (!childIsInFirstRow) {
            outRect.top = mVerticalDivider.intrinsicHeight
        }
    }

    /**
     * Adds horizontal dividers to a RecyclerView with a GridLayoutManager or its
     * subclass.
     *
     * @param canvas The [Canvas] onto which dividers will be drawn
     * @param parent The RecyclerView onto which dividers are being added
     */
    private fun drawHorizontalDividers(canvas: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        val rowCount = childCount / mNumColumns
        val lastRowChildCount = childCount % mNumColumns

        for (i in 1 until mNumColumns) {
            val lastRowChildIndex: Int
            if (i < lastRowChildCount) {
                lastRowChildIndex = i + rowCount * mNumColumns
            } else {
                lastRowChildIndex = i + (rowCount - 1) * mNumColumns
            }

            val firstRowChild = parent.getChildAt(i)
            val lastRowChild = parent.getChildAt(lastRowChildIndex)

            val dividerTop = firstRowChild.top
            val dividerRight = firstRowChild.left
            val dividerLeft = dividerRight - mHorizontalDivider.intrinsicWidth
            val dividerBottom = lastRowChild.bottom

            mHorizontalDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mHorizontalDivider.draw(canvas)
        }
    }

    /**
     * Adds vertical dividers to a RecyclerView with a GridLayoutManager or its
     * subclass.
     *
     * @param canvas The [Canvas] onto which dividers will be drawn
     * @param parent The RecyclerView onto which dividers are being added
     */
    private fun drawVerticalDividers(canvas: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        val rowCount = childCount / mNumColumns
        var rightmostChildIndex: Int
        for (i in 1..rowCount) {
            if (i == rowCount) {
                rightmostChildIndex = parent.childCount - 1
            } else {
                rightmostChildIndex = i * mNumColumns + mNumColumns - 1
            }

            val leftmostChild = parent.getChildAt(i * mNumColumns)
            val rightmostChild = parent.getChildAt(rightmostChildIndex)

            val dividerLeft = leftmostChild.left
            val dividerBottom = leftmostChild.top
            val dividerTop = dividerBottom - mVerticalDivider.intrinsicHeight
            val dividerRight = rightmostChild.right

            mVerticalDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mVerticalDivider.draw(canvas)
        }
    }
}