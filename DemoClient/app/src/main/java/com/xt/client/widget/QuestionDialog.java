package com.xt.client.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xt.client.R;

public class QuestionDialog extends Dialog {

    private TextView mTvTitle;
    private Button mBtnYes;
    private Button mBtnNo;

    public QuestionDialog(Context context) {
        super(context);

        setContentView(R.layout.dialog_question);

        mTvTitle = findViewById(R.id.tv_title);
        mBtnYes = findViewById(R.id.btn_yes);
        mBtnNo = findViewById(R.id.btn_no);
        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = mTvTitle.getText().toString();
                mTvTitle.setText(s + "？");
            }
        });

    }

    public void show(String title) {
        mTvTitle.setText(title);
        show();
    }
}