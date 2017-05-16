package com.wolff.wardrobe.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wolff.wardrobe.R;
import com.wolff.wardrobe.fragments.Fragment_WItem;
import com.wolff.wardrobe.objects.WItem;
import com.wolff.wardrobe.objects.WItemLab;

import java.util.List;
import java.util.UUID;

public class Activity_WItem_Pager extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<WItem> mWItems;
    public static final String EXTRA_WITEM_ID = "witem_id";

    public static Intent newIntent(Context packageContext, UUID witemId) {
        Intent intent = new Intent(packageContext, Activity_WItem_Pager.class);
        intent.putExtra(EXTRA_WITEM_ID, witemId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_witem_pager);
        Log.e("ON CREATE","1");
        UUID wItemId = (UUID) getIntent().getSerializableExtra(EXTRA_WITEM_ID);
        Log.e("ON CREATE","2");
        mViewPager = (ViewPager)findViewById(R.id.viewPager_container);
        Log.e("ON CREATE","3");
        mWItems = WItemLab.get(this).getSelectedWItems(null);
        Log.e("ON CREATE","4");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.e("ON CREATE","5");
        mViewPager.setAdapter(new  FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                WItem item = mWItems.get(position);
                Log.e("ON CREATE","7");

                return Fragment_WItem.newInstance(item.getId(),getApplicationContext());
            }

            @Override
            public int getCount() {
                return mWItems.size();
            }
        });
        Log.e("ON CREATE","6");
        for (int i = 0; i < mWItems.size(); i++) {
            if (mWItems.get(i).getId().equals(wItemId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
       }

    }
}
