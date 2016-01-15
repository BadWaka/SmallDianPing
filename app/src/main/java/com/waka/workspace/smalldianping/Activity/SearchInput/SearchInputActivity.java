package com.waka.workspace.smalldianping.Activity.SearchInput;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.waka.workspace.smalldianping.Activity.ResultList.ResultListActivity;
import com.waka.workspace.smalldianping.Adapter.MyFragmentPagerAdapter;
import com.waka.workspace.smalldianping.Activity.SearchInput.Adapter.SearchInputRecyclerViewAdapter;
import com.waka.workspace.smalldianping.Constant;
import com.waka.workspace.smalldianping.DataBase.DBHelper;
import com.waka.workspace.smalldianping.Activity.SearchInput.Fragment.HotSearchFragment;
import com.waka.workspace.smalldianping.DataBase.Order;
import com.waka.workspace.smalldianping.R;

import java.util.ArrayList;

/**
 * 搜索输入Activity
 */
public class SearchInputActivity extends AppCompatActivity implements View.OnClickListener, HotSearchFragment.OnFragmentInteractionListener, TextWatcher {

    private static final String TAG = "SearchInputActivity";

    //标题栏
    private LinearLayout mLayoutBack, mLayoutSearch, mLayoutClear;
    private AutoCompleteTextView mAutoCompleteTextView;
    private String[] mTips;//提示字符串数组
    //热门搜索
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ArrayList<Fragment> mFragmentList;
    private String[] hotSearch1 = new String[]{"王的盛宴", "金钩钓", "肯德基", "麦当劳", "德克士", "铁木真", "金真子", "兰州拉面", "沙县小吃"};
    private String[] hotSearch2 = new String[]{"必胜客", "好伦哥", "东来顺", "正一味", "韩国烤肉", "吉野家", "穿穿先生", "烤羊肉", "火锅"};
    //历史记录
    private RecyclerView mRecyclerView;
    private SearchInputRecyclerViewAdapter mRecyclerViewAdapter;
    private ArrayList<String> mStrHistoryList;
    //数据库相关
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_input);
        initView();
        initData();
        initEvent();

//        Order addorder=new Order("wxj","B0FFFAHQ4Q","goods1",2,88.8);
//        addorder.addOrder();


    }

    private void initView() {
        Log.i(TAG, "initView");
        mLayoutBack = (LinearLayout) findViewById(R.id.layoutBack);
        mLayoutSearch = (LinearLayout) findViewById(R.id.layoutSearch);
        mLayoutClear = (LinearLayout) findViewById(R.id.layoutClear);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
    }

    private void initData() {
        //初始化dbHelper
        mDBHelper = new DBHelper(this);
        mDB = mDBHelper.getWritableDatabase();

        //初始化ViewPager和Fragment
        HotSearchFragment fragment1 = HotSearchFragment.newInstance(hotSearch1);
        HotSearchFragment fragment2 = HotSearchFragment.newInstance(hotSearch2);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(fragment1);
        mFragmentList.add(fragment2);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mTabLayout.setTabsFromPagerAdapter(adapter);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);

        //从数据库中读取历史记录数据，并添加到list中
        mStrHistoryList = new ArrayList<>();
        Cursor cursor = mDB.rawQuery("select * from " + Constant.TABLE_NAME_SEARCH_HISTORY, null);
        if (cursor.moveToFirst()) {
            do {
                String history = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_HISTORY_IN_SEARCH_HISTORY));
                mStrHistoryList.add(history);
            } while (cursor.moveToNext());
        }
        cursor.close();

        //初始化RecyclerView
        mRecyclerViewAdapter = new SearchInputRecyclerViewAdapter(this, mStrHistoryList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画效果
        View view = LayoutInflater.from(this).inflate(R.layout.recycleview_footer_view_in_activity_search_input, mRecyclerView, false);
        mRecyclerViewAdapter.setFooterView(view);//添加FooterView

        //设置自动提示
        setmAutoCompleteTextViewTips();
    }

    private void initEvent() {
        mLayoutBack.setOnClickListener(this);
        mLayoutSearch.setOnClickListener(this);
        mLayoutClear.setOnClickListener(this);
        mAutoCompleteTextView.addTextChangedListener(this);
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
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//淡入淡出
                break;
            //清除按钮
            case R.id.layoutClear:
                mAutoCompleteTextView.setText("");
                break;
            //搜索按钮
            case R.id.layoutSearch:
                String strSearch = mAutoCompleteTextView.getText().toString();
                if (!strSearch.isEmpty()) {
                    //向历史记录数据库中插入数据
                    ContentValues values = new ContentValues();
                    values.put(Constant.COLUMN_NAME_HISTORY_IN_SEARCH_HISTORY, strSearch);
                    long status = mDB.insert(Constant.TABLE_NAME_SEARCH_HISTORY, null, values);
                    Log.i(TAG, "status---->" + status);
                    if (status != -1) {
                        //在Recycleview中添加
                        mRecyclerViewAdapter.addData(strSearch);
                    }
                    setmAutoCompleteTextViewTips();
                    Intent intent = new Intent(this, ResultListActivity.class);
                    intent.putExtra("searchKey", mAutoCompleteTextView.getText().toString());
                    startActivityForResult(intent, Constant.RESULT_LIST_ACTIVITY_REQUEST_CODE);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);//由左向右滑入的效果
                }
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

    /**
     * 实现接口，与Fragment进行通信
     *
     * @param s
     */
    @Override
    public void onFragmentInteraction(String s) {
        setSearchText(s);
    }

    /**
     * 设置输入框内容
     *
     * @param s
     */
    public void setSearchText(String s) {
        mAutoCompleteTextView.setText(s);
        mAutoCompleteTextView.setSelection(s.length());//设置光标位置
    }

    /**
     * 设置自动提示
     */
    private void setmAutoCompleteTextViewTips() {
        //设置mAutoCompleteTextView中的数据,将list的中的数据放在String[] mTips中
        mTips = null;
        mTips = new String[mStrHistoryList.size()];
        for (int i = 0; i < mStrHistoryList.size(); i++) {
            mTips[i] = mStrHistoryList.get(i);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.auto_complete_textview_item, mTips);//创建适配器
        mAutoCompleteTextView.setAdapter(arrayAdapter);
        mAutoCompleteTextView.setThreshold(1);
    }

    /**
     * 清除提示数组
     */
    public void clearmTips() {
        mTips = null;
        mAutoCompleteTextView.setAdapter(null);
    }

    /**
     * 监听editText输入变化
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!mAutoCompleteTextView.getText().toString().isEmpty()) {//如果文本不为空，显示清除按钮
            mLayoutClear.setVisibility(View.VISIBLE);
        } else {
            mLayoutClear.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
