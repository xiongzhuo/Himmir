package com.himmiractivity.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import activity.hamir.com.himmir.R;

/**
 * Created by Administrator on 2016/3/9.
 */
public class UiUtil {

    public static void Toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void showPopDown(View v, Context context, List<String> list) {
        WindowManager wm = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.lv_gray_bg));
        layout.setOrientation(LinearLayout.VERTICAL);
        ListView listView = new ListView(context);
        LinearLayout.LayoutParams paramsk = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(paramsk);
        ArrayAdapter<String> array = new ArrayAdapter<>(context,
                R.layout.lv_textview_item, list);
        listView.setAdapter(array);
        layout.addView(listView);

        final PopupWindow popupWindow = new PopupWindow(layout, 300, height / 3, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.getContentView().measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAsDropDown(v, v.getWidth() / 2 - 150, 0, Gravity.NO_GRAVITY);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static void showViewInVisible(View view, boolean show) {
        if (view == null)
            return;
        if (show) {
            if (view.getVisibility() == View.VISIBLE)
                return;
            view.setVisibility(View.VISIBLE);
        } else {
            if (view.getVisibility() == View.INVISIBLE)
                return;
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void shwoViewGone(View view, boolean show) {
        if (show) {
            if (view.getVisibility() == View.VISIBLE)
                return;
            view.setVisibility(View.VISIBLE);
        } else {
            if (view.getVisibility() == View.GONE)
                return;
            view.setVisibility(View.GONE);
        }
    }


}
