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


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import io.realm.OrderedRealmCollection;

class MyListAdapter extends RealmBaseAdapter<Item> implements ListAdapter {

    MyListAdapter(OrderedRealmCollection<Item> realmResults) {
        super(realmResults);
    }

    private static class ViewHolder {
        TextView text = null;
        TextView category = null;
        TextView date = null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_listview_main_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text = convertView.findViewById(R.id.view_item_text);
            viewHolder.category = convertView.findViewById(R.id.view_item_category);
            viewHolder.date = convertView.findViewById(R.id.view_date_creation);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (adapterData != null) {
            final Item item = adapterData.get(position);
            viewHolder.text.setText(item.getText());
            viewHolder.date.setText(item.getDateCreated());
            viewHolder.category.setText(item.getCategory());
        }
        return convertView;
    }


    public void addItem(Item item){

        RealmHelper helper = new RealmHelper();
        helper.addItem(item);
    }
}

