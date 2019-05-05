package com.petterp.guosai.GuosaiTest;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.petterp.guosai.Post;
import com.petterp.guosai.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Petterp on ${DATE}
 * Summary:天气信息  需要注意的是布局，使用相对来完成。
 * 邮箱：1509492795@qq.com
 */
public class WeatherActivity extends AppCompatActivity {
    private LineChart chart;
    private TextView mWendu;
    private TextView mWenduState;
    private ImageView mWenduRefresh;
    private TextView mWeatherNowYesterday;
    private TextView mWeatherDateYesterday;
    private TextView mWeatherNowToday;
    private TextView mWeatherDateToday;
    private TextView mWeatherNowTomorrow;
    private TextView mWeatherDateTomorrow;
    private TextView mWeatherNowTues;
    private TextView mWeatherDateTues;
    private TextView mWeatherNowWednesday;
    private TextView mWeatherDateWednesday;
    private TextView mWeatherNowThurs;
    private TextView mWeatherDateThurs;
    List<Entry> max = new ArrayList<>();
    List<Entry> min = new ArrayList<>();
    private ProgressDialog progressDialog;
    private  List<TextView> dates=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatch);
        chart = findViewById(R.id.chart);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("正在請求數據");
        progressDialog.show();
        initView();
        setPost();
    }

    private void setPost() {
        max.clear();
        min.clear();
        Post.budler().setPost("http://192.168.1.107:8088/transportservice/action/GetWeather.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                try {
                    Log.e("demo",res);
                    JSONObject jsonObject = new JSONObject(res);
                    if ("S".equals(jsonObject.getString("RESULT"))) {
                        mWendu.setText(jsonObject.getString("WCurrent"));
                        JSONArray jsonArray=new JSONArray(jsonObject.getString("ROWS_DETAIL"));
                        final int size=jsonArray.length();
                        for (int i=0;i<size;i++){
                            String[] data=jsonArray.getJSONObject(i).getString("temperature").split("~");
                            max.add(new Entry(i,Integer.parseInt(data[1])));
                            min.add(new Entry(i,Integer.parseInt(data[0])));
                            dates.get(i).setText(jsonArray.getJSONObject(i).getString("WData"));
                        }
                        setData();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        mWendu = (TextView) findViewById(R.id.wendu);
        mWenduState = (TextView) findViewById(R.id.wendu_state);
        mWenduRefresh = (ImageView) findViewById(R.id.wendu_refresh);
        mWeatherNowYesterday = (TextView) findViewById(R.id.weather_now_yesterday);
        mWeatherDateYesterday = (TextView) findViewById(R.id.weather_date_yesterday);
        mWeatherNowToday = (TextView) findViewById(R.id.weather_now_today);
        mWeatherDateToday = (TextView) findViewById(R.id.weather_date_today);
        mWeatherNowTomorrow = (TextView) findViewById(R.id.weather_now_tomorrow);
        mWeatherDateTomorrow = (TextView) findViewById(R.id.weather_date_tomorrow);
        mWeatherNowTues = (TextView) findViewById(R.id.weather_now_tues);
        mWeatherDateTues = (TextView) findViewById(R.id.weather_date_tues);
        mWeatherNowWednesday = (TextView) findViewById(R.id.weather_now_wednesday);
        mWeatherDateWednesday = (TextView) findViewById(R.id.weather_date_wednesday);
        mWeatherNowThurs = (TextView) findViewById(R.id.weather_now_thurs);
        mWeatherDateThurs = (TextView) findViewById(R.id.weather_date_thurs);
        dates.add(mWeatherDateYesterday);
        dates.add(mWeatherDateToday);
        dates.add(mWeatherDateTomorrow);
        dates.add(mWeatherDateTues);
        dates.add(mWeatherDateWednesday);
        dates.add(mWeatherDateThurs);
        chart.getXAxis().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        findViewById(R.id.wendu_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
             setPost();
            }
        });
    }

    private void setData() {
        LineDataSet setMax = new LineDataSet(max, "");
        LineDataSet setMin = new LineDataSet(min, "");
        setMax.setColor(Color.BLUE);
        setMin.setColor(Color.GREEN);
        setMax.setLineWidth(4f);
        setMin.setLineWidth(4f);
        setMax.setDrawCircleHole(false);
        setMin.setDrawCircleHole(false);
        setMax.setCircleRadius(8f);
        setMin.setCircleRadius(8f);
        LineData data = new LineData(setMax, setMin);
        data.setValueTextSize(15f);
        chart.setData(data);
        chart.invalidate();
    }
}
