package com.waka.workspace.smalldianping.Activity.Main.Fragment.Order;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waka.workspace.smalldianping.Activity.Main.Fragment.Order.Adapter.AllOrderRecyclerViewAdapter;
import com.waka.workspace.smalldianping.Constant;
import com.waka.workspace.smalldianping.DataBase.CommodityDB;
import com.waka.workspace.smalldianping.DataBase.DBHelper;
import com.waka.workspace.smalldianping.DataBase.Order;
import com.waka.workspace.smalldianping.DataBase.StoreDB;
import com.waka.workspace.smalldianping.QRCODE.QRCodeImageCreater;
import com.waka.workspace.smalldianping.R;

/**
 * 订单详情Activity
 */
public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //Toolbar
    private Toolbar toolbar;
    private ImageView imgToolbar;

    //订单状态，点击可跳转到评价Activity
    private CardView cvOrderStatus;
    private TextView tvOrderStatus, tvCompleteTime;//订单状态和订单完成时间

    //订单详情
    private TextView tvOrderID;//订单号码
    private TextView tvPayTime;//付款时间
    private TextView tvStoreName;//商家名称
    private TextView tvStoreAddress;//商家地址
    private TextView tvOrderGoodsName;//商品名称
    private TextView tvOrderGoodsNumber;//商品数量
    private TextView tvOrderTotalCost;//订单金额
    private LinearLayout layoutOrderKey;//订单使用码布局
    private TextView tvOrderKey;//订单使用码
    private TextView tvOrderSuggestion;
    public static String order_id;
    private ImageView ivQRCode;

    //FloatingActionButton，点击可跳转到评价Activity
    private FloatingActionButton fabGoEvaluate;
    private boolean isJumpToOrderEvaluateActivity = true;//跳转到订单评价界面标记
    QRCodeImageCreater qrcodeic;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ivQRCode.setImageBitmap(qrcodeic.getBitmap());
                    break;
            }
        }
    };

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        initData();
        initEvent();
    }

    /**
     * initView
     */
    private void initView() {
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgToolbar = (ImageView) findViewById(R.id.imgToolbar);

        //订单状态
        cvOrderStatus = (CardView) findViewById(R.id.cvOrderStatus);
        tvOrderStatus = (TextView) findViewById(R.id.tvOrderStatus);
        tvCompleteTime = (TextView) findViewById(R.id.tvCompleteTime);
        tvOrderSuggestion = (TextView) findViewById(R.id.tvOrderSuggestion);

        //订单详情
        tvOrderID = (TextView) findViewById(R.id.tvOrderID);
        tvPayTime = (TextView) findViewById(R.id.tvPayTime);
        tvStoreName = (TextView) findViewById(R.id.tvStoreName);
        tvStoreAddress = (TextView) findViewById(R.id.tvStoreAddress);
        tvOrderGoodsName = (TextView) findViewById(R.id.tvOrderGoodsName);
        tvOrderGoodsNumber = (TextView) findViewById(R.id.tvOrderGoodsNumber);
        tvOrderTotalCost = (TextView) findViewById(R.id.tvOrderTotalCost);
        tvOrderKey = (TextView) findViewById(R.id.tvOrderKey);
        layoutOrderKey = (LinearLayout) findViewById(R.id.layoutOrderKey);
        ivQRCode = (ImageView) findViewById(R.id.qrcode_evaluate_detail_activity_imageview);

        //FloatingActionButton，点击可跳转到评价Activity
        fabGoEvaluate = (FloatingActionButton) findViewById(R.id.fabGoEvaluate);


    }

    /**
     * initData
     */
    private void initData() {
        //设置toolbar
        toolbar.setTitle(getString(R.string.activity_order_detail_title));// 标题的文字需在setSupportActionBar之前，不然会无效
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        order_id = intent.getStringExtra("orderId");
        Order order = new Order(order_id);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StoreDB store = new StoreDB(order.getStore_id(), db);
        Bitmap bitmap = CommodityDB.getGoodsBitmap(order.getGoods_id());
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.order_detail_background);
        }
        imgToolbar.setBackground(new BitmapDrawable(bitmap));
        db.close();

        tvOrderID.setText(String.valueOf(order.getOrder_id()));
        tvPayTime.setText(order.getStr_order_pay_datetime());
        tvStoreName.setText(store.getStoreName());
        tvStoreAddress.setText(store.getStoreSnippet());
        tvOrderGoodsName.setText(CommodityDB.getGoodsName(order.getGoods_id()));
        tvOrderGoodsNumber.setText(String.valueOf(order.getGoods_num()));
        tvOrderTotalCost.setText(String.valueOf(order.getOrder_total_cost()));
        tvOrderKey.setText(order.getOrder_key());
        qrcodeic = new QRCodeImageCreater(order.getOrder_key(), mHandler);
        qrcodeic.start();

    }

    /**
     * initEvent
     */
    private void initEvent() {
        //设置toolbar返回键监听
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fabGoEvaluate.setOnClickListener(this);
    }

    /**
     * 点击事件分发
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabGoEvaluate:
                if (isJumpToOrderEvaluateActivity) {
                    isJumpToOrderEvaluateActivity = false;
                    gotoOrderEvaluateActivity();
                } else {
                    Animator animator = AnimatorInflater.loadAnimator(OrderDetailActivity.this, R.animator.anim_rotation_circle);
                    animator.setInterpolator(new AnticipateOvershootInterpolator());//设置补间器，设置动画的速率
                    animator.setTarget(fabGoEvaluate);
                    animator.start();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到OrderEvaluateActivity
     */
    private void gotoOrderEvaluateActivity() {
        //先让FloatingActionButton转一圈
        final Intent intent = new Intent(OrderDetailActivity.this, OrderEvaluateActivity.class);
        Animator animator = AnimatorInflater.loadAnimator(OrderDetailActivity.this, R.animator.anim_rotation_circle);
        animator.setInterpolator(new AnticipateOvershootInterpolator());//设置补间器，设置动画的速率
        animator.setTarget(fabGoEvaluate);
        animator.start();
        //设置动画监听器
        animator.addListener(new AnimatorListenerAdapter() {
            //动画结束时 跳转
            @Override
            public void onAnimationEnd(Animator animation) {
                startActivityForResult(intent, Constant.ORDER_EVALUATE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    /**
     * 接收Activity回传的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.ORDER_EVALUATE_ACTIVITY_REQUEST_CODE:
                isJumpToOrderEvaluateActivity = true;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Order order = new Order(order_id);
        if (order.getOrder_state() == 1) {
            isJumpToOrderEvaluateActivity = false;
            tvOrderStatus.setText("已评价");
            tvCompleteTime.setText(order.getStr_evaluation_datetime());
            tvOrderSuggestion.setText("感谢您对订单做出评价！");
        }
    }
}


