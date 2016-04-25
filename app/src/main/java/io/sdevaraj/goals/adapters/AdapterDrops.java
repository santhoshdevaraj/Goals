package io.sdevaraj.goals.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import io.sdevaraj.goals.AppBucketDrops;
import io.sdevaraj.goals.R;
import io.sdevaraj.goals.beans.Drop;

/**
 * AdapterDrops (Adapter) provide a binding from the data set to views
 * that are displayed within a RecyclerView.
 */
public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    private LayoutInflater mInflater;
    private RealmResults<Drop> mResults;
    private Realm mRealm;
    public static final int COUNT_FOOTER = 1;
    public static final int COUNT_NO_ITEMS = 1;
    public static final int ITEM = 0;
    public static final int NO_ITEM = 1;
    public static final int FOOTER = 2;
    private int mFilterOption;
    private AddListener mAddListener;
    private MarkListener mMarkListener;
    private Context mContext;

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results, AddListener listener, MarkListener markListener) {
        mInflater = LayoutInflater.from(context);
        mResults = results;
        mAddListener = listener;
        mMarkListener = markListener;
        mRealm = realm;
        mContext = context;
    }

    /**
     * Removes the element from the DB when its swiped and notifies the observers.
     */
    @Override
    public void onSwipe(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).removeFromRealm();
            mRealm.commitTransaction();
            notifyItemRemoved(position);
        }
    }

    /**
     * Marks the item as complete in the DB on button click.
     */
    public void markComplete(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).setCompleted(true);
            mRealm.commitTransaction();
            notifyItemChanged(position);
        }
    }


    /**
     * View holder definition. Represents a single row within the recycler view.
     * TODO: Investigate if mTextWhat can be cached.
     * TODO: Why is swipe listener implemented by adapter whilst mark listener is implemented by drop holder ?
     */
    public static class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextWhat;
        TextView mTextWhen;
        MarkListener mMarkListener;
        Context mContext;

        public DropHolder(View itemView, MarkListener markListener) {
            super(itemView);
            mContext = itemView.getContext();
            mMarkListener = markListener;
            itemView.setOnClickListener(this);
            mTextWhat = (TextView) itemView.findViewById(R.id.tv_what);
            mTextWhen = (TextView) itemView.findViewById(R.id.tv_when);
        }

        @Override
        public void onClick(View v) {
            mMarkListener.onMark(getAdapterPosition());
        }

        /**
         * Sets the text for what in each drop. Called from onBindViewHolder.
         */
        public void setWhat(String what) {
            mTextWhat.setText(what);
        }

        /**
         * Sets the text for when in each drop. Called from onBindViewHolder.
         */
        public void setWhen(long when) {
            mTextWhen.setText(DateUtils.getRelativeTimeSpanString(when, System.currentTimeMillis(),
                    DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));
        }

        /**
         * Sets the background color in each drop. Called from onBindViewHolder.
         */
        public void setBackground(boolean completed) {
            Drawable drawable;
            if (completed) {
                drawable = ContextCompat.getDrawable(mContext, R.color.bg_drop_complete);
            } else {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.bg_row_drop);
            }
            itemView.setBackground(drawable);
        }
    }

    /**
     * View holder definition. Represents the footer within the recycler view.
     */
    public class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button mBtnFooter;

        public FooterHolder(View itemView) {
            super(itemView);
            mBtnFooter = (Button) itemView.findViewById(R.id.btn_footer);
            mBtnFooter.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAddListener.add();
        }
    }

    /**
     * View holder definition. Represents the no item view within the recycler view.
     */
    public class NoItemsHolder extends RecyclerView.ViewHolder {

        public NoItemsHolder(View itemView) {
            super(itemView);
        }

    }

    /**
     * Called from ActivityMain. Redraws the adapter, when the RealmChangeListener is triggered on
     * RealmResults change.
     */
    public void Update(RealmResults<Drop> results) {
        mResults = results;
        mFilterOption = AppBucketDrops.load(mContext);
        // Notify any registered observers that the data set has changed. Calls the onChanged()
        // method in the registered observer(s).
        notifyDataSetChanged();
    }

    /**
     * Creates a binding between the layout (using LayoutManager) and the viewHolder.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == ITEM) {
            view = mInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(view, mMarkListener);
        } else if (viewType == NO_ITEM) {
            view = mInflater.inflate(R.layout.no_item, parent, false);
            return new NoItemsHolder(view);
        }
        else {
            view = mInflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(view);
        }
    }

    /**
     * Gets the type of itemView ie if its a normal recyclable element ie drop or a special
     * element like a footer.
     */
    @Override
    public int getItemViewType(int position) {
        if (!mResults.isEmpty()) {
            if (position < mResults.size()) {
                return ITEM;
            } else {
                return FOOTER;
            }
        } else {
            if (mFilterOption == Filter.COMPLETE || mFilterOption == Filter.INCOMPLETE) {
                if (position == 0) {
                    return NO_ITEM;
                } else {
                    return FOOTER;
                }
            } else { //just completing the clause;
                return FOOTER;
            }
        }
    }

    /**
     * Creates a binding between the viewHolder and data set.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = mResults.get(position);
            dropHolder.setWhat(drop.getWhat());
            dropHolder.setWhen(drop.getWhen());
            dropHolder.setBackground(drop.isCompleted());
        }
    }

    /**
     * Assigns the id for each item.
     */
    @Override
    public long getItemId(int position) {
        if (position < mResults.size()) {
            return mResults.get(position).getAdded();
        } else {
            return super.getItemId(position);
        }
    }

    /**
     * Gets the count of drops for drawing the adapter.
     * TODO: Investigate if mResults can be null
     */
    @Override
    public int getItemCount() {
        if (!mResults.isEmpty()) {
            return mResults.size() + COUNT_FOOTER;
        }
        else {
            if (mFilterOption == Filter.LEAST_TIME_LEFT || mFilterOption == Filter.MOST_TIME_LEFT || mFilterOption == Filter.NONE) {
                return mResults.size() + COUNT_FOOTER;
            } else {
                return COUNT_NO_ITEMS + COUNT_FOOTER;
            }
        }
    }

}
