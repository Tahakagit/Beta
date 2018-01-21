package com.example.franc.misteryapp;

/**
 * Created by franc on 09/01/2018.
 */
/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private ArrayList<Enemy> mDataset;
    private OnItemSelectedListener iface;
    private OnItemDeselectedListener iface2;
    Activity activity;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mTextView;
        public TextView mCardView;
        public boolean isSelected = false;
        CardView cv;

        public ViewHolder(View v) {
            super(v);
            cv = (CardView)itemView.findViewById(R.id.cv);
            mCardView = (TextView)itemView.findViewById(R.id.enemy_name);

            mTextView = v;
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyListAdapter(ArrayList<Enemy> myDataset, Activity activity) {
        mDataset = myDataset;
        this.activity = activity;
        this.iface = (OnItemSelectedListener) activity;
        this.iface2 = (OnItemDeselectedListener) activity;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.enemy_row, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mCardView.setText(mDataset.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mDataset.get(position).isSelected()){
                    holder.cv.setCardBackgroundColor(view.getResources().getColor(R.color.cardview_light_background));
                    mDataset.get(position).setSelected(false);
                    iface2.onItemDeselected(mDataset.get(position));
                }else {
                    holder.cv.setCardBackgroundColor(view.getResources().getColor(R.color.colorAccent));
                    mDataset.get(position).setSelected(true);
                    iface.onItemSelected(mDataset.get(position));
/*
                    holder.isSelected = false;
*/
                }
            }

        });
    }
    public interface OnItemDeselectedListener {

        void onItemDeselected(Enemy item);
    }


    public interface OnItemSelectedListener {

        void onItemSelected(Enemy item);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
