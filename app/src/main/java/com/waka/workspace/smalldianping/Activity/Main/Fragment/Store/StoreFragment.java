package com.waka.workspace.smalldianping.Activity.Main.Fragment.Store;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.waka.workspace.smalldianping.Activity.CityPicker.CityPickerDialogActivity;
import com.waka.workspace.smalldianping.Activity.Main.Fragment.Store.Adapter.FoodTypeRecyclerViewAdapter;
import com.waka.workspace.smalldianping.Activity.Main.Fragment.Store.Adapter.GuessYouLikeListViewAdapter;
import com.waka.workspace.smalldianping.Constant;
import com.waka.workspace.smalldianping.CustomViews.ListViewForScrollView;
import com.waka.workspace.smalldianping.QRCODE.CaptureActivity;
import com.waka.workspace.smalldianping.R;
import com.waka.workspace.smalldianping.Activity.SearchInput.SearchInputActivity;
import com.waka.workspace.smalldianping.StaticValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StoreFragment
 * Created by waka on 2015/12/22.
 */
public class StoreFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "StoreFragment";

    //标题栏
    private LinearLayout mLayoutCity, mLayoutScan;
    private TextView mTvCity;
    private ImageView mImgCityArrow;
    private CardView mCvSearch;

    //食品种类RecyclerView
    private RecyclerView mRvFoodType;
    private List<Map<String, Object>> mListFoodType;
    private FoodTypeRecyclerViewAdapter mFoodTypeAdapter;

    //猜你喜欢ListView
    private ListViewForScrollView mLvGuessYouLike;
    private List<Map<String, Object>> mListGuessYouLike;
    private GuessYouLikeListViewAdapter mGuessYouLikeAdapter;

    /**
     * 构造方法
     */
    public StoreFragment() {

    }

    /**
     * newInstance，可传入数据，推荐用初始化方法
     *
     * @param bundle
     * @return
     */
    public static StoreFragment newInstance(Bundle bundle) {
        StoreFragment fragment = new StoreFragment();
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
        View view = inflater.inflate(R.layout.fragment_store_in_activity_main, container, false);
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
        mCvSearch = (CardView) view.findViewById(R.id.cvSearch);
        mLayoutCity = (LinearLayout) view.findViewById(R.id.layoutCity);
        mTvCity = (TextView) view.findViewById(R.id.tvCity);
        mImgCityArrow = (ImageView) view.findViewById(R.id.imgCityArrow);
        mLayoutScan = (LinearLayout) view.findViewById(R.id.layoutScan);
        mRvFoodType = (RecyclerView) view.findViewById(R.id.rvFoodType);
        mLvGuessYouLike = (ListViewForScrollView) view.findViewById(R.id.lvGuessYouLike);
    }

    /**
     * initData
     */
    private void initData() {
        //初始化FoodTypeRecyclerView数据
        mListFoodType = new ArrayList<>();
        addDataToListFoodType(R.drawable.chinese_food, "中餐");
        addDataToListFoodType(R.drawable.western_food, "西餐");
        addDataToListFoodType(R.drawable.junk_food, "快餐");
        addDataToListFoodType(R.drawable.pizza, "披萨");
        addDataToListFoodType(R.drawable.drink, "饮品");
        addDataToListFoodType(R.drawable.hot_pot, "火锅");
        addDataToListFoodType(R.drawable.fruit, "水果");
        addDataToListFoodType(R.drawable.meat, "烤肉");
        addDataToListFoodType(R.drawable.icecream, "冰激凌");
        addDataToListFoodType(R.drawable.tea, "茶馆");
        mRvFoodType.setLayoutManager(new GridLayoutManager(this.getActivity(), 5));
        mFoodTypeAdapter = new FoodTypeRecyclerViewAdapter(this.getActivity(), mListFoodType);
        mRvFoodType.setAdapter(mFoodTypeAdapter);

        //初始化GuessYouLikeListView数据
        mListGuessYouLike = new ArrayList<>();
        addDataToListGuessYouLike(R.drawable.food_1, "梅菜扣肉", "1.3km", "【土吧那鸡】泡芙套餐，建议2人使用，包间免费，免预约", "59", "67", "7575");
        addDataToListGuessYouLike(R.drawable.food_2, "香辣鸡翅", "<500m", "【王府半岛酒店JING餐厅】北京市东城区王府井金鱼胡同8号王府半岛酒店B1楼", "89", "92", "4874");
        addDataToListGuessYouLike(R.drawable.food_3, "涮羊肉", "5.34km", "【九十九顶毡房】北京市海淀区 东升乡马坊村永泰庄北路9号(地铁8号线永泰庄站东北口...", "78", "83", "758");
        addDataToListGuessYouLike(R.drawable.food_4, "北京烤鸭", "<200m", "【胡大饭馆】北京市东城区东直门内大街233号", "58.2", "59", "2257");
        addDataToListGuessYouLike(R.drawable.food_5, "干锅虾", ">10km", "【晋风庄园(双林东路店)】北京市丰台区小屯村双林东路南口", "159", "161", "254");
        addDataToListGuessYouLike(R.drawable.food_6, "宫保鸡丁", "4.80km", "【港丽餐厅(世界城店)】北京市朝阳区金汇路8-9号世界城商业街102号铺", "67", "67", "5489");
        addDataToListGuessYouLike(R.drawable.food_7, "蚂蚁上树", "10.84km", "【西湖春天】北京市朝阳区广顺北大街33号嘉茂购物中心6-7楼", "89", "110", "1546");
        mGuessYouLikeAdapter = new GuessYouLikeListViewAdapter(this.getActivity(), mListGuessYouLike);
        mLvGuessYouLike.setAdapter(mGuessYouLikeAdapter);
    }

    /**
     * initEvent
     */
    private void initEvent() {
        mCvSearch.setOnClickListener(this);
        mLayoutCity.setOnClickListener(this);
        mLayoutScan.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutCity:
                mImgCityArrow.setImageResource(R.mipmap.ic_keyboard_arrow_up_white_24dp);
                Intent intent1 = new Intent(this.getActivity(), CityPickerDialogActivity.class);
                getActivity().startActivityForResult(intent1, Constant.CITY_PICKER_DIALOG_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.cvSearch:
                Intent intent = new Intent(this.getActivity(), SearchInputActivity.class);
                getActivity().startActivityForResult(intent, Constant.SEARCH_INPUT_ACTIVITY_REQUEST_CODE);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);//由左向右滑入的效果
                break;
            case R.id.layoutScan:
                Intent intent2 = new Intent(this.getActivity(), CaptureActivity.class);
                this.startActivityForResult(intent2,Constant.CAPTURE_ACTIVITY_REQUEST_CODE);

                break;
            default:
                break;
        }
    }

    /**
     * 添加数据至FoodTypeList
     *
     * @param typeicon
     * @param typename
     */
    private void addDataToListFoodType(int typeicon, String typename) {
        Map<String, Object> map = new HashMap<>();
        map.put("typeicon", typeicon);
        map.put("typename", typename);
        mListFoodType.add(map);
    }

    /**
     * 添加数据至GuessYouLikeList
     *
     * @param imgStoreIcon
     * @param tvStoreName
     * @param tvDistance
     * @param tvDescription
     * @param tvPrice
     * @param tvNormalPrice
     * @param tvAlreadySold
     */
    private void addDataToListGuessYouLike(int imgStoreIcon, String tvStoreName, String tvDistance, String tvDescription, String tvPrice, String tvNormalPrice, String tvAlreadySold) {
        Map<String, Object> map = new HashMap<>();
        map.put("imgStoreIcon", imgStoreIcon);
        map.put("tvStoreName", tvStoreName);
        map.put("tvDistance", tvDistance);
        map.put("tvDescription", tvDescription);
        map.put("tvPrice", "￥" + tvPrice);
        map.put("tvNormalPrice", "门市价：￥" + tvNormalPrice);
        map.put("tvAlreadySold", "已售" + tvAlreadySold);
        mListGuessYouLike.add(map);
    }

    /**
     * 设置箭头方向
     */
    public void setImgCityArrowBack() {
        mImgCityArrow.setImageResource(R.mipmap.ic_keyboard_arrow_down_white_24dp);
    }

    /**
     * 设置tvCity的文本
     *
     * @param text
     */
    public void setTvCityText(String text) {
        mTvCity.setText(text);
        StaticValues.cityName = text;
    }

}
