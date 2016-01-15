package com.waka.workspace.smalldianping.Activity.StoreDetail.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.waka.workspace.smalldianping.DataBase.CommodityInformation;
import com.waka.workspace.smalldianping.DataBase.Order;
import com.waka.workspace.smalldianping.R;

import java.util.List;

/**
 * Created by WWC on 2016/1/4.
 */
public class StoreInformationAdapter extends ArrayAdapter<CommodityInformation>{
    private int mresourceId;
    public StoreInformationAdapter(Context context,int textViewResourceId,List<CommodityInformation> objects) {
        super(context, textViewResourceId, objects);
        mresourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommodityInformation commodityInformation = getItem(position); // 获取当前项的CommodityInformation实例
        View view = LayoutInflater.from(getContext()).inflate(mresourceId, null);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        TextView name = (TextView) view.findViewById(R.id.name_textView);
        TextView price = (TextView) view.findViewById(R.id.price_textView);
        TextView sold = (TextView) view.findViewById(R.id.sold_textView);
        Bitmap bitmap = commodityInformation.getImage();
        if(bitmap.getWidth()<bitmap.getHeight())
            bitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getWidth());
        else
            bitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getHeight(),bitmap.getHeight());
        image.setImageBitmap(bitmap);

        name.setText(commodityInformation.getName());
        price.setText("价格:" + String.valueOf(commodityInformation.getPrice()) + "元");
        sold.setText("已售:"+String.valueOf(commodityInformation.getSold())+"份");
        return view;
    }
}