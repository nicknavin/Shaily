package com.app.session.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.app.session.R;
import com.app.session.activity.ui.baseviewmodels.ViewModelFactory;
import com.app.session.activity.viewmodel.OtherUserSubscripionGroupViewModel;
import com.app.session.adapter.SubscriptionStoriesAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.data.model.Root;
import com.app.session.data.model.SubscriptionStories;
import com.app.session.data.model.SubscriptionStoriesRoot;
import com.app.session.data.model.SubscriptionStoriesRootBody;
import com.app.session.data.model.UserSubscriptionGroupsBody;
import com.app.session.databinding.ActivitySubcriptionOtherUserGroupBinding;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;

import java.util.LinkedList;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class OtheUserSubscriptionGroupDetailActivity extends BaseActivity implements View.OnClickListener , PopupMenu.OnMenuItemClickListener{

    UserSubscriptionGroupsBody subscriptionGroupsBody;
    LinearLayoutManager linearLayoutManager;
    OtherUserSubscripionGroupViewModel viewModel;
    ActivitySubcriptionOtherUserGroupBinding binding;

    SubscriptionStoriesAdapter subscriptionStoriesAdapter;
    private boolean loaddingDone = true;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subcription_other_user_group);

        setupViewModel();
        getGroupData();
        setupObserver();
        callApi();

    }

    LinkedList<SubscriptionStories> storyDataArrayList=new LinkedList<>();
    private void setupStoryRecylerview()
    {

        linearLayoutManager=new LinearLayoutManager(context);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        subscriptionStoriesAdapter=new SubscriptionStoriesAdapter(context, storyDataArrayList, viewModel.groupName, viewModel.groupiconUrl, new ObjectCallback() {
            @Override
            public void getObject(Object object, int position,View view)
            {
                SubscriptionStories stories=(SubscriptionStories)object;

                if(view.getId()==R.id.imgRemove)
                {
                    if(viewModel.isPurchased) {
                        showMenu(view);
                    }
                }else {
                    Intent intent = new Intent(context, ShowGroupStoryActivity.class);
                    intent.putExtra("DATA", stories);
                    startActivity(intent);
                    log(stories.toString());
                }
            }
        });
        binding.recyclerView.setAdapter(subscriptionStoriesAdapter);
    }
    public void setUpRecyclerListener() {
        loading = true;
        loaddingDone = true;
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();


                if (loading && loaddingDone)
                {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                    {

                        loading = false;
                        Utility.Log("inside the recly litner");
                        callApi();


                    }
                }
            }
        });
    }

    public void setSwipeLayout() {

        binding.swipeRefreshLayout.setVisibility(View.VISIBLE);

        binding.swipeRefreshLayout.setRefreshing(false);
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                viewModel.page=1;
                binding.swipeRefreshLayout.setRefreshing(true);
                storyDataArrayList=new LinkedList<>();
                callApi();
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this, new ViewModelFactory( userId,accessToken)).get(OtherUserSubscripionGroupViewModel.class);


    }

    private void getGroupData() {

        if (getIntent().getParcelableExtra(Constant.SUBS_GROUP) != null) {
            subscriptionGroupsBody = getIntent().getParcelableExtra(Constant.SUBS_GROUP);
            viewModel.groupName = subscriptionGroupsBody.getGroup_name();
            viewModel.groupiconUrl = subscriptionGroupsBody.getGroup_image_url();
            viewModel.subscriptionId = subscriptionGroupsBody.get_id();
            viewModel.getUserSubscriptionGroups().setValue(subscriptionGroupsBody);

            binding.setGroup(viewModel);
            binding.setLifecycleOwner(this);
            binding.laytoolbar.txtSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.purchaseSubsciptionGroup();
                }
            });
            binding.laytoolbar.imgCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });


            initView();
            if(!accessToken.isEmpty()) {
                setupStoryRecylerview();
                setUpRecyclerListener();
                setSwipeLayout();
            }
        }



    }

    private void callApi() {
        if(!userId.isEmpty()) {
            viewModel.getSubscriptionsStoriesApi();
        }
    }

    private void setupObserver() {
        viewModel.getRootMutableLiveData().observe(this, new Observer<Root>() {
            @Override
            public void onChanged(Root root) {
                showToast(root.getMessage());
                viewModel.isPurchased=true;
                callApi();
            }
        });
        viewModel.getUserSubscriptionGroups().observe(this, new Observer<UserSubscriptionGroupsBody>() {
            @Override
            public void onChanged(UserSubscriptionGroupsBody userSubscriptionGroupsBody) {
                viewModel.subscriptionId = userSubscriptionGroupsBody.get_id();
            }
        });
        viewModel.getSubscriptionStoriesMutableLiveData().observe(this, new Observer<SubscriptionStoriesRoot>() {
            @Override
            public void onChanged(SubscriptionStoriesRoot subscriptionStoriesRoot)
            {


                SubscriptionStoriesRootBody body=subscriptionStoriesRoot.getSubscriptionStoriesRootBody();
                 viewModel.isPurchased=body.isPurchased();
                setupStoryData(body);
                if (body.isPurchased())
                {
                    binding.laytoolbar.layPurchase.setVisibility(View.GONE);



                } else {
                    binding.laytoolbar.layStory.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    private void setupStoryData(SubscriptionStoriesRootBody body)
    {
        binding.swipeRefreshLayout.setRefreshing(false);
        binding.shimmerViewContainer.stopShimmerAnimation();
        binding.shimmerViewContainer.setVisibility(View.GONE);
        viewModel.totalPage=body.getTotal_Page();
        LinkedList<SubscriptionStories> list=new LinkedList<>();
        if(body.getSubscriptionStories()!=null) {
            list = body.getSubscriptionStories();

            if (list.size() > 0) {
                if (viewModel.page <= viewModel.totalPage) {
                    loading = true;
                    viewModel.page++;
                }

                for (SubscriptionStories subscriptionStories : list) {
                    storyDataArrayList.addLast(subscriptionStories);
                }

                subscriptionStoriesAdapter.notifyDataSetChanged();


            }
        }
    }

    private void initView()
    {

        binding.laytoolbar.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.imgVideoCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subscriptionGroupsBody.getGroup_introduction_video_url() != null && !subscriptionGroupsBody.getGroup_introduction_video_url().isEmpty()) {
                    String videoUrl = Urls.BASE_IMAGES_URL + subscriptionGroupsBody.getGroup_introduction_video_url();
                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("URL", videoUrl);
                    startActivity(intent);
                }
            }
        });



        binding.layTop.setOnClickListener(this);


        if(userId.isEmpty())
        {
            binding.laytoolbar.layStory.setVisibility(View.VISIBLE);
            binding.laytoolbar.layPurchase.setVisibility(View.GONE);
            binding.layGroupStory.setVisibility(View.GONE);
            binding.shimmerViewContainer.setVisibility(View.GONE);
            binding.swipeRefreshLayout.setVisibility(View.GONE);
        }


    }





    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgDrop:
                setVisibilityGroupDetail();
                break;
                case R.id.layTop:
                setVisibilityGroupDetail();
                break;

            case R.id.txtGroupNameCheck:


setVisibilityGroupDetail();
                break;
        }
    }



    private void setVisibilityGroupDetail()
    {
        if(binding.imgDrop.isChecked())
        {
            binding.imgDrop.setChecked(false);

            binding.layGroup.setVisibility(View.GONE);
        }
        else
        {
            binding.imgDrop.setChecked(true);
            binding.layGroup.setVisibility(View.VISIBLE);
        }
    }





    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_view_story, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.menu_report:

                return true;
        }
        return false;
    }
}
