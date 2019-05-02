package com.petterp.test51.Fragment2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.petterp.test51.Bean.AllUser;
import com.petterp.test51.Bean.AllcarBean;
import com.petterp.test51.Post;
import com.petterp.test51.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Petterp on 2019/5/2
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class PieFragment_1 extends Fragment {
    private PieChart chart;
    private List<String> list = new ArrayList<>();
    private List<String> frts = new ArrayList<>();
    private DecimalFormat decimalFormat=new DecimalFormat("#0.00");
    private int mode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie1, container, false);
        chart=view.findViewById(R.id.chart);
        post();
        return view;
    }

    private void post() {
        Post.budler().setPost("http://192.168.1.107:8088/transportservice/action/GetAllCarPeccancy.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                Gson gson = new Gson();
                AllcarBean bean = gson.fromJson(res, AllcarBean.class);
                int size = bean.getROWS_DETAIL().size();
                for (int i = 0; i < size; i++) {
                    //获取违章车牌号
                    list.add(bean.getROWS_DETAIL().get(i).getCarnumber());
                }
                remove(list);
                setUser();
            }
        });
    }

    private void setUser() {
        Post.budler().setPost("http://192.168.1.107:8088/transportservice/action/GetCarInfo.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                Gson gson = new Gson();
                AllUser bean = gson.fromJson(res, AllUser.class);
                int size = bean.getROWS_DETAIL().size();
                for (int i = 0; i < size; i++) {
                    frts.add(bean.getROWS_DETAIL().get(i).getCarnumber());
                }
                setData();
            }
        });
    }

    private void remove(List list) {
        HashSet set = new HashSet(list);
        list.clear();
        list.addAll(set);
    }

    private void setData() {
        List<PieEntry> list=new ArrayList<>();
        final float[] sum=new float[2];
        sum[0]= (float) (this.list.size()*1.0/frts.size());
        sum[1]=1-sum[0];
        list.add(new PieEntry(sum[0],""));
        list.add(new PieEntry(sum[1],""));
        PieDataSet set=new PieDataSet(list,"");
        set.setColors(Color.parseColor("#2F4554"),Color.parseColor("#C23531"));
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (v==sum[0]){
                    return "有违章"+decimalFormat.format(v*100)+"%";
                }else{
                    return "无违章"+decimalFormat.format(v*100)+"%";
                }
            }
        });
        set.setValueLinePart1OffsetPercentage(100f);
        set.setValueLinePart1Length(0.8f);
        set.setValueLinePart2Length(0.1f);
        set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data=new PieData(set);
        chart.setData(data);
        chart.invalidate();
    }
}
