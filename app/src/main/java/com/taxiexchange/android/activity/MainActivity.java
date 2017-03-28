package com.taxiexchange.android.activity;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import com.taxiexchange.android.R;
import com.taxiexchange.android.activity.fragments.ListAcceptedFragment;
import com.taxiexchange.android.activity.fragments.ListAuctionFragment;
import com.taxiexchange.android.activity.fragments.ListJoinedFragment;
import com.taxiexchange.android.activity.fragments.UserInfoFragment;
import com.taxiexchange.android.config.Apps;
import com.taxiexchange.android.config.PreferenceManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String PACKAGE_NAME;
    private final String TAG = getClass().getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private int mPosition = 0;
    private String[] mListTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
        String userMobile = PreferenceManager.getString(Apps.USER_MOBILE);
        boolean sign = PreferenceManager.getBoolean(Apps.IS_SIGN);
        Log.e(TAG, "Sign = " + String.valueOf(sign));
        Log.e(TAG, "Sign = " + userMobile);
        if (!sign || userMobile == null) {
            startLoginActivity();
        }
        initDrawer();

        PreferenceManager.writePreference(Apps.USER_INFO_STATE, false);
        PACKAGE_NAME = getApplicationContext().getPackageName();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.itemList:

                if(!PreferenceManager.getBoolean(Apps.USER_INFO_STATE))
                    showUserInfo();
                else
                    hideUserInfo();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_list_calendar:
                mPosition = 0;
                break;
            case R.id.nav_list_joined:
                mPosition = 1;
                break;
            case R.id.nav_list_accepted:
                mPosition = 2;
                break;
            default:
                break;
        }

        hideKeyboard();
        startFragment(mPosition);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void showUserInfo(){
        Fragment fragment = new UserInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_main, fragment)
                .addToBackStack("user")
                .commitAllowingStateLoss();
        PreferenceManager.writePreference(Apps.USER_INFO_STATE, true);
    }

    private void hideUserInfo(){
        getSupportFragmentManager().popBackStack ("user", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        PreferenceManager.writePreference(Apps.USER_INFO_STATE, false);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void startFragment(int mPosition) {
        Fragment fragment = new ListAuctionFragment();
        String tag = mListTitle[mPosition];
        setTitle(tag);
        switch (mPosition) {
            case 0:
                fragment = new ListAuctionFragment();
                break;
            case 1:
                fragment = new ListJoinedFragment();
                break;
            case 2:
                fragment = new ListAcceptedFragment();
                break;

            default:
                break;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, fragment, tag)
                .commitAllowingStateLoss();
    }



    private void initDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // init navigation menu
        mListTitle = getResources().getStringArray(R.array.titles);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        startFragment(0);
    }

    @Override
    protected void onPause() {
        hideUserInfo();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
