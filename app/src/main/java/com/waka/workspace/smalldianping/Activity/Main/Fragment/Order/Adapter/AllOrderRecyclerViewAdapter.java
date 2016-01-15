package com.waka.workspace.smalldianping.Activity.Main.Fragment.Order.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.waka.workspace.smalldianping.Activity.Main.Fragment.Order.OrderDetailActivity;
import com.waka.workspace.smalldianping.Constant;
import com.waka.workspace.smalldianping.R;

import java.util.List;
import java.util.Map;

/**
 * AllOrderRecyclerViewAdapter
 * Created by waka on 2016/1/7.
 */
public class AllOrderRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Map<String, Object>> mDatas;

    /**
     * 构造方法
     *
     * @param context
     * @param datas
     */
    public AllOrderRecyclerViewAdapter(Context context, List<Map<String, Object>> datas) {
        mContext = context;
        mDatas = datas;
    }

    /**
     * 创建ViewHolder,初始化View
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_food_order_in_fragment_all_order, parent, false));
        return viewHolder;
    }

    /**
     * 绑定ViewHolder,初始化数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).orderId = (String) mDatas.get(position).get("orderId");
        ((MyViewHolder) holder).tvOrderTitle.setText((String) mDatas.get(position).get("tvOrderTitle"));
        ((MyViewHolder) holder).tvOrderStatus.setText((String) mDatas.get(position).get("tvOrderStatus"));
//        ((MyViewHolder) holder).imgOrderIcon.setBackgroundResource((Integer) mDatas.get(position).get("imgOrderIcon"));
        //TODO 将int型改为bitmap
        ((MyViewHolder) holder).imgOrderIcon.setBackground(new BitmapDrawable((Bitmap) mDatas.get(position).get("imgOrderIcon")));
        ((MyViewHolder) holder).tvCreateOrderTime.setText((String) mDatas.get(position).get("tvCreateOrderTime"));
        ((MyViewHolder) holder).tvOrderPrice.setText((String) mDatas.get(position).get("tvOrderPrice"));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 自定义ViewHolder
     */
    protected class MyViewHolder extends RecyclerView.ViewHolder {

        protected String orderId;
        protected CardView cvFoodOrder;
        protected ImageView imgOrderType, imgOrderIcon;
        protected TextView tvOrderTitle, tvOrderStatus, tvCreateOrderTime, tvOrderPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            orderId = "";
            cvFoodOrder = (CardView) itemView.findViewById(R.id.cvFoodOrder);
            imgOrderType = (ImageView) itemView.findViewById(R.id.imgOrderType);
            imgOrderIcon = (ImageView) itemView.findViewById(R.id.imgOrderIcon);
            tvOrderTitle = (TextView) itemView.findViewById(R.id.tvOrderTitle);
            tvOrderStatus = (TextView) itemView.findViewById(R.id.tvOrderStatus);
            tvCreateOrderTime = (TextView) itemView.findViewById(R.id.tvCreateOrderTime);
            tvOrderPrice = (TextView) itemView.findViewById(R.id.tvOrderPrice);

            cvFoodOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("orderId", orderId);
                    ((Activity) mContext).startActivityForResult(intent, Constant.ORDER_DETAIL_ACTIVITY_REQUEST_CODE);
                }
            });
        }
    }
}
