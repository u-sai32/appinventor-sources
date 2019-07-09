package com.google.appinventor.components.runtime;

import com.github.mikephil.charting.data.LineData;

public class AreaChartDataModel extends LineChartDataModel {
    /**
     * Initializes a new AreaChartDataModel object instance.
     *
     * @param data Line Chart Data object instance
     */
    public AreaChartDataModel(LineData data) {
        super(data);
    }

    @Override
    public void setColor(int argb) {
        super.setColor(argb);
        dataset.setFillColor(argb);
    }

    @Override
    protected void setDefaultStylingProperties() {
        super.setDefaultStylingProperties();
        dataset.setDrawFilled(true);
        dataset.setFillAlpha(255);
    }
}
