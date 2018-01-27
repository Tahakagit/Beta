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
    static Realm realm;
    static String starSystem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_goto, container, false);


    }

    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        Button next;
        Button prev;
        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);

        final List<String> arraylist = retrieve();

        getStarFromSpinner(arraylist, view);

        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


                ((DialogActivity)getActivity()).getLocation(getStarFromSpinner(arraylist, view));


            }
        } );

        prev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ((DialogActivity)getActivity()).goBack();

            }
        } );



    }

    // start spinner
    public String getStarFromSpinner(List<String> starSystems, View view){
        // passa una lista di tutti gli starsystem
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, starSystems);
        spinner = view.findViewById(R.id.location_spinner);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                starSystem =  parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return starSystem;
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
