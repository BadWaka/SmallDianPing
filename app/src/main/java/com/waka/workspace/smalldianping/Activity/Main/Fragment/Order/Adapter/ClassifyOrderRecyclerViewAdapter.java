package com.waka.workspace.smalldianping.Activity.Main.Fragment.Order.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.waka.workspace.smalldianping.R;

import java.util.List;
import java.util.Map;

/**
 * AllOrderRecyclerViewAdapter
 * Created by waka on 2016/1/7.
 */
public class ClassifyOrderRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Map<String, Object>> mDatas;

    /**
     * 构造方法
     *
     * @param context
     * @param datas
     */
    public ClassifyOrderRecyclerViewAdapter(Context context, List<Map<String, Object>> datas) {
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
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_classify_order_in_fragment_classify_order, parent, false));
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
        ((MyViewHolder) holder).imgClassifyOrderIcon.setBackgroundResource((Integer) mDatas.get(position).get("imgClassifyOrderIcon"));
        ((MyViewHolder) holder).tvClassifyOrderName.setText((String) mDatas.get(position).get("tvClassifyOrderName"));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 自定义ViewHolder
     */
    protected class MyViewHolder extends RecyclerView.ViewHolder {

        protected CardView cvClassifyOrder;
        protected ImageView imgClassifyOrderIcon;
        protected TextView tvClassifyOrderName;

        public MyViewHolder(View itemView) {
            super(itemView);
            cvClassifyOrder = (CardView) itemView.findViewById(R.id.cvClassifyOrder);
            imgClassifyOrderIcon = (ImageView) itemView.findViewById(R.id.imgClassifyOrderIcon);
            tvClassifyOrderName = (TextView) itemView.findViewById(R.id.tvClassifyOrderName);
        }
    }
}
