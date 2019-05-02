package com.petterp.test51.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
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
import com.petterp.test51.Bean.AllUser;
import com.petterp.test51.Bean.AllcarBean;
import com.petterp.test51.Post;
import com.petterp.test51.R;

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
public class Fragment_4 extends Fragment {
    private BarChart chart;
    private DecimalFormat decimalFormat = new DecimalFormat("##.00");
    private List<String> list1, list2, list3;
    private int[] weizhang = new int[5];
    private int[] wuweizhang = new int[5];
    private final int size = 5;

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
        xAxis.setLabelCount(4);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(5f);
        final String[] setXs = {"90后", "80后", "70后", "60后", "50后"};
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return v>4?"":setXs[(int) v];
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
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setYOffset(45);
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
                AllcarBean bean = gson.fromJson(res, AllcarBean.class);
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


                final int mode1 = list3.size();
                for (int i = 0; i < mode1; i++) {
                    if (list3.get(i).charAt(8) == '9') {
                        ++weizhang[0];
                    } else if (list3.get(i).charAt(8) == '8') {
                        ++weizhang[1];
                    } else if (list3.get(i).charAt(8) == '7') {
                        ++weizhang[2];
                    } else if (list3.get(i).charAt(8) == '6') {
                        ++weizhang[3];
                    } else if (list3.get(i).charAt(8) == '5') {
                        ++weizhang[4];
                    }
                }

                final int mode2 = list2.size();
                for (int i = 0; i < mode2; i++) {
                    if (list2.get(i).charAt(8) == '9') {
                        ++wuweizhang[0];
                    } else if (list2.get(i).charAt(8) == '8') {
                        ++wuweizhang[1];
                    } else if (list2.get(i).charAt(8) == '7') {
                        ++wuweizhang[2];
                    } else if (list2.get(i).charAt(8) == '6') {
                        ++wuweizhang[3];
                    } else if (list2.get(i).charAt(8) == '5') {
                        ++wuweizhang[4];
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
        final float[] sum = new float[5];
        for (int i = 0; i < size; i++) {
            sum[i] = (float) (weizhang[i] * 1.0 / wuweizhang[i]);
            list.add(new BarEntry(i, new float[]{wuweizhang[i] - weizhang[i],weizhang[i]}));
        }
        BarDataSet set = new BarDataSet(list, "");
        set.setStackLabels(new String[]{"无违章", "有违章"});
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
