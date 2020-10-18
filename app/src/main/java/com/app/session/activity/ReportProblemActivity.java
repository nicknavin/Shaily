package com.app.session.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;

public class ReportProblemActivity extends BaseActivity implements View.OnClickListener{

    Context context;
    CustomTextView btn_cancel,btn_Send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        context=this;
        initView();
    }
    private void initView()
    {
        ((ImageView)findViewById(R.id.imgBack)).setOnClickListener(this);
        btn_cancel=(CustomTextView)findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_Send=(CustomTextView)findViewById(R.id.btn_Send);
        btn_Send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btn_Send:
                break;
            case R.id.btn_cancel:

                break;
        }




    }
}
