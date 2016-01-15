package com.waka.workspace.smalldianping.Activity.SearchInput.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.waka.workspace.smalldianping.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 热门搜索Fragment
 * Created by waka on 2015/12/24.
 */
public class HotSearchFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "HotSearchFragment";

    private GridView mGridView;
    private String[] mItemData;

    private OnFragmentInteractionListener mListener;

    /**
     * 构造方法
     */
    public HotSearchFragment() {

    }

    /**
     * newInstance，可传入数据，推荐用初始化方法
     *
     * @param data
     * @return
     */
    public static HotSearchFragment newInstance(String[] data) {
        HotSearchFragment fragment = new HotSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("hot_search", data);
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
            mItemData = getArguments().getStringArray("hot_search");
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
        View view = inflater.inflate(R.layout.fragment_hot_search_in_activity_search_input, container, false);
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
        mGridView = (GridView) view.findViewById(R.id.gridView);
    }

    /**
     * initData
     */
    private void initData() {
        ArrayList<HashMap<String, Object>> mapArrayList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < mItemData.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("hot_search", mItemData[i]);
            mapArrayList.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), mapArrayList, R.layout.gridview_item_in_fragment_hot_search, new String[]{"hot_search"}, new int[]{R.id.tvHotSearch});
        mGridView.setAdapter(simpleAdapter);
    }

    /**
     * initEvent
     */
    private void initEvent() {
        mGridView.setOnItemClickListener(this);
    }

    /**
     * GridView,Item点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view.findViewById(R.id.tvHotSearch);
        sendDataToActivity(textView.getText().toString());
        Log.i(TAG, textView.getText().toString());
    }

    /**
     * 定义交互接口，与activity进行交互
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String s);
    }

    /**
     * 向Activity传递数据
     *
     * @param s
     */
    public void sendDataToActivity(String s) {
        if (mListener != null) {
            mListener.onFragmentInteraction(s);
        }
    }

    /**
     * 建立联系时
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * 断开联系时
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
