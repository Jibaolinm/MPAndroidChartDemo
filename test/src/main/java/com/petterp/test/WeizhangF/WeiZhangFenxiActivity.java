package com.petterp.test.WeizhangF;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import com.petterp.test.Post;
import com.petterp.test.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class WeiZhangFenxiActivity extends AppCompatActivity implements View.OnClickListener {
    private Button huanjingRili;
    private LineChart chart;
    private String timeFrom = "2016-5-1";
    private String timeTo = "2017-1-1";
    private long dateFrom;
    private long dateto;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private TextView textFrom;
    private TextView textTo;
    private List<String> beanlistBF;
    private List<String> beanlist;
    private List<Long> beantime;
    private int size;
    private HashMap<String, Integer> map = new HashMap(7);
    private int sizebf;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_zhang_fenxi);
        initView();
        initChart();
        initData();
        setPost();
    }

    private void initView() {
        huanjingRili = (Button) findViewById(R.id.huanjing_rili);
        chart = (LineChart) findViewById(R.id.chart);
        huanjingRili.setOnClickListener(this);
        findViewById(R.id.time_from).setOnClickListener(this);
        findViewById(R.id.time_to).setOnClickListener(this);
        huanjingRili.setOnClickListener(this);
        textFrom = (TextView) findViewById(R.id.text_from);
        textFrom.setOnClickListener(this);
        textTo = (TextView) findViewById(R.id.text_to);
        textTo.setOnClickListener(this);
    }

    private void initData() {
        try {
            dateFrom = simpleDateFormat.parse(timeFrom).getTime();
            dateto = simpleDateFormat.parse(timeTo).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("正在请求数据");
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.huanjing_rili:
                new Dia2(this).show();
                break;
            case R.id.time_from:
                new Dia1(this, 1).show();
                break;
            case R.id.time_to:
                new Dia1(this, 2).show();
                break;
        }
    }


    private class Dia1 extends Dialog {
        private int postion;
        private TextView dateFromBack;
        private TextView dateFromOk;
        private DatePicker date;

        public Dia1(Context context, int postion) {
            super(context);
            this.postion = postion;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.huanjing_dia_1);
            dateFromBack = findViewById(R.id.date_from_back);
            dateFromOk = findViewById(R.id.date_from_ok);
            date = findViewById(R.id.date_from);
            String[] data;
            if (postion == 1) {
                data = timeFrom.split("-");
            } else {
                data = timeTo.split("-");
            }
            date.init(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                }
            });
            dateFromBack.setOnClickListener(v -> dismiss());
            dateFromOk.setOnClickListener(v -> {
                if (postion == 1) {
                    timeFrom = date.getYear() + "-" + date.getMonth() + "-" + date.getDayOfMonth();
                    try {
                        dateFrom = simpleDateFormat.parse(timeFrom).getTime();
                        textFrom.setText(timeFrom);
                        setTime();
                        dismiss();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    timeTo = date.getYear() + "-" + date.getMonth() + "-" + date.getDayOfMonth();
                    try {
                        dateto = simpleDateFormat.parse(timeTo).getTime();
                        textTo.setText(timeTo);
                        setTime();
                        dismiss();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private class Dia2 extends Dialog {

        private DatePicker date;
        private TextView dateToBack;
        private TextView dateToOk;

        public Dia2(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.huanjing_dia_2);
            date = findViewById(R.id.date_to);
            dateToBack = findViewById(R.id.date_to_back);
            dateToOk = findViewById(R.id.date_to_ok);
            String[] data = timeFrom.split("-");
            date.init(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                }
            });
            dateToBack.setOnClickListener(v -> dismiss());
            dateToOk.setOnClickListener(v -> {
                timeTo = date.getYear() + "-" + date.getMonth() + "-" + date.getDayOfMonth();
                try {
                    dateto = simpleDateFormat.parse(timeTo).getTime();
                    textFrom.setText(timeTo);
                    setTime();
                    dismiss();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private void setPost() {
        Post.budler().setPost("http://192.168.1.101:8088/transportservice/action/GetAllCarPeccancy.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                try {
                    Gson gson = new Gson();
                    WeiBean bean = gson.fromJson(res, WeiBean.class);
                    size = bean.getROWS_DETAIL().size();
                    beanlist = new ArrayList<>();
                    beantime = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        beanlist.add(bean.getROWS_DETAIL().get(i).getPaddr());
                        beantime.add(simpleDateFormat.parse(bean.getROWS_DETAIL().get(i).getPdatetime()).getTime());
                    }
                    beanlistBF = new ArrayList<>();
                    beanlistBF.addAll(beanlist);
                    HashSet set = new HashSet(beanlist);
                    sizebf = set.size();
                    beanlist.clear();
                    beanlist.addAll(set);
                    try {
                        setTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initChart() {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        String[] setxs = {"学院路", "联想路", "医院路", "幸福路", "环城快速路", "环城高速"};
        xAxis.setLabelCount(6, true);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter((v, axisBase) -> v > 5 ? "" : setxs[(int) v]);
        xAxis.setTextSize(20f);
        xAxis.setAxisLineColor(Color.parseColor("#61A0A8"));
        xAxis.setTextColor(Color.parseColor("#61A0A8"));
        xAxis.setAxisLineWidth(2f);
        chart.getAxisLeft().setXOffset(45);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.setExtraOffsets(10, 0, 10, 10);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setTextSize(20f);
        chart.getAxisLeft().enableGridDashedLine(10f, 10f, 0);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
    }

    private void setTime() throws ParseException {
        for (int i = 0; i < sizebf; i++) {
            map.put(beanlist.get(i), 0);
            for (int j = 0; j < size; j++) {
                if (beanlist.get(i).equals(beanlistBF.get(j))) {
                    long date = beantime.get(j);
                    if (date >= dateFrom && date <= dateto) {
                        int mode = map.get(beanlist.get(i));
                        map.put(beanlist.get(i), ++mode);
                    }
                }
            }
        }
        setData();
    }

    private void setData() {
        Log.e("Demo", map.get(beanlist.get(0)) + "");
        List<Entry> list = new ArrayList<>();
        list.add(new Entry(0, map.get("学院路")));
        list.add(new Entry(1, map.get("联想路")));
        list.add(new Entry(2, map.get("医院路")));
        list.add(new Entry(3, map.get("幸福路")));
        list.add(new Entry(4, map.get("环城快速路")));
        list.add(new Entry(5, map.get("环城高速")));
        LineDataSet set = new LineDataSet(list, "");
        set.setColor(Color.RED);
        set.setValueTextSize(20f);
        set.setValueTextColor(Color.RED);
        set.setDrawCircleHole(false);
        set.setCircleRadius(8f);
        set.setLineWidth(3f);
        LineData data = new LineData(set);
        chart.setData(data);
        chart.invalidate();
    }

}
