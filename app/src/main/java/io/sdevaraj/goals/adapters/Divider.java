package io.sdevaraj.goals.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.sdevaraj.goals.R;

/**
 * An ItemDecoration allows the application to add a special drawing and layout offset
 * to specific item views from the adapter's data set. This can be useful for drawing dividers
 * between items, highlights, visual grouping boundaries and more.
 *
 * We draw the divider line at certain locations within the canvas supplied to the recycler view
 * based on the calculated right, left, top and bottom displacements. After the divider lines are
 * laid, the items in the recycler views are laid.
 */
public class Divider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mOrientation;

    /**
     * Constructor to create the divider drawable object from xml.
     */
    public Divider(Context context, int orientation) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider);

        if (orientation != LinearLayoutManager.VERTICAL) {
            throw new IllegalArgumentException("The orientation must be vertical for the divider to be used");
        }
        mOrientation = orientation;
    }

    /**
     * Draws the divider lines before the recycler views are drawn.
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawHorizontalDivider(c, parent, state);
        }
    }

    /**
     * Loops through each drop in the recycler view and places the divider above it based on the
     * displacements calculated.
     */
    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left, top, right, bottom;
        left = parent.getPaddingLeft();
        right = parent.getWidth() - parent.getPaddingRight();
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            if (parent.getAdapter().getItemViewType(i) != AdapterDrops.FOOTER) {
                View current = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) current.getLayoutParams();
                top = current.getTop() - params.topMargin;
                bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    /**
     * Gets the offset for each view inside the recycler view.
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}
