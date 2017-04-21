package com.himmiractivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.himmiractivity.Adapter.AddedAdapter;
import com.himmiractivity.Adapter.ListAdapter;
import com.himmiractivity.Adapter.RvcAdapter;
import com.himmiractivity.App;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.base.BaseBusNoSocllowActivity;
import com.himmiractivity.entity.ManagerShardBean;
import com.himmiractivity.interfaces.MyOnSlipStatusListener;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.AddedShardRequest;
import com.himmiractivity.view.AlxRefreshLoadMoreRecyclerView;
import com.himmiractivity.view.SwipeListLayout;
import com.himmiractivity.xlistview.XListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import activity.hamir.com.himmir.R;
import butterknife.BindView;


public class AddedSharedActivity extends BaseBusNoSocllowActivity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    @BindView(R.id.lv_added)
    XListView listView;
    List<ManagerShardBean.ManagerShardSum> list = new ArrayList<>();
    private Set<SwipeListLayout> sets = new HashSet<>();
    ManagerShardBean managerShardBean;
    AddedAdapter adapter;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    if (msg.obj != null) {
                        managerShardBean = (ManagerShardBean) msg.obj;
                        list = managerShardBean.getShareUserList();
                        adapter = new AddedAdapter(AddedSharedActivity.this, sets, list);
                        listView.setAdapter(adapter);
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentLayoutId() {
        return R.layout.added_shared;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        setMidTxt("已加分享");
        initTitleBar();
        initRechclerView();
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
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
        super.onDestroy();
    }

    private void initRechclerView() {
        AddedShardRequest allDeviceInfoRequest = new AddedShardRequest(sharedPreferencesDB, this, mHandler);
        try {
            allDeviceInfoRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
            case SCROLL_STATE_TOUCH_SCROLL:
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                break;

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
