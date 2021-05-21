package com.app.session.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.app.session.R;
import com.app.session.activity.ui.baseviewmodels.ViewModelFactory;
import com.app.session.activity.viewmodel.OtherUserSubscripionGroupViewModel;
import com.app.session.activity.viewmodel.PurchaseGroupStoriesViewModel;
import com.app.session.adapter.SubscriptionStoriesAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.data.model.PurchaseSubscribeGroup;
import com.app.session.data.model.Root;
import com.app.session.data.model.SubscriptionStories;
import com.app.session.data.model.SubscriptionStoriesRoot;
import com.app.session.data.model.SubscriptionStoriesRootBody;
import com.app.session.data.model.UserSubscriptionGroupsBody;
import com.app.session.databinding.ActivityPurchaseGroupStoryBinding;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

public class PurchaseGroupStoryActivity extends BaseActivity implements View.OnClickListener , PopupMenu.OnMenuItemClickListener {

    ActivityPurchaseGroupStoryBinding binding;
    PurchaseGroupStoriesViewModel viewModel;
    private boolean loaddingDone = true;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;
    SubscriptionStoriesAdapter subscriptionStoriesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_purchase_group_story);
        viewModel = new ViewModelProvider(this, new ViewModelFactory( userId,accessToken)).get(PurchaseGroupStoriesViewModel.class);
        binding.setGroup(viewModel);
        binding.setLifecycleOwner(this);
        if (getIntent().getParcelableExtra(Constant.PURCHASE_GROUP) != null)
        {
            PurchaseSubscribeGroup purchaseSubscribeGroup=getIntent().getParcelableExtra(Constant.PURCHASE_GROUP);
            viewModel.groupiconUrl=purchaseSubscribeGroup.getSubscriptionIdData().getGroup_image_url();
            viewModel.groupName=purchaseSubscribeGroup.getSubscriptionIdData().getGroup_name();
            viewModel.getPurchaseSubscribeGroupMutableLiveData().setValue(purchaseSubscribeGroup);
        }
        initView();
        setupStoryRecylerview();
        setUpRecyclerListener();
        setSwipeLayout();
        setupObserver();
        callApi();


    }
    private void initView()
    {
        binding.laytoolbarStory.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.imgVideoCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=viewModel.getPurchaseSubscribeGroupMutableLiveData().getValue().getSubscriptionIdData().getGroup_introduction_video_url();
                if ( url!= null && !url.isEmpty()) {
                    String videoUrl = Urls.BASE_IMAGES_URL + url;
                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("URL", videoUrl);
                    startActivity(intent);
                }
            }
        });



        binding.layTop.setOnClickListener(this);




    }
    private void callApi() {
        viewModel.getSubscriptionsStoriesApi();
    }

    private void setupObserver() {


        viewModel.getSubscriptionStoriesMutableLiveData().observe(this, new Observer<SubscriptionStoriesRoot>() {
            @Override
            public void onChanged(SubscriptionStoriesRoot subscriptionStoriesRoot)
            {


                SubscriptionStoriesRootBody body=subscriptionStoriesRoot.getSubscriptionStoriesRootBody();
                setupStoryData(body);

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
        list =body.getSubscriptionStories();

        if(list.size()>0)
        {
            if(viewModel.page <=viewModel.totalPage)
            {
                loading=true;
                viewModel.page++;
            }

            for(SubscriptionStories subscriptionStories:list)
            {
                storyDataArrayList.addLast(subscriptionStories);
            }

            subscriptionStoriesAdapter.notifyDataSetChanged();


        }
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
                if(view.getId()==R.id.imgRemove)
                {
                    showMenu(view);
                }
                else
                {
                    SubscriptionStories stories=(SubscriptionStories)object;
                    Intent intent=new Intent(context,ShowGroupStoryActivity.class);
                    intent.putExtra("DATA",stories);
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