package com.petterp.guosai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.petterp.guosai.Environment.Environment;
import com.petterp.guosai.LifeZhushou.LifeActivity;
import com.petterp.guosai.ShujuFenxi.ShujuActivity;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.shuju).setOnClickListener(this);
        findViewById(R.id.envir).setOnClickListener(this);
        findViewById(R.id.life).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shuju:IntentPlay(ShujuActivity.class);break;
            case R.id.envir:IntentPlay(Environment.class);break;
            case R.id.life:IntentPlay(LifeActivity.class);break;
            default:break;
        }
    }
    private void IntentPlay(Class c){
        Intent intent=new Intent(this,c);
        startActivity(intent);
    }
}
