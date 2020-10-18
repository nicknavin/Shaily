package com.app.session.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.base.BaseActivity;

public class CouresesActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coureses);
        initView();
    }
    private void initView()
    {
        ((ImageView)findViewById(R.id.imgBack)).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.imgBack:
                onBackPressed();
                break;

        }
}

}
