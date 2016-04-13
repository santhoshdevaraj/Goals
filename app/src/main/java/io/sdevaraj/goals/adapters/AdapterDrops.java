package io.sdevaraj.goals.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.sdevaraj.goals.R;

/**
 * Created by sdevaraj on 4/12/16.
 */
public class AdapterDrops extends RecyclerView.Adapter<AdapterDrops.DropHolder> {

    private LayoutInflater mInflater;
    private ArrayList<String> mItems = new ArrayList<>();

    public AdapterDrops(Context context) {
        mInflater = LayoutInflater.from(context);
        mItems = generateValues();
    }

    public static ArrayList<String> generateValues() {
        ArrayList<String> dummy = new ArrayList<>();
        for (int i=1; i < 101; i++) {
            dummy.add("Item " + i);
        }
        return dummy;
    }

    public static class DropHolder extends RecyclerView.ViewHolder {

        TextView mText;
        public DropHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.tv_what);
        }
    }

    @Override
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_drop, parent, false);
        DropHolder holder = new DropHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        holder.mText.setText(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return 100;
    }

}
