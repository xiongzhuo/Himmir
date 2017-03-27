package com.himmiractivity.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.himmiractivity.Utils.StringUtil;
import com.himmiractivity.entity.RedSignNew;

import activity.hamir.com.himmir.R;

public class ToastView {
    public static Toast toast;
    private static int time;
    private static Timer timer;

    public ToastView() {
    }

    public ToastView(Context context, String text) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.toast_view, null);
        TextView textView = (TextView) view.findViewById(R.id.toast_text);
        textView.setText(text);

        if (toast != null) {
            toast.cancel();
        } else {
            toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
        }
    }

    public void showToast(Context context, RedSignNew redSignNew) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.sign_toast_view, null);
        TextView textView = (TextView) view
                .findViewById(R.id.tv_sign_toas_head);
        TextView tv_sign_toas_content = (TextView) view
                .findViewById(R.id.tv_sign_toas_content);
        int[] color = new int[]{0xffffffff, 0xFFDF2321, 0xffffffff};
        String[] texts = new String[3];
        String[] texts1 = new String[3];
        float[] size = new float[3];
        String recordMessage = redSignNew.getRecordMessage();
        String signMoney = redSignNew.getSignMoney() + "";
        String signAwardMessage = redSignNew.getSignAwardMessage();
        String signAwardMoney = redSignNew.getSignAwardMoney() + "";
        //
        if (recordMessage.indexOf(signMoney) > -1) {
            String str1 = recordMessage.substring(0,
                    recordMessage.indexOf(signMoney));
            String str2 = recordMessage.substring(
                    recordMessage.indexOf(signMoney), recordMessage.length())
                    .replace(signMoney, "");
            texts[0] = str1;
            texts[1] = signMoney;
            texts[2] = str2;
            size[0] = 1.0f;
            size[1] = 1.5f;
            size[2] = 1.0f;
            StringUtil.setSpannableString(texts, color, size, textView);
        }
        if (signAwardMessage.indexOf(signAwardMoney) > -1) {
            String str1 = signAwardMessage.substring(0,
                    signAwardMessage.indexOf(signAwardMoney));
            String str2 = signAwardMessage.substring(
                    signAwardMessage.indexOf(signAwardMoney),
                    signAwardMessage.length()).replace(signAwardMoney, "");
            texts1[0] = str1;
            texts1[1] = signAwardMoney;
            texts1[2] = str2;
            size[0] = 1.0f;
            size[1] = 1.5f;
            size[2] = 1.0f;
            StringUtil.setSpannableString(texts1, color, size,
                    tv_sign_toas_content);
        }
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }

    // 设置toast显示位置
    public void setGravity(int gravity, int xOffset, int yOffset) {
        toast.setGravity(gravity, xOffset, yOffset);
    }

    // 设置toast显示时间
    public void setDuration(int duration) {
        toast.setDuration(duration);
    }

    // 设置toast显示时间(自定义时间)
    public void setLongTime(int duration) {
        time = duration;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (time - 1000 >= 0) {
                    show();
                    time = time - 1000;
                } else {
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    // 显示toast
    public void show() {
        if (toast != null) {
            toast.show();
        }
    }

    // 取消toast
    public void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
