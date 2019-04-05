package cecs343.pollio;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements PollFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener{

    FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, PollFragment.newInstance("NEW POLLS"));
                    ft.commit();
                    return true;
                case R.id.navigation_starred:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, PollFragment.newInstance("STARRED POLLS"));
                    ft.commit();
                    return true;
                case R.id.navigation_account:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, new AccountFragment());
                    ft.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Begin the fragment transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with default fragment (Poll display)
        ft.replace(R.id.fragment_placeholder, PollFragment.newInstance("NEW POLLS"));
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
