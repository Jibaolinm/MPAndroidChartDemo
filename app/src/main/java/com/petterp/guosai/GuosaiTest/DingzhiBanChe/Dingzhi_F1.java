package com.petterp.guosai.GuosaiTest.DingzhiBanChe;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.petterp.guosai.PostUtils;
import com.petterp.guosai.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Petterp on 2019/5/10
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class Dingzhi_F1 extends Fragment implements View.OnClickListener {
    private Button back;
    private TextView title;
    private ExpandableListView listview;
    private WeizhangBean bean;
    private ListViewAdapter adapter;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dinghi_f1, container, false);
        initView(view);
        setPost();
        return view;
    }

    private void initView(View view) {
        back = (Button) view.findViewById(R.id.back);
        title = (TextView) view.findViewById(R.id.title);
        listview = (ExpandableListView) view.findViewById(R.id.listview);
        title.setText("定制班车");
        back.setOnClickListener(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("正在请求");
        progressDialog.setMessage("Loading。。。");
        progressDialog.show();
        listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Frits.name = bean.getROWS_DETAIL().get(groupPosition).getSites().get(childPosition);
                Frits.money = bean.getROWS_DETAIL().get(groupPosition).getTicket()+"";
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
        }
    }

    private class ListViewAdapter extends BaseExpandableListAdapter {


        @Override
        public int getGroupCount() {
            return bean.getROWS_DETAIL().size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return bean.getROWS_DETAIL().get(groupPosition).getSites().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return bean.getROWS_DETAIL().get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return bean.getROWS_DETAIL().get(groupPosition).getSites().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = View.inflate(getActivity(), R.layout.dingzhi_list_item, null);
            TextView dingzhi_list_id = view.findViewById(R.id.dingzhi_list_id);
            TextView dingzhi_list_name = view.findViewById(R.id.dingzhi_list_name);
            TextView dingzhi_list_time1 = view.findViewById(R.id.dingzhi_list_time1);
            TextView dingzhi_list_time2 = view.findViewById(R.id.dingzhi_list_time2);
            TextView ppp = view.findViewById(R.id.ppp);
            dingzhi_list_id.setText(bean.getROWS_DETAIL().get(groupPosition).getId() + "号线");
            dingzhi_list_name.setText(bean.getROWS_DETAIL().get(groupPosition).getTime().get(0).getSite() + "—" +
                    bean.getROWS_DETAIL().get(groupPosition).getTime().get(1).getSite());
            dingzhi_list_time1.setText(bean.getROWS_DETAIL().get(groupPosition).getTime().get(0).getStarttime() + "-" +
                    bean.getROWS_DETAIL().get(groupPosition).getTime().get(0).getEndtime());
            dingzhi_list_time2.setText(bean.getROWS_DETAIL().get(groupPosition).getTime().get(1).getStarttime() + "-" +
                    bean.getROWS_DETAIL().get(groupPosition).getTime().get(1).getEndtime());
            ppp.setText("票价：￥" + bean.getROWS_DETAIL().get(groupPosition).getTicket() + " 里程：" + bean.getROWS_DETAIL().get(groupPosition).getMileage());
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = View.inflate(getActivity(), R.layout.dingzhi_list_item2, null);
            TextView text_name_no = view.findViewById(R.id.text_name_no);
            TextView text_name = view.findViewById(R.id.text_name);
            if (childPosition == 0) {
                text_name_no.setText("起点：");
                text_name_no.setVisibility(View.VISIBLE);
            }
            if (childPosition == bean.getROWS_DETAIL().get(groupPosition).getSites().size() - 1) {

                text_name_no.setText("终点：");
                text_name_no.setVisibility(View.VISIBLE);
            }
            text_name.setText(bean.getROWS_DETAIL().get(groupPosition).getSites().get(childPosition));
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private void setPost() {
        PostUtils.Builder().setOkHttpClient("GetBusInfo.do", "{\"Line\":0,\"UserName\":\"user1\"}", new PostUtils.Post() {
            @Override
            public void success(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("RESULT").equals("S")) {
                        Gson gson = new Gson();
                        bean = gson.fromJson(s, WeizhangBean.class);
                        adapter = new ListViewAdapter();
                        listview.setAdapter(adapter);
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "网络请求失败，正在尝试重新请求。。。", Toast.LENGTH_SHORT).show();
                    setPost();
                }
            }
        });
    }
}
