package com.google.appinventor.components.runtime;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.google.appinventor.components.runtime.util.YailList;

import java.util.ArrayList;

public class ScatterChartDataModel extends PointChartDataModel<ScatterDataSet, ScatterData> {
  /**
   * Initializes a new ScatterChartDataModel object instance.
   *
   * @param data Chart data instance
   */
  public ScatterChartDataModel(ScatterData data) {
    super(data);
    dataset = new ScatterDataSet(new ArrayList<Entry>(), "");
    this.data.addDataSet(dataset); // Safe add
    setDefaultStylingProperties();
  }

  @Override
  public void addEntryFromTuple(YailList tuple) {
    // Construct an entry from the prvoided tuple
    Entry entry = getEntryFromTuple(tuple);

    // If entry constructed successfully, add it to the Data Series
    if (entry != null) {
      entries.add(entry);
    }
  }

  @Override
  protected void setDefaultStylingProperties() {
    getDataset().setScatterShape(ScatterChart.ScatterShape.CIRCLE);
  }
}
