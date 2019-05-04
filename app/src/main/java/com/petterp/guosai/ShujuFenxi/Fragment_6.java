package com.petterp.guosai.ShujuFenxi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
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
import com.petterp.guosai.Post;
import com.petterp.guosai.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Petterp on 2019/5/2
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class Fragment_6 extends Fragment {
    private BarChart chart;
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private int[] sums = new int[12];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_bar3, container, false);
        chart = view.findViewById(R.id.chart);
        initChart();
        setCar();
        return view;
    }

    private void initChart() {
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelCount(12);
        xAxis.setAxisMaximum(12f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(-45);
        final String[] data = {"0:00:01", "2:00:00",
                "2:00:01", "4:00:00",
                "4:00:01", "6:00:00",
                "6:00:01", "8:00:00",
                "8:00:01", "10:00:00",
                "10:00:01", "12:00:00",
                "12:00:01", "14:00:00",
                "14:00:01", "16:00:00",
                "16:00:01", "18:00:00",
                "18:00:01", "20:00:00",
                "20:00:01", "22:00:00",
                "22:00:01", "24:00:00"};
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return v > 11 ? "" : data[(int) v];
            }
        });
        YAxis left = chart.getAxisLeft();
        left.setDrawGridLines(false);
        left.setTextSize(20f);
        left.setLabelCount(5);
        left.setAxisMinimum(0f);
        left.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return v == 0 ? "0.00%" : decimalFormat.format(v*100) + "%";
            }
        });
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
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
                        list.add(bean.getROWS_DETAIL().get(i).getPdatetime().substring(10, 12));
                    }
                    for (int i = 0; i < size; i++) {
                        final String time = list.get(i);
                        if (time.equals("00") || time.equals("01")) {
                            ++sums[0];
                        } else if (time.equals("02") || time.equals("03")) {
                            ++sums[1];
                        } else if (time.equals("04") || time.equals("05")) {
                            ++sums[2];
                        } else if (time.equals("06") || time.equals("07")) {
                            ++sums[3];
                        } else if (time.equals("08") || time.equals("09")) {
                            ++sums[4];
                        } else if (time.equals("10") || time.equals("11")) {
                            ++sums[5];
                        } else if (time.equals("12") || time.equals("13")) {
                            ++sums[6];
                        } else if (time.equals("14") || time.equals("15")) {
                            ++sums[7];
                        } else if (time.equals("16") || time.equals("17")) {
                            ++sums[8];
                        } else if (time.equals("18") || time.equals("19")) {
                            ++sums[9];
                        } else if (time.equals("20") || time.equals("21")) {
                            ++sums[10];
                        } else if (time.equals("22") || time.equals("23")) {
                            ++sums[11];
                        }
                    }
                }
                setData();
            }
        });
    }

    private void setData() {
        List<BarEntry> list = new ArrayList<>();
        int state = 0;
        for (int i = 0; i < 12; i++) {
            state += sums[i];
        }
        for (int i = 0; i < 12; i++) {
            list.add(new BarEntry(i, (float) (sums[i] * 1.0 / state)));
        }
        List<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.LIBERTY_COLORS) {
            colors.add(color);
        }
        for (int color : ColorTemplate.COLORFUL_COLORS) {
            colors.add(color);
        }
        colors.add(ColorTemplate.PASTEL_COLORS[0]);
        colors.add(ColorTemplate.PASTEL_COLORS[1]);

        BarDataSet set = new BarDataSet(list, "");
        set.setColors(colors);
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
