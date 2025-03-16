package com.example.studentscoremanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class CustomDialog extends Dialog implements android.view.View.OnClickListener {
    public Button yes, no;
    public Activity activity;
    EventListener onYesClicked;

    public CustomDialog(@NonNull Context context, EventListener onYesClicked) {
        super(context);

        activity = (Activity) context;
        this.onYesClicked = onYesClicked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_dialog);

        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                onYesClicked.doSomething();
                break;
            case R.id.btn_no:
//                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}