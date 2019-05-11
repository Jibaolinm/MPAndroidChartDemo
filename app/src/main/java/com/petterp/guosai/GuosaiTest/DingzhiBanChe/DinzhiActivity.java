package com.petterp.guosai.GuosaiTest.DingzhiBanChe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.petterp.guosai.R;

public class DinzhiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinzhi);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Dingzhi_F1()).commit();
    }
}
