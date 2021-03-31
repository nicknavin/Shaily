package com.app.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.ReqUpdateBank;
import com.app.session.data.model.Root;
import com.app.session.data.model.UserBank;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBankDetail extends BaseActivity implements View.OnClickListener {

    CustomEditText edt_Bankname, edt_BranchName, edt_AccountNo, edt_BankIbn, edt_swifcode, edt_branch_address, edt_pref_lang;
    CustomTextView txtUpdate_bank;
Context context;
UserBank bank;
// 9926912138
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_detail);
        if(getIntent().getParcelableExtra("DATA")!=null)
        {
            bank=getIntent().getParcelableExtra("DATA");
        }
        context=this;
        initView();
    }

    public void initView()
    {
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        edt_Bankname = (CustomEditText) findViewById(R.id.edt_Bankname);
        edt_BranchName = (CustomEditText) findViewById(R.id.edt_BranchName);
        edt_AccountNo = (CustomEditText) findViewById(R.id.edt_AccountNo);
        edt_BankIbn = (CustomEditText) findViewById(R.id.edt_BankIbn);
        edt_swifcode = (CustomEditText) findViewById(R.id.edt_swifcode);
        edt_branch_address = (CustomEditText) findViewById(R.id.edt_branch_address);
        txtUpdate_bank = (CustomTextView) findViewById(R.id.txt_bank_Update);
        txtUpdate_bank.setOnClickListener(this);

        if(bank!=null)
        {
            edt_AccountNo.setText(bank.getAccount_number());
            edt_BankIbn.setText(bank.getIban_code());
            edt_Bankname.setText(bank.getBank_name());
            edt_BranchName.setText(bank.getBank_address());
            edt_swifcode.setText(bank.getSwift_code());

        }


    }

    private void callGetProfile(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();


                    try {

                        JSONArray jsonArray = js.getJSONArray("User_Detail");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        JSONArray bank_detail = js.getJSONArray("bank_detail");
                        if (bank_detail.length() > 0) {
                            JSONObject objectbank = bank_detail.getJSONObject(0);
                            edt_Bankname.setText(objectbank.getString("bank_name"));
                            edt_AccountNo.setText(objectbank.getString("account_number"));
                            edt_BankIbn.setText(objectbank.getString("iban_code"));
                            edt_swifcode.setText(objectbank.getString("swift_code"));
                            edt_BranchName.setText(objectbank.getString("bank_address"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

                    } else {

                    }
                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                    MyDialog.iPhone(getString(R.string.something_wrong), context);
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            MyDialog.iPhone(getString(R.string.something_wrong), context);
        }
    }

    private Map<String, Object> getParamProfile() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);

        return params;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_bank_Update:



                    if (validBank()) {

                        callUpdateBankDetail();
                    }

                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            default:
                break;
        }

    }


    private boolean validBank() {
        if (edt_Bankname.getText().toString().isEmpty()) {
            edt_Bankname.setError(context.getResources().getString(R.string.valid_bank_name));
            return false;
        }
        if (edt_BranchName.getText().toString().isEmpty()) {
            edt_BranchName.setError(context.getResources().getString(R.string.valid_bank_branc));
            return false;
        }
//        if (edt_branch_address.getText().toString().isEmpty()) {
//            edt_branch_address.setError(context.getResources().getString(R.string.valid_bank_address));
//            return false;
//        }
        if (edt_AccountNo.getText().toString().isEmpty()) {
            edt_AccountNo.setError(context.getResources().getString(R.string.valid_bank_account));
            return false;
        }

        if (edt_BankIbn.getText().toString().isEmpty()) {
            edt_BankIbn.setError(context.getResources().getString(R.string.valid_bank_ibn));
            return false;
        }
        if (edt_swifcode.getText().toString().isEmpty()) {
            edt_swifcode.setError(context.getResources().getString(R.string.valid_bank_swif));
            return false;
        }


        return true;
    }

    private void callUpdateBankDetail() {

        try {
            showLoading();

            ReqUpdateBank  reqUpdateBank=new ReqUpdateBank();
            reqUpdateBank.setUserId(userId);
            UserBank userBank=new UserBank();
            userBank.setAccount_number(edt_AccountNo.getText().toString());
            userBank.setBank_address(edt_BranchName.getText().toString());
            userBank.setBank_name(edt_Bankname.getText().toString());
            userBank.setIban_code(edt_BankIbn.getText().toString());
            userBank.setSwift_code(edt_swifcode.getText().toString());
            userBank.setCurrency_symbol("en");
            reqUpdateBank.setUserBank(userBank);
            ApiInterface apiInterface= ApiClientNew.getClient().create(ApiInterface.class);
            Call<Root> call=apiInterface.reqUpateUserBankDetails(accessToken,reqUpdateBank);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response)
                {
                  dismiss_loading();
                  if(response.body()!=null)
                  {
                      if(response.body().getStatus()==200)
                      {
                          Intent intent = new Intent();
                          setResult(Constant.PAGE_REFRESH, intent);
                          finish();
                      }
                  }
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
dismiss_loading();
                }
            });






        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParamBankDetail() {

        Map<String, Object> params = new HashMap<>();
        params.put("user_cd", userId);
        params.put("token_id", accessToken);
        params.put("bank_name", edt_Bankname.getText().toString());
        params.put("account_number", edt_AccountNo.getText().toString());
        params.put("iban_code", edt_BankIbn.getText().toString());
        params.put("swift_code", edt_swifcode.getText().toString());
        params.put("bank_address", edt_BranchName.getText().toString());
        return params;
    }


}
