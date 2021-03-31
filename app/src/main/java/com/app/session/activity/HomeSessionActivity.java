package com.app.session.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.fragment.HomeSessionFragment;
import com.app.session.interfaces.RequestCallback;
import com.app.session.data.model.CategoryBody;
import com.app.session.data.model.CategoryData;
import com.app.session.data.model.CategoryRoot;
import com.app.session.data.model.LanguageCd;
import com.app.session.data.model.UserLangauges;
import com.app.session.network.ApiClientExplore;
import com.app.session.network.ApiInterface;
import com.app.session.network.BaseAsych;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.app.session.utility.Utility;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeSessionActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    CustomEditText edt_search;
    CardView cardView;
    String key;
    ArrayList<CategoryData> consultantDataArrayList=new ArrayList<>();
    ArrayList<CategoryBody> categoryBodies=new ArrayList<>();
    ArrayList<UserLangauges> userLangaugesArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_consultant);
        initView();
        loadFragment(new HomeSessionFragment());
    }


    private void initView()
    {





    }


    private void initViewold()
    {
        ((ImageView)findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cardView=(CardView)findViewById(R.id.card_view);
        edt_search = (CustomEditText) findViewById(R.id.edt_search);
        viewPager = (ViewPager) findViewById(R.id.viewpager_favourite);

//        edt_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                key = s.toString().trim();
//                if(sessionFragment!=null) {
//                    sessionFragment.getFilter(key);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH)
//                {
//                    if(sessionFragment!=null) {
//                        sessionFragment.getFilter(key);
//                    }
//                    return true;
//
//                }
//                return false;
//            }
//        });


    }


    private void initTablayout() {


        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_favourite);
        tabLayout.setupWithViewPager(viewPager);
        setCustomTextView();

    }


    public void setCustomTextView() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            CustomTextView tv = (CustomTextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(10, 0, 10, 0);
            tab.requestLayout();
            tabLayout.getTabAt(i).setCustomView(tv);

        }
    }

    HomeSessionFragment sessionFragment;

    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new AllStoryFragment(), "All");

//        for(int i=0;i<categoryBodies.size();i++)
//        {
//            String name =categoryBodies.get(i).getCategoryName();
//            homeStoryFragment = HomeStoryFragment.newInstance(categoryBodies.get(i).getId());
//            adapter.addFragment(homeStoryFragment , name);
//        }


        adapter.addFragment(new HomeSessionFragment(), "All User");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.getItem(position).isVisible();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private void getAllCategory() {
        if (isInternetConnected()) {
            showLoading();
            new BaseAsych(Urls.GET_LANG_CATEGORY, getParamAllCategory(), new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String success) {
                    dismiss_loading();
                    try {
                        cardView.setVisibility(View.VISIBLE);
                        JSONObject jsonObject=js.getJSONObject("result");
                        if (!jsonObject.getString("rstatus").equals("0"))
                        {
                            JSONArray jsonArray = js.getJSONArray("cat_data");
                            Type type = new TypeToken<ArrayList<CategoryData>>() {}.getType();
                            consultantDataArrayList = new Gson().fromJson(jsonArray.toString(), type);
                            initTablayout();
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
    private String getParamAllCategory()
    {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_cd", userId);
            jsonObject.put("language_cd", "en");
            return jsonObject.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    private void getCategories()
    {
        if (Utility.isConnectingToInternet(context))
        {

            userLangaugesArrayList= DataPrefrence.getLanguageDb(context, Constant.LANG_DB);
            LanguageCd languageCd=new LanguageCd();
            ArrayList<String> langCdList=new ArrayList<>();
            for(UserLangauges userLangauges: userLangaugesArrayList)
            {
                langCdList.add(userLangauges.getCode());
            }

            languageCd.setLanguageCd(langCdList);
            ApiInterface apiInterface = ApiClientExplore.getClient().create(ApiInterface.class);
            Call<CategoryRoot> call = apiInterface.reqGetCategories(accessToken, languageCd);
            call.enqueue(new Callback<CategoryRoot>() {
                @Override
                public void onResponse(Call<CategoryRoot> call, Response<CategoryRoot> response)
                {
                    if(response.body()!=null)
                    {
                        if (response.body().getStatus()==200)
                        { cardView.setVisibility(View.VISIBLE);
                            categoryBodies=response.body().getBody();
                            initTablayout();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CategoryRoot> call, Throwable t) {

                }
            });



        } else {
            showInternetConnectionToast();
        }
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
