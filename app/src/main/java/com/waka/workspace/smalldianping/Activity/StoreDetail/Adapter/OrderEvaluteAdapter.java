package com.waka.workspace.smalldianping.Activity.StoreDetail.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.waka.workspace.smalldianping.Activity.Main.MainActivity;
import com.waka.workspace.smalldianping.DataBase.CommodityInformation;
import com.waka.workspace.smalldianping.DataBase.Order;
import com.waka.workspace.smalldianping.R;

import java.util.List;

/**
 * Created by WWC on 2016/1/11.
 */
public class OrderEvaluteAdapter extends ArrayAdapter<Order> {
    private int mresource;
    public OrderEvaluteAdapter(Context context, int resource,List<Order> objects) {
        super(context, resource,objects);
        this.mresource=resource;
    }
    @Override
    public View getView(int position, View evaluteView, ViewGroup parent) {
        Order order = getItem(position); // 获取当前项的Order实例
        View view = LayoutInflater.from(getContext()).inflate(mresource, null);
        TextView patron_textView = (TextView) view.findViewById(R.id.patron_textView);
        RatingBar evalute_ratingBar = (RatingBar) view.findViewById(R.id.evalute_ratingBar);
        TextView date_textview = (TextView) view.findViewById(R.id.date_textview);
        TextView evalute_textView = (TextView) view.findViewById(R.id.evalute_textView);
        patron_textView.setText(order.getUser_id());
        evalute_ratingBar.setRating((float)(order.get_order_rating()));
        date_textview.setText(order.str_evaluation_datetime().toString());
        evalute_textView.setText("       "+order.get_order_evaluation());
        return view;
    }
}
