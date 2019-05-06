package com.petterp.guosai.ShujuFenxi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
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
import com.petterp.guosai.Bean.AllUser;
import com.petterp.guosai.Bean.AllcarWeizhang;
import com.petterp.guosai.Post;
import com.petterp.guosai.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Petterp on 2019/5/2
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class Fragment_5 extends Fragment {
    private BarChart chart;
    private DecimalFormat decimalFormat = new DecimalFormat("##.00");
    private List<String> list1, list2, list3;
    private int[] weizhang = new int[2];
    private int[] wuweizhang = new int[2];
    private final int size = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar2, container, false);
        chart = view.findViewById(R.id.chart);
        initChart();
        setCar();
        return view;
    }

    private void initChart() {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(2);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(2f);
        final String[] setXs = {"女性", "男性"};
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return v>1?"":setXs[(int) v];
            }
        });

        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0);
        left.setTextSize(20f);

        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        Legend legend = chart.getLegend();
        legend.setDrawInside(true);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setXOffset(30);
        chart.animateXY(1000,1000);

    }


    private void setCar() {
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        Post.budler().setPost("http://192.168.1.107:8088/transportservice/action/GetAllCarPeccancy.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                Gson gson = new Gson();
                AllcarWeizhang bean = gson.fromJson(res, AllcarWeizhang.class);
                if (bean.getERRMSG().equals("成功")) {
                    int size = bean.getROWS_DETAIL().size();
                    for (int i = 0; i < size; i++) {
                        list1.add(bean.getROWS_DETAIL().get(i).getCarnumber());
                    }
                    remove(list1);
                    setUser();
                }
            }
        });
    }


    private void setUser() {
        Post.budler().setPost("http://192.168.1.107:8088/transportservice/action/GetCarInfo.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                Gson gson = new Gson();
                AllUser bean = gson.fromJson(res, AllUser.class);
                if (bean.getERRMSG().equals("成功")) {
                    int size = bean.getROWS_DETAIL().size();
                    for (int i = 0; i < size; i++) {
                        list2.add(bean.getROWS_DETAIL().get(i).getPcardid());
                    }
                }

                //解法思路
                /*
                 * 先将违章车辆信息拿到，取里面的车牌号，放入HashSet里除去重复车牌
                 * 将所有车辆数据信息拿到，然后存入他们的身份证号
                 * 与平台所有车辆信息的车牌进行对比，只有符合是平台下的车辆才加入到相应的集合中。
                 * 然后将数据进行清洗，所有车辆-违章车辆=未违章车辆，根据身份证中的年代位数计算相应的值。
                 * */

                for (String a : list1) {
                    for (AllUser.ROWSDETAILBean b : bean.getROWS_DETAIL()) {
                        if (a.equals(b.getCarnumber())) {
                            list3.add(b.getPcardid());
                            continue;
                        }
                    }
                }
                final int mode = list3.size();
                for (int i=0;i<mode;i++){
                    if (list3.get(i).charAt(16)%2==0){
                        ++weizhang[0];
                    }else{
                        ++weizhang[1];
                    }
                }

                final int mode2 = list2.size();
                for (int i=0;i<mode2;i++){
                    if (list2.get(i).charAt(16)%2==0){
                        ++wuweizhang[0];
                    }else{
                        ++wuweizhang[1];
                    }
                }
                setData();
            }
        });
    }

    private void remove(List list) {
        HashSet hashSet = new HashSet(list);
        list.clear();
        list.addAll(hashSet);
    }


    private void setData() {
        List<BarEntry> list = new ArrayList<>();
        final float[] sum = new float[2];
        for (int i = 0; i < size; i++) {
            sum[i] = (float) (weizhang[i] * 1.0 / wuweizhang[i]);
            list.add(new BarEntry(i, new float[]{wuweizhang[i] - weizhang[i],weizhang[i]}));
        }
        BarDataSet set = new BarDataSet(list, "");
        set.setStackLabels(new String[]{"无违法", "有违法"});
        set.setColors(Color.parseColor("#91C7AE"),Color.parseColor("#D48265"));
        set.setValueTextColor(Color.RED);
        set.setValueTextSize(20f);
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                for (int j=0;j<size;j++){
                    if (v==weizhang[j]){
                        return decimalFormat.format(sum[j]*100)+"%";
                    }
                }
                return "";
            }
        });
        BarData data = new BarData(set);
        data.setBarWidth(0.6f);
        chart.setData(data);
        chart.invalidate();
    }
}
