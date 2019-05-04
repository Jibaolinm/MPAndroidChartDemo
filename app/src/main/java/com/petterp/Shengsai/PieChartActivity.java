package com.petterp.Shengsai;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.bean.Frits;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * auther: Petterp on 2019/4/17
 * Summary:饼状图的点击隐藏
 */
public class PieChartActivity extends BaseActivity {
    private TextView timeShishi;
    private TextView updateTime;
    private PieChart chart;
    private TextView envirTitle;
    private TextView envirScale;
    private TextView envirMax;
    private TextView envirMin;
    private List<Integer> co = new ArrayList<>();
    private List<Integer> guang = new ArrayList<>();
    private List<Integer> shi = new ArrayList<>();
    private List<Integer> wen = new ArrayList<>();
    private List<Integer> pm = new ArrayList<>();
    private Frits[] frits = new Frits[5];
    private int[] modes = new int[5];
    private boolean[] scale = new boolean[5];
    //颜色集合
    private int[] colors = {Color.parseColor("#91C7AE"), Color.parseColor("#D48265"), Color.parseColor("#61A0A8")
            , Color.parseColor("#2F4554"), Color.parseColor("#C23531")};
    private DecimalFormat decimalFormat = new DecimalFormat("##");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private static final int SIZE = 5;
    private OkHttpGet okhttp;
    private ProgressDialog progressDialog;

    @Override
    protected void initializeWithState(Bundle savedInstanceState) {

    }

    @Override
    protected String getLayoutTitle() {
        return "环境监测";
    }

    @Override
    protected void onAfter() {
        finish();
    }

