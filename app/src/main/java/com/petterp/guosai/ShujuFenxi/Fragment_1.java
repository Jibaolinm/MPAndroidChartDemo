package com.petterp.guosai.ShujuFenxi;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.petterp.guosai.Bean.AllUser;
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
@SuppressLint("ValidFragment")
public class Fragment_1 extends Fragment {
    private State state;
    private PieChart chart;
    private int[] sums=new int[2];
    private DecimalFormat decimalFormat=new DecimalFormat("0.00");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_pie1,container,false);
        chart=view.findViewById(R.id.chart);
        initChar();
        init();
        return view;
    }

    public Fragment_1(State state) {
        this.state = state;
    }

    private void init(){
        setCar();
    }

    private void setCar(){

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
                    sums[0]=remove(list);
                    setUser();
                }
            }
        });
    }

    private void setUser(){
        Post.budler().setPost("http://192.168.1.107:8088/transportservice/action/GetCarInfo.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                Gson gson=new Gson();
                AllUser bean=gson.fromJson(res,AllUser.class);
                if (bean.getERRMSG().equals("成功")){
                    int size=bean.getROWS_DETAIL().size();
                    List<String> list=new ArrayList<>();
                    for (int i=0;i<size;i++){
                        list.add(bean.getROWS_DETAIL().get(i).getCarnumber());
                    }
                    sums[1]=list.size();
                    setData();
                    state.ok();
                }
            }
        });
    }

    private int remove(List list){
        HashSet hashSet=new HashSet(list);
        return hashSet.size();
    }

    private void initChar(){
        chart.setDrawCenterText(false);
        chart.setDrawHoleEnabled(false);
        chart.setDrawEntryLabels(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(30,30,30,30);
        chart.animateXY(1000,1000);
    }

    private void setData(){
        List<PieEntry> list=new ArrayList<>();
        final float a= (float) (sums[0]*1.0/sums[1]);
        final float b=1-a;
        Log.e("demo",a+"----"+b);
        list.add(new PieEntry(sums[0],0));
        list.add(new PieEntry(sums[1],1));
        PieDataSet set=new PieDataSet(list,"");
        set.setColors(Color.parseColor("#2F4554"),Color.parseColor("#C23531"));
        set.setValueLinePart1Length(1f);
        set.setValueLinePart1OffsetPercentage(100f);
        set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        set.setValueLinePart2Length(0.2f);
        set.setValueTextSize(20f);
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (v==sums[0]){
                    return "有违章"+decimalFormat.format(a*100)+"%";
                }else{
                    return "无违章"+decimalFormat.format(b*100)+"%";
                }
            }
        });
        PieData data=new PieData(set);
        chart.setData(data);
        chart.invalidate();
    }

}
