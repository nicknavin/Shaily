package com.app.session.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.session.R;
import com.app.session.adapter.SearchCurrencyNewAdapter;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.data.model.CurrencyBody;
import com.app.session.data.model.CurrencyRef;
import com.app.session.data.model.CurrencyRefResponse;
import com.app.session.data.model.CurrencyRoot;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchCurrencyActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    String key;
    CustomEditText edtSearch;

//    SearchCurrencyAdapter searchCurrencyAdapter;
    SearchCurrencyNewAdapter searchCurrencyNewAdapter;
    ImageView imgsearch, imgCross, imgBack;
    String languageCd = "";

    ArrayList<CurrencyRef> currencyRefsList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_language_category);

        if (getIntent().getStringExtra("language_cd") != null) {
            languageCd = getIntent().getStringExtra("language_cd");
        }

        initView();
       callGetCurrencyRef();


    }


    private void initView() {


        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        imgsearch = (ImageView) findViewById(R.id.imgsearch);
        imgCross = (ImageView) findViewById(R.id.imgCross);
        imgCross.setOnClickListener(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        edtSearch = (CustomEditText) findViewById(R.id.edtSearch);
        edtSearch.setHint(context.getResources().getString(R.string.search_currency));
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                key = s.toString().trim();


                searchCurrencyNewAdapter.getFilter().filter(key);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    searchCurrencyNewAdapter.getFilter().filter(key);


                    return true;
                }
                return false;
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgCross:
                edtSearch.setText(null);
//                setLayoutMargin(0);
                break;

        }
    }









    private void callGetCurrencyRef() {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<CurrencyRoot> call = apiInterface.reqGetCurrencies();

            call.enqueue(new Callback<CurrencyRoot>() {
                @Override
                public void onResponse(Call<CurrencyRoot> call, Response<CurrencyRoot> response)
                {
                    dismiss_loading();
                    CurrencyRoot root=response.body();
                    if(root.getStatus()==200)
                    {
                        ArrayList<CurrencyBody> currencyBodyArrayList=root.getCurrencyBodyArrayList();

                        searchCurrencyNewAdapter=new SearchCurrencyNewAdapter(context, currencyBodyArrayList, new ObjectCallback() {
                            @Override
                            public void getObject(Object object, int position, View view)
                            {
                                CurrencyBody body=(CurrencyBody)object;
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result",body.getCurrency_name());
                                returnIntent.putExtra("id",body.get_id());
                                setResult(Constant.REQUEST_RESULT, returnIntent);
                                finish();
                            }


                        });



                        recyclerView.setAdapter(searchCurrencyNewAdapter);

                    }
                }

                @Override
                public void onFailure(Call<CurrencyRoot> call, Throwable t) {
                    dismiss_loading();
                }
            });

//            call.enqueue(new Callback<CurrencyRefResponse>() {
//                @Override
//                public void onResponse(Call<CurrencyRefResponse> call, Response<CurrencyRefResponse> response) {
//                    dismiss_loading();
//                    CurrencyRefResponse responseBody = response.body();
//                    currencyRefsList= responseBody.getCurrencyRefsList();
//                    searchCurrencyAdapter=new SearchCurrencyAdapter(context, currencyRefsList, new CurrencyCallback() {
//                        @Override
//                        public void getTitle(CurrencyRef titleRef)
//                        {
//
//                                Intent returnIntent = new Intent();
//                                returnIntent.putExtra("result",titleRef.getCurrency_name_en());
//                                returnIntent.putExtra("id",titleRef.getCurrency_cd());
//                                setResult(Constant.REQUEST_RESULT, returnIntent);
//                                finish();
//
//                        }
//                    });
//                    recyclerView.setAdapter(searchCurrencyAdapter);
//                }
//
//                @Override
//                public void onFailure(Call<CurrencyRefResponse> call, Throwable t) {
//                    dismiss_loading();
//                }
//            });
        } else {
            showInternetConnectionToast();
        }
    }


}
