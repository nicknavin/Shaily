package com.app.session.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.app.session.R;
import com.app.session.adapter.FinancialAdapter;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;
import com.app.session.model.Financial;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FinancialActivity extends BaseActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Context context;
    FinancialAdapter financialAdapter;
    ArrayList<Financial> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial);
        context = this;
        initView();
    }

    public void initView() {
        ((ImageView)findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((CustomTextView)findViewById(R.id.header)).setText(context.getResources().getString(R.string.finacial));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        for (int i = 0; i < 10; i++)
        {
            Financial financial=new Financial();
            financial.setAmount("500");
            financial.setDate("10Feb2018");
            financial.setUser_name("Sharma ji");
            arrayList.add(financial);
        }
        financialAdapter = new FinancialAdapter(context, arrayList);
        recyclerView.setAdapter(financialAdapter);

    }

}
