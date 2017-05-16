package com.wolff.wardrobe.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wolff.wardrobe.R;
import com.wolff.wardrobe.fragments.Fragment_WItem;


public class Activity_NewWItem extends AppCompatActivity {
    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, Activity_NewWItem.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_witem);
        Fragment fragment = Fragment_WItem.newInstance(null,getApplicationContext());
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_new_witem, fragment);
        fragmentTransaction.commit();

    }
}
