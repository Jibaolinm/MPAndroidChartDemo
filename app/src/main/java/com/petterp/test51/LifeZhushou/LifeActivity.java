package com.petterp.test51.LifeZhushou;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.petterp.test51.Post;
import com.petterp.test51.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LifeActivity extends AppCompatActivity {
    private LineChart chart;
    private List<Fragment> list = new ArrayList<>();
    private TextView jintian;
    private TextView a;
    private TextView zi;
    private TextView b;
    private TextView gan;
    private TextView c;
    private TextView chuan;
    private TextView d;
    private TextView yundong;
    private TextView e;
    private TextView kong;
    private TextView jin;
    private ViewPager viewpager;
    private CountDownTimer count;
    private Random random = new Random();
    private LifeAirIteFragment airIteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);
        initView();
        infoChart();

        list.add(airIteFragment=new LifeAirIteFragment());
        LifeAdapter adapter=new LifeAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);

        count = new CountDownTimer(Integer.MAX_VALUE, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setLifeZhishu();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }


    private void initView() {
        jintian = (TextView) findViewById(R.id.jintian);
        a = (TextView) findViewById(R.id.a);
        zi = (TextView) findViewById(R.id.zi);
        b = (TextView) findViewById(R.id.b);
        gan = (TextView) findViewById(R.id.gan);
        c = (TextView) findViewById(R.id.c);
        chuan = (TextView) findViewById(R.id.chuan);
        d = (TextView) findViewById(R.id.d);
        yundong = (TextView) findViewById(R.id.yundong);
        e = (TextView) findViewById(R.id.e);
        kong = (TextView) findViewById(R.id.kong);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        jin = findViewById(R.id.jin);
        chart = findViewById(R.id.chart);
        findViewById(R.id.refresh).setOnClickListener(v -> infoChart());
    }


    private void infoChart() {
        List<Entry> max = new ArrayList<>();
        List<Entry> min = new ArrayList<>();
        Post.budler().setPost("http://192.168.1.105:8088/transportservice/action/GetWeather.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @SuppressLint("SetTextI18n")
            @Override
            public void Ok(String res) {
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    if ("成功".equals(jsonObject.getString("ERRMSG"))) {
                        jintian.setText(jsonObject.getString("WCurrent"));
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("ROWS_DETAIL"));
                        int size = jsonArray.length();
                        for (int i = 0; i < size; i++) {
                            String data = jsonArray.getJSONObject(i).getString("temperature");
                            String[] mode = data.split("~");
                            max.add(new Entry(i, Integer.parseInt(mode[1])));
                            min.add(new Entry(i, Integer.parseInt(mode[0])));
                        }
                        jin.setText("今天:" + min.get(1).getY() + "-" + max.get(1).getY());
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                XAxis xAxis = chart.getXAxis();
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                chart.setExtraTopOffset(10);
                xAxis.setTextSize(15);
                xAxis.setAxisMaximum(5f);
                xAxis.setLabelCount(5);
                String[] setXs = {"昨天", "今天", "明天", "周五", "周六", "周日"};
                xAxis.setValueFormatter((v, axisBase) -> setXs[(int) v]);
                chart.getLegend().setEnabled(false);
                chart.getAxisLeft().setDrawLabels(false);
                chart.getAxisLeft().setDrawAxisLine(false);
                chart.getDescription().setEnabled(false);
                chart.getAxisRight().setEnabled(false);
                LineDataSet set = new LineDataSet(max, "");
                LineDataSet set2 = new LineDataSet(min, "");
                set.setColor(Color.parseColor("#C23531"));
                set2.setColor(Color.parseColor("#61A0A8"));
                set.setDrawCircleHole(false);
                set2.setDrawCircleHole(false);
                set.setCircleRadius(3f);
                set2.setCircleRadius(3f);
                set.setCircleColor(Color.parseColor("#C23531"));
                set2.setCircleColor(Color.parseColor("#61A0A8"));
                LineData data = new LineData(set, set2);
                data.setValueTextSize(12f);
                chart.setData(data);
                chart.invalidate();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setLifeZhishu() {
        int mzi = random.nextInt(100);
        int mcold = random.nextInt(500);
        int mclad = random.nextInt(100);
        int msports = random.nextInt(100);
        int mair = random.nextInt(100);
        a.setText("中等(" + mzi + ")");
        b.setText("中等(" + mcold + ")");
        c.setText("中等(" + mclad + ")");
        d.setText("中等(" + msports + ")");
        e.setText("中等(" + mair + ")");
        setIntegral();
    }

    private void setIntegral() {
        Post.budler().setPost("http://192.168.1.105:8088/transportservice/action/GetAllSense.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                try {
                    Log.e("Demo",res);
                    JSONObject jsonObject = new JSONObject(res);
                    if ("成功".equals(jsonObject.getString("ERRMSG"))) {
                        airIteFragment.setData(jsonObject.getInt("pm2.5"));
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private class LifeAdapter extends FragmentStatePagerAdapter {
        public LifeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


}
