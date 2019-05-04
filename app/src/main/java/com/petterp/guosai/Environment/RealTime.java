package com.petterp.guosai.Environment;

import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.petterp.guosai.Post;
import com.petterp.guosai.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Petterp on 2019/5/4
 * Summary:实体类。
 * 邮箱：1509492795@qq.com
 */
public class RealTime extends AppCompatActivity{
    private ViewPager viewPager;
    private List<ItemFragment> list=new ArrayList<>();
    private ItemFragment f1,f2,f3,f4,f5,f6;
    private Frits frits;
    private CountDownTimer count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time);
        info();
        count=new CountDownTimer(Integer.MAX_VALUE,3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setData();
            }

            @Override
            public void onFinish() {

            }
        };
        CountDownTimer c=new CountDownTimer(100,100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                count.start();
            }
        }.start();
    }
    private void info(){
        viewPager=findViewById(R.id.viewpager);
        list.add(f1=new ItemFragment(0));
        list.add(f2=new ItemFragment(1));
        list.add(f3=new ItemFragment(2));
        list.add(f4=new ItemFragment(3));
        list.add(f5=new ItemFragment(4));
        list.add(f6=new ItemFragment(5));
        FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getIntent().getIntExtra("postion",0));

    }

    private class  FragmentAdapter extends FragmentStatePagerAdapter{

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }
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

    private void setData2() {
        frits.f = (int) (Math.random()*5);
        Environment.list.add(0,frits);
        if (list.size() > 20) {
            list.remove(20);
        }
        f1.setData();
        f2.setData();
        f3.setData();
        f4.setData();
        f5.setData();
        f6.setData();
    }
}
