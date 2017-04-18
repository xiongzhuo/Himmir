package com.himmiractivity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.himmiractivity.Adapter.RoomAdapter;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.ArticleInfo;
import com.himmiractivity.entity.UserRoom;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.DefaultUserRoomRequest;
import com.himmiractivity.view.HomeDialog;
import com.himmiractivity.xlistview.XListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;

/**
 * Created by Administrator on 2017/3/21.
 */

public class InstallRoomActivity extends BaseBusActivity {
    @BindView(R.id.listview_room)
    XListView listview;
    @BindView(R.id.tv_add_room)
    TextView tvAddRoom;
    HomeDialog homeDialog;
    ArticleInfo articleInfo;
    RoomAdapter adapter;
    List<UserRoom> list = new ArrayList<>();
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    articleInfo = (ArticleInfo) msg.obj;
                    list = articleInfo.getUserRoom();
                    adapter = new RoomAdapter(
                            InstallRoomActivity.this, list);
                    listview.setAdapter(adapter);
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    @Override
    protected int getContentLayoutId() {
        return R.layout.install_room_activity;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setMidTxt("选择安装房间");
        initTitleBar();
        setRightView("确定", true);
        tvAddRoom.setOnClickListener(this);
        if (getIntent().getExtras().getSerializable("articleInfo") != null) {
            Bundle bundle = getIntent().getExtras();
            articleInfo = (ArticleInfo) bundle.getSerializable("articleInfo");
            adapter = new RoomAdapter(
                    InstallRoomActivity.this, articleInfo.getUserRoom());
            adapter.setPosition2(bundle.getInt("position", -1));
            listview.setAdapter(adapter);
        } else {
            try {
                DefaultUserRoomRequest defaultUserRoomRequest = new DefaultUserRoomRequest(sharedPreferencesDB, InstallRoomActivity.this, handler);
                defaultUserRoomRequest.requestCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            case R.id.btn_right:
                comit();
                break;
            case R.id.btn_canel:
                homeDialog.close();
                break;
            case R.id.btn_comit:
                if (TextUtils.isEmpty(homeDialog.getName())) {
                    ToastUtil.show(InstallRoomActivity.this, "请输入房间名称");
                    return;
                }
                boolean isRoom = false;
                for (int i = 0; i < list.size(); i++) {
                    if (homeDialog.getName().equals(list.get(i).getRoom_name())) {
                        isRoom = true;
                    }
                }
                if (isRoom) {
                    ToastUtil.show(InstallRoomActivity.this, "请输入不同的房间名称");
                    return;
                }
                UserRoom dafalutUserRoom = new UserRoom();
                dafalutUserRoom.setRoom_name(homeDialog.getName());
                list.add(dafalutUserRoom);
                if (adapter != null) {
                    adapter.setData(list);
                } else {
                    adapter = new RoomAdapter(
                            InstallRoomActivity.this, list);
                    listview.setAdapter(adapter);
                }
                homeDialog.close();
                break;
            case R.id.tv_add_room:
                if (homeDialog == null) {
                    homeDialog = new HomeDialog(InstallRoomActivity.this);
                }
                Window w = homeDialog.getWindow();
                if (w != null) {
                    w.setWindowAnimations(R.style.mystyle1);
                }
                homeDialog.show();
                //只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
                homeDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                int width = getWindowManager().getDefaultDisplay().getWidth();
                homeDialog.getWindow().setLayout((int) (width * 0.8),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                homeDialog.setListener();
                break;
            default:
                break;

        }
    }

    private void comit() {
        if (adapter.getPosition2() < 0) {
            ToastUtil.show(InstallRoomActivity.this, "至少选择一个房间");
            return;
        }
        Intent intent = new Intent();
        Bundle bun = new Bundle();
        bun.putSerializable(StatisConstans.KEY_SAVE_LABLE,
                (Serializable) articleInfo);
        bun.putInt("position", adapter.getPosition2());
        intent.putExtras(bun);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
