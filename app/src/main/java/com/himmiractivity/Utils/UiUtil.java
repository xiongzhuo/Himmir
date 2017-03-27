package com.himmiractivity.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import activity.hamir.com.himmir.R;

/**
 * Created by Administrator on 2016/3/9.
 */
public class UiUtil {
    public interface downOnclick {
        void onTvkClick();

        void onTvcClick();
    }

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

    public static void showPopDown(View v, Context context, final downOnclick downOnclick) {
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundColor(context.getResources().getColor(R.color.gray));
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView tvk = new TextView(context);
        LinearLayout.LayoutParams paramsk = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvk.setGravity(Gravity.CENTER);
        tvk.setPadding(0, 15, 0, 15);
        tvk.setLayoutParams(paramsk);
        tvk.setText("客厅");
        tvk.setTextColor(Color.WHITE);
        TextView tvc = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvc.setGravity(Gravity.CENTER);
        tvc.setPadding(0, 15, 0, 15);
        tvc.setLayoutParams(params);
        tvc.setText("厨房");
        tvc.setTextColor(Color.WHITE);
        layout.addView(tvc);
        layout.addView(tvk);

        final PopupWindow popupWindow = new PopupWindow(layout, 200, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.getContentView().measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAsDropDown(v, v.getWidth() / 2 - 100, 0, Gravity.NO_GRAVITY);
        tvk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downOnclick.onTvkClick();
                popupWindow.dismiss();
            }
        });
        tvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downOnclick.onTvcClick();
                popupWindow.dismiss();
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
