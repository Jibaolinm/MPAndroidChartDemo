package com.petterp.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.petterp.test.WeizhangF.WeiZhangFenxiActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                IntentPlay(WeiZhangFenxiActivity.class);
                break;
            default:
                break;
        }
    }

    private void IntentPlay(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
