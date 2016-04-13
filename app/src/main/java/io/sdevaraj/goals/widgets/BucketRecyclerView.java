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

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
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

    private void toggleViews() {
        if (getAdapter() != null && !mEmptyViews.isEmpty() && !mNonEmptyViews.isEmpty()) {
            if (getAdapter().getItemCount() == 0 ) {
                // hide the recycler view
                Util.showViews(mEmptyViews);
                // show all the empty views
                setVisibility(View.GONE);
                // hide all the views to be hidden
                Util.hideViews(mNonEmptyViews);
            } else {
                // show all the other views
                Util.showViews(mNonEmptyViews);
                // show the recycler view
                setVisibility(View.VISIBLE);
                // hide all the empty views
                Util.hideViews(mEmptyViews);
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

    public void hideIfEmpty(View... views) {
        mNonEmptyViews = Arrays.asList(views);
    }

    public void showIfEmpty(View... views) {
        mEmptyViews = Arrays.asList(views);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mObserver);
        }
        mObserver.onChanged();
    }
}
