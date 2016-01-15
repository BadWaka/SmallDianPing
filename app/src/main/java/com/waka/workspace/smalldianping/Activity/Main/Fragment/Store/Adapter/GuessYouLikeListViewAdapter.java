package com.waka.workspace.smalldianping.Activity.Main.Fragment.Store.Adapter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waka.workspace.smalldianping.Activity.ResultList.ResultListActivity;
import com.waka.workspace.smalldianping.R;

import java.util.List;
import java.util.Map;

/**
 * GuessYouLikeListViewAdapter
 * Created by waka on 2016/1/5.
 */
public class GuessYouLikeListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, Object>> mDatas;

    public GuessYouLikeListViewAdapter(Context context, List<Map<String, Object>> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new MyViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_guess_you_like_in_fragment_store, parent, false);
            viewHolder.cvGuessYouLike = (CardView) convertView.findViewById(R.id.cvGuessYouLike);
            viewHolder.imgStoreIcon = (ImageView) convertView.findViewById(R.id.imgStoreIcon);
            viewHolder.tvStoreName = (TextView) convertView.findViewById(R.id.tvStoreName);
            viewHolder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            viewHolder.tvNormalPrice = (TextView) convertView.findViewById(R.id.tvNormalPrice);
            viewHolder.tvAlreadySold = (TextView) convertView.findViewById(R.id.tvAlreadySold);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }
        //绑定数据
        viewHolder.imgStoreIcon.setBackgroundResource((Integer) mDatas.get(position).get("imgStoreIcon"));
        viewHolder.tvStoreName.setText((String) mDatas.get(position).get("tvStoreName"));
        viewHolder.tvDistance.setText((String) mDatas.get(position).get("tvDistance"));
        viewHolder.tvDescription.setText((String) mDatas.get(position).get("tvDescription"));
        viewHolder.tvPrice.setText((String) mDatas.get(position).get("tvPrice"));
        viewHolder.tvNormalPrice.setText((String) mDatas.get(position).get("tvNormalPrice"));
        viewHolder.tvAlreadySold.setText((String) mDatas.get(position).get("tvAlreadySold"));

        //注册点击事件
        final TextView tvPrice = viewHolder.tvPrice;
        final MyViewHolder finalViewHolder = viewHolder;
        viewHolder.cvGuessYouLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //代码设置属性动画
//                ObjectAnimator animScaleX = ObjectAnimator.ofFloat(tvPrice, "scaleX", 1f, 1.5f, 1f);//X轴缩放
//                ObjectAnimator animScaleY = ObjectAnimator.ofFloat(tvPrice, "scaleY", 1f, 1.5f, 1f);//Y轴缩放
//                //组合动画
//                AnimatorSet animatorSet = new AnimatorSet();
//                animatorSet.play(animScaleX).with(animScaleY);
//                animatorSet.setDuration(1000);
//                animatorSet.start();

                //xml设置属性动画
                Animator animator = AnimatorInflater.loadAnimator(mContext, R.animator.anim_scale_big_and_small);
                animator.setTarget(tvPrice);
                animator.start();

                //设置动画监听器
                animator.addListener(new AnimatorListenerAdapter() {
                    //动画结束时 do something...
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent(mContext, ResultListActivity.class);
                        intent.putExtra("searchKey", finalViewHolder.tvStoreName.getText().toString());
                        mContext.startActivity(intent);
                    }
                });
            }
        });
        return convertView;
    }

    /**
     * 内部类ViewHolder
     */
    final class MyViewHolder {
        public CardView cvGuessYouLike;
        public ImageView imgStoreIcon;
        public TextView tvStoreName, tvDistance, tvDescription, tvPrice, tvNormalPrice, tvAlreadySold;
    }
}
