package com.example.franc.misteryapp;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by franc on 23/01/2018.
 */

public class MyNavigationEnemyAdapter extends RecyclerView.Adapter<MyNavigationEnemyAdapter.ViewHolder> {
    private List<Enemy> mDataset;
    Activity activity;
    static OnEnemyApproached iface;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mTextView;
        public TextView mCardView;
        public TextView enemyHealth;

        public boolean isSelected = false;
        CardView cv;

        public ViewHolder(View v) {
            super(v);
            cv = (CardView)itemView.findViewById(R.id.cv_location);
            mCardView = (TextView)itemView.findViewById(R.id.location_name);


            mTextView = v;
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyNavigationEnemyAdapter(List<Enemy> myDataset) {
        mDataset = myDataset;
    }
    public MyNavigationEnemyAdapter(OnEnemyApproached callback) {
        this.iface = callback;
    }


    public  void UpdateAdapter(List<Enemy> newDataset){

        mDataset = newDataset;
        notifyDataSetChanged();
    }
    // Create new views (invoked by the layout manager)
    @Override
    public MyNavigationEnemyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_row, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
/*
        Enemy enemy = new Enemy();
        enemy.fetchById(mDataset.get(position).getId());
*/
        holder.mCardView.setText(mDataset.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //creating a popup menu
                // todo must become popup window
                PopupMenu popup = new PopupMenu(NavigationActivity.context, holder.itemView, 1, R.style.GreenText, 0);
                //inflating menu from xml resource
                popup.inflate(R.menu.option_menu_enemy);

                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                final Intent attackEnemy = new Intent(NavigationActivity.context, BattleActivity.class);
/*
                                EnemyQueue queue = new EnemyQueue();
                                RealmHelper helper = new RealmHelper();
                                queue.setExist(1);
                                queue.setEnemyBuffer(mDataset.get(holder.getAdapterPosition()));
                                helper.addItem(queue);
*/
/*
                                iface.onEnemyClose(mDataset.get(holder.getAdapterPosition()));
*/
                                NavigationActivity.context.startActivity(attackEnemy);

/*
                                queue.setEnemyBuffer(mDataset.get(position));
*/
                                break;
                            case R.id.menu2:
                                //handle menu2 click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }

        });

    }

    public interface OnEnemyApproached {

        void onEnemyClose(Enemy item);
        void onEnemyFar(Enemy item);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
