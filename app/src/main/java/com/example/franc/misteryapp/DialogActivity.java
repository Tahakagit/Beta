package com.example.franc.misteryapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class DialogActivity extends AppCompatActivity implements DialogFragmentGoto.SendToDialogActivity {

    static int i = 0;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final ArrayList<Fragment> fragments = new ArrayList <>();

    static Button next;
    static String starSystem = null;

    RealmHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        next = findViewById(R.id.next);

        helper = new RealmHelper();


        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        fragments.add(new DialogFragmentGoto());

        fragmentTransaction.add(R.id.fragmentcontainer, fragments.get(i));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }

    @Override
    public void navigateTo(String star) {
        starSystem = star;
        helper.setPlayerLocation(starSystem);
        NavigationActivity.navigationEnemyAdapter.UpdateAdapter(helper.getEnemiesAtPLayerPosition());
        i = 0;
        finish();

    }
}
