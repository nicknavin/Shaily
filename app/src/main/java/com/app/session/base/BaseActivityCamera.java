package com.app.session.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.widget.FrameLayout;
import android.widget.Toast;

import com.app.session.utility.Methods;

import org.json.JSONObject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;


public class BaseActivityCamera extends AppCompatActivity {

   public JSONObject jsonObject = new JSONObject();
    public enum LayoutManagerEnum {
        LINEAR,
        GRID,
        STAGGERED
    }

    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    public enum AbsListViewType {
        LIST_VIEW,
        GRID_VIEW
    }

    // Common options
    protected int threshold = 4;
    protected int totalPages = 3;
    protected int itemsPerPage = 10;
    protected long networkDelay = 2000;
    protected boolean addLoadingRow = true;
    protected boolean customLoadingListItem = false;

    // RecyclerView specific options
    protected LayoutManagerEnum layoutManagerEnum = LayoutManagerEnum.LINEAR;
    protected Orientation orientation = Orientation.VERTICAL;
    protected boolean reverseLayout = false;

    // AbsListView specific options
    protected AbsListViewType absListViewType = AbsListViewType.LIST_VIEW;
    protected boolean useHeaderAndFooter = false;

    private ActionBarDrawerToggle drawerToggle;
    private FrameLayout container;
    protected ProgressDialog mProgressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("dummy_value", 0);
        super.onSaveInstanceState(outState, outPersistentState);
    }
    public void showProgress(String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            dismissProgress();
        mProgressDialog = ProgressDialog.show(this,"", msg);
    }
    protected void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public Boolean isInternetConnected() {
        return Methods.isInternetConnected(BaseActivityCamera.this);
    }

    public  void showToast(String x)
    {
        Toast.makeText(getApplicationContext(),x, Toast.LENGTH_SHORT).show();
    }

    public  void showInternetConnectionToast()
    {
        Toast.makeText(getApplicationContext(),"Check Internet Connection", Toast.LENGTH_SHORT).show();
    }
}
