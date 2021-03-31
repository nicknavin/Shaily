package com.app.session.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.androidquery.AQuery;
import com.app.session.R;
import com.app.session.adapter.CustomAdapter;
import com.app.session.adapter.CustomSpinnerCountryAdapters;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.ApiCallback;
import com.app.session.data.model.Country;
import com.app.session.data.model.Root;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.PermissionsUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationSecondActivity extends BaseActivity implements View.OnClickListener {

    Context context;
    CustomTextView txtBack, txtNext, edtCountry;
    CustomEditText edtFullname, edtCountryCode, edtMobileNumber;
    String fullname = "", countryCode = "", mobileNo = "", countryId = "";
    Spinner spinnerCountry;
    private ArrayList<Country> countryArrayList = new ArrayList<>();

    private static final String TAG = "RegistrationSecondActivity";
    String contact_no = "";
    ImageView img_flag,imgFlag;
    RegistrationSecondActivity activity;
    CustomTextView txtSelectCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_second_new);
        context = this;
        activity = this;
        initView();
        if (isConnectingToInternet(context)) {
            //callGetCountry(getParam(lang), Urls.GET_COUNTRY);
            callGetCountry(lang);
        } else {
            showInternetPop(context);
        }
    }

    private void initView() {
//         spinner = findViewById(R.id.spinnerLang);
        ((CustomTextView) findViewById(R.id.header)).setText(context.getResources().getString(R.string.registration));
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        imgFlag = (ImageView) findViewById(R.id.imgFlag);

        edtCountryCode = (CustomEditText) findViewById(R.id.edt_cntryCode);
        edtMobileNumber = (CustomEditText) findViewById(R.id.edt_MobileNo);
        txtBack = (CustomTextView) findViewById(R.id.txtBack);
        txtBack.setOnClickListener(this);
        txtSelectCountry = (CustomTextView) findViewById(R.id.txtSelectCountry);
        txtSelectCountry.setOnClickListener(this);
        txtNext = (CustomTextView) findViewById(R.id.txtNext);
        txtNext.setOnClickListener(this);


        spinnerCountry = (Spinner) findViewById(R.id.spin_cntry);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(countryArrayList.size()>0) {

//                    Toast.makeText(context, countryArrayList.get(i).getCountryName(), Toast.LENGTH_SHORT).show();
                    Country country=countryArrayList.get(i);
                    countryId=country.get_id();
                    countryCode=country.getDial_cd();
                    edtCountryCode.setText("+"+country.getDial_cd().trim());
                    String url= Urls.BASE_IMAGES_URL+"userFiles/falgs-mini/"+countryCode+".png";
                    Picasso.with(context).load(url).placeholder(R.mipmap.country_icon).into(imgFlag);
                }
                }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.txtBack:
                onBackPressed();
                break;
            case R.id.txtNext:
                if (isValid()) {
//                    callSendOTP(getParamSendOTP(), Urls.SEND_OTP);
                    setSMSPermission();
                }
                break;
            case R.id.edt_selectCountry:
                Intent intent = new Intent(context, SelectCountryActivity.class);
                intent.putExtra("TYPE", "Country");
                startActivityForResult(intent, Constant.PICK_COUNTRY);
                break;
            case R.id.txtSelectCountry:
                dialogSpinner();
                break;

        }
    }


    public void setSMSPermission() {
//        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW)
//        {
//            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.READ_SMS, "Read Messages")) {
//                return;
//            }
//
//            if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
//                if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.RECEIVE_SMS, "Write External Storage")) {
//                    return;
//                }
//            }
//        }
//        callSendOTP(getParamSendOTP(), Urls.SEND_OTP);
        callUserMobileVerify();
    }
    private boolean isValid() {
     //   fullname = edtFullname.getText().toString().trim();
        mobileNo = edtMobileNumber.getText().toString().trim();
        countryCode = edtCountryCode.getText().toString().trim();
        String namePattrn = "[a-zA-z]+([ '-][a-zA-Z]+)*";

//        if (fullname.isEmpty()) {
//            edtFullname.setError(context.getResources().getString(R.string.error_name));
//            return false;
//        }
//        if (!Pattern.matches(namePattrn, fullname) || fullname.length() < 3) {
//
//            edtFullname.setError(context.getResources().getString(R.string.error_name_invalid));
//
//            return false;
//        }
        if (countryId.isEmpty()) {
            MyDialog.iPhone(context.getResources().getString(R.string.error_countryid), context);
            return false;
        }
        if (countryCode.isEmpty()) {
            edtCountryCode.setError(context.getResources().getString(R.string.error_coutnry_code));
            return false;
        }
        if (mobileNo.isEmpty()) {
            edtMobileNumber.setError(context.getResources().getString(R.string.error_mobile));

            return false;
        }
        if (mobileNo.length() < 6 || mobileNo.length() > 20 || !Patterns.PHONE.matcher(mobileNo).matches()) {
            edtMobileNumber.setError(context.getResources().getString(R.string.error_mobile_invalid));
            return false;
        }


        return true;
    }
    private void callGetCountry(String lang) {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getCountries();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();
                        try {

                            JSONObject js = new JSONObject(data);

                            if (js.getInt("status")==200) {
                                try {
                                    JSONArray jsonArray = js.getJSONArray("body");
//                                    Country country0 = new Country();
//                                    country0.setCountry_cd("");
//                                    country0.setCountryName("Select Country");
//                                    country0.setDial_cd("Code");
//                                    countryArrayList.add(country0);
                                    for (int i = 0; i < jsonArray.length(); i++)
                                    {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Country country = new Country();
                                        country.set_id(jsonObject.getString("_id"));
                                        country.setCountry_cd(jsonObject.getString("country_cd"));
                                        country.setCountryName(jsonObject.getString("country_english"));
//                                        country.setCountry_icon(jsonObject.getString("country_icon"));
                                        country.setDial_cd(jsonObject.getString("dial_cd"));
                                        country.setCurrency_symbol(jsonObject.getString("currency_symbol"));
                                        countryArrayList.add(country);
                                    }

//
                                    if (countryArrayList != null) {
                                        CustomAdapter customAdapter = new CustomAdapter(context, countryArrayList);
                                        spinnerCountry.setAdapter(customAdapter);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetConnectionToast();
        }
    }
    private void callSendOTP()
    {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.callSendOTP(mobileNo,"+" + countryCode);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();
                        try {

                            JSONObject js = new JSONObject(data);
                            if (js.getBoolean("Status")) {
                                callNextPage();
//                                DataPrefrence.setPref(context, Constant.COUNTRY_ID, countryId);
//                                DataPrefrence.setPref(context, Constant.MOBILE_NO, mobileNo);
//                                DataPrefrence.setPref(context, Constant.COUNTRY_CODE, countryCode);
////                                DataPrefrence.setPref(context, Constant.FULLNAME, fullname);
//                                startActivity(new Intent(context, VerificationCodeActivity.class));
                            }
                            else
                            {

                                showToast(js.getString("Message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetConnectionToast();
        }
    }
    private void callNextPage()
    {
        DataPrefrence.setPref(context, Constant.COUNTRY_ID, countryId);
        DataPrefrence.setPref(context, Constant.MOBILE_NO, mobileNo);
        DataPrefrence.setPref(context, Constant.COUNTRY_CODE, countryCode);
        Intent intent=new Intent(context,RegistrationThirdStageActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PICK_COUNTRY) {
            String name = "", id = "";
            if (data != null) {
                if (data.getStringExtra("NAME") != null) {
                    name = data.getStringExtra("NAME");
                }
                if (data.getStringExtra("ID") != null) {
                    countryId = data.getStringExtra("ID");
                }
                if (data.getStringExtra("DIAL_CD") != null) {
                    countryCode = data.getStringExtra("DIAL_CD");
                }
                if (data.getStringExtra("URL") != null) {

                    String imageUrl = data.getStringExtra("URL");
                    AQuery aQuery = null;
                    aQuery = new AQuery(context);
                    aQuery.id(img_flag).image(imageUrl, false, false, 80, R.mipmap.profile_large, null, AQuery.FADE_IN);

                }
                edtCountryCode.setText(countryCode);
                edtCountry.setText(name);
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionsUtils.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Log.d("Checking", "permissions: " + Arrays.asList(permissions) + ", grantResults:" + Arrays.asList(grantResults));
                if (PermissionsUtils.getInstance(context).areAllPermissionsGranted(grantResults)) {
//                    callSendOTP(getParamSendOTP(), Urls.SEND_OTP);
                    //callSendOTP();
                } else {
                    checkIfPermissionsGranted();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }
    public void checkIfPermissionsGranted() {
        AlertDialog alertDialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(getString(R.string.permission));
        alertDialogBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                goToSettings();
            }
        });

        alertDialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }




    RecyclerView recyclerViewSpinner;
    public void dialogSpinner() {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.myspinner_layout);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            recyclerViewSpinner=(RecyclerView)dd.findViewById(R.id.recyclerView);
            recyclerViewSpinner.setLayoutManager(new LinearLayoutManager(context));
            CustomSpinnerCountryAdapters customSpinnerCountryAdapters=new CustomSpinnerCountryAdapters(context, countryArrayList, new ApiCallback() {
                @Override
                public void result(String x)
                {
                    int position = Integer.parseInt(x);
                    Country country = countryArrayList.get(position);
                    countryId=country.get_id();
                    txtSelectCountry.setText(country.getCountryName());
                    edtCountryCode.setText("+"+country.getDial_cd());
                    imgFlag.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(country.getCountry_icon()).placeholder(R.mipmap.country_icon).into(imgFlag);
                    showToast(country.getCountryName());
                    dd.dismiss();
                }
            }) ;
            recyclerViewSpinner.setAdapter(customSpinnerCountryAdapters);
            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }




    private void callUserMobileVerify()
    {
        if (isConnectingToInternet(context))
        {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<Root> call = apiInterface.userMobileVerify(getParameter());
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    dismiss_loading();
                    Root responseBody = response.body();
                 if(responseBody.getStatus()==200)
                 {
                     //callSendOTP();
                     callNextPage();
                 }
                 else
                 {
                     showToast(responseBody.getMessage());
                 }

                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetConnectionToast();
        }
    }
    private JsonObject getParameter()
    {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("mobile_no",countryCode+mobileNo);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            return gsonObject;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return gsonObject;
    }




}
