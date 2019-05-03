package cecs343.pollio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import android.view.MenuItem;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import static com.github.mikephil.charting.animation.Easing.Linear;

/**
 * Activity that shows a results chart for an individual poll.
 * (May add other information as it becomes available.)
 */

public class ChartActivity extends AppCompatActivity {

    private ArrayList<BarEntry> entries;
    private String[] xAxisLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button

        // Parcelable is not working for PollItem, so we have to do this

        // Getting information to put into graph
        ArrayList<String> optionText = getIntent().getStringArrayListExtra("optionText");
        ArrayList<Integer> optionVotes = getIntent().getIntegerArrayListExtra("optionVotes");

        entries = new ArrayList<>();
        xAxisLabels = new String[optionText.size()];

        for (int i = 0; i < optionText.size(); i++ ){
            entries.add( i, new BarEntry( (float) i , (float) optionVotes.get(i) ));
            xAxisLabels[i] = optionText.get(i);
        }

        BarChart chart;

        // title
        TextView tv = findViewById(R.id.poll_title);
        tv.setText(getIntent().getStringExtra("title"));

        // Make chart
        chart = findViewById(R.id.results_chart);
        BarDataSet bds = new BarDataSet(entries, "Options");
        bds.setValueTextColor(R.color.pollyGray);
        bds.setValueFormatter(new ChartValueFormatter());
        bds.setColors(new int[]{R.color.colorPoll1, R.color.colorPoll2, R.color.colorPoll3, R.color.colorPoll4,
                R.color.pollyGray}, this);
        BarData data = new BarData(bds);
        chart.setData(data);

        // X Axis Customization
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(45f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(R.color.pollyGray);
        xAxis.setAxisLineColor(R.color.pollyGray);
        xAxis.setGridColor(R.color.pollyGray);

        // Y Axis Customization
        chart.getAxisRight().setEnabled(false); // no right side axis
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setGranularity(1f);
        yAxis.setAxisLineColor(R.color.pollyGray);
        yAxis.setGridColor(R.color.pollyGray);
        yAxis.setTextColor(R.color.pollyGray);
        yAxis.setAxisMinValue(0f);

        // Legend
        chart.getLegend().setEnabled(false);

        // Random other things
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
        chart.setTouchEnabled(false);

        // Animations
        chart.animateY(1000, Linear);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
