package io.sdevaraj.goals.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.sdevaraj.goals.utils.Util;

/**
 * Created by sdevaraj on 4/13/16.
 */
public class BucketRecyclerView extends RecyclerView {

    private List<View> mNonEmptyViews = Collections.emptyList();
    private List<View> mEmptyViews = Collections.emptyList();

    /**
     * Observer for refreshing the view based on the data set changes.
     */
    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        /**
         * Called by notifyDataSetChanged() to refresh the views.
         */
        @Override
        public void onChanged() {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            toggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            toggleViews();
        }
    };

    /**
     * Based on presence/absence of drops, displays/hides the toolbar and the empty_drop view.
     * If no drops, hide the toolbar but display the empty_drop view.
     * Else, display the toolbar and hide the empty_drop view.
     */
    private void toggleViews() {
        if (getAdapter() != null && !mEmptyViews.isEmpty() && !mNonEmptyViews.isEmpty()) {
            if (getAdapter().getItemCount() == 1 ) { // getItemCount(), gets count of items in the data set attached to the adapter
                Util.showViews(mEmptyViews);    // show the empty_drop view
                setVisibility(View.GONE);       // hide the recycler view ie list of drops
                Util.hideViews(mNonEmptyViews); // hide the toolbar view
            } else {
                Util.showViews(mNonEmptyViews); // show the toolbar view
                setVisibility(View.VISIBLE);    // show the recycler view ie list of drops
                Util.hideViews(mEmptyViews);    // hide the empty_drop view
            }
        }
    }

    public BucketRecyclerView(Context context) {
        super(context);
    }

    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Gets the list of views to be displayed/hidden when there is a list of drops
     */
    public void hideIfEmpty(View... views) {
        mNonEmptyViews = Arrays.asList(views);
    }

    /**
     * Gets the list of views to be displayed/hidden when there are no drops
     */
    public void showIfEmpty(View... views) {
        mEmptyViews = Arrays.asList(views);
    }

    /**
     * Registers the adapter and observer for the recycler view.
     * TODO: Why is onChanged() called below ?
     */
    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mObserver);
        }
        mObserver.onChanged();
    }
}
