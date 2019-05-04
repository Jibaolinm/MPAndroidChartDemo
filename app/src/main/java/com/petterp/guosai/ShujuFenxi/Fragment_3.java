package com.petterp.guosai.ShujuFenxi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.petterp.guosai.Bean.AllcarBean;
import com.petterp.guosai.Post;
import com.petterp.guosai.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author Petterp on 2019/5/1
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class Fragment_3 extends Fragment {
    private HorizontalBarChart chart;
    private DecimalFormat decimalFormat=new DecimalFormat("##.00");
    private List<String> d1,d2,d3;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bar1,container,false);
        chart=view.findViewById(R.id.chart);
        initChart();
        setPost();
        return view;
    }
    private void initChart(){
        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(2);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(3f);
        final String[] setXs={"1-2条违章","3-5条违章","5条以上违章"};
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return setXs[(int) v];
            }
        });

        YAxis right=chart.getAxisRight();
        right.setAxisMaximum(1);
        right.setAxisMinimum(0);
        right.setDrawGridLines(false);
        right.setLabelCount(7);
        right.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                 if (v==0){
                     return "0.00%";
                 }
                 return decimalFormat.format(v*100)+"%";
            }
        });

        YAxis left=chart.getAxisLeft();
        left.setAxisMinimum(0);
        left.setDrawLabels(false);
        left.setTextSize(20f);
        left.setDrawAxisLine(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.animateXY(1000,1000);

    }
    private void setPost(){
        d1=new ArrayList<>();
        d2=new ArrayList<>();
        d3=new ArrayList<>();
        Post.budler().setPost("http://192.168.1.107:8088/transportservice/action/GetAllCarPeccancy.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                Gson gson=new Gson();
                AllcarBean bean=gson.fromJson(res,AllcarBean.class);
                if (bean.getERRMSG().equals("成功")){
                    int size=bean.getROWS_DETAIL().size();
                    List<String> list=new ArrayList<>();
                    for (int i=0;i<size;i++){
                        list.add(bean.getROWS_DETAIL().get(i).getCarnumber());
                    }
                    //除重复
                    HashSet<String> set=new HashSet<>(list);
                    for (String string:set){
                        if (Collections.frequency(list,string)>5){
                            d3.add(string);
                        }else if (Collections.frequency(list,string)>2){
                            d2.add(string);
                        }else{
                            d1.add(string);
                        }
                    }
                    setData();
                }
            }
        });
    }
    private void setData(){
        List<BarEntry> list=new ArrayList<>();
        final int size=d1.size()+d2.size()+d3.size();
        float[] sum=new float[3];
        sum[0]= (float) (d1.size()*1.0/size);
        sum[1]= (float) (d2.size()*1.0/size);
        sum[2]= (float) (d3.size()*1.0/size);
        for (int i=0;i<3;i++){
            list.add(new BarEntry(i,sum[i]));
        }
        BarDataSet set=new BarDataSet(list,"");
        set.setColors(Color.parseColor("#91C7AE"),Color.parseColor("#D48265"),Color.parseColor("#61A0A8"));
        set.setValueTextColor(Color.RED);
        set.setValueTextSize(20f);
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return decimalFormat.format(v*100)+"%";
            }
        });
        BarData data=new BarData(set);
        data.setBarWidth(0.6f);
        chart.setData(data);
        chart.invalidate();
    }
}
