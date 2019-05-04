package com.petterp.guosai.ShujuFenxi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.petterp.guosai.Bean.AllcarBean;
import com.petterp.guosai.Post;
import com.petterp.guosai.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Petterp on 2019/5/1
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class Fragment_2 extends Fragment {
    private PieChart chart;
    private int[] sums = new int[2];
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie1, container, false);
        chart = view.findViewById(R.id.chart);
        initChar();
        init();
        return view;
    }

    private void init() {
        setCar();
    }

    private void setCar() {

        Post.budler().setPost("http://192.168.1.107:8088/transportservice/action/GetAllCarPeccancy.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                Gson gson = new Gson();
                AllcarBean bean = gson.fromJson(res, AllcarBean.class);
                if (bean.getERRMSG().equals("成功")) {
                    int size = bean.getROWS_DETAIL().size();
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        list.add(bean.getROWS_DETAIL().get(i).getCarnumber());
                    }
                    //逻辑就是先去请求数据，因为有些数据重复，所以将数据存入HashSet，利用HashSet不能存入重复数据的特点，算出实际违章车牌树。
                    //然后计算未重复的车牌数量,然后用其/实际违章车牌树即为未违章车牌。
                    List<String> fritslist = new ArrayList<>();
                    fritslist.addAll(list);
                    remove(list);
                    for (int i = 0; i < size; i++) {
                        int mode = 0;
                        for (int j = 0; j < size; j++) {
                            if (i != j) {
                                if (fritslist.get(i).equals(fritslist.get(j))) {
                                    mode = 1;
                                    break;
                                } else {
                                    mode = 0;
                                }
                            }
                        }
                        if (mode == 0) {
                            ++sums[1];
                        }
                    }
                    sums[0] = list.size() - sums[1];
                    setData();
                }
            }
        });
    }


    private void remove(List list) {
        HashSet hashSet = new HashSet(list);
        list.clear();
        list.addAll(hashSet);
    }

    private void initChar() {
        chart.setDrawCenterText(false);
        chart.setDrawHoleEnabled(false);
        chart.setDrawEntryLabels(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(30, 30, 30, 30);
        chart.animateXY(1000, 1000);

    }

    private void setData() {
        List<PieEntry> list = new ArrayList<>();
        final float a = (float) (sums[0] * 1.0 / (sums[1]+sums[0]));
        final float b = 1 - a;
        list.add(new PieEntry(sums[0], 0));
        list.add(new PieEntry(sums[1], 1));
        PieDataSet set = new PieDataSet(list, "");
        set.setColors(Color.parseColor("#C23531"), Color.parseColor("#2F4554"));
        set.setValueLinePart1Length(1f);
        set.setValueLinePart1OffsetPercentage(100f);
        set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        set.setValueLinePart2Length(0.2f);
        set.setValueTextSize(20f);
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (v == sums[0]) {
                    return "有重复违章" + decimalFormat.format(a * 100) + "%";
                } else {
                    return "无重复违章" + decimalFormat.format(b * 100) + "%";
                }
            }
        });
        PieData data = new PieData(set);
        chart.setData(data);
        chart.invalidate();
    }

}
