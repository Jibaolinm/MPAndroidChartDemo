package com.petterp.guosai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.petterp.guosai.Environment.Environment;
import com.petterp.guosai.GuosaiTest.DingzhiBanChe.DinzhiActivity;
import com.petterp.guosai.GuosaiTest.HuanJingJiance.HuanJingjianActivity;
import com.petterp.guosai.GuosaiTest.Login.LoginActivity;
import com.petterp.guosai.GuosaiTest.Weather.WeatherActivity;
import com.petterp.guosai.GuosaiTest.WeiZhang.WeiZhangActivity;
import com.petterp.guosai.GuosaiTest.Zhizhutu.WeiZhanglei;
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
        findViewById(R.id.weather).setOnClickListener(this);
        findViewById(R.id.weizhang).setOnClickListener(this);
        findViewById(R.id.huanjing).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.dingzhi).setOnClickListener(this);
        SharedPreferences.Editor shar=getSharedPreferences("data",MODE_PRIVATE).edit();
        PostUtils.Builder().setOkHttpClient("GetCarInfo.do", "{\"UserName\":\"user1\"}", new PostUtils.Post() {
            @Override
            public void success(String s) {
                Log.e("demo",s);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shuju:IntentPlay(ShujuActivity.class);break;
            case R.id.envir:IntentPlay(Environment.class);break;
            case R.id.life:IntentPlay(LifeActivity.class);break;
            case R.id.weather:IntentPlay(WeatherActivity.class);break;
            case R.id.weizhang:IntentPlay(WeiZhanglei.class);break;
            case R.id.huanjing:IntentPlay(HuanJingjianActivity.class);break;
            case R.id.login:IntentPlay(LoginActivity.class);break;
            case R.id.dingzhi:IntentPlay(DinzhiActivity.class);break;
            default:break;
        }
    }


    private void IntentPlay(Class c){
        Intent intent=new Intent(this,c);
        startActivity(intent);
    }
}
