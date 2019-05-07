package com.petterp.guosai.GuosaiTest.Zhizhutu;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.google.gson.Gson;
import com.petterp.guosai.Bean.AllcarBean;
import com.petterp.guosai.Bean.AllcarWeizhang;
import com.petterp.guosai.Post;
import com.petterp.guosai.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 违章分析，重写了RadarChart和 XAxisRendererRaderChart源码 改了setColor属性
 */
public class WeiZhanglei extends AppCompatActivity {
    private RadarCharts chart;
    //具体违章信息Bean存储
    private List<String> listbean = new ArrayList<>();
    //车牌号存储
    private List<String> listpcode = new ArrayList<>();
    private List<String> listwei = new ArrayList<>();
    private List<Map.Entry<String, Integer>> list;
    private ProgressDialog progressDialog;
    private View mColor1;
    private TextView mText1;
    private View mColor2;
    private TextView mText2;
    private View mColor3;
    private TextView mText3;
    private View mColor4;
    private TextView mText4;
    private View mColor5;
    private TextView mText5;
    private int colors[]={Color.parseColor("#6600FF"),Color.parseColor("#EF5AA1"),
            Color.parseColor("#EF5AA1"),Color.parseColor("#33FF66"),
            Color.parseColor("#009DD9")};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_zhanglei);
        initView();
        chart = findViewById(R.id.chart);
        initChart();
        setWeiBean();
    }

    private void setWeiBean() {
        Post.budler().setPost("http://192.168.1.104:8088/transportservice/action/GetPeccancyType.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                Gson gson = new Gson();
                AllcarBean bean = gson.fromJson(res, AllcarBean.class);
                int size = bean.getROWS_DETAIL().size();
                for (int i = 0; i < size; i++) {
                    listpcode.add(bean.getROWS_DETAIL().get(i).getPcode());
                    listbean.add(bean.getROWS_DETAIL().get(i).getPremarks());
                }
            }
        });
        setWeiZhang();
    }

    private void setWeiZhang() {
        Post.budler().setPost("http://192.168.1.104:8088/transportservice/action/GetAllCarPeccancy.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                Gson gson = new Gson();
                AllcarWeizhang bean = gson.fromJson(res, AllcarWeizhang.class);
                int size = bean.getROWS_DETAIL().size();
                for (int i = 0; i < size; i++) {
                    listwei.add(bean.getROWS_DETAIL().get(i).getPcode());
                }

                HashMap<String, Integer> map = new HashMap<>();
                int sizebf = listpcode.size();
                for (int i = 0; i < sizebf; i++) {
                    map.put(listbean.get(i), 0);
                    for (int j = 0; j < size; j++) {
                        if (listpcode.get(i).equals(listwei.get(j))) {
                            int mode = map.get(listbean.get(i));
                            map.put(listbean.get(i), ++mode);
                        }
                    }
                }

                //转换
                list = new ArrayList<>(map.entrySet());
                Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
                setData();
                for (int i = 0; i < list.size(); i++) {
                    Log.e("Demo", list.get(i).getKey() + ":" + list.get(i).getValue());
                }
                progressDialog.dismiss();
            }
        });
    }

    private void initChart() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("正在請求數據");
        progressDialog.show();
        //设置线条可见
        chart.setDrawWeb(true);
        //设置线条颜色
        chart.setWebColorInner(Color.parseColor("#C7C7C7"));
        //设置中心线颜色
        chart.setWebColor(Color.parseColor("#C7C7C7"));
        //设置标签大小，也就是最顶部的各个点
        chart.getXAxis().setTextSize(30F);
        chart.getXAxis().setValueFormatter((v, axisBase) -> {
            chart.getXAxis().setTextColor(colors[(int) v]);
            return "●";
        });
        YAxis yAxis = chart.getYAxis();
        //设置显示5个标签，并且平分
        yAxis.setLabelCount(5, true);
        //设置线条从0开始
        yAxis.setAxisMinimum(0f);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
    }

    private void setData() {
        List<RadarEntry> list1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list1.add(new RadarEntry(list.get(i).getValue(), list.get(i).getKey()));
        }
        chart.getYAxis().setAxisMaximum(list.get(0).getValue());
        //更改颜色组，即右侧的颜色块
        setLegled(colors[0],list.get(0).getKey(),mColor1,mText1);
        setLegled(colors[1],list.get(1).getKey(),mColor2,mText2);
        setLegled(colors[2],list.get(2).getKey(),mColor3,mText3);
        setLegled(colors[3],list.get(3).getKey(),mColor4,mText4);
        setLegled(colors[4],list.get(4).getKey(),mColor5,mText5);
        RadarDataSet set = new RadarDataSet(list1, "");
        //设置线条颜色
        set.setColor(Color.BLUE);
        //设置填充透明度
        set.setFillAlpha(50);
        //设置填充颜色
        set.setFillColor(Color.BLUE);
        //允许填充
        set.setDrawFilled(true);


        RadarData data = new RadarData(set);
        data.setDrawValues(false);
        chart.setData(data);
        chart.invalidate();
    }

    private void initView() {
        mColor1 = (View) findViewById(R.id.color_1);
        mText1 = (TextView) findViewById(R.id.text_1);
        mColor2 = (View) findViewById(R.id.color_2);
        mText2 = (TextView) findViewById(R.id.text_2);
        mColor3 = (View) findViewById(R.id.color_3);
        mText3 = (TextView) findViewById(R.id.text_3);
        mColor4 = (View) findViewById(R.id.color_4);
        mText4 = (TextView) findViewById(R.id.text_4);
        mColor5 = (View) findViewById(R.id.color_5);
        mText5 = (TextView) findViewById(R.id.text_5);
    }
    private void setLegled(int color,String text,View view,TextView textView){
        view.setBackgroundColor(color);
        textView.setText(text);
    }
}
