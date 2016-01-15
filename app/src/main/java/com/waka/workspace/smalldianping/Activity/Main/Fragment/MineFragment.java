package com.waka.workspace.smalldianping.Activity.Main.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.waka.workspace.smalldianping.Constant;
import com.waka.workspace.smalldianping.Activity.Main.MainActivity;
import com.waka.workspace.smalldianping.R;

/**
 * MineFragment
 * Created by waka on 2015/12/22.
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MineFragment";
    private CardView mCvUser;
    private TextView mTvLoginTips, mTvUserName;
    private TextView mTvGoToRegist;
    private Button btnLoginOut;

    /**
     * 构造方法
     */
    public MineFragment() {

    }

    /**
     * newInstance，可传入数据，推荐用初始化方法
     *
     * @param bundle
     * @return
     */
    public static MineFragment newInstance(Bundle bundle) {
        MineFragment fragment = new MineFragment();
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
        View view = inflater.inflate(R.layout.fragment_mine_in_activity_main, container, false);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    private void initView(View view) {
        mCvUser = (CardView) view.findViewById(R.id.cvUser);
        mTvUserName = (TextView) view.findViewById(R.id.tvUserName);
        mTvGoToRegist = (TextView) view.findViewById(R.id.regist_mine_fragment_textview);
        btnLoginOut = (Button) view.findViewById(R.id.btnLoginOut);

    }

    private void initData() {
        updateUIByIsLogined();
    }

    private void initEvent() {

        mCvUser.setOnClickListener(this);
        mTvGoToRegist.setOnClickListener(this);
        btnLoginOut.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvUser:
                Intent intent = new Intent(this.getActivity(), LoginActivity.class);
                getActivity().startActivityForResult(intent, Constant.LOGIN_ACTIVITY_REQUEST_CODE);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);//由左向右滑入的效果
                break;
            case R.id.regist_mine_fragment_textview:
                Intent intent2 = new Intent(this.getActivity(), RegisterActivity.class);
                this.startActivityForResult(intent2,Constant.REGIST_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.btnLoginOut:
                MainActivity.login_user_id="";
                updateUIByIsLogined();
                break;
            default:
                break;
        }
    }

    /**
     * 根据用户登录状态更新UI
     */
    public void updateUIByIsLogined() {


        if(MainActivity.login_user_id!=""){
            btnLoginOut.setVisibility(View.VISIBLE);
            mTvUserName.setText("当前用户：" + MainActivity.login_user_id);
        }else{
            mTvUserName.setText("点击登录");
            btnLoginOut.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUIByIsLogined();
    }
}
