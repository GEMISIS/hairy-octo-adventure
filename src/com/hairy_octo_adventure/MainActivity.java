package com.hairy_octo_adventure;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private TextView current_accel;

	private LinearLayout main_layout;
	private GraphView accelGraph;
	private GraphViewSeries accelSeriesX, accelSeriesY, accelSeriesZ;

	private SensorManager sensorManager;
	private Sensor accel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		current_accel = (TextView) findViewById(R.id.current_accel);

		accelGraph = new LineGraphView(this, "Z Accel");
		accelGraph.setScrollable(true);
		accelGraph.setViewPort(1, 8);
		accelGraph.setManualYAxisBounds(-2.0, 2.0);

		main_layout = (LinearLayout) findViewById(R.id.main_layout);
		main_layout.addView(accelGraph);

		accelSeriesX = new GraphViewSeries(
				new GraphViewData[] { new GraphViewData(0.0, 0.0) });
		accelSeriesX.getStyle().color = Color.RED;
		accelSeriesY = new GraphViewSeries(
				new GraphViewData[] { new GraphViewData(0.0, 0.0) });
		accelSeriesY.getStyle().color = Color.BLUE;
		accelSeriesZ = new GraphViewSeries(
				new GraphViewData[] { new GraphViewData(0.0, 0.0) });
		accelSeriesZ.getStyle().color = Color.GREEN;
		accelGraph.addSeries(accelSeriesX);
		accelGraph.addSeries(accelSeriesY);
		accelGraph.addSeries(accelSeriesZ);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		sensorManager.registerListener(this, accel,
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	double previous = 0.0;

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		double valueX = new BigDecimal(arg0.values[0]).setScale(0,
				RoundingMode.HALF_DOWN).doubleValue();
		double valueY = new BigDecimal(arg0.values[1]).setScale(0,
				RoundingMode.HALF_DOWN).doubleValue();
		double valueZ = new BigDecimal(arg0.values[2]).setScale(0,
				RoundingMode.HALF_DOWN).doubleValue();
		current_accel.setText("Current Accel: " + valueX + ", " + valueY + ", "
				+ valueZ);
		accelSeriesX.appendData(new GraphViewData(previous, valueX), true, 10);
		accelSeriesY.appendData(new GraphViewData(previous, valueY), true, 10);
		accelSeriesZ.appendData(new GraphViewData(previous, valueZ), true, 10);
		previous += 1;

	}
}
