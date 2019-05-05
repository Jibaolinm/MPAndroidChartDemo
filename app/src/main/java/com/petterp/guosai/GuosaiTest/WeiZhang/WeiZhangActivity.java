package com.petterp.guosai.GuosaiTest.WeiZhang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.petterp.guosai.R;

/**
 * 违章分析
 *
 * @author Pettepr
 */
public class WeiZhangActivity extends AppCompatActivity implements View.OnClickListener {
    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_zhang);
        initView();
    }

    private void initView() {
        chart=findViewById(R.id.chart);
        findViewById(R.id.weizhang_rili).setOnClickListener(this);
        findViewById(R.id.date_from).setOnClickListener(this);
        findViewById(R.id.date_to).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weizhang_rili:
            case R.id.date_from:new DiaLog(this).show();break;
            case R.id.date_to:new DiaLog(this).show();break;
            default:
                break;
        }
    }
    private void setData(){

    }
    private void setShuju(){

    }
}
