package com.himmiractivity.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import activity.hamir.com.himmir.R;

public class ResetNameDialog extends Dialog {

    public static final int DEFAULT_STYLE = R.style.DialogThemeTwo;

    Context context;
    private TextView tvHint, tvHead;
    Button btnComit, btnCanel;
    EditText etNewName;

    public ResetNameDialog(Context context) {
        this(context, DEFAULT_STYLE);
    }

    public ResetNameDialog(Context context, int style) {
        super(context, style);
        this.context = context;
        this.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_renname);
        tvHead = (TextView) findViewById(R.id.tv_head);
        btnComit = (Button) findViewById(R.id.btn_comit);
        btnCanel = (Button) findViewById(R.id.btn_canel);
        etNewName = (EditText) findViewById(R.id.et_new_name);
        tvHint = (TextView) findViewById(R.id.tv_head);
    }

    public String getName() {
        return etNewName.getText().toString().trim();
    }

    public void init(String head, String body, String no, String yes, String choose) {
        if (choose.equals("rename")) {
            tvHint.setVisibility(View.GONE);
            etNewName.setVisibility(View.VISIBLE);
            btnComit.setTextColor(ContextCompat.getColor(context, R.color.royalblue));
            btnCanel.setTextColor(ContextCompat.getColor(context, R.color.royalblue));

        } else {
            tvHint.setVisibility(View.VISIBLE);
            etNewName.setVisibility(View.GONE);
            tvHint.setText(body);
            btnComit.setTextColor(ContextCompat.getColor(context, R.color.black));
            btnCanel.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        tvHead.setVisibility(View.VISIBLE);
        tvHead.setText(head);
        btnCanel.setText(no);
        btnComit.setText(yes);
    }

    public void setListener() {
        btnComit.setOnClickListener((View.OnClickListener) context);
        btnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
    }

    public void close() {
        if (this != null && this.isShowing()) {
            dismiss();
        }
    }
}
