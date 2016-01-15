package com.waka.workspace.smalldianping.Activity.Main;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.waka.workspace.smalldianping.Activity.Main.Fragment.Order.OrderFragment;
import com.waka.workspace.smalldianping.Adapter.MyFragmentPagerAdapter;
import com.waka.workspace.smalldianping.Constant;
import com.waka.workspace.smalldianping.Activity.Main.Fragment.MineFragment;
import com.waka.workspace.smalldianping.Activity.Main.Fragment.Store.StoreFragment;
import com.waka.workspace.smalldianping.DataBase.CommodityDB;
import com.waka.workspace.smalldianping.DataBase.DBHelper;
import com.waka.workspace.smalldianping.DataBase.Order;
import com.waka.workspace.smalldianping.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AMapLocationListener {

    private static final String TAG = "MainActivity";
    private long mExitTime = 0;//退出时间，用来实现双击退出功能

    private CoordinatorLayout mCoordinatorLayout;//总布局main

    //主体Fragment和ViewPager
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragmentList;
    private StoreFragment storeFragment;
    private OrderFragment orderFragment;
    private MineFragment mineFragment;
    private TabLayout mTabLayout;

    //底部选项卡
    private LinearLayout mLayoutStore, mLayoutOrder, mLayoutMine;
    private ImageView mImgStore, mImgOrder, mImgMine;
    private TextView mTvStore, mTvOrder, mTvMine;

    //定位相关
    private AMapLocationClient mAMapLocationClient;
    public static LatLng loactionLatLng = null;
    public static String locationCityName = null;

    //公共标志
    public static boolean isLogined = false;//是否登录标志
    public static String  login_user_id="";


    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");
        initView();
        initData();
        initEvent();



    }

    private void initView() {
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutMain);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        //底部选项卡
        mLayoutStore = (LinearLayout) findViewById(R.id.layoutStore);
        mLayoutOrder = (LinearLayout) findViewById(R.id.layoutOrder);
        mLayoutMine = (LinearLayout) findViewById(R.id.layoutMine);
        mImgStore = (ImageView) findViewById(R.id.imgStore);
        mImgOrder = (ImageView) findViewById(R.id.imgOrder);
        mImgMine = (ImageView) findViewById(R.id.imgMine);
        mTvStore = (TextView) findViewById(R.id.tvStore);
        mTvOrder = (TextView) findViewById(R.id.tvOrder);
        mTvMine = (TextView) findViewById(R.id.tvMine);
    }

    private void initData() {
        Order.dbhelper=new DBHelper(this);
        CommodityDB.dbHelper=new DBHelper(this);
        mAMapLocationClient = new AMapLocationClient(getApplicationContext());
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        ////设置是否返回地址信息（默认返回地址信息）
        //        mLocationOption.setNeedAddress(true);
        ////设置是否强制刷新WIFI，默认为强制刷新
        //        mLocationOption.setWifiActiveScan(true);
        ////设置是否允许模拟位置,默认为false，不允许模拟位置
        //        mLocationOption.setMockEnable(false);
        ////设置定位间隔,单位毫秒,默认为2000ms
        //        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mAMapLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mAMapLocationClient.startLocation();
        mAMapLocationClient.setLocationListener(this);
        //初始化ViewPager和Fragment
        storeFragment = StoreFragment.newInstance(null);
        orderFragment = OrderFragment.newInstance(null);
        mineFragment = MineFragment.newInstance(null);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(storeFragment);
        mFragmentList.add(orderFragment);
        mFragmentList.add(mineFragment);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mTabLayout.setTabsFromPagerAdapter(adapter);
        mViewPager.setAdapter(adapter);//设置适配器
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);//设置当前页为第一页
        mTvStore.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
        mImgStore.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
    }

    private void initEvent() {
        mViewPager.addOnPageChangeListener(new MainOnPageChangeListener());//设置翻页监听
        mLayoutStore.setOnClickListener(this);
        mLayoutOrder.setOnClickListener(this);
        mLayoutMine.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutStore:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.layoutOrder:
                mViewPager.setCurrentItem(1);
                orderFragment.mAllOrderFragment.onResume();
                break;
            case R.id.layoutMine:
                mViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    /**
     * 接收别的Activity返回的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //from LoginActivity
            case Constant.LOGIN_ACTIVITY_REQUEST_CODE:
                break;
            //from CityPickerDialogActivity
            case Constant.CITY_PICKER_DIALOG_ACTIVITY_REQUEST_CODE:
                storeFragment.setImgCityArrowBack();//当CityPickerDialogActivity退出的时候，将箭头方向改回来
                if (resultCode == RESULT_OK) {
                    String city = data.getStringExtra("city");
                    String[] param = city.split("-");
                    storeFragment.setTvCityText(param[1]);
//                    if (param[2].equals("其他")) {
//                        storeFragment.setTvCityText(param[1]);
//                    } else {
//                        storeFragment.setTvCityText(param[2]);
//                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 双击退出
     */
    private void doubleClickExit() {
        Log.i(TAG, "doubleClickExit---->" + mExitTime);
        //如果时间间隔大于2秒
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Snackbar.make(mCoordinatorLayout, R.string.activity_main_double_exit_tips, Snackbar.LENGTH_SHORT).setAction("delete", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//淡入淡出
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
                doubleClickExit();
                break;
        }
        return true;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                loactionLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());//获取经度
                locationCityName = aMapLocation.getCity();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError",
                        "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 内部类，实现OnPageChangeListener接口
     */
    class MainOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setTabColor(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 设置底部标签的颜色和动画
     *
     * @param tabPosition
     */
    private void setTabColor(int tabPosition) {
        Animator animator = AnimatorInflater.loadAnimator(MainActivity.this, R.animator.anim_rotation_circle);
        switch (tabPosition) {
            case 0:
                //设置旋转动画
                animator.setTarget(mImgStore);
                animator.setInterpolator(new AnticipateOvershootInterpolator());
                animator.start();
                //设置文字颜色
                mTvStore.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                mTvOrder.setTextColor(Color.WHITE);
                mTvMine.setTextColor(Color.WHITE);
                //设置图片颜色
                mImgStore.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                mImgOrder.setColorFilter(Color.WHITE);
                mImgMine.setColorFilter(Color.WHITE);
                break;
            case 1:
                //设置旋转动画
                animator.setTarget(mImgOrder);
                animator.setInterpolator(new AnticipateOvershootInterpolator());
                animator.start();
                //设置文字颜色
                mTvStore.setTextColor(Color.WHITE);
                mTvOrder.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                mTvMine.setTextColor(Color.WHITE);
                //设置图片颜色
                mImgStore.setColorFilter(Color.WHITE);
                mImgOrder.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                mImgMine.setColorFilter(Color.WHITE);
                break;
            case 2:
                //设置旋转动画
                animator.setTarget(mImgMine);
                animator.setInterpolator(new AnticipateOvershootInterpolator());
                animator.start();
                //设置文字颜色
                mTvStore.setTextColor(Color.WHITE);
                mTvOrder.setTextColor(Color.WHITE);
                mTvMine.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                //设置图片颜色
                mImgStore.setColorFilter(Color.WHITE);
                mImgOrder.setColorFilter(Color.WHITE);
                mImgMine.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                break;
            default:
                break;
        }
    }

}
