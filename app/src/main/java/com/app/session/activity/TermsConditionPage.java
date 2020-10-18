package com.app.session.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.app.session.R;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;

public class TermsConditionPage extends BaseActivity implements View.OnClickListener {

    CustomTextView btn_continue, btn_exit;
CheckBox checkTerms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition_page);
        initView();
    }

    private void initView() {
        checkTerms=(CheckBox)findViewById(R.id.checkTC);
    btn_continue=(CustomTextView)findViewById(R.id.btn_continue);
    btn_continue.setOnClickListener(this);
    btn_exit=(CustomTextView)findViewById(R.id.btn_exit);
    btn_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_continue:
                if(checkTerms.isChecked()) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                else
                {
                    showToast("Please accept Terms and Conditions");
                }
                break;
            case R.id.btn_exit:


                finishAffinity();
                System.exit(0);
                break;
        }
    }
}
