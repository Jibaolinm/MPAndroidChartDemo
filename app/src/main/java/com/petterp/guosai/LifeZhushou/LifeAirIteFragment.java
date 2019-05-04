package com.petterp.guosai.LifeZhushou;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.petterp.guosai.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Petterp on 2019/5/4
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class LifeAirIteFragment extends Fragment {
    private BarChart chart;
    private List<BarEntry> list = new ArrayList<>();
    private List<Integer> sum=new ArrayList<>();
    private TextView title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.life_air_item, container, false);
        chart = view.findViewById(R.id.life_air_chart);
        title=view.findViewById(R.id.life_zuicha);
        info();
        return view;
    }

    private void info() {
        chart.setMarker(new LifeMarker(getContext()));
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(19);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(25f);
        xAxis.setTextSize(15F);
        List<String> list = new ArrayList();
        list.add("03");
        list.add("06");
        list.add("09");
        for (int i = 12; i <= 60; i += 3) {
            list.add("" + i);
        }
        xAxis.setValueFormatter((v, axisBase) -> v>19?"":list.get((int) v));

        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0f);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
    }

    public void setData(int k) {
        sum.add(0,k);
        list.add(0, new BarEntry(0, k));
        final int size = list.size();
        for (int i = 0; i < size; i++) {
            list.get(i).setX(i);
        }
        if (list.size() > 20) {
            List<Integer> sums=new ArrayList<>();
            sums.addAll(sum);
            Collections.sort(sums);
            title.setText("过去1分钟空气最差值为："+sums.get(20));
            sum.remove(20);
            list.remove(20);
        }
        BarDataSet set = new BarDataSet(list, "");
        set.setColor(Color.parseColor("#6984A7"));
        set.setDrawValues(false);
        BarData data = new BarData(set);
        data.setBarWidth(0.6F);
        chart.setData(data);
        chart.invalidate();
    }
}
