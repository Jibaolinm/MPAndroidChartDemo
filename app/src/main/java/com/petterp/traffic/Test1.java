package com.petterp.traffic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.petterp.test51.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Petterp on 2019/5/2
 * Summary:红绿灯设置类
 * 邮箱：1509492795@qq.com
 */
public class Test1 extends Fragment {
    private FragmentAdapter adapter;
    private List<Frits> list = new ArrayList<>();
    private ListView listView;
    private CountDownTimer count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        listView = view.findViewById(R.id.list_view);
        setData();
        return view;
    }


    //设置数据
    private void setData() {
        //添加默认配置时间
        list.add(new Frits(1, 5, 5, 5));
        list.add(new Frits(2, 5, 5, 5));
        list.add(new Frits(3, 5, 5, 5));
        adapter = new FragmentAdapter();
        listView.setAdapter(adapter);
        count = new CountDownTimer(Integer.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                init();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    //执行加载逻辑
    private void init(){
        final  int size=list.size();
        for (int i=0;i<size;i++){
            setColor(i);
            setColor2(i);
            adapter.notifyDataSetChanged();
        }
    }

    //A栏目设置 顺序：绿，黄，红
    private void setColor(int i){
        if (list.get(i).stateAgre>=0) {
            //设置相应绿灯
            list.get(i).setModeA(list.get(i).stateAgre--);
            list.get(i).setColorA(R.drawable.green);
        } else if (list.get(i).stateAyel>=0) {
            //设置相应黄灯
            list.get(i).setModeA(list.get(i).stateAyel--);
            list.get(i).setColorA(R.drawable.yellow);
        } else if (list.get(i).stateAred>=0) {
            //设置相应红灯
            list.get(i).setModeA(list.get(i).stateAred--);
            list.get(i).setColorA(R.drawable.red);
        } else {
            list.get(i).stateAred=list.get(i).red;
            list.get(i).stateAyel=list.get(i).yel;
            list.get(i).stateAgre=list.get(i).gre;
            setColor(i);
        }
    }

    //B栏目设置  顺序：红，绿，黄
    private void setColor2(int i){
        if (list.get(i).stateBred >= 0) {
            //设置相应红灯
            list.get(i).setModeB(list.get(i).stateBred--);
            list.get(i).setColorB(R.drawable.red);
        } else if (list.get(i).stateBgre >= 0) {
            //设置相应绿灯
            list.get(i).setModeB(list.get(i).stateBgre--);
            list.get(i).setColorB(R.drawable.green);
        } else if (list.get(i).stateByel >= 0) {
            //设置相应黄灯
            list.get(i).setModeB(list.get(i).stateByel--);
            list.get(i).setColorB(R.drawable.yellow);
        } else {
            list.get(i).stateBred=list.get(i).red;
            list.get(i).stateByel=list.get(i).yel;
            list.get(i).stateBgre=list.get(i).gre;
            setColor2(i);
        }
    }


    private class FragmentAdapter extends BaseAdapter {
        View rootView;
        TextView tv_line;
        TextView tv_config;
        TextView tv_green_line;
        TextView tv_yellow_line;
        TextView tv_red_line;
        TextView tv_h;
        TextView tv_v;
        TextView tv_number1;
        LinearLayout linear1;
        TextView tv_number2;
        LinearLayout linear2;
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder") View view = View.inflate(getContext(), R.layout.item, null);
            this.rootView = view;
            initid();
            //设置数据
            tv_number1.setText(list.get(position).getModeA()+"");
            linear1.setBackgroundResource(list.get(position).getColorA());
            tv_number2.setText(list.get(position).getModeB()+"");
            linear2.setBackgroundResource(list.get(position).getColorB());
            //点击事件
            view.findViewById(R.id.bt_h).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(position).stateAgre=30;
                }
            });
            view.findViewById(R.id.bt_v).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(position).stateBred=30;
                }
            });
            return view;
        }
        private void initid(){
            this.tv_line = (TextView) rootView.findViewById(R.id.tv_line);
            this.tv_config = (TextView) rootView.findViewById(R.id.tv_config);
            this.tv_green_line = (TextView) rootView.findViewById(R.id.tv_green_line);
            this.tv_yellow_line = (TextView) rootView.findViewById(R.id.tv_yellow_line);
            this.tv_red_line = (TextView) rootView.findViewById(R.id.tv_red_line);
            this.tv_h = (TextView) rootView.findViewById(R.id.tv_h);
            this.tv_v = (TextView) rootView.findViewById(R.id.tv_v);
            this.tv_number1 = (TextView) rootView.findViewById(R.id.tv_number1);
            this.linear1 = (LinearLayout) rootView.findViewById(R.id.linear1);
            this.tv_number2 = (TextView) rootView.findViewById(R.id.tv_number2);
            this.linear2 = (LinearLayout) rootView.findViewById(R.id.linear2);
        }
    }
}
