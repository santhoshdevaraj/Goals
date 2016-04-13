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
 * Created by sdevaraj on 4/12/16.
 */
public class AdapterDrops extends RecyclerView.Adapter<AdapterDrops.DropHolder> {

    private LayoutInflater mInflater;
    private RealmResults<Drop> mResults;
    public static final String TAG = "stan";

    public AdapterDrops(Context context, RealmResults<Drop> results) {
        mInflater = LayoutInflater.from(context);
        mResults = results;
    }

    public static class DropHolder extends RecyclerView.ViewHolder {

        TextView mText;

        public DropHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.tv_what);
        }
    }

    public void Update(RealmResults<Drop> results) {
        mResults = results;
        notifyDataSetChanged();
    }

    @Override
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_drop, parent, false);
        DropHolder holder = new DropHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        Drop drop = mResults.get(position);
        holder.mText.setText(drop.getWhat());
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

}
