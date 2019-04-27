package cecs343.pollio;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Random;

public class PollFeedActivity extends AppCompatActivity implements PollFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener{


    FragmentTransaction ft;

    private ArrayList<PollItem> newPolls = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, PollFragment.newInstance(newPolls));
                    ft.commit();
                    return true;
                case R.id.navigation_starred:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_placeholder, PollFragment.newInstance(newPolls));
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
        setContentView(R.layout.activity_poll_feed);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        newPolls = Requestor.getHotPolls(getApplicationContext(), "e4bKUDvF0lU2Q8ubyIs0GgAfeKi1");

        //nitTestPolls();

        // Begin the fragment transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with default fragment (Poll display)
        ft.replace(R.id.fragment_placeholder, PollFragment.newInstance(newPolls));
        ft.commit();
    }

    private void initTestPolls() {
        Random r = new Random();
        for(int i = 0; i < 200; i++) {
            int numOpts = r.nextInt(5 - 2 + 1) + 2;
            PollItem poll = new PollItem("Test new poll " + i, false);
            ArrayList<PollOption> pollOpts = new ArrayList<PollOption>();

            for(int j = 0; j < numOpts; j++) {
                pollOpts.add(new PollOption("Poll option " + (j + 1), 1));
            }
            poll.setOptions(pollOpts);
            newPolls.add(poll);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
