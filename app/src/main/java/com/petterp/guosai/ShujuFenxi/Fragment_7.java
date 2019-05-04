package com.petterp.guosai.ShujuFenxi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.petterp.guosai.Bean.AllcarBean;
import com.petterp.guosai.Bean.WeiZhang;
import com.petterp.guosai.Post;
import com.petterp.guosai.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Petterp on 2019/5/2
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class Fragment_7 extends Fragment {
    private HorizontalBarChart chart;
    private DecimalFormat decimalFormat = new DecimalFormat("##.00");
    private XAxis xAxis;
    private List<Map.Entry<String, Integer>> entryList;
    private List<String> frits;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar1, container, false);
        chart = view.findViewById(R.id.chart);
        initChart();
        setCar();
        return view;
    }

    private void initChart() {
        xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(10);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(10f);
        xAxis.setTextSize(20f);


        YAxis right = chart.getAxisRight();
        right.setDrawGridLines(false);
        right.setAxisMinimum(0f);
        right.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return decimalFormat.format(v * 100) + "%";
            }
        });

        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0);
        left.setDrawLabels(false);
        left.setDrawAxisLine(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);

    }

    private void setCar(){
        Post.budler().setPost("http://192.168.1.107:8088/transportservice/action/GetAllCarPeccancy.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                Gson gson=new Gson();
                AllcarBean bean=gson.fromJson(res,AllcarBean.class);
                if (bean.getERRMSG().equals("成功")){
                    int size=bean.getROWS_DETAIL().size();
                    //先存整个list的数据吗
                    frits = new ArrayList<>();
                    for (int i=0;i<size;i++){
                        frits.add(bean.getROWS_DETAIL().get(i).getPcode());
                    }
                }
                setPost();
            }
        });
    }



    private void setPost() {
        Post.budler().setPost("http://192.168.1.107:8088/transportservice/action/GetPeccancyType.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                Gson gson = new Gson();
                WeiZhang bean = gson.fromJson(res, WeiZhang.class);
                if (bean.getERRMSG().equals("成功")) {
                    int size = bean.getROWS_DETAIL().size();
                    List<String> list = new ArrayList<>();
                    List<String> list2=new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        list.add(bean.getROWS_DETAIL().get(i).getPcode());
                        list2.add(bean.getROWS_DETAIL().get(i).getPremarks());
                    }

                    //总违规次数
                    final int mode1 = frits.size();
                    //违规示例
                    //存储Map，将违规内容做key,将次数当做value
                    Map<String, Integer> map = new HashMap<>(size);
                    for (int i = 0; i < size; i++) {
                        map.put(list2.get(i), 0);
                        for (int j = 0; j < mode1; j++) {
                            if (list.get(i).equals(frits.get(j))) {
                                int mode = map.get(list2.get(i));
                                map.put(list2.get(i), mode + 1);
                            }
                        }
                    }

                    //将Map转为List
                    entryList = new ArrayList<>(map.entrySet());
                    //降序处理
                    Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
                        @Override
                        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                            return o2.getValue().compareTo(o1.getValue());
                        }
                    });

                    for (int i=0;i<size;i++){
                        Log.e("demo","key:"+entryList.get(i).getKey()+"----value:"+entryList.get(i).getValue());
                    }

                }
                setData();
            }
        });
    }

    private void setData() {
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return v > 9 ? "" : entryList.get((int) v).getKey();
            }
        });
        final int size = 10;
        int mode = 0;
        for (int i=0;i<size;i++){
            mode+=entryList.get(i).getValue();
        }
        List<BarEntry> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new BarEntry(i, (float) (entryList.get(i).getValue()*1.0/mode)));
        }
        BarDataSet set = new BarDataSet(list, "");
        List<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.LIBERTY_COLORS) {
            colors.add(color);
        }
        for (int color : ColorTemplate.COLORFUL_COLORS) {
            colors.add(color);
        }
        set.setColors(colors);
        set.setValueTextSize(20f);
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return decimalFormat.format(v * 100) + "%";
            }
        });
        BarData data = new BarData(set);
        data.setBarWidth(0.6f);
        chart.setData(data);
        chart.invalidate();
    }
}
