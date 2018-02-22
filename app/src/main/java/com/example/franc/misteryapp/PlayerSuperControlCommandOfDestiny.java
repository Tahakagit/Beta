package com.example.franc.misteryapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.realm.RealmResults;

/**
 * Created by franc on 22/02/2018.
 */

public class PlayerSuperControlCommandOfDestiny implements MenuFragmentGoTo.SendToDialogActivity {

    RealmHelper helper;
    Context context;
    FragmentManager fragmentManager;
    boolean fabClicked = false;

    public PlayerSuperControlCommandOfDestiny(RealmHelper helper, Context context, FragmentManager fManager) {
        this.helper = helper;
        this.context = context;
        this.fragmentManager = fManager;
    }

    // START PLAYER MENU
    public void startPlayerMenu(){
        /**
         *
         *
         *
         * todo get player health
         */

        BottomSheetBehavior bSBehavior;
        LinearLayout ll;
        MyWeaponsAdapter weaponsAdapter;
        final ArrayList<Fragment> fragments = new ArrayList <>();


        RealmResults<WeaponSet> weapons;
        RecyclerView rVWeapons;
        RecyclerView.LayoutManager mLayoutManagerWeapons;

        weapons = generateWeapons(25);
        mLayoutManagerWeapons = new LinearLayoutManager(context);

        weaponsAdapter = new MyWeaponsAdapter(weapons, helper);
        ll = ((NavigationActivity)context).getWindow().peekDecorView().findViewById(R.id.bottom_sheet);

/*
        ll = context.(R.id.bottom_sheet);
*/
/*
        rVWeapons = findViewById(R.id.rv_weapons_bottomsheet);
        rVWeapons.setLayoutManager(mLayoutManagerWeapons);
        rVWeapons.setAdapter(weaponsAdapter);
*/

        bSBehavior = BottomSheetBehavior.from(ll);
        fragments.add(new MenuFragmentGoTo());
        startFragmentsInActivityDialog(fragments);

    }

    /**
     * SET PLAYER NEW LOCATION AND UPDATES UI
     * IFACE CALLED FROM DIALOGFRAMENTGOTO
     * @param star String new location to navigate to
     */
    public void navigateTo(String star) {
        RealmHelper helper = new RealmHelper();
        helper.setPlayerLocation(star);
        NavigationActivity.navigationEnemyAdapter.UpdateAdapter(helper.getEnemiesAtPLayerPosition());
        NavigationActivity.navigationAdapter.UpdateAdapter(helper.getPlacesAtPLayerPosition());
/*
        finish();
*/
    }

    // inserisce N armi e ne restituisce il RealmResult
    public RealmResults<WeaponSet> generateWeapons(int weaponsNumber){
        helper.resetWeapons();
        for (int i = 0 ; i < weaponsNumber ; i++){
            WeaponSet weapons = new WeaponSet();
            if (i == 0)
                weapons.setViewType(0);
            else
                weapons.setViewType(1);
            weapons.setWeaponName("Missile");
            weapons.setWeaponDamage(3);
            weapons.setWeapondID(randomIdentifier());

            helper.addWeapon(weapons);
        }

        return helper.getWeapons();
    }

    // GENERA STRINGHE CASUALI
    public String randomIdentifier() {
        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
        final java.util.Random rand = new java.util.Random();
        final Set<String> identifiers = new HashSet<String>();

        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }

    /**
     * Starts a fragments sequence in dialogactivity
     *
     * @param fragmentsArrayList arraylist filled with fragments to show
     */
    public void startFragmentsInActivityDialog(ArrayList<Fragment> fragmentsArrayList){
        int i = 0;

        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment frag:fragmentsArrayList) {
            fragmentTransaction.add(R.id.fragmentcontainer, frag);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // START FLOATING MENU
    public void startFab(){
        FloatingActionButton fab = ((NavigationActivity)context).getWindow().peekDecorView().findViewById(R.id.fab);;
        boolean clicked = false;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                final FloatingActionButton fab1 = ((NavigationActivity)context).getWindow().peekDecorView().findViewById(R.id.fab_1);

                if (fabClicked){
                    // CLOSE FAB MENU
                    final Animation hide_fab_1 = AnimationUtils.loadAnimation(((NavigationActivity) context).getApplication(), R.anim.hide_fab_1);

                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab1.getLayoutParams();
                    layoutParams.rightMargin -= (int) (fab1.getWidth() * 1.7);
/*
                    layoutParams.bottomMargin -= (int) (fab1.getHeight() * 0.25);
*/
                    fab1.setLayoutParams(layoutParams);
                    fab1.startAnimation(hide_fab_1);
                    fab1.setClickable(false);
                    fabClicked = false;

                }else {
                    //OPENS FAB MENU
                    fabClicked = true;

                    final Animation show_fab_1 = AnimationUtils.loadAnimation(((NavigationActivity) context).getApplication(), R.anim.show_fab_1);

                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab1.getLayoutParams();
                    layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
/*
                    layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
*/
                    fab1.setLayoutParams(layoutParams);

                    fab1.startAnimation(show_fab_1);
                    fab1.setClickable(true);
                    fab1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Intent intent = new Intent(NavigationActivity.context,DialogActivity.class);
                            context.startActivity(intent);

                        }
                    });


                }
            }
        });

    }

}

