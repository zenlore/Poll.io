package cecs343.pollio;

import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * A class that formats the chart values (numbers on top of the bars) to be integers rather than floats
 * (MPAndroidChart makes it necessary to create a whole class just for this, apparently.)
 */

public class ChartValueFormatter extends ValueFormatter {

    public ChartValueFormatter() {

    }

    @Override
    public String getFormattedValue(float value) {
        return "" + ((int) value);
    }

}
