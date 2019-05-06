package com.petterp.guosai.GuosaiTest.WeiZhang;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.petterp.guosai.Bean.AllcarWeizhang;
import com.petterp.guosai.Post;
import com.petterp.guosai.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 违章分析
 *  涉及数据的处理，及DatePicker控件的使用
 * @author Pettepr
 */
public class WeiZhangActivity extends AppCompatActivity implements View.OnClickListener {
    private LineChart chart;
    /**
     * 用于存储相应的数据合集
     */
    private Map<String, Integer> map = new HashMap<>(7);
    //存储数据集合，-> 违章地点，时间
    private List<Frits> mbflist;
    //违章地点
    private List<String> mlist;
    //时间显示
    private TextView timefrom, timeto;
    //时间戳，用来比较时间
    private long datefrom;
    private long dateto;
    //需要显示的时间
    private String time1 = "2016-5-1";
    private String time2 = "2017-1-1";

    //时间转化
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ProgressDialog progressDialog;
    //Label 标签的数据组
    private String[] res = {"学院路", "联想路", "医院路", "幸福路", "环城快速路", "环城高速"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_zhang);
        //初始化布局等View
        initView();
        //初始化Linchart
        initChart();
        //进行数据处理
        initData();
    }

    @SuppressLint({"WrongViewCast", "CutPasteId", "NewApi"})
    private void initView() {
        chart = findViewById(R.id.chart);
        timefrom = findViewById(R.id.time_from);
        timeto = findViewById(R.id.time_to);
        findViewById(R.id.weizhang_rili).setOnClickListener(this);
        findViewById(R.id.date_from).setOnClickListener(this);
        findViewById(R.id.date_to).setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    private void initData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在请求数据");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        try {
            datefrom = simpleDateFormat.parse("2016-5-1").getTime();
            dateto = simpleDateFormat.parse("2017-1-1").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setPost();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weizhang_rili:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    String[] data = time1.split("-");
                    new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                        time1 = year + "-" + (month + 1) + "-" + dayOfMonth;
                        timefrom.setText(time1);
                        try {
                            datefrom = simpleDateFormat.parse(time1).getTime();
                            //清洗数据
                            rinse();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }, Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])).show();
                } else {
                    new Dia2(this).show();
                }
                break;
            case R.id.date_from:
                new Dia(this, 1).show();
                break;
            case R.id.date_to:
                new Dia(this, 2).show();
                break;
            default:
                break;
        }
    }


    /**
     * 网络处理，这里对数据进行清洗，并调用 rinse()方法决定需要显示的数据
     */
    private void setPost() {
        Post.budler().setPost("http://192.168.1.107:8088/transportservice/action/GetAllCarPeccancy.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                Gson gson = new Gson();
                AllcarWeizhang bean = gson.fromJson(res, AllcarWeizhang.class);
                int size = bean.getROWS_DETAIL().size();
                mlist = new ArrayList<>();
                mbflist = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    Frits frits = new Frits();
                    try {
                        frits.date = simpleDateFormat.parse(bean.getROWS_DETAIL().get(i).getPdatetime()).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    frits.name = bean.getROWS_DETAIL().get(i).getPaddr();
                    mbflist.add(frits);
                    mlist.add(bean.getROWS_DETAIL().get(i).getPaddr());
                }

                //数据清洗
                HashSet set = new HashSet(mlist);
                mlist.clear();
                mlist.addAll(set);

                //存入map
                rinse();
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 数据清洗，与时间戳相关
     * for
     *    for循环在嵌套的时候，先储存map,然后判断时间戳 -> equals判断地名
     *      ->取出相应的map_value ，进行重新++ put储存
     *      最后调用setData 设置数据
     */
    private void rinse() {
        int sizebf = mbflist.size();
        int size = mlist.size();
        for (int i = 0; i < size; i++) {
            map.put(mlist.get(i), 0);
            for (int j = 0; j < sizebf; j++) {
                long time = mbflist.get(j).date;
                if (time >= datefrom && time <= dateto) {
                    if (mlist.get(i).equals(mbflist.get(j).name)) {
                        int mode = map.get(mlist.get(i));
                        map.put(mlist.get(i), ++mode);
                    }
                }
            }
        }
        setData();
    }

    /**
     * 初始化MP
     * chart.getAxisLeft().setXOffset(45); 设置y轴偏移。
     */
    private void initChart() {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.parseColor("#75A1A6"));
        xAxis.setTextSize(20F);
        xAxis.setLabelCount(6, true);
        xAxis.setAxisLineWidth(3);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.parseColor("#75A1A6"));
        xAxis.setValueFormatter((v, axisBase) -> v > 5 ? "" : res[(int) v]);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setTextSize(20f);
        //设置y轴偏移
        chart.getAxisLeft().setXOffset(45);
        chart.getAxisLeft().setAxisMinimum(0f);
        //以虚线模式下画网格线
        chart.getAxisLeft().enableGridDashedLine(10f, 10f, 0f);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(0, 0, 50, 15);
    }

    /**
     * 设置MP
     */
    private void setData() {
        List<Entry> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(new Entry(i, map.get(res[i])));
        }
        LineDataSet set = new LineDataSet(list, "");
        set.setDrawCircleHole(false);
        set.setColor(Color.parseColor("#C63F46"));
        set.setCircleRadius(8f);
        set.setLineWidth(3f);
        set.setValueTextSize(20f);
        set.setCircleColor(Color.parseColor("#C63F46"));
        LineData data = new LineData(set);
        data.setValueTextColor(Color.parseColor("#C63F46"));
        chart.setData(data);
        chart.invalidate();
    }


    /**
     * 显示时间的Dialog
     * 注意时间的切割，每次刚显示dialog ,必须是显示的时间，所以需要和timefrom，timeto关联
     * 通过传递mode 来确定来处理那个显示
     * datePicker.init(....) 设置默认时间，及响应时间
     * datePicker.getYear()...  获取年份等，月份需要+1
     */
    private class Dia extends Dialog {
        private int mode;
        private DatePicker datePicker;
        private String[] data;

        public Dia(Context context, int mode) {
            super(context);
            this.mode = mode;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.weizhang_date);
            datePicker = findViewById(R.id.weizhang_datepicker);

            //判断是进时间还是出时间
            if (mode == 1) {
                data = time1.split("-");
            } else {
                data = time2.split("-");
            }
            datePicker.init(Integer.parseInt(data[0]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[2]), (view, year, monthOfYear, dayOfMonth) -> {
            });
            findViewById(R.id.btn_dia_back).setOnClickListener(v -> dismiss());
            findViewById(R.id.btn_dia_queding).setOnClickListener(v -> {
                try {
                    String time = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                    Long date = simpleDateFormat.parse(time).getTime();
                    if (mode == 1) {
                        datefrom = date;
                        time1 = time;
                        timefrom.setText(time1);
                    } else {
                        dateto = date;
                        time2 = time;
                        timeto.setText(time2);
                    }
                    rinse();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismiss();
            });
        }
    }


    /**
     * 显示日历的dialog
     * 这里需要注意在switch里选择时，不同Android 版本的处理，Android5.0以下，皆没有
     */
    private class Dia2 extends Dialog {
        private DatePicker datePicker;

        @SuppressLint("ResourceType")
        public Dia2(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.weizhang_rili);
            datePicker = findViewById(R.id.weizhang_rili);
            String[] data = time1.split("-");
            datePicker.init(Integer.parseInt(data[0]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[2]), (view, year, monthOfYear, dayOfMonth) -> {
            });
            findViewById(R.id.weizhang_rili_back).setOnClickListener(v -> dismiss());
            findViewById(R.id.weizhang_rili_ok).setOnClickListener(v -> {
                try {
                    time1 = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                    datefrom = simpleDateFormat.parse(time1).getTime();
                    timefrom.setText(time1);
                    rinse();
                    dismiss();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });

        }
    }
}
