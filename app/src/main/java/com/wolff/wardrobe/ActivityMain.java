package com.wolff.wardrobe;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wolff.wardrobe.activities.Activity_NewWItem;
import com.wolff.wardrobe.activities.Activity_WItem_Pager;
import com.wolff.wardrobe.fragments.Fragment_ListWItem;
import com.wolff.wardrobe.fragments.Fragment_SelectionWItem;
import com.wolff.wardrobe.fragments.Fragment_logo;
import com.wolff.wardrobe.fragments.Fragment_preferences;
import com.wolff.wardrobe.objects.WItem;
import com.wolff.wardrobe.objects.WItemLab;
import com.wolff.wardrobe.yahooWeather.WeatherInfo;
import com.wolff.wardrobe.yahooWeather.YahooWeather;
import com.wolff.wardrobe.yahooWeather.YahooWeatherInfoListener;


public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,YahooWeatherInfoListener {
    YahooWeather mYahooWeather;
    WeatherInfo mWeatherInfo;
    FloatingActionButton fab;
    Fragment_ListWItem fragment_list_witem;
    Fragment_SelectionWItem fragment_selectionWItem;
    //FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WItem item = new WItem();
                //WItemLab.get(getApplicationContext()).addWItem(item);
                Intent intent = Activity_NewWItem.newIntent(getApplicationContext());
                startActivity(intent);
                //Log.e("CLICK ITEM",""+item.getTitle());
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
        Fragment_logo fragment_logo = new Fragment_logo();
        displayFragment(fragment_logo);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//=================================================================================================
        mYahooWeather = YahooWeather.getInstance(5000, true);
        String _location = "Kiev";
        if (!TextUtils.isEmpty(_location)) {
                searchByPlaceName(_location);
        }
        //-----------------------------------------------------------------------------------------
        fragment_list_witem = new Fragment_ListWItem();
        displayFragment(fragment_list_witem);
        fragment_selectionWItem = new Fragment_SelectionWItem();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

     @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_catalog) {
            displayFragment(fragment_list_witem);
        } else if (id == R.id.nav_select) {
            //Bundle args = new Bundle();
            //args.putSerializable("WeatherInfo", mWeatherInfo);
            //fragment_selectionWItem.setArguments(args);
            displayFragment(fragment_selectionWItem);
        } else if (id == R.id.nav_settings) {
            Fragment_preferences fp =  new Fragment_preferences();
            displayFragment(fp);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    ///=============================================================================================
    private void displayFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_activity_main_container, fragment);
        fragmentTransaction.commit();
        if (fragment.getClass().getSimpleName().equalsIgnoreCase("Fragment_ListWItem")) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void gotWeatherInfo(WeatherInfo weatherInfo, YahooWeather.ErrorType errorType) {
        if (weatherInfo != null) {
            if (mYahooWeather.getSearchMode() == YahooWeather.SEARCH_MODE.GPS) {
                if (weatherInfo.getAddress() != null) {
                }
            }
            mWeatherInfo = weatherInfo;
            Log.e("== CURRENT","====== CURRENT ======" + "\n" +
                    "date: " + weatherInfo.getCurrentConditionDate() + "\n" +
                    "weather: " + weatherInfo.getCurrentText() + "\n" +
                    "temperature in ºC: " + weatherInfo.getCurrentTemp() + "\n" +
                    "wind chill: " + weatherInfo.getWindChill() + "\n" +
                    "wind direction: " + weatherInfo.getWindDirection() + "\n" +
                    "wind speed: " + weatherInfo.getWindSpeed() + "\n" +
                    "Humidity: " + weatherInfo.getAtmosphereHumidity() + "\n" +
                    "Pressure: " + weatherInfo.getAtmospherePressure() + "\n" +
                    "Visibility: " + weatherInfo.getAtmosphereVisibility());

        }
    }
    private void searchByPlaceName(String location) {
        mYahooWeather.setNeedDownloadIcons(true);
        mYahooWeather.setUnit(YahooWeather.UNIT.CELSIUS);
        mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.PLACE_NAME);
        mYahooWeather.queryYahooWeatherByPlaceName(getApplicationContext(), location, ActivityMain.this);
    }

}