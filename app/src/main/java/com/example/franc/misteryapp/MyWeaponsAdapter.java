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


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmResults;

public class MyWeaponsAdapter extends RecyclerView.Adapter<MyWeaponsAdapter.ViewHolder> {
    private RealmResults<WeaponSet> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mTextView;
        public TextView mCardView;
        CardView cv;

        public ViewHolder(View v) {
            super(v);
            cv = (CardView)itemView.findViewById(R.id.cv);
            mCardView = (TextView)itemView.findViewById(R.id.weapon_name);

            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyWeaponsAdapter(RealmResults<WeaponSet> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyWeaponsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weapon_row, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mCardView.setText(mDataset.get(position).getWeaponName());

    }


    public int deleteItemAt(int position){
        RealmHelper helper = new RealmHelper();
        int weaponPower;

        //ho la posizione della lista
        weaponPower = helper.removeWeapondAt(position);

        notifyItemRangeRemoved(position, 1);
        //query realm results alla posizione
        //ritornare

        return weaponPower;
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
