package com.himmiractivity.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.himmiractivity.circular_progress_bar.DrawHookView;

import activity.hamir.com.himmir.R;

public class ModifySuccessView extends Dialog {

    public static final int DEFAULT_STYLE = R.style.DialogTheme;
    DrawHookView progress;

    Context context;
    private TextView textView;
    LinearLayout ll_close;

    public ModifySuccessView(Context context) {
        this(context, DEFAULT_STYLE);
    }

    public ModifySuccessView(Context context, int style) {
        super(context, style);
        this.context = context;
        this.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_success);
        textView = (TextView) findViewById(R.id.text_message);
        progress = (DrawHookView) findViewById(R.id.progress);
        ll_close = (LinearLayout) findViewById(R.id.ll_close);
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        progress.setmCallBack(new DrawHookView.CallBack() {
            @Override
            public void doSomeThing() {
                close();
            }
        });
    }

    /**
     * 调改方法前 请先调show方法
     *
     * @param message
     */
    public void setMessage(String message) {
        textView.setText(message);
    }

    public void setMessage(int resId) {
        textView.setText(resId);
    }

    public void close() {
        if (this != null && this.isShowing()) {
            dismiss();
        }
    }
}
