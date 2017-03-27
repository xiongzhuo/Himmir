package com.himmiractivity.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import activity.hamir.com.himmir.R;

public class HomeDialog extends Dialog {

    public static final int DEFAULT_STYLE = R.style.DialogThemeTwo;

    Context context;
    Button btnComit, btnCanel;
    EditText etNewName;

    public HomeDialog(Context context) {
        this(context, DEFAULT_STYLE);
    }

    public HomeDialog(Context context, int style) {
        super(context, style);
        this.context = context;
        this.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_home);
        btnComit = (Button) findViewById(R.id.btn_comit);
        btnCanel = (Button) findViewById(R.id.btn_canel);
        etNewName = (EditText) findViewById(R.id.et_home_name);
        setListener();
    }

    public String getName() {
        return etNewName.getText().toString().trim();
    }

    public void setListener() {
        btnComit.setOnClickListener((View.OnClickListener) context);
        btnCanel.setOnClickListener((View.OnClickListener) context);
    }

    public void close() {
        if (this != null && this.isShowing()) {
            dismiss();
        }
    }
}
