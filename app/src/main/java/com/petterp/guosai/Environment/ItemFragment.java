package com.petterp.guosai.Environment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.petterp.guosai.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Petterp on 2019/5/4
 * Summary:所对应的Fragment
 * 邮箱：1509492795@qq.com
 */
@SuppressLint("ValidFragment")
public class ItemFragment extends Fragment{
    private LineChart chart;
    private int postion;

    public ItemFragment(int postion) {
        this.postion = postion;
    }

    private List<Entry> list=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.envir_item_lincharat, container, false);
        chart=view.findViewById(R.id.chart);
        init();
        setData();
        return view;
    }
    private void init(){
        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(15f);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setLabelCount(20);
        xAxis.setAxisMaximum(23f);
        List<String> list=new ArrayList<>();
        list.add("55:03");
        list.add("55:06");
        list.add("55:09");
        for (int i=12;i<60;i+=3){
            list.add("55:"+i);
        }
        list.add("56:00");
        xAxis.setValueFormatter((v, axisBase) -> v>19?"":list.get((int) v));

        chart.getAxisLeft().setDrawAxisLine(true);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setTextSize(15f);
        chart.setExtraOffsets(15,0,0,15);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
    }

    public void setData(){
        list.clear();
        final int szie=Environment.list.size();
        for (int i=0;i<szie;i++){
            list.add(new BarEntry(i,Environment.list.get(i).getPostion(postion)));
        }
        LineDataSet set=new LineDataSet(list,"");
        set.setColor(Color.parseColor("#3C3F41"));
        set.setCircleRadius(5f);
        set.setCircleColor(Color.parseColor("#3C3F41"));
        set.setDrawCircleHole(false);
        set.setDrawValues(false);
        LineData data=new LineData(set);
        if (chart != null) {
            chart.setData(data);
            chart.invalidate();
        }
    }
}
