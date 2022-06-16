package com.example.demophone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.demophone.ServerThread;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {

    Button graphBtn;
    TextView helloWorld;
    static LineChart hrChart;
    static LineChart scChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        graphBtn = (Button) findViewById(R.id.graphBtn);
        helloWorld = (TextView) findViewById(R.id.helloWorld);

//        graphBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), realtimeChart.class);
//                startActivity(intent);
//            }
//        });

        helloWorld.setText(wifiIpAddress());
        ServerThread thread = new ServerThread();
        thread.start();

        hrChart = (LineChart) findViewById(R.id.hrChart);
        scChart = (LineChart) findViewById(R.id.stepChart);

        initChart(hrChart, "Heart-Rate");
        initChart(scChart, "Step-Count");


    }

    public String wifiIpAddress() {
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();

        } catch (UnknownHostException ex) {
            Log.e("WIFI", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }

    public void initChart(LineChart chart, String dataType){

        chart.setDrawGridBackground(true);
        chart.setBackgroundColor(Color.BLACK);
        chart.setGridBackgroundColor(Color.BLACK);

        // description text
        chart.getDescription().setEnabled(true);
        Description des = chart.getDescription();
        des.setEnabled(true);
        des.setText(dataType);
        des.setTextSize(15f);
        des.setTextColor(Color.WHITE);

        // touch gestures (false-비활성화)
        chart.setTouchEnabled(false);

        // scaling and dragging (false-비활성화)
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        //auto scale
        chart.setAutoScaleMinMaxEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        //X축
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setDrawAxisLine(false);

        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setDrawGridLines(false);

        //Legend
        Legend l = chart.getLegend();
        l.setEnabled(true);
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setTextSize(12f);
        l.setTextColor(Color.WHITE);

        //Y축
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(getResources().getColor(R.color.black));
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(getResources().getColor(R.color.black));
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);


        // don't forget to refresh the drawing
        chart.invalidate();
    }

    public static void addEntry(LineChart chart, double num) {
        LineData data = chart.getData();
//        Log.d("main", chart.toString());
        if (data == null) {
            data = new LineData();
            chart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }


        data.addEntry(new Entry((float)set.getEntryCount(), (float)num), 0);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        chart.notifyDataSetChanged();

        chart.setVisibleXRangeMaximum(150);
        // this automatically refreshes the chart (calls invalidate())
        chart.moveViewTo(data.getEntryCount(), 50f, YAxis.AxisDependency.LEFT);

    }

    private static LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Real-time data from watch");
        set.setLineWidth(1f);
        set.setDrawValues(false);
        set.setValueTextColor(R.color.white);
        set.setColor(R.color.white);
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawCircles(false);
        set.setHighLightColor(Color.rgb(190, 190, 190));

        return set;
    }

}