package com.himmiractivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.TextView;

import com.himmiractivity.Adapter.ListAdapter;
import com.himmiractivity.App;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.circular_progress_bar.CircularProgressBar;
import com.himmiractivity.entity.ManagerShardBean;
import com.himmiractivity.interfaces.OnllClick;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.DelSharedUseRequest;
import com.himmiractivity.request.ManagerShardRequest;
import com.himmiractivity.xlistview.IXListViewRefreshListener;
import com.himmiractivity.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import activity.hamir.com.himmir.R;
import butterknife.BindView;


public class ManagerSharedActivity extends BaseBusActivity implements AdapterView.OnItemClickListener, IXListViewRefreshListener, OnllClick {
    @BindView(R.id.lv_manager)
    XListView listView;
    @BindView(R.id.progress)
    CircularProgressBar progressBar;
    @BindView(R.id.vs)
    ViewStub vs;
    List<ManagerShardBean.ManagerShardSum> list = new ArrayList<>();
    ManagerShardBean managerShardBean;
    ListAdapter adapter;
    int delPos = 0;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.MSG_DELETE:
                    list.remove(delPos);
                    adapter.setLists(list);
                    break;
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    managerShardBean = (ManagerShardBean) msg.obj;
                    if (!managerShardBean.getShareUserList().isEmpty()) {
                        vs.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        list = managerShardBean.getShareUserList();
                        adapter = new ListAdapter(ManagerSharedActivity.this, list);
                        adapter.setOnLLClick(ManagerSharedActivity.this);
                        listView.setAdapter(adapter);
                        onlod();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        vs.inflate();
                    }
                    break;
                case StatisConstans.MSG_RECEIVED_BOUND:
                    vs.inflate();
                    progressBar.setVisibility(View.GONE);
                    list.clear();
                    adapter.setLists(list);
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentLayoutId() {
        return R.layout.manager_shared;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        setMidTxt("管理分享");
        initTitleBar();
        initRechclerView();
        listView.setOnItemClickListener(this);
        listView.setPullRefreshEnable(this);
        // 魅族机型隐藏HODE
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        listView.NotRefreshAtBegin();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void initRechclerView() {
        ManagerShardRequest allDeviceInfoRequest = new ManagerShardRequest(sharedPreferencesDB, this, mHandler);
        try {
            allDeviceInfoRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh() {
        initRechclerView();
    }

    private void onlod() {
        listView.stopLoadMore();// 停止加载更多，重置footer view
        listView.stopRefresh();// 停止刷新，重置header view
        // listView.setRefreshTime("刚刚");//设置刷新的时间
    }

    @Override
    public void onLLClick(int position) {
        delPos = position;
        try {
            DelSharedUseRequest delSharedUseRequest = new DelSharedUseRequest(sharedPreferencesDB, list.get(position).getUser_key(), ManagerSharedActivity.this, mHandler);
            delSharedUseRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
