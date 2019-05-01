package cecs343.pollio;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class ChartValueFormatter extends ValueFormatter {

    public ChartValueFormatter() {

    }

    @Override
    public String getFormattedValue(float value) {
        return "" + ((int) value);
    }

}
