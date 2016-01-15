package com.waka.workspace.smalldianping.Activity.Main.Fragment.Order;


import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waka.workspace.smalldianping.Activity.Main.Fragment.Order.Adapter.AllOrderRecyclerViewAdapter;
import com.waka.workspace.smalldianping.Activity.Main.MainActivity;
import com.waka.workspace.smalldianping.DataBase.CommodityDB;
import com.waka.workspace.smalldianping.DataBase.Order;
import com.waka.workspace.smalldianping.DataBase.StoreDB;
import com.waka.workspace.smalldianping.R;


import java.util.ArrayList;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AllOrderFragment全部订单碎片
 * Created by waka on 2016/01/07.
 */
public class AllOrderFragment extends Fragment {

    private static final String TAG = "AllOrderFragment";

    //所有订单RecyclerView
    private RecyclerView mRvAllOrder;
    private List<Map<String, Object>> mListAllOrder;
    private AllOrderRecyclerViewAdapter mAdapterAllOrder;

    private Handler mHandler = new Handler() {
        int flag = 0;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (flag == 0) {
                        mRvAllOrder.setLayoutManager(new LinearLayoutManager(AllOrderFragment.this.getActivity()));
                        mAdapterAllOrder = new AllOrderRecyclerViewAdapter(AllOrderFragment.this.getActivity(), mListAllOrder);
                        mRvAllOrder.setAdapter(mAdapterAllOrder);
                        flag = 1;
                    } else {
                        mAdapterAllOrder.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    /**
     * 构造方法
     */
    public AllOrderFragment() {

    }

    /**
     * newInstance，可传入数据，推荐用初始化方法
     *
     * @param bundle
     * @return
     */
    public static AllOrderFragment newInstance(Bundle bundle) {
        AllOrderFragment fragment = new AllOrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_all_order_in_fragment_order, container, false);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new MyThread().start();
    }

    /**
     * initView
     *
     * @param view
     */
    private void initView(View view) {
        mRvAllOrder = (RecyclerView) view.findViewById(R.id.rvAllOrder);
    }

    /**
     * initData
     */
    private void initData() {
        mListAllOrder = new ArrayList<>();
    }

    /**
     * initEvent
     */
    private void initEvent() {
    }

    /**
     * 添加数据至AllOrderList
     *
     * @param tvOrderTitle
     * @param tvOrderStatus
     * @param imgOrderIcon
     * @param tvCreateOrderTime
     * @param tvOrderPrice
     */
    private void addDataToListAllOrder(String orderId, String tvOrderTitle, String tvOrderStatus, Bitmap imgOrderIcon, String tvCreateOrderTime, String tvOrderPrice) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("tvOrderTitle", tvOrderTitle);
        map.put("tvOrderStatus", tvOrderStatus);
        map.put("imgOrderIcon", imgOrderIcon);
        map.put("tvCreateOrderTime", tvCreateOrderTime);
        map.put("tvOrderPrice", tvOrderPrice);
        mListAllOrder.add(0, map);
    }

    /**
     * 读取数据库中订单信息线程
     */
    class MyThread extends Thread {
        @Override
        public void run() {
            mListAllOrder.clear();
            List<Order> listallorders = Order.getUserAllOrdersByUserID(MainActivity.login_user_id);
            for (int i = 0; i < listallorders.size(); i++) {
                String tmpgoodsname = CommodityDB.getGoodsName(listallorders.get(i).getGoods_id());
                String tmporderstate;
                if (listallorders.get(i).getOrder_state() == 0) {
                    tmporderstate = "未评价";
                } else {
                    tmporderstate = "已完成";
                }
                String tmptatalcost = "总价：￥" + String.valueOf(listallorders.get(i).getOrder_total_cost());

                String strpaydatetime = listallorders.get(i).getStr_order_pay_datetime();
                strpaydatetime = strpaydatetime.substring(0, strpaydatetime.length() - 3);
                String goodsId = listallorders.get(i).getGoods_id();
                Bitmap bitmap = CommodityDB.getGoodsBitmap(goodsId);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(AllOrderFragment.this.getResources(), R.mipmap.ic_launcher);
                }
                addDataToListAllOrder(String.valueOf(listallorders.get(i).getOrder_id()), tmpgoodsname, tmporderstate, bitmap, strpaydatetime, tmptatalcost);
            }

            Message message = Message.obtain(mHandler);
            message.what = 1;
            message.sendToTarget();
        }
    }
}