    /**
     * 数据的处理，第一次需设置默认显示的小框
     * 注意 妥协算法 的使用
     */
    @Override
    protected void initData() {
        okhttp = OkHttpGet.builder().setContext(this);
        Log.e("demo", "开始");
        okhttp.setGet("10000", res -> {
            try {
                JSONArray jsonArray = new JSONArray(res);
                final int size = jsonArray.length();
                for (int i = 0; i < size; i++) {
                    setShuju(pm, 0, jsonArray.getJSONObject(i).getInt("pm"), 200);
                    setShuju(co, 1, jsonArray.getJSONObject(i).getInt("co"), 2000);
                    setShuju(guang, 2, jsonArray.getJSONObject(i).getInt("guangzhao"), 2000);
                    setShuju(shi, 3, jsonArray.getJSONObject(i).getInt("shidu"), 200);
                    setShuju(wen, 4, jsonArray.getJSONObject(i).getInt("temp"), 40);
                }
                setScales(0, pm);
                setScales(1, co);
                setScales(2, guang);
                setScales(3, shi);
                setScales(4, wen);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData();
                        progressDialog.dismiss();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        setTime();
    }

    @Override
    protected void initView() {
        findViewById(R.id.pm).setOnClickListener(this);
        findViewById(R.id.co).setOnClickListener(this);
        findViewById(R.id.guang).setOnClickListener(this);
        findViewById(R.id.shi).setOnClickListener(this);
        findViewById(R.id.wen).setOnClickListener(this);
        chart = findViewById(R.id.chart);
        timeShishi = findViewById(R.id.time_shishi);
        updateTime = findViewById(R.id.update_time);
        envirMax = findViewById(R.id.envir_max);
        envirMin = findViewById(R.id.envir_min);
        envirTitle = findViewById(R.id.envir_title);
        envirScale = findViewById(R.id.envir_scale);
        chart.setDrawEntryLabels(false);
        chart.setDrawHoleEnabled(false);
        chart.setExtraOffsets(15, 15, 15, 15);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                Toast.makeText(PieChartActivity.this, entry.getY() + "", Toast.LENGTH_SHORT).show();
                if (entry.getY() == modes[0]) {
                    setTitle(0, "PM2.5");
                } else if (entry.getY() == modes[1]) {
                    setTitle(1, "Co2");
                } else if (entry.getY() == modes[2]) {
                    setTitle(2, "光照");
                } else if (entry.getY() == modes[3]) {
                    setTitle(3, "湿度");
                } else {
                    setTitle(4, "温度");
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在请求");
        progressDialog.setTitle("Loading");
        progressDialog.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.pie_activity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pm:
                setMode(0);
                break;
            case R.id.co:
                setMode(1);
                break;
            case R.id.guang:
                setMode(2);
                break;
            case R.id.shi:
                setMode(3);
                break;
            case R.id.wen:
                setMode(4);
                break;
            default:
                break;

        }
    }

    /**
     * 设置数据，需注意if语句的逻辑
     * 如果未选中，则存数据，然后注意颜色，标签的存储即可。
     */
    private void setData() {
        int sum = 0;
        List<PieEntry> list = new ArrayList<>();
        List<Integer> color = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            if (!scale[i]) {
                sum += modes[i];
                list.add(new PieEntry(modes[i]));
                color.add(colors[i]);
            } else {
                envirTitle.setText("XXX");
                envirScale.setText("xx%");
                envirMax.setText("xxx");
                envirMin.setText("xxx");
            }
        }
        for (int i = 0; i < SIZE; i++) {
            //数字格式化确定比例，保证数组没有百分比
            frits[i].scale = decimalFormat.format((modes[i] * 1.0 / sum) * 100) + "%";
        }

        PieDataSet set = new PieDataSet(list, "");
        set.setValueLinePart1OffsetPercentage(100f);
        set.setValueLinePart1Length(0.8f);
        set.setValueLinePart2Length(0.1f);
        set.setColors(color);
        set.setValueTextColors(color);
        set.setValueFormatter((v, entry, i, viewPortHandler) -> {
            if (v == modes[0]) {
                return "PM2.5";
            } else if (v == modes[1]) {
                return "Co2";
            } else if (v == modes[2]) {
                return "光照强度";
            } else if (v == modes[3]) {
                return "湿度";
            } else {
                return "温度";
            }
        });
        set.setValueTextSize(25f);
        set.setValueLineWidth(1f);
        set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(set);
        chart.setData(data);
        chart.invalidate();
    }

    /**
     * 设置小框里的内容
     *
     * @param i
     * @param title
     */
    @SuppressLint("SetTextI18n")
    private void setTitle(int i, String title) {
        envirTitle.setText(title);
        envirScale.setText(frits[i].scale);
        envirMax.setText("" + frits[i].max);
        envirMin.setText("" + frits[i].min);
    }


    /**
     * 计算时间，逻辑采用sp存储上一次时间，使用最新时间-sp时间，然后存储最新时间。
     * 时间的计算，毫秒/1000，大于60单独设置，否则 最新数据
     */
    @SuppressLint("SetTextI18n")
    private void setTime() {
        SharedPreferences.Editor shar = getSharedPreferences("data", MODE_PRIVATE).edit();
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        long time = System.currentTimeMillis();
        timeShishi.setText(simpleDateFormat.format(time));
        long timeSp = data.getLong("time", time);
        shar.putLong("time", time).apply();
        int scale = (int) ((time - timeSp) / 1000);
        if (scale > 60) {
            updateTime.setText("最近更新：" + (scale / 60) + "分钟前");
        } else {
            updateTime.setText("最近更新：最近数据");
        }
    }


    /**
     * 设置标记位，即是否选中了某个项
     *
     * @param i
     */
    private void setMode(int i) {
        if (scale[i]) {
            scale[i] = false;
        } else {
            scale[i] = true;
        }
        setData();
    }

    private void setShuju(List<Integer> list, int i, int sum, int max) {
        if (sum > max) {
            ++modes[i];
        }
        list.add(sum);
    }

    private void setScales(int i, List<Integer> list) {
        Collections.sort(list);
        Frits frits = new Frits();
        frits.min = list.get(0);
        frits.max = list.get(list.size() - 1);
        this.frits[i] = frits;
    }
}
