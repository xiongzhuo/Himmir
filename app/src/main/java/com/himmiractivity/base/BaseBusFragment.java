package com.himmiractivity.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.himmiractivity.Utils.ToastUtils;

import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ProjectName：WisdomBus
 * PackageName：com.zhcx.wisdombus.base
 * description:
 * Date：2017/3/8
 * Author：xz
 * ClassName:BaseBusFragment
 **/
public abstract class BaseBusFragment extends Fragment {
    protected View mViewLayout;
    protected Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == this.mViewLayout) {
            this.mViewLayout = inflater.inflate(this.getContentLayoutId(), container, false);
        }
        if (null != this.mViewLayout.getParent()) {
            ViewGroup parent = (ViewGroup) this.mViewLayout.getParent();
            parent.removeView(this.mViewLayout);
        }
        mContext = this.getActivity();
        checkNotNull(mViewLayout);
        ButterKnife.bind(this, mViewLayout);
        this.initView(this.mViewLayout, savedInstanceState);
        this.initData();
        LogUtils.e("onCreateView");
        return mViewLayout;
    }

    /**
     * 视图id
     *
     * @return layout id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     *
     * @param view
     * @param savedInstanceState
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * Initialize the Activity data
     */
    protected abstract void initData();

    /**
     * Toast
     *
     * @param msg
     */
    public void showToast(String msg) {
        this.showToast(msg, Toast.LENGTH_SHORT);
    }

    public void showToast(String msg, int duration) {
        if (msg == null) return;
        if (duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG) {
            ToastUtils.show(this.getActivity(), msg, duration);
        } else {
            ToastUtils.show(this.getActivity(), msg, ToastUtils.LENGTH_SHORT);
        }
    }

    public void showToast(int resId) {
        this.showToast(resId, Toast.LENGTH_SHORT);
    }

    public void showToast(int resId, int duration) {
        if (duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG) {
            ToastUtils.show(this.getActivity(), resId, duration);
        } else {
            ToastUtils.show(this.getActivity(), resId, ToastUtils.LENGTH_SHORT);
        }
    }
}
