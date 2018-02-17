package com.example.franc.misteryapp;

import io.realm.annotations.PrimaryKey;

/**
 * Created by franc on 09/01/2018.
 */

public class Item extends io.realm.RealmObject {

    @PrimaryKey
    private String text;
    private String category;
    private String dateCreated;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
