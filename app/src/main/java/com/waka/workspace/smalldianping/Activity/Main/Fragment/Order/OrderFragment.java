package com.waka.workspace.smalldianping.Activity.Main.Fragment.Order;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waka.workspace.smalldianping.Adapter.MyFragmentPagerAdapter;
import com.waka.workspace.smalldianping.R;

import java.util.ArrayList;


/**
 * OrderFragment
 * <p/>
 * Created by waka on 2015/12/22.
 */
public class OrderFragment extends Fragment {

    private static final String TAG = "OrderFragment";

    //主题ViewPager和Fragment
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragmentList;
    private ArrayList<String> mTitleList;
    public AllOrderFragment mAllOrderFragment;
    private ClassifyOrderFragment mClassifyOrderFragment;


    /**
     * 构造方法
     */
    public OrderFragment() {

    }

    /**
     * newInstance，可传入数据，推荐用初始化方法
     *
     * @param bundle
     * @return
     */
    public static OrderFragment newInstance(Bundle bundle) {
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果有数据的话，可以取出来
        if (getArguments() != null) {

        }
    }

    /**
     * onCreateView，关联布局,创建View
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable//表示参数可为null
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_in_activity_main, container, false);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    /**
     * initView
     *
     * @param view
     */
    private void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
    }

    /**
     * initData
     */
    private void initData() {
        //初始化ViewPager和Fragment
        mAllOrderFragment = AllOrderFragment.newInstance(null);
        mClassifyOrderFragment = ClassifyOrderFragment.newInstance(null);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mAllOrderFragment);
        mFragmentList.add(mClassifyOrderFragment);
        mTitleList = new ArrayList<>();
        mTitleList.add(getString(R.string.fragment_order_all));
        mTitleList.add(getString(R.string.fragment_order_classify));
        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragmentList, mTitleList);//注：Fragment中嵌套Fragment时需要使用getChildFragmentManager
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setCurrentItem(0);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabsFromPagerAdapter(fragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * initEvent
     */
    private void initEvent() {
    }


}
