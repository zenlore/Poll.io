package cecs343.pollio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import static com.github.mikephil.charting.animation.Easing.Linear;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // TODO: Fix weird parcelable problem
        // get the poll
        PollItem poll = getIntent().getExtras().getParcelable("poll");

        // Getting information to put into graph
        ArrayList<PollOption> pollOptions = poll.getOptions();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] xAxisLabels = new String[pollOptions.size()];

        for (int i = 0; i < pollOptions.size(); i++ ){
            entries.add( i, new BarEntry( (float) i , (float) pollOptions.get(i).getVotes() ));
            xAxisLabels[i] = pollOptions.get(i).getText();
        }

        BarChart chart;

        // title
        TextView tv = findViewById(R.id.poll_title);
        tv.setText(poll.getTitle());

        // Make chart
        chart = findViewById(R.id.results_chart);
        BarDataSet bds = new BarDataSet(entries, "Options");
        bds.setColors( new ArrayList<>( Arrays.asList(R.color.colorPoll1, R.color.colorPoll2,
                R.color.colorPoll3, R.color.colorPoll4, R.color.pollyGray) ) );
        BarData data = new BarData( bds );
        bds.setValueFormatter(new ChartValueFormatter());
        bds.setValueTextColor(R.color.pollyGray);
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

        // Legend
        chart.getLegend().setEnabled(false);

        // Random other things
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));

        // Animations
        chart.animateY(1000, Linear);

        // refresh
        chart.invalidate();

    }
}
