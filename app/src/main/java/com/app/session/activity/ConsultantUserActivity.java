package com.app.session.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.app.session.R;
import com.app.session.api.AqueryCall;
import com.app.session.api.Urls;
import com.app.session.base.BaseTabActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.customview.MyDialog;
import com.app.session.interfaces.RequestCallback;
import com.app.session.network.ApiClientNew;
import com.app.session.network.ApiInterface;
import com.app.session.network.BaseAsych;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultantUserActivity extends BaseTabActivity implements View.OnClickListener
{
    TabHost TabHostWindow;
    Intent intent;
    Context context;
    Activity activity;
    TabSpec TabMenu1,TabMenu2,TabMenu3,TabMenu4;
    boolean flag_tab;
    public ImageView imgLogout, imgNetwork, imgnotification, imgsearch;
    String tab_selection="0";
    View viewImgProfil,viewExplore,viewChat;
    CircleImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTransition();
        setContentView(R.layout.activity_tab_demo);
        context = this;
        activity = this;
        if(getIntent().getStringExtra("TAB")!=null)
        {
            tab_selection=getIntent().getStringExtra("TAB");

        }
        initView();
        //callGetProfile(getParamProfile(), Urls.GET_USER_DETAIL);

    }

    @Override
    protected void onResume() {
        super.onResume();

        profileUrl=DataPrefrence.getPref(context,Constant.PROFILE_IMAGE,"");

        if(profileUrl!=null&&!profileUrl.isEmpty()) {
            System.out.println("consult user url : "+profileUrl);
            Picasso.with(context).load(profileUrl).error(R.mipmap.profile_large).placeholder(R.mipmap.profile_large).into(imgProfile);
        }
    }

    private void initView()
    {
        viewImgProfil=(View)findViewById(R.id.viewImgProfil);
        viewExplore=(View)findViewById(R.id.viewExplore);
        viewChat=(View)findViewById(R.id.viewChat);
        imgProfile=(CircleImageView) findViewById(R.id.imgProfile);

//        imgsearch = (ImageView) findViewById(R.id.imgsearch);
//        imgsearch.setOnClickListener(this);
//        imgLogout = (ImageView) findViewById(R.id.imgLogout);
//        imgLogout.setVisibility(View.VISIBLE);
//        imgnotification = (ImageView) findViewById(R.id.imgnotification);
//        imgnotification.setOnClickListener(this);
//        imgLogout.setImageResource(R.mipmap.setting);
//        imgLogout.setOnClickListener(this);
        TabHostWindow = (TabHost) findViewById(android.R.id.tabhost);
        TabHostWindow.setup();
        TabHostWindow.setOnTabChangedListener(listener);
        tabMyProfile(0);
        ((ImageView)findViewById(R.id.imgProfile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectorTab();
                viewImgProfil.setVisibility(View.VISIBLE);
                tabMyProfile(0);
            }
        });
        ((CustomTextView)findViewById(R.id.txtExplore)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                selectorTab();
                viewExplore.setVisibility(View.VISIBLE);
                tabMyProfile(1);
            }
        });
        ((CustomTextView)findViewById(R.id.txtChat)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                selectorTab();
                viewChat.setVisibility(View.VISIBLE);
                tabMyProfile(2);
            }
        });

        ((ImageView)findViewById(R.id.imgSetting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

//        getTabHost().getChildAt(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        //Creating tab menu.
//
//         TabMenu1= TabHostWindow.newTabSpec(context.getResources().getString(R.string.story));
//        TabMenu1.setIndicator(createTabView(context.getResources().getString(R.string.story)));
//        TabMenu1.setContent(new Intent(this, UnseenMyStoryActivity.class));
//        TabHostWindow.addTab(TabMenu1);


//
//
//
//
//        TabMenu2 = TabHostWindow.newTabSpec(context.getResources().getString(R.string.consultation));
//        TabMenu2.setIndicator(createTabView(context.getResources().getString(R.string.consultation)));
//        intent = new Intent(getApplicationContext(), ConversationActivity.class);
//        intent.putExtra("PAGE", "0");
//        if (ApplozicClient.getInstance(getApplicationContext()).isContextBasedChat()) {
//            intent.putExtra(ConversationUIService.CONTEXT_BASED_CHAT, true);
//        }
//        TabMenu2.setContent(intent);
//        TabHostWindow.addTab(TabMenu2);
//
//        TabMenu3 = TabHostWindow.newTabSpec(context.getResources().getString(R.string.expert));
//        TabMenu3.setIndicator(createTabView(context.getResources().getString(R.string.expert)));
//        TabMenu3.setContent(new Intent(this, ConsultantActivity.class));
//        TabHostWindow.addTab(TabMenu3);
//




//        if(tab_selection.equals("2")) {
//            TabHostWindow.setCurrentTab(1);
//        }else
//        {
//            TabHostWindow.setCurrentTab(0);
//        }


    }

    TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
            System.out.println("tabId " + tabId);
//            if (!tabId.equals(context.getResources().getString(R.string.consultation))) {
//                flag_tab=false;
//                imgsearch.setVisibility(View.VISIBLE);
//            } else {
//                flag_tab=true;
//                imgsearch.setVisibility(View.VISIBLE);
//            }
        }
    };


    private void tabMyProfile(int tab)
    {
        TabHostWindow.clearAllTabs();
        TabMenu1= TabHostWindow.newTabSpec(context.getResources().getString(R.string.story));
        TabMenu1.setIndicator(createTabView(context.getResources().getString(R.string.story)));
        Intent intent;
        if(tab==0) {

intent=new Intent(getApplicationContext(), MyProfileActivityNew.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TabMenu1.setContent(intent);
        }
        if(tab==1) {
            intent=new Intent(getApplicationContext(), ExpertFollowingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TabMenu1.setContent(intent);

        }if(tab==2) {
             intent=new Intent(getApplicationContext(), ChattedUserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TabMenu1.setContent(intent);
        }

        TabHostWindow.addTab(TabMenu1);
    }



    private void selectorTab()
    {
        viewImgProfil.setVisibility(View.GONE);
        viewExplore.setVisibility(View.GONE);
        viewChat.setVisibility(View.GONE);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {


        MenuInflater menuInflater = getMenuInflater();

        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        menuItem_setting = menu.findItem(R.id.action_setting);
//        menuItem_notification = menu.findItem(R.id.action_notification);
        MenuItem menuItem_search = menu.findItem(R.id.action_search);
        menuItem_search.setVisible(false);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return super.onMenuItemSelected(featureId, item);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
//            case android.R.id.home:
//                openLeft();
//                break;

            case R.id.action_setting:
                View vItem = this.findViewById(R.id.action_setting);
                showMenu(vItem);
                break;


            case R.id.action_search:

                break;
            case R.id.action_notification:
                startActivity(new Intent(context, NotificationActivity.class));
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_edit_profile:
                        if (isConnectingToInternet(context)) {
                            if (is_consultant.equals("0") && is_company.equals("1")) {
                                startActivity(new Intent(context, ProfileCompanyEditActivity.class));
                            } else
                                if (is_consultant.equals("1"))
                                {
                                startActivity(new Intent(context, ProfileEditConsultantActivity.class));
                            } else if (is_consultant.equals("0") ) {
                                startActivity(new Intent(context, ProfileEditUserActivity.class));
                            }
                        } else {
                            showInternetPop(context);
                        }

                        return true;
                    case R.id.menu_edit_mystory:

                        if (isConnectingToInternet(context)) {
                            startActivity(new Intent(context, CreateSpecialGroupActivity.class));
                        } else {
                            showInternetPop(context);
                        }

                       return true;
                    case R.id.menu_pwd:
                        if (isConnectingToInternet(context)) {
                            startActivity(new Intent(context, ChangePasswordActivity.class));
                        } else {
                            showInternetPop(context);
                        }
                        return true;
                    case R.id.menu_status:
//                File file = new File(Environment.getExternalStorageDirectory(),
//                        "Report.pdf");
//                Uri path = Uri.fromFile(file);
//                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
//                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                pdfOpenintent.setDataAndType(path, "application/pdf");
//                try {
//                    startActivity(pdfOpenintent);
//                }
//                catch (ActivityNotFoundException e) {
//
//                }

//
//                AssetManager assetManager = getAssets();
//
//                InputStream in = null;
//                OutputStream out = null;
//                File file = new File(getFilesDir(), "mypdf.pdf");
//                try
//                {
//                    in = assetManager.open("mypdf.pdf");
//                    out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
//
//                    copyFile(in, out);
//                    in.close();
//                    in = null;
//                    out.flush();
//                    out.close();
//                    out = null;
//                } catch (Exception e)
//                {
//                    Log.e("tag", e.getMessage());
//                }
//
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(
//                        Uri.parse("file://" + getFilesDir() + "/mypdf.pdf"),
//                        "application/pdf");
//
//                startActivity(intent);
//                return true;
                    case R.id.menu_edit_financial:
                        startActivity(new Intent(context, FinancialActivity.class));
                        return true;
                    case R.id.menu_edit_coureses:
                        startActivity(new Intent(context, CouresesActivity.class));
                        return true;
                    case R.id.menu_edit_report:
                        startActivity(new Intent(context, ReportProblemActivity.class));
                        return true;
                    case R.id.menu_invite:
                        if (isConnectingToInternet(context)) {
                            shareTextUrl();
                        } else {
                            showInternetPop(context);
                        }
                        return true;
                    case R.id.menu_logout:
                        if (isConnectingToInternet(context)) {
                            logoutDialog(1);
                        } else {
                            showInternetPop(context);
                        }
                        return true;
                    case R.id.menu_delete:
                        if (isConnectingToInternet(context)) {
                            logoutDialog(0);
                        } else {
                            showInternetPop(context);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        if (is_company.equals("0") && is_consultant.equals("0")) {
            inflater.inflate(R.menu.menu_user, popup.getMenu());
        } else {
            inflater.inflate(R.menu.menu_company, popup.getMenu());
        }
        popup.show();
    }

    private void shareTextUrl() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: http://www.consultlot.com/");
        startActivity(Intent.createChooser(shareIntent, "http://www.consultlot.com/"));
    }

    public void logoutDialog(int type) {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_iphone_confirm);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            if(type==1) {
                ((CustomTextView) dd.findViewById(R.id.tvConfirmOk)).setText("Logout");
            }else
            {
                ((CustomTextView) dd.findViewById(R.id.tvConfirmTitle)).setText("Do you want delete account");
            }

            ((CustomTextView) dd.findViewById(R.id.tvConfirmOk)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(type==1) {
//                        getUserLogout();
                        clearDataBase();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        getUserDeleteAccount();
                    }
                    dd.dismiss();
                }
            });

            ((CustomTextView) dd.findViewById(R.id.tvConfirmCancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
                }
            });
            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }

    private void callUserLogout(Map<String, Object> param, String url) {

        try {
            showLoading();
            AqueryCall request = new AqueryCall(this);
            request.commonAPI(url, param, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {

                    dismiss_loading();

                    clearDataBase();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();


                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    if (failed.contains(context.getResources().getString(R.string.unauthorized_error))) {
                        unAuthorized();

                    } else {
                        MyDialog.iPhone(failed, context);
                    }
                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
//            MyDialog.iPhone(getString(R.string.something_wrong), appContext);
        }
    }

    private Map<String, Object> getParam() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("token_id", accessToken);


        return params;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PICK_PREF_LANGUAGE) {
            String language_cd = "", language_name = "";
            if (data != null) {

                if (data.getStringExtra("NAME") != null) {
                    language_name = data.getStringExtra("NAME");

                }
                if (data.getStringExtra("ID") != null) {
                    language_cd = data.getStringExtra("ID");
                }
                DataPrefrence.setPref(context, Constant.ID, language_cd);
            }

        }
    }

    private View createTabView(String name)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_name, null);
        CustomTextView txt = (CustomTextView) view.findViewById(R.id.label);
