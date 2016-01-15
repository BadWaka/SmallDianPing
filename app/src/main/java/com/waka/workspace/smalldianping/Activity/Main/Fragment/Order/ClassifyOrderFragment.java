package com.waka.workspace.smalldianping.Activity.Main.Fragment.Order;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waka.workspace.smalldianping.Activity.Main.Fragment.Order.Adapter.ClassifyOrderRecyclerViewAdapter;
import com.waka.workspace.smalldianping.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassifyOrderFragment分类订单碎片
 * Created by waka on 2016/01/07.
 */
public class ClassifyOrderFragment extends Fragment {

    private static final String TAG = "ClassifyOrderFragment";

    //所有订单RecyclerView
    private RecyclerView mRvClassifyOrder;
    private List<Map<String, Object>> mListClassifyOrder;
    private ClassifyOrderRecyclerViewAdapter mAdapterClassifyOrder;

    /**
     * 构造方法
     */
    public ClassifyOrderFragment() {

    }

    /**
     * newInstance，可传入数据，推荐用初始化方法
     *
     * @param bundle
     * @return
     */
    public static ClassifyOrderFragment newInstance(Bundle bundle) {
        ClassifyOrderFragment fragment = new ClassifyOrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_classify_order_in_fragment_order, container, false);
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
        mRvClassifyOrder = (RecyclerView) view.findViewById(R.id.rvClassifyOrder);
    }

    /**
     * initData
     */
    private void initData() {
        mListClassifyOrder = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            addDataToClassifyOrder(R.mipmap.ic_launcher, "美食");
        }
        mRvClassifyOrder.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mAdapterClassifyOrder = new ClassifyOrderRecyclerViewAdapter(this.getActivity(), mListClassifyOrder);
        mRvClassifyOrder.setAdapter(mAdapterClassifyOrder);
    }

    /**
     * initEvent
     */
    private void initEvent() {
    }

    /**
     * 添加数据至ClassifyOrder
     *
     * @param imgClassifyOrderIcon
     * @param tvClassifyOrderName
     */
    private void addDataToClassifyOrder(int imgClassifyOrderIcon, String tvClassifyOrderName) {
        Map<String, Object> map = new HashMap<>();
        map.put("imgClassifyOrderIcon", imgClassifyOrderIcon);
        map.put("tvClassifyOrderName", tvClassifyOrderName);
        mListClassifyOrder.add(map);
    }
}
