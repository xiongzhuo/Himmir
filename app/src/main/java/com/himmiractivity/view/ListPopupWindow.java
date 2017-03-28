package com.himmiractivity.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.PopupWindow;
import android.widget.TextView;

import com.himmiractivity.Utils.UiUtil;
import com.himmiractivity.activity.MainActivity;

import java.util.List;

import activity.hamir.com.himmir.R;

/**
 * Created by liguoqing on 2016/4/28.
 */
public class ListPopupWindow extends PopupWindow {
    public interface downOnclick {
        void onDownItemClick(int position);
    }

    private Context mContext;
    private List<String> list;

    public ListPopupWindow(Context context, View parentView, List<String> list, downOnclick downOnclick) {
        /// backgroundAlpha(0.5f);
        mContext = context;
        this.list = list;
        initPopupWindow(parentView, downOnclick);
    }

    private void initPopupWindow(View view, final downOnclick downOnclick) {
        WindowManager wm = (WindowManager) mContext.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        View popView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow, null);

        final PopupWindow mPopupWindow = new PopupWindow(popView, 300,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);

        MyMaxHeightListView listView = (MyMaxHeightListView) popView.findViewById(R.id.card_ListView);
        // 设置 PopupWindow 最大高度，如果再高，改成滑动
        listView.setListViewHeight((int) (height / 3));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.lv_textview_item, list);
        listView.setAdapter(adapter);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mPopupWindow.showAsDropDown(view, view.getWidth() / 2 - 150, 0, Gravity.NO_GRAVITY);
        mPopupWindow.update();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 处理点中需要处理的问题；
                downOnclick.onDownItemClick(position);
                mPopupWindow.dismiss();
            }
        });

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((MainActivity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((MainActivity) mContext).getWindow().setAttributes(lp);
    }
}
