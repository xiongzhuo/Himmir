package com.himmiractivity.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneDialog extends Dialog {

    public static final int DEFAULT_STYLE = R.style.DialogThemeTwo;

    Context context;
    @BindView(R.id.tv_service_phone)
    TextView tvServicePhone;
    @BindView(R.id.tv_sell_phone)
    TextView tvSellPhone;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.ll_dimis)
    LinearLayout llDimis;

    public PhoneDialog(Context context) {
        this(context, DEFAULT_STYLE);
    }

    public PhoneDialog(Context context, int style) {
        super(context, style);
        this.context = context;
        this.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_phone);
        ButterKnife.bind(this);
        setListener();
    }

    public void setListener() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        tvSellPhone.setOnClickListener((View.OnClickListener) context);
        tvServicePhone.setOnClickListener((View.OnClickListener) context);
        llDimis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
    }

    public String getServicePhone() {
        return tvServicePhone.getText().toString().trim();
    }

    public String getSellPhone() {
        return tvSellPhone.getText().toString().trim();
    }

    public void close() {
        if (this != null && this.isShowing()) {
            dismiss();
        }
    }
}
