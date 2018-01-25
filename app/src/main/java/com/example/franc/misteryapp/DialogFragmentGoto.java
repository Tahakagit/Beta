package com.example.franc.misteryapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.realm.Realm;
import io.realm.RealmResults;


public class DialogFragmentGoto extends Fragment {
    static Spinner spinner;
    Realm realm;
    static RealmResults<LocationRealmObject> realmSelect;
    static String contos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        realmSelect = realm.where(LocationRealmObject.class).findAllAsync();


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_goto, container, false);


    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Button next;
        Button prev;
        List<String> arraylist = retrieve();

        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arraylist);
        spinner = view.findViewById(R.id.location_spinner);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
/*
                contos = view.toString();
*/
                contos =  parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


                ((DialogActivity)getActivity()).getLocation(contos);


            }
        } );

        prev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ((DialogActivity)getActivity()).goBack();

            }
        } );



    }


    public List<String> retrieve()
    {
        List<String> location=new ArrayList<String>();

        RealmResults<LocationRealmObject> locations=realm.where(LocationRealmObject.class).distinctValues("locationStar").findAll();
        for(LocationRealmObject s: locations)
        {
            location.add(s.getLocationStar().toString());
        }
        return location;
    }


}
