package io.sdevaraj.goals.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import io.sdevaraj.goals.R;
import io.sdevaraj.goals.beans.Drop;

/**
 *  AdapterDrops (Adapter) provide a binding from the data set to views
 *  that are displayed within a RecyclerView.
 */
public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener{

    private LayoutInflater mInflater;
    private RealmResults<Drop> mResults;
    private Realm mRealm;
    public static final int ITEM = 0;
    public static final int FOOTER = 1;
    public static final String TAG = "Santhosh";
    private AddListener mAddListener;

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results, AddListener listener) {
        mInflater = LayoutInflater.from(context);
        mResults = results;
        mAddListener = listener;
        mRealm = realm;
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
     * View holder definition. Represents a single row within the recycler view.
     * TODO: Investigate if mText can be cached.
     */
    public static class DropHolder extends RecyclerView.ViewHolder {
        TextView mText;

        public DropHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.tv_what);
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
     * Called from ActivityMain. Redraws the adapter, when the RealmChangeListener is triggered on
     * RealmResults change.
     */
    public void Update(RealmResults<Drop> results) {
        mResults = results;
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
            return new DropHolder(view);
        } else {
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
        if (position < mResults.size()) {
            return ITEM;
        } else {
            return FOOTER;
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
            dropHolder.mText.setText(drop.getWhat());
        }
    }

    /**
     * Gets the count of drops for drawing the adapter.
     * TODO: Investigate if mResults can be null
     */
    @Override
    public int getItemCount() {
        return mResults.size() + 1;
    }

}
