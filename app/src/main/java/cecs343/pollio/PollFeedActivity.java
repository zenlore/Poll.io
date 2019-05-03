package cecs343.pollio;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;


public class PollFeedActivity extends AppCompatActivity implements PollFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener{

    FragmentTransaction ft;

    PollFragment newPolls;
    PollFragment favPolls;
    PollFragment myPolls;
    PollFragment votedPolls;
    AccountFragment account;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, newPolls);
                    setTitle(R.string.title_home);
                    ft.commit();
                    return true;
                case R.id.navigation_starred:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, favPolls);
                    ft.commit();
                    setTitle(R.string.title_starred);
                    return true;
                case R.id.navigation_mine:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, myPolls);
                    ft.commit();
                    setTitle(R.string.title_mypolls);
                    return true;
                case R.id.navigation_voted:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, votedPolls);
                    ft.commit();
                    setTitle(R.string.title_voted);
                    return true;
                case R.id.navigation_account:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, account);
                    ft.commit();
                    setTitle(R.string.title_account);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_feed);

        // requesting dangerous permission: (not sure if all these are necessary)
        requestPermission("android.permission.ACCESS_FINE_LOCATION");
        requestPermission("android.permission.ACCESS_COARSE_LOCATION");
        requestPermission("android.permission.WRITES_EXTERNAL_STORAGE");

        // GPS SETUP:
        GPSListener.initGPS(PollFeedActivity.this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        newPolls = PollFragment.newInstance("new");
        favPolls = PollFragment.newInstance("favorites");
        myPolls = PollFragment.newInstance("my");
        votedPolls = PollFragment.newInstance("voted");
        account = new AccountFragment();

        // Begin the fragment transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with default fragment (Poll display)
        ft.replace(R.id.fragment_placeholder, newPolls);
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void requestPermission(String permissionRequested){
        if (ContextCompat.checkSelfPermission(this,
                permissionRequested)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("REQUESTING", permissionRequested);
            ActivityCompat.requestPermissions(this, new String[]{permissionRequested},0);

        } else {
            // Permission has already been granted
            Log.i(permissionRequested, "ALREADY GRANTED ");
        }
    }

}
