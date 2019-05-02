package cecs343.pollio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

public class NewPollActivity extends AppCompatActivity{

    private Button submitBtn;
    private Button addNewOptionBtn;
    private ListView listViewOfOptions;
    private PollOptionsAdapter pollOptionsAdapter;
    public ArrayList<EditModel> optionsList;

    private PollItem pollItem;
    private EditText editTextPollTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poll);

        optionsList = populateList();
        pollOptionsAdapter = new PollOptionsAdapter(this, optionsList);

        listViewOfOptions = (ListView) findViewById(R.id.poll_options);
        listViewOfOptions.setAdapter(pollOptionsAdapter);

        editTextPollTitle = (EditText) findViewById(R.id.poll_title_question);

        submitBtn = (Button) findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pollItem = new PollItem(editTextPollTitle.getText().toString(), false, getIDfromDB());

                // adding optionsLists to pollItem:
                for (int i = 0; i < optionsList.size(); i++) {
                    pollItem.addPollOption(new PollOption(optionsList.get(i).getEditTextValue(), 0));
                }

                // adding GPS coordinates to pollItem:
                pollItem.setLatitude(PollFeedActivity.gps.getLatitude());
                pollItem.setLongitude(PollFeedActivity.gps.getLongitude());

                // logging pollItem to see if contents are correct:
                Log.i("TITLE", pollItem.getTitle());
                for (int i = 0; i < pollItem.getOptions().size(); i++) {
                    Log.i(String.valueOf(i+1), pollItem.getOptions().get(i).getText());
                }
                Log.i("NUM OF OPTIONS", String.valueOf(pollItem.getNumOptions()));
                Log.i("LATITUDE", Double.toString(pollItem.getLatitude()));
                Log.i("LONGITUDE", Double.toString(pollItem.getLongitude()));
                Log.i("CITY", PollFeedActivity.gps.getCityName());

                // destroy this instance of NewPollActivity:
                finish();
            }
        });

        addNewOptionBtn = (Button) findViewById(R.id.main_add_btn);
        addNewOptionBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(optionsList.size() < 5) {
                    EditModel em = new EditModel();
                    optionsList.add(em);
                    listViewOfOptions.setAdapter(new PollOptionsAdapter(NewPollActivity.this, optionsList));
                    pollOptionsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private ArrayList<EditModel> populateList(){
        ArrayList<EditModel> list = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            EditModel editModel = new EditModel();
            list.add(editModel);
        }
        return list;
    }

    private int getIDfromDB(){
        return 1337;
    }



}