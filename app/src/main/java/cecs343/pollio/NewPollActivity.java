package cecs343.pollio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

public class NewPollActivity extends AppCompatActivity {

    private Button submitBtn;
    private Button addNewOptionBtn;
    private ListView lv;
    private PollOptionsAdapter pollOptionsAdapter;
    public ArrayList<EditModel> optionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poll);

        optionsList = populateList();
        pollOptionsAdapter = new PollOptionsAdapter(this, optionsList);

        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(pollOptionsAdapter);

        submitBtn = (Button) findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < optionsList.size(); i++){
                    System.out.println(optionsList.get(i).getEditTextValue());
                }
            }
        });

        addNewOptionBtn = (Button) findViewById(R.id.main_add_btn);
        addNewOptionBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(optionsList.size() < 8) {
                    EditModel em = new EditModel();
                    optionsList.add(em);
                    lv.setAdapter(new PollOptionsAdapter(NewPollActivity.this, optionsList));
                    pollOptionsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private ArrayList<EditModel> populateList(){
        ArrayList<EditModel> list = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            EditModel editModel = new EditModel();
            list.add(editModel);
        }
        return list;
    }
}
