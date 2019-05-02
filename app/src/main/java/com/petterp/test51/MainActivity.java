package com.petterp.test51;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.petterp.test51.Fragment.Fragment_1;
import com.petterp.test51.Fragment.Fragment_3;
import com.petterp.test51.Fragment.Fragment_4;
import com.petterp.test51.Fragment.Fragment_5;
import com.petterp.test51.Fragment.Fragment_6;
import com.petterp.test51.Fragment.Fragment_7;
import com.petterp.test51.Fragment.State;
import com.petterp.test51.Fragment2.Fragment_2;
import com.petterp.test51.Fragment2.PieFragment_1;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements State {
    private Fragment_1 fragment1;
    private Fragment_2 fragment2;
    private List<Fragment> list;
    private ViewPager viewPager;
    private View view1, view2, view3, view4, view5, view6, view7;
    private TextView title;
    private final int COLOR_HIDE = R.drawable.view_hide;
    private final int COLOR_SHOW = R.drawable.view_show;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("正在请求数据");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    private void initView() {
        view1 = findViewById(R.id.road_view1);
        view2 = findViewById(R.id.road_view2);
        view3 = findViewById(R.id.road_view3);
        view4 = findViewById(R.id.road_view4);
        view5 = findViewById(R.id.road_view5);
        view6 = findViewById(R.id.road_view6);
        view7 = findViewById(R.id.road_view7);
        title = findViewById(R.id.title);
        viewPager = findViewById(R.id.viewpager);
    }

    private void initData() {
        list = new ArrayList<>();
        list.add(new PieFragment_1());
        list.add(new Fragment_2());
        list.add(new Fragment_3());
        list.add(new Fragment_4());
        list.add(new Fragment_5());
        list.add(new Fragment_6());
        list.add(new Fragment_7());
        viewPager.setAdapter(new FragmentBase(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                switch (i) {
                    case 0:
                        setBackgroud(view1, "View1");
                        break;
                    case 1:
                        setBackgroud(view2, "View1");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void ok() {
        progressDialog.dismiss();
    }

    private class FragmentBase extends FragmentStatePagerAdapter {

        public FragmentBase(FragmentManager fm) {
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

    private void setBackgroud(View view, String res) {
        view1.setBackgroundResource(COLOR_HIDE);
        view2.setBackgroundResource(COLOR_HIDE);
        view3.setBackgroundResource(COLOR_HIDE);
        view4.setBackgroundResource(COLOR_HIDE);
        view5.setBackgroundResource(COLOR_HIDE);
        view6.setBackgroundResource(COLOR_HIDE);
        view7.setBackgroundResource(COLOR_HIDE);
        view.setBackgroundResource(COLOR_SHOW);
        title.setText(res);
    }

}
