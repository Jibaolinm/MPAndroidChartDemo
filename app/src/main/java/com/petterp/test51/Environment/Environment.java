package com.petterp.test51.Environment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petterp.test51.Post;
import com.petterp.test51.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Environment extends AppCompatActivity implements View.OnClickListener {
    private TextView wendu;
    private LinearLayout view1;
    private TextView shidu;
    private LinearLayout view2;
    private TextView guang;
    private LinearLayout view3;
    private TextView pm;
    private LinearLayout view4;
    private TextView co;
    private LinearLayout view5;
    private TextView daolu;
    private LinearLayout view6;
    public static List<Frits> list = new ArrayList<>();
    private Frits frits;
    private CountDownTimer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);
        initView();
        count = new CountDownTimer(Integer.MAX_VALUE, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setData();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void initView() {
        wendu = (TextView) findViewById(R.id.wendu);
        view1 = (LinearLayout) findViewById(R.id.view1);
        shidu = (TextView) findViewById(R.id.shidu);
        view2 = (LinearLayout) findViewById(R.id.view2);
        guang = (TextView) findViewById(R.id.guang);
        view3 = (LinearLayout) findViewById(R.id.view3);
        pm = (TextView) findViewById(R.id.pm);
        view4 = (LinearLayout) findViewById(R.id.view4);
        co = (TextView) findViewById(R.id.co);
        view5 = (LinearLayout) findViewById(R.id.view5);
        daolu = (TextView) findViewById(R.id.daolu);
        view6 = (LinearLayout) findViewById(R.id.view6);
        view1.setOnClickListener(this);
        view2.setOnClickListener(this);
        view3.setOnClickListener(this);
        view4.setOnClickListener(this);
        view5.setOnClickListener(this);
        view6.setOnClickListener(this);
    }

    private void setData() {
        Post.budler().setPost("http://192.168.1.105:8088/transportservice/action/GetAllSense.do", "{\"UserName\":\"user1\"}", new Post.post() {
            @Override
            public void Ok(String res) {
                try {
                    frits = new Frits();
                    JSONObject jsonObject = new JSONObject(res);
                    frits.a = jsonObject.getInt("temperature");
                    frits.b = jsonObject.getInt("humidity");
                    frits.c = jsonObject.getInt("LightIntensity");
                    frits.d = jsonObject.getInt("pm2.5");
                    frits.e = jsonObject.getInt("co2");
                    setData2();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData2() {
        frits.f = (int) (Math.random() * 5);
        wendu.setText("" + frits.a);
        shidu.setText("" + frits.b);
        guang.setText("" + frits.c);
        pm.setText("" + frits.d);
        co.setText("" + frits.e);
        daolu.setText("" + frits.f);
        if (frits.a > 100) {
            view1.setBackgroundResource(R.drawable.envir_back_1);
        } else {
            view1.setBackgroundResource(R.drawable.envir_back_2);
        }
        if (frits.b > 100) {
            view2.setBackgroundResource(R.drawable.envir_back_1);
        } else {
            view2.setBackgroundResource(R.drawable.envir_back_2);
        }
        if (frits.c > 100) {
            view3.setBackgroundResource(R.drawable.envir_back_1);
        } else {
            view3.setBackgroundResource(R.drawable.envir_back_2);
        }
        if (frits.d > 100) {
            view4.setBackgroundResource(R.drawable.envir_back_1);
        } else {
            view4.setBackgroundResource(R.drawable.envir_back_2);
        }
        if (frits.e > 100) {
            view5.setBackgroundResource(R.drawable.envir_back_1);
        } else {
            view5.setBackgroundResource(R.drawable.envir_back_2);
        }
        if (frits.f > 100) {
            view6.setBackgroundResource(R.drawable.envir_back_1);
        } else {
            view6.setBackgroundResource(R.drawable.envir_back_2);
        }
        list.add(0, frits);
        if (list.size() > 20) {
            list.remove(20);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (count != null) {
            count.cancel();
            count = null;
        }
    }
    private void IntenPlay(int postion){
        Intent intent=new Intent(this,RealTime.class);
        intent.putExtra("postion",postion);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view1:IntenPlay(0);break;
            case R.id.view2:IntenPlay(1);break;
            case R.id.view3:IntenPlay(2);break;
            case R.id.view4:IntenPlay(3);break;
            case R.id.view5:IntenPlay(4);break;
            case R.id.view6:IntenPlay(5);break;
            default:
                break;
        }
    }
}
