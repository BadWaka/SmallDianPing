package com.waka.workspace.smalldianping.Activity.ResultList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.waka.workspace.smalldianping.Activity.ResultList.Adapter.SearchResultListViewAdapter;
import com.waka.workspace.smalldianping.Activity.ResultMap.ResultMapActivity;
import com.waka.workspace.smalldianping.Activity.StoreDetail.StoreDetailActivity;
import com.waka.workspace.smalldianping.Constant;
import com.waka.workspace.smalldianping.DataBase.DBHelper;
import com.waka.workspace.smalldianping.R;
import com.waka.workspace.smalldianping.StaticValues;

/**
 * 搜索结果列表
 * Created by wang_xiaojie on 2015/12/29 0029.
 */
public class ResultListActivity extends AppCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private static final String TAG = "ResultListActivity2";

    //标题栏
    private LinearLayout mLayoutBack, mLayoutMap;
    private CardView mCvSearch;
    private TextView mTvSearchKey;
    private String mStrSearchKey, mStrSearchCity;//搜索关键字和搜索目标城市
    private Spinner mSprFoodType;//美食种类下拉菜单
    private Spinner mSprAdName;//选择区名称下拉淡菜
    private Spinner mSprResortType;//选择排序方式下拉菜单
    public ListView mListViewSearchResult;//结果列表
    public TextView mTvLoading;

    SearchResultListViewAdapter AdapterSR;
    int mLastVisibleItemNum;

    DBHelper dbhelper;

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list2);
        initView();
        initData();
        initEvent();
    }

    /**
     * initView
     */
    private void initView() {
        mLayoutBack = (LinearLayout) findViewById(R.id.layoutBack);
        mLayoutMap = (LinearLayout) findViewById(R.id.layoutMap);
        mCvSearch = (CardView) findViewById(R.id.cvSearch);
        mTvSearchKey = (TextView) findViewById(R.id.tvSearchKey);
        mListViewSearchResult = (ListView) findViewById(R.id.search_result_listview);
        mSprResortType = (Spinner) findViewById(R.id.resort_spinner);
        mSprFoodType = (Spinner) findViewById(R.id.foodtype_spinner);
        mSprAdName = (Spinner) findViewById(R.id.adname_spinner);
        mTvLoading = (TextView) findViewById(R.id.loading_textview);
    }

    /**
     * initData
     */
    private void initData() {
        //获得搜索的关键词
        Intent intent = getIntent();
        mStrSearchKey = intent.getStringExtra("searchKey");
        mStrSearchCity = StaticValues.cityName;
        dbhelper = new DBHelper(this);
        mTvSearchKey.setText(mStrSearchKey);

        String[] mItems = getResources().getStringArray(R.array.resorttype_arry);
        ArrayAdapter<String> resortadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mItems);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        mSprResortType.setAdapter(resortadapter);
        mSprResortType.setOnItemSelectedListener(this);

        String[] mItems2 = getResources().getStringArray(R.array.foodtype_array);
        ArrayAdapter<String> foodtypeadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mItems2);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        mSprFoodType.setAdapter(foodtypeadapter);
        mSprFoodType.setOnItemSelectedListener(this);

        String[] mItems3 = null;
        if (StaticValues.districtNames == null) {
            mItems3 = getResources().getStringArray(R.array.adname_arry);
        } else {
            mItems3 = StaticValues.districtNames;
        }
        ArrayAdapter<String> adnameadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mItems3);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        mSprAdName.setAdapter(adnameadapter);
        mSprAdName.setOnItemSelectedListener(this);

        AdapterSR = new SearchResultListViewAdapter(this, mStrSearchKey, mStrSearchCity, this);
        mListViewSearchResult.setAdapter(AdapterSR);
    }

    /**
     * initEvent
     */
    private void initEvent() {
        mLayoutBack.setOnClickListener(this);
        mLayoutMap.setOnClickListener(this);
        mCvSearch.setOnClickListener(this);
        mListViewSearchResult.setOnScrollListener(this);
        mListViewSearchResult.setOnItemClickListener(this);
    }

    /**
     * 点击事件分发
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //退出按钮
            case R.id.layoutBack:
                SearchResultListViewAdapter.searchstate = false;
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//淡入淡出
                break;
            //搜索栏
            case R.id.cvSearch:
                SearchResultListViewAdapter.searchstate = false;
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//淡入淡出
                break;
            //地图按钮
            case R.id.layoutMap:
                //把可见的结果商家显示到地图上
                for (int i = 0; i < AdapterSR.getCount(); i++) {
                    SearchResultListViewAdapter.poiItems.add(SearchResultListViewAdapter.storeDBs.get(i).getPoiItem());
                }
                Intent intent = new Intent(this, ResultMapActivity.class);
                startActivityForResult(intent, Constant.RESULT_MAP_ACTIVITY_REQUEST_CODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);//由左向右滑入的效果
                break;
            default:
                break;
        }
    }

    /**
     * 重写返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//淡入淡出
                break;
        }
        return true;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mLastVisibleItemNum == AdapterSR.mShowResultNum
                && AdapterSR.mShowResultNum != SearchResultListViewAdapter.storeDBs.size()) {
            AdapterSR.mShowResultNum += 15;
            //假如剩余没显示的结果商家不足15个
            if (AdapterSR.mShowResultNum > SearchResultListViewAdapter.storeDBs.size()) {
                AdapterSR.mShowResultNum = SearchResultListViewAdapter.storeDBs.size();
            }

            AdapterSR.notifyDataSetChanged();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mLastVisibleItemNum = firstVisibleItem + visibleItemCount;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //如果是来自选择排序的事件
        if (parent.getId() == R.id.resort_spinner) {
            resortBy(position);
        }
        //如果是来自选择美食类型或者商家区域的事件
        else {
            String selectedadname = mSprAdName.getSelectedItem().toString();
            String selectedfoodtype = mSprFoodType.getSelectedItem().toString();
            SearchResultListViewAdapter.storeDBs.clear();
            if (selectedfoodtype.equals("全部")) {
                SearchResultListViewAdapter.storeDBs.addAll(SearchResultListViewAdapter.allStoreDBs);
            } else {
                for (int i = 0; i < SearchResultListViewAdapter.allStoreDBs.size(); i++) {
                    if (SearchResultListViewAdapter.allStoreDBs.get(i).getPoiItem().getTypeDes().split(";")[2].equals(selectedfoodtype)) {
                        SearchResultListViewAdapter.storeDBs.add(SearchResultListViewAdapter.allStoreDBs.get(i));
                    }
                }
            }
            if (!selectedadname.equals("全部")) {
                for (int i = 0; i < SearchResultListViewAdapter.storeDBs.size(); i++) {
                    if (!SearchResultListViewAdapter.storeDBs.get(i).getPoiItem().getAdName().equals(selectedadname)) {
                        SearchResultListViewAdapter.storeDBs.remove(i);
                        i--;
                    }
                }
            }
            resortBy(mSprResortType.getSelectedItemPosition());

            if (SearchResultListViewAdapter.storeDBs.size() > 15) {
                AdapterSR.mShowResultNum = 15;
            }
            else {

                AdapterSR.mShowResultNum = SearchResultListViewAdapter.storeDBs.size();
            }

            if( AdapterSR.mShowResultNum==0 && SearchResultListViewAdapter.searchstate==false){
                mTvLoading.setText("没有符合条件的结果！");
                mListViewSearchResult.setVisibility(ListView.GONE);
                mTvLoading.setVisibility(TextView.VISIBLE);
            }
            else if(SearchResultListViewAdapter.searchstate==false){
                mListViewSearchResult.setVisibility(ListView.VISIBLE);
                mTvLoading.setVisibility(TextView.GONE);
            }
            SearchResultListViewAdapter.poiItems.clear();
            for (int i = 0; i < AdapterSR.mShowResultNum; i++) {
                SearchResultListViewAdapter.poiItems.add(SearchResultListViewAdapter.storeDBs.get(i).getPoiItem());
            }
        }
        AdapterSR.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void resortBy(int selectedpos) {
        switch (selectedpos) {
            case 0:
                AdapterSR.resort(SearchResultListViewAdapter.RESORT_BY_DISTANCE);
                break;
            case 1:
                AdapterSR.resort(SearchResultListViewAdapter.RESORT_BY_GOODEVALUATE);
                break;
            case 2:
                AdapterSR.resort(SearchResultListViewAdapter.RESORT_BY_AVGCOSTCHEAP);
                break;
            case 3:
                AdapterSR.resort(SearchResultListViewAdapter.RESORT_BY_AVGCOSTEXPENSIVE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StoreDetailActivity.presentStore = SearchResultListViewAdapter.storeDBs.get(position);
        Intent intent = new Intent(this, StoreDetailActivity.class);
        startActivityForResult(intent, Constant.STORE_DETAIL_ACTIVITY_REQUEST_CODE);
    }
}
