package io.sdevaraj.goals.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmResults;
import io.sdevaraj.goals.R;
import io.sdevaraj.goals.beans.Drop;

/**
 *  AdapterDrops (Adapter) provide a binding from the data set to views
 *  that are displayed within a RecyclerView.
 */
public class AdapterDrops extends RecyclerView.Adapter<AdapterDrops.DropHolder> {

    private LayoutInflater mInflater;
    private RealmResults<Drop> mResults;
    public static final String TAG = "Santhosh";

    public AdapterDrops(Context context, RealmResults<Drop> results) {
        mInflater = LayoutInflater.from(context);
        mResults = results;
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
     * Called from Activitymain. Redraws the adapter, when the RealmChangeListener is triggered on
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
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_drop, parent, false);
        DropHolder holder = new DropHolder(view);
        return holder;
    }

    /**
     * Creates a binding between the viewHolder and data set.
     */
    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        Drop drop = mResults.get(position);
        holder.mText.setText(drop.getWhat());
    }

    /**
     * Gets the count of drops for drawing the adapter.
     */
    @Override
    public int getItemCount() {
        return mResults.size();
    }

}