//        txt.setTextColor(context.getResources().getColor(R.color.black));
        txt.setText(name);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgLogout:
                showMenu(imgLogout);
                break;
            case R.id.imgnotification:

                startActivity(new Intent(context, NotificationActivity.class));

                break;
            case R.id.imgsearch:
                if(flag_tab)
                {
                    intent = new Intent(context, SelectRegistertLanguageActivity.class);
                    startActivityForResult(intent, Constant.PICK_PREF_LANGUAGE);
                }
                else
                {
                    startActivity(new Intent(context,SearchUserActivity.class));
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent(Constant.CUSTOME_BROADCAST);
        // You can also include some extra data.
        intent.putExtra("title", "search_contact");
        intent.putExtra("message", "");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }




    private void getUserDetail()
    {
        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call= apiInterface.getUserDetailRequest(userId,accessToken);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    dismiss_loading();
                    ResponseBody responseBody=response.body();
                    try {
                        String data=  responseBody.string();
                        try {

                            JSONObject js =new JSONObject(data);

                            if(js.getBoolean("Status")) {
                                try {
                                    JSONArray jsonArray = js.getJSONArray("User_Detail");

                                    JSONObject jsonObject = jsonArray.getJSONObject(0);


                                    if (jsonObject.getString("imageUrl") != null && !jsonObject.getString("imageUrl").isEmpty()) {
                                        Picasso.with(context).load(jsonObject.getString("imageUrl")).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.mipmap.profile_large).into(imgProfile);
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
        }
        else
        {
            showInternetConnectionToast();
        }
    }

    private void getUserLogout() {

        if (isConnectingToInternet(context)) {
            showLoading();
            ApiInterface apiInterface = ApiClientNew.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.callLogoutRequest(userId, accessToken);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismiss_loading();
                    ResponseBody responseBody = response.body();
                    try {
                        String data = responseBody.string();
                        try {

                            JSONObject js = new JSONObject(data);

                            //if(js.getBoolean("Status"))
                            {
                                clearDataBase();
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
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

    private void getUserDeleteAccount() {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.DELETE_MY_ACCOUNT, getDeleteObj(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        JSONObject jsonObject=js.getJSONObject("result");
                        if (!jsonObject.getString("rstatus").equals("0"))
                        {
                            clearDataBase();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }

                @Override
                public void onFailed(JSONObject js, String failed, int status) {
                    dismiss_loading();
                    showToast(failed);


                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    dismiss_loading();
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    dismiss_loading();
                }
            }).execute();

        } else {
            showInternetConnectionToast();
        }
    }

    private String getDeleteObj() {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_cd", userId);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }




}
