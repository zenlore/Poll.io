package cecs343.pollio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class NewPollActivity extends AppCompatActivity{

    private ListView listViewOfOptions;
    private PollOptionsAdapter pollOptionsAdapter;
    public ArrayList<EditModel> optionsList;

    private PollItem pollItem;
    private EditText editTextPollTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poll);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // shows back button on the action bar

        setTitle("Add New Poll");

        optionsList = populateList();
        pollOptionsAdapter = new PollOptionsAdapter(this, optionsList);

        listViewOfOptions = (ListView) findViewById(R.id.poll_options);
        listViewOfOptions.setAdapter(pollOptionsAdapter);

        editTextPollTitle = (EditText) findViewById(R.id.poll_title_question);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_poll_action_bar_layout, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_new_poll_opt) {
            if(optionsList.size() < 5) {
                EditModel em = new EditModel();
                optionsList.add(em);
                listViewOfOptions.setAdapter(new PollOptionsAdapter(NewPollActivity.this, optionsList));
                pollOptionsAdapter.notifyDataSetChanged();
            }
        }
        else if (id == R.id.submit_new_poll) {
                pollItem = new PollItem(editTextPollTitle.getText().toString(), false, 0);

                // adding optionsLists to pollItem:
                for (int i = 0; i < optionsList.size(); i++) {
                    pollItem.addPollOption(new PollOption(optionsList.get(i).getEditTextValue(), 0));
                }

                // adding GPS coordinates to pollItem:
                pollItem.setLatitude(GPSListener.getInstance().getLatitude());
                pollItem.setLongitude(GPSListener.getInstance().getLongitude());

                HashMap<String, String> args = pollItem.getArgs();
                Requestor.postRequest(getApplicationContext(), "create", FirebaseAuth.getInstance().getCurrentUser(), args);

                // logging pollItem to see if contents are correct:
                Log.i("TITLE", pollItem.getTitle());
                for (int i = 0; i < pollItem.getOptions().size(); i++) {
                    Log.i(String.valueOf(i+1), pollItem.getOptions().get(i).getText());
                }
                Log.i("NUM OF OPTIONS", String.valueOf(pollItem.getNumOptions()));
                Log.i("LATITUDE", Double.toString(pollItem.getLatitude()));
                Log.i("LONGITUDE", Double.toString(pollItem.getLongitude()));
                Log.i("CITY", GPSListener.getInstance().getCityName());

                // destroy this instance of NewPollActivity:
                finish();
        }
        else if(id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<EditModel> populateList(){
        ArrayList<EditModel> list = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            EditModel editModel = new EditModel();
            list.add(editModel);
        }
        return list;
    }
}