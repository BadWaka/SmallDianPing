package com.waka.workspace.smalldianping.Activity.Main.Fragment.Order;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.waka.workspace.smalldianping.DataBase.Order;
import com.waka.workspace.smalldianping.R;

/**
 * 订单评价Activity
 */
public class OrderEvaluateActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener, View.OnClickListener {

    //Toolbar
    private Toolbar toolbar;
    private ImageView imgToolbar;

    //星星条
    private RatingBar ratingBar;

    //评价输入栏
    private AutoCompleteTextView actvEvaluate;

    //fab
    private FloatingActionButton fabCommitEvaluate;

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_evaluate);
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
        //星星条
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //评价输入栏
        actvEvaluate = (AutoCompleteTextView) findViewById(R.id.actvEvaluate);
        //fab
        fabCommitEvaluate = (FloatingActionButton) findViewById(R.id.fabCommitEvaluate);
    }

    /**
     * initData
     */
    private void initData() {
        //设置toolbar
        toolbar.setTitle(getString(R.string.activity_order_evaluate_title));// 标题的文字需在setSupportActionBar之前，不然会无效
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        //设置星星bar回调监听
        ratingBar.setOnRatingBarChangeListener(this);
        //fab
        fabCommitEvaluate.setOnClickListener(this);


    }

    /**
     * 星星bar变化回调
     *
     * @param ratingBar
     * @param rating
     * @param fromUser
     */
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        String ratingText = "";
        if (rating >= 0 && rating <= 1) {
            ratingText = "Oh~~~ Shit!";
        } else if (rating > 1 && rating <= 2) {
            ratingText = "Damn It";
        } else if (rating > 2 && rating <= 3) {
            ratingText = "Emmmmmm...";
        } else if (rating > 3 && rating <= 4) {
            ratingText = "Not So Bad~";
        } else if (rating > 4 && rating < 5) {
            ratingText = "Nice Job Man!";
        } else if (rating == 5) {
            ratingText = "What The F**king Awesome!!!";
        }
        Snackbar.make(ratingBar, rating * 2 + "分 ", Snackbar.LENGTH_SHORT).setAction(ratingText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    /**
     * 点击事件分发
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCommitEvaluate:
                commitEvaluate();
                break;
            default:
                break;
        }
    }

    /**
     * 提交评价
     */
    private void commitEvaluate() {
        //先让FloatingActionButton转一圈
        Animator animator = AnimatorInflater.loadAnimator(OrderEvaluateActivity.this, R.animator.anim_rotation_circle);
        animator.setInterpolator(new AnticipateOvershootInterpolator());//设置补间器，设置动画的速率
        animator.setTarget(fabCommitEvaluate);
        animator.start();
        //设置动画监听器
        animator.addListener(new AnimatorListenerAdapter() {
            //动画结束时 跳转
            @Override
            public void onAnimationEnd(Animator animation) {
                if (actvEvaluate.getText().toString().equals("") || ratingBar.getNumStars() == 0) {
                    return;
                }
                Order.addOrderevaluation(Integer.valueOf(OrderDetailActivity.order_id), actvEvaluate.getText().toString(), ratingBar.getRating());
                finish();
            }
        });
    }
}
