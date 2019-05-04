package com.petterp.Shengsai;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.petterp.guosai.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * auther: Petterp on 2019/4/17
 * Summary:柱状图的点击隐藏标签
 *
 */
public class RoadActivity extends BaseActivity {
    private BarChart chart;
    //数据集
    private List<float[]> sums = new ArrayList<>();
    //BarChart数组集
    private List<List<BarEntry>> lists = new ArrayList<>();
    private List<BarDataSet> sets = new ArrayList<>();
    //标记位
    private boolean[] modes = new boolean[7];
    private int[] colors = {Color.parseColor("#C23531"),
            Color.parseColor("#2F4554"), Color.parseColor("#61A0A8"),
            Color.parseColor("#D48265"), Color.parseColor("#91C7AE"),
            Color.parseColor("#749F83"), Color.parseColor("#CA8622")
    };
    private DecimalFormat decimalFormat = new DecimalFormat("#");
    private OkhttpRoad okhttp;
    //线程池
    private ExecutorService fool;
    private CountDownTimer count;
    private static final int SIZE = 7;
    private ProgressDialog progressDialog;

    @Override
    protected void initializeWithState(Bundle savedInstanceState) {

    }

    @Override
    protected String getLayoutTitle() {
        return "路况分析";
    }

    @Override
    protected void onAfter() {
        finish();
    }

    /**
     *数据的初始化工作
     */
    @Override
    protected void initData() {
        okhttp = new OkhttpRoad(this);
        for (int i = 0; i < SIZE; i++) {
            sums.add(new float[SIZE]);
        }
        fool = Executors.newFixedThreadPool(SIZE);
        count = new CountDownTimer(Integer.MAX_VALUE, 5000) {
            @Override
            public void onTick(long l) {
                initPost();
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }

    /**
     * 网络请求方法，内部采用线程池进行处理
     */
    private void initPost() {
        for (int i = 0; i < SIZE; i++) {
            int finalI = i;
            fool.execute(() -> {
                for (int j = 0; j < SIZE; j++) {
                    setPost(j, sums.get(finalI));
                    if (finalI == 6 && j == 6) {
                        runOnUiThread(() -> {
                            setData();
                            progressDialog.dismiss();
                        });
                    }
                }
            });
        }
    }

    @Override
    protected void initView() {

        chart = findViewById(R.id.chart);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在请求数据");
        progressDialog.setTitle("Loading");
        progressDialog.show();
        initChart();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.road_activity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.road_1:
                setMode(0);
                break;
            case R.id.road_2:
                setMode(1);
                break;
            case R.id.road_3:
                setMode(2);
                break;
            case R.id.road_4:
                setMode(3);
                break;
            case R.id.road_5:
                setMode(4);
                break;
            case R.id.road_6:
                setMode(5);
                break;
            case R.id.road_7:
                setMode(6);
                break;
            default:
                break;
        }
    }

    /**
     * 网络请求组件
     * @param i
     * @param data
     */
    private void setPost(int i, float[] data) {
        okhttp.setOkHttpClient("action/GetRoadStatus.do", "{\"RoadId\":" + (i + 1) + ",\"UserName\":\"user1\"}", new OkhttpRoad.Post() {
            @Override
            public void on(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("ERRMSG").equals("成功")) {
                        data[i] = jsonObject.getInt("Status");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 初始化块，需要注意自定义标签时的设置
     */
    private void initChart() {
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(25f);
        xAxis.setAxisMaximum(7f);
        xAxis.setAxisMinimum(0f);
        xAxis.setLabelCount(7);
        xAxis.setAxisLineWidth(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(true);
        String[] setX = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        xAxis.setValueFormatter((v, axisBase) -> setX[(int) Math.abs(v % 7)]);

        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0f);
        left.setTextSize(25f);
        left.setAxisMaximum(5f);
        left.setLabelCount(5);
        left.setAxisLineWidth(1f);
        String[] setleft = {"畅通", "缓行", "一般拥堵", "中度拥堵", "严重拥堵"};
        left.setValueFormatter((v, axisBase) -> v > 4 ? "" : setleft[(int) v]);
        YAxis right = chart.getAxisRight();
        right.setLabelCount(5);
        right.setTextSize(25f);
        right.setAxisMaximum(5f);
        right.setAxisMinimum(0f);
        right.setValueFormatter((v, axisBase) -> decimalFormat.format(v));
        right.setDrawGridLines(false);

        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraBottomOffset(20);
    }

    /**
     *设置数据，需要注意for循环语句
     * 尽可能优化代码，减少冗余
     */
    private void setData() {
        lists.clear();
        sets.clear();
        rinse();
        for (int i = 0; i < SIZE; i++) {
            lists.add(new ArrayList<>());
            for (int j = 0; j < SIZE; j++) {
                lists.get(i).add(new BarEntry(i, sums.get(i)[j]));
            }
            sets.add(new BarDataSet(lists.get(i), ""));
        }
        //设置颜色
        setColor();
        BarData data = new BarData(sets.get(0), sets.get(1), sets.get(2), sets.get(3), sets.get(4), sets.get(5), sets.get(6));
        data.setDrawValues(false);
        //BarChart分组显示设置，规则为 ->
        // 每一组的长度为1f, (组内柱子的个数*柱子之间的距离) + 每组之间的距离=1f
        data.setBarWidth((1 - 0.3f - 0.14f) / 7);
        //参数分别为，起始点，组间距离，柱间距离
        data.groupBars(0, 0.3f, 0.02f);
        chart.setData(data);
        chart.invalidate();
    }


    /**
     * 设置颜色
     */
    private void setColor() {
        for (int i = 0; i < SIZE; i++) {
                sets.get(i).setColor(colors[i]);
        }
    }

    /**
     * 关闭定时器，关闭线程池
     * 关闭线程池有两种方法
     * shutdownNow()  禁止再加入任务，并关闭正在工作线程
     * shutdown()     禁止再加入任务，并等待工作线程执行结束
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (count != null) {
                count.cancel();
                count = null;
            }
            if (fool != null) {
                fool.shutdownNow();
                fool = null;
            }
        } catch (Exception ignored) {

        }
    }


    /**
     * 数据清洗，通过标记位来卡
     * 需要注意的是，每个i所代表的就是一组数据的第一个柱子
     * 这里也做了数据的 妥协处理 ，如果数据为null,则将数据置为1，避免空数据
     */
    private void rinse() {
        for (int i = 0; i < SIZE; i++) {
            if (modes[i]) {
                for (int j = 0; j < SIZE; j++) {
                    sums.get(i)[j] = 0;
                }
            } else {
                for (int j = 0; j < SIZE; j++) {
                    if (sums.get(i)[j] == 0) {
                        sums.get(i)[j] = 1;
                    }
                }
            }
        }
    }

    /**
     * 标记位的设置
     * @param postion
     */
    private void setMode(int postion) {
        if (modes[postion]) {
            modes[postion] = false;
        } else {
            modes[postion] = true;
        }
        setData();
    }
}
