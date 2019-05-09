package com.petterp.guosai.GuosaiTest.HuanJingJiance;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.petterp.guosai.Post;
import com.petterp.guosai.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 使用Frits当做Bean类存储数据，使用List来切合数据
 */
public class HuanJingjianActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TextView time;
    private TextView timeUpdate;
    private PieChart chart;
    private TextView itemTitle;
    private TextView pmMax;
    private TextView pmMin;
    private TextView pmMean;
    private TextView coMax;
    private TextView coMin;
    private TextView coMean;
    private TextView guangMax;
    private TextView guangMin;
    private TextView guangMean;
    private TextView shiMax;
    private TextView shiMin;
    private TextView shiMean;
    private TextView wenMax;
    private TextView wenMin;
    private TextView wenMean;
    private List<Integer> colors = new ArrayList<>();
    private boolean mode = false;
    private List<Frits> modelist;
    private float a;
    private float b;
    private float c;
    private float d;
    private float e;
    private CountDownTimer count;
    private LinearLayout item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huan_jingjian);
        initView();
        initChart();
        setTime();
        count = new CountDownTimer(Integer.MAX_VALUE, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setPost();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void initView() {
        time = (TextView) findViewById(R.id.time);
        timeUpdate = (TextView) findViewById(R.id.time_update);
        chart = (PieChart) findViewById(R.id.chart);
        itemTitle = (TextView) findViewById(R.id.item_title);
        pmMax = (TextView) findViewById(R.id.pm_max);
        pmMin = (TextView) findViewById(R.id.pm_min);
        pmMean = (TextView) findViewById(R.id.pm_mean);
        coMax = (TextView) findViewById(R.id.co_max);
        coMin = (TextView) findViewById(R.id.co_min);
        coMean = (TextView) findViewById(R.id.co_mean);
        guangMax = (TextView) findViewById(R.id.guang_max);
        guangMin = (TextView) findViewById(R.id.guang_min);
        guangMean = (TextView) findViewById(R.id.guang_mean);
        shiMax = (TextView) findViewById(R.id.shi_max);
        shiMin = (TextView) findViewById(R.id.shi_min);
        shiMean = (TextView) findViewById(R.id.shi_mean);
        wenMax = (TextView) findViewById(R.id.wen_max);
        wenMin = (TextView) findViewById(R.id.wen_min);
        wenMean = (TextView) findViewById(R.id.wen_mean);
        item=findViewById(R.id.huanjing_item);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("正在请求数据");
        progressDialog.setTitle("Loading///");
        progressDialog.show();
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
    }

    /**
     * 网络请求，当i=4的时候刷新mp
     */
    private void setPost() {
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            Post.budler().setPost("http://192.168.1.101:8088/transportservice/action/GetAllSense.do", "{\"UserName\":\"user1\"}", res -> {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(res);
                    if ("S".equals(jsonObject.getString("RESULT"))) {
                        Frits frits = new Frits();
                        frits.co = jsonObject.getInt("co2");
                        frits.pm = jsonObject.getInt("pm2.5");
                        frits.guang = jsonObject.getInt("LightIntensity");
                        frits.shi = jsonObject.getInt("humidity");
                        frits.wen = jsonObject.getInt("temperature");
                        setSave(finalI, frits);
                        if (finalI == 4) {
                            setData();
                            if (mode){
                                Log.e("demo","执行");
                                setItem();
                            }
                            progressDialog.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 一些配置数据，颜色数组，mp的一些基本配置及扇形点击事件
     */
    private void initChart() {
        chart.setDrawHoleEnabled(false);
        chart.setDrawCenterText(false);
        colors.add(Color.parseColor("#91C7AE"));
        colors.add(Color.parseColor("#D48265"));
        colors.add(Color.parseColor("#61A0A8"));
        colors.add(Color.parseColor("#2F4554"));
        colors.add(Color.parseColor("#C23531"));
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                item.setVisibility(View.VISIBLE);
                mode=true;
                if (entry.getY() == a) {
                    modelist=UserBean.BEIJING;
                    itemTitle.setText("北京");
                    setItem();
                } else if (entry.getY() == b) {
                    modelist=UserBean.SHANGHAI;
                    itemTitle.setText("上海");
                    setItem();
                } else if (entry.getY() == c) {
                    modelist=UserBean.SHENGZHENG;
                    itemTitle.setText("深圳");
                    setItem();
                } else if (entry.getY() == d) {
                    modelist=UserBean.CHONGQING;
                    itemTitle.setText("重庆");
                    setItem();
                } else {
                    modelist=UserBean.XUNAN;
                    itemTitle.setText("雄安");
                    setItem();
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        chart.setExtraOffsets(15,15,15,15);
    }

    /**
     * 数据存储的时候，需要通过相应的下标确定给谁存
     * @param postion
     * @param frits
     */
    private void setSave(int postion, Frits frits) {
        switch (postion) {
            case 0:
                UserBean.BEIJING.add(frits);
                break;
            case 1:
                UserBean.SHENGZHENG.add(frits);
                break;
            case 2:
                UserBean.SHANGHAI.add(frits);
                break;
            case 3:
                UserBean.XUNAN.add(frits);
                break;
            case 4:
                UserBean.CHONGQING.add(frits);
                break;
            default:
                break;
        }
    }

    /**
     * 设置MP的数据
     */
    private void setData() {
        List<PieEntry> list = new ArrayList<>();
        int size = UserBean.BEIJING.size() - 1;
        a = (float) (UserBean.BEIJING.get(size).pm+0.01);
        b = (float) (UserBean.SHANGHAI.get(size).pm+0.02);
        c = (float) (UserBean.SHENGZHENG.get(size).pm+0.03);
        d = (float) (UserBean.CHONGQING.get(size).pm+0.04);
        e = (float) (UserBean.XUNAN.get(size).pm+0.05);
        list.add(new PieEntry(a));
        list.add(new PieEntry(b));
        list.add(new PieEntry(c));
        list.add(new PieEntry(d));
        list.add(new PieEntry(e));

        PieDataSet set = new PieDataSet(list, "");
        set.setValueFormatter((v, entry, i, viewPortHandler) -> {
            if (v == a) {
                return "北京";
            } else if (v == b) {
                return "上海";
            } else if (v == c) {
                return "深圳";
            } else if (v == d) {
                return "重庆";
            } else {
                return "雄安";
            }
        });
        set.setValueTextColors(colors);
        set.setColors(colors);
        set.setValueTextSize(15f);
        set.setValueLinePart1OffsetPercentage(100f);
        set.setValueLinePart1Length(0.7f);
        set.setValueLinePart2Length(0.2f);
        set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(set);
        data.setValueTextSize(15f);
        chart.setData(data);
        chart.invalidate();
    }

    /**
     * 设置子项Item
     * 排序比较，然后将数据显示在item里面
     */
    @SuppressLint("SetTextI18n")
    private void setItem() {
        int[] sums = new int[5];
        int[] modes = new int[5];
        int size = modelist.size();
        for (Frits frits : modelist) {
            sums[0] += frits.pm;
            sums[1] += frits.co;
            sums[2] += frits.guang;
            sums[3] += frits.shi;
            sums[4] += frits.wen;
        }
        for (int i = 0; i < 5; i++) {
            modes[i] = sums[i] / size;
        }
        Collections.sort(modelist, (o1, o2) -> Integer.compare(o2.pm, o1.pm));
        pmMax.setText(String.valueOf(modelist.get(0).pm));
        pmMin.setText(String.valueOf(modelist.get(size - 1).pm));
        pmMean.setText(String.valueOf(modes[0]));

        Collections.sort(modelist, (o1, o2) -> Integer.compare(o2.co, o1.co));
        coMax.setText(String.valueOf(modelist.get(0).co));
        coMin.setText(String.valueOf(modelist.get(size - 1).co));
        coMean.setText(String.valueOf(modes[1]));

        Collections.sort(modelist, (o1, o2) -> Integer.compare(o2.guang, o1.guang));
        guangMax.setText("" + modelist.get(0).guang);
        guangMin.setText("" + modelist.get(size - 1).guang);
        guangMean.setText("" + modes[2]);

        Collections.sort(modelist, (o1, o2) -> Integer.compare(o2.shi, o1.shi));
        shiMax.setText("" + modelist.get(0).shi);
        shiMin.setText("" + modelist.get(size - 1).shi);
        shiMean.setText("" + modes[3]);

        Collections.sort(modelist, (o1, o2) -> Integer.compare(o2.wen, o1.wen));
        wenMax.setText("" + modelist.get(0).wen);
        wenMin.setText("" + modelist.get(size - 1).wen);
        wenMean.setText("" + modes[4]);
    }

    /**
     * 设置时间
     */
    @SuppressLint("SetTextI18n")
    private void setTime(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        SharedPreferences.Editor shar=getSharedPreferences("time",MODE_PRIVATE).edit();
        SharedPreferences data=getSharedPreferences("time",MODE_PRIVATE);
        long timexin=System.currentTimeMillis();
        long spTime=data.getLong("time",timexin);
        int mode= (int) ((timexin-spTime)/1000);
        if (mode>60){
            timeUpdate.setText("最近更新："+(mode/60)+"分钟之前");
        }else{
            timeUpdate.setText("最近更新：最新数据");
        }
        time.setText(simpleDateFormat.format(timexin));
        shar.putLong("time",timexin).apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (count != null) {
            count.cancel();
            count=null;
        }
    }
}
