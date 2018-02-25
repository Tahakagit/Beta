package com.example.franc.misteryapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.realm.RealmResults;

/**
 * Created by franc on 22/02/2018.
 */

public class PlayerSuperControlCommandOfDestiny {

    RealmHelper helper;
    Context context;
    FragmentManager fragmentManager;
    boolean fabClicked = false;

    PlayerSuperControlCommandOfDestiny(RealmHelper helper, Context context, FragmentManager fManager) {
        this.helper = helper;
        this.context = context;
        this.fragmentManager = fManager;
    }

    // START PLAYER MENU
    void startPlayerMenu(LinearLayout bottomSheet, ViewPager vPager){
        /**
         *
         *
         *
         * todo get player health
         */


        BottomSheetBehavior bSBehavior;
        final ArrayList<Fragment> fragments = new ArrayList <>();

        bSBehavior = BottomSheetBehavior.from(bottomSheet);
        fragments.add(new MenuFragmentWeapons());
        fragments.add(new MenuFragmentGoTo());
        final Button menuPrev = bottomSheet.findViewById(R.id.id_btn_player_prev);
        final Button menuNext = bottomSheet.findViewById(R.id.id_btn_player_next);


        bSBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:{
                        menuPrev.setText("");
                        menuNext.setText("");
                    }
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        menuPrev.setText("PREV");
                        menuNext.setText("NEXT");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        menuPrev.setText("");
                        menuNext.setText("");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        ViewPageAdapterPlayerMenu vpAdapter = new ViewPageAdapterPlayerMenu(fragmentManager, fragments);

        startFragmentsInActivityDialog(fragments);
        vPager.setAdapter(vpAdapter);

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
    private RealmResults<WeaponSet> generateWeapons(int weaponsNumber){
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
    private String randomIdentifier() {
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
    private void startFragmentsInActivityDialog(ArrayList<Fragment> fragmentsArrayList){
        int i = 0;


/*
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment frag:fragmentsArrayList) {
            fragmentTransaction.add(R.id.fragmentcontainer, frag);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
*/
    }

    // START FLOATING MENU
    public void startFab(FloatingActionButton fab){
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

