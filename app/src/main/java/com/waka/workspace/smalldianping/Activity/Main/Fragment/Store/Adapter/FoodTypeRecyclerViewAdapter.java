package com.waka.workspace.smalldianping.Activity.Main.Fragment.Store.Adapter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waka.workspace.smalldianping.Activity.ResultList.ResultListActivity;
import com.waka.workspace.smalldianping.Constant;
import com.waka.workspace.smalldianping.R;

import java.util.List;
import java.util.Map;

/**
 * 食品类型适配器
 * Created by waka on 2016/1/4.
 */
public class FoodTypeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Map<String, Object>> mDatas;

    /**
     * 构造方法
     *
     * @param context
     * @param datas
     */
    public FoodTypeRecyclerViewAdapter(Context context, List<Map<String, Object>> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycleview_item_food_type_in_fragment_store, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).imgFoodType.setBackgroundResource((Integer) mDatas.get(position).get("typeicon"));
        ((MyViewHolder) holder).tvFoodType.setText((String) mDatas.get(position).get("typename"));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 自定义ViewHolder
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout layoutFoodType;
        public ImageView imgFoodType;
        public TextView tvFoodType;

        public MyViewHolder(View itemView) {
            super(itemView);
            layoutFoodType = (LinearLayout) itemView.findViewById(R.id.layoutFoodType);
            imgFoodType = (ImageView) itemView.findViewById(R.id.imgFoodType);
            tvFoodType = (TextView) itemView.findViewById(R.id.tvFoodType);

            layoutFoodType.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layoutFoodType:
                    Snackbar.make(layoutFoodType, tvFoodType.getText().toString(), Snackbar.LENGTH_SHORT).show();
                    //动画,让图片旋转一圈
                    Animator animator = AnimatorInflater.loadAnimator(mContext, R.animator.anim_rotation_circle);
                    animator.setInterpolator(new BounceInterpolator());//设置补间器，设置动画的速率
                    animator.setTarget(imgFoodType);
                    animator.start();
                    //设置动画监听器
                    animator.addListener(new AnimatorListenerAdapter() {
                        //动画结束时 do something...
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
//                            Intent intent = new Intent(mContext, ResultListActivity.class);
//                            intent.putExtra("searchKey", tvFoodType.getText().toString());
//                            mContext.startActivity(intent);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }
}
