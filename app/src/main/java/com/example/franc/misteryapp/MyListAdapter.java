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


import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.net.FileNameMap;
import java.util.ArrayList;
import java.util.TimerTask;

import io.realm.OrderedRealmCollection;

class MyListAdapter extends BaseAdapter{
    private Context context; //context

    ArrayList<Enemy> enemies = null;

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    MyListAdapter(Context context, ArrayList<Enemy> enemies) {

        this.enemies = enemies;
        this.context = context;

    }

    private static class ViewHolder {
        TextView name = null;

    }

    public void updateData(ArrayList<Enemy> enemies){

        this.enemies = enemies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_listview_main_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.enemy_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (enemies != null) {
            final Enemy item = enemies.get(position);
            viewHolder.name.setText(item.getName());
        }



        return convertView;
    }


    public void addItem(Item item){

        RealmHelper helper = new RealmHelper();
        helper.addItem(item);
    }

    class MyThread extends Thread {
        private Handler handler;
        private int i = 0;
        public MyThread(Handler handler) {
            this.handler = handler;
        }
        public void run() {
            try {
                while(true) {
                    notifyMessage("Secondi "+i);
                    Thread.sleep(1000);
                    i++;
                }
            }catch(InterruptedException ex) {}
        }

        private void notifyMessage(String str) {
            Message msg = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("refresh", ""+str);
            msg.setData(b);
            handler.sendMessage(msg);
        }
    }
}

