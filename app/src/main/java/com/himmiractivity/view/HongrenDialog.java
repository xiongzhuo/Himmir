package com.himmiractivity.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;


/**
 * @author tangpanxing
 */
public class HongrenDialog extends Dialog {
    int layoutRes;//布局文件
    Context context;

    public HongrenDialog(Context context) {
        super(context);
        this.context = context;
    }

    public HongrenDialog(Context context, int resLayout) {
        super(context, resLayout);
        this.context = context;
        this.layoutRes = resLayout;
    }

    /**
     * 自定义主题及布局的构造方法
     *
     * @param context
     * @param theme
     * @param resLayout
     */
    public HongrenDialog(Context context, int theme, int resLayout) {
        super(context, theme);
        this.context = context;
        this.layoutRes = resLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutRes);
    }
}