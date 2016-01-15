package com.waka.workspace.smalldianping.Activity.StoreDetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.waka.workspace.smalldianping.Activity.StoreDetail.Adapter.OrderEvaluteAdapter;
import com.waka.workspace.smalldianping.Activity.StoreDetail.Adapter.StoreInformationAdapter;
import com.waka.workspace.smalldianping.DataBase.*;
import com.waka.workspace.smalldianping.R;

import java.util.ArrayList;
import java.util.List;


public class StoreDetailActivity extends AppCompatActivity {
    private TabHost myTabHost;
    private int[]layRes={R.id.tab_order_dish,R.id.tab_evaluate,R.id.tab_store};
    private String[] mtitlebar={"点菜","评价","商家"};
    private List<CommodityInformation> mcommodityList = new ArrayList<CommodityInformation>();
    float rating;
    private static String storeID;
    public static StoreDB presentStore;
    private ListView listView;
    private ListView evalute_listView;
    private LinearLayout linearLayout;
    private List<Order> evaluteList = new ArrayList<Order>();
    private List<Order> part_evaluteList = new ArrayList<Order>();
    private Button all_evalute_button;
    private Button good_evalute_button;
    private Button bad_evalute_button;
    private Button middle_evalute_button;
    private OrderEvaluteAdapter evalute_adapter;
    private Order order;
    private int choiceFlag=0;
    private TextView locate_textView;
    private TextView consumption_per_person_textView;
    private TextView store_introduce_textView;
    private TextView phone_number_textView;
    private TextView businessarea_textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        storeID=presentStore.getStoreID();
        TextView store_name_textView= (TextView) findViewById(R.id.store_name_textView);//商店名称
        store_name_textView.setText(presentStore.getStoreName());
        RatingBar ratingBar= (RatingBar) findViewById(R.id.ratingBar);//商店评分
        rating= (float) presentStore.getStoreRating();
        if(rating<0)
            rating=0;
        TextView textView= (TextView) findViewById(R.id.rating_textView);
        textView.setText("   " + String.valueOf(rating));
        ratingBar.setRating(rating);

        ImageView imageView= (ImageView) findViewById(R.id.topimageView);//商店首页头部图片
        if(presentStore.getImageBytes()!=null)
        {
            Bitmap bitmap= BitmapFactory.decodeByteArray(presentStore.getImageBytes(), 0, presentStore.getImageBytes().length);
            imageView.setImageBitmap(bitmap);
        }
        else
        {
            textView.setText("   " + "暂未入住");
        }
        // 初始化商品数据
        CommodityDB commodityDB=new CommodityDB(this,storeID);
        commodityDB.initIntroduction();
        mcommodityList=commodityDB.commodityList;
        StoreInformationAdapter adapter = new StoreInformationAdapter(StoreDetailActivity.this, R.layout.store_detail, mcommodityList);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StoreDetailActivity.this, CommodityDetailActivity.class);
                CommodityDetailActivity.commodityInformation = (CommodityInformation) listView.getItemAtPosition(position);
                startActivity(intent);
            }
        });

        //评价信息显示
        order=new Order("","","",0,0.0);
        evaluteList=order.getUserAllOrdersByStoreID(storeID);
        for(int i=0;i<evaluteList.size();i++) {
            if(evaluteList.get(i).getOrder_state()==1)
                part_evaluteList.add(evaluteList.get(i));
        }
        evalute_adapter = new OrderEvaluteAdapter(StoreDetailActivity.this, R.layout.evalute_detail, part_evaluteList);
        evalute_listView= (ListView) findViewById(R.id.evalute_listview);
        evalute_listView.setAdapter(evalute_adapter);

        this.myTabHost= (TabHost) super.findViewById(R.id.tabhost);
        this.myTabHost.setup();
        for(int i=0;i<3;i++)
        {
            TabHost.TabSpec myTab=myTabHost.newTabSpec("tab" + i);
            myTab.setIndicator(this.mtitlebar[i]);
            myTab.setContent(this.layRes[i]);
            this.myTabHost.addTab(myTab);
        }
        this.myTabHost.setCurrentTab(0);
        TabWidget tabWidget=myTabHost.getTabWidget();
        for(int i=0;i<tabWidget.getChildCount();i++)
        {
            View view=tabWidget.getChildTabViewAt(i);
            final TextView tv=(TextView)view.findViewById(android.R.id.title);
            tv.setTextSize(18);
            tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
        }


        all_evalute_button= (Button) findViewById(R.id.all_evalute_button);
        good_evalute_button= (Button) findViewById(R.id.good_evalute_button);
        middle_evalute_button= (Button) findViewById(R.id.middle_evalute_button);
        bad_evalute_button= (Button) findViewById(R.id.bad_evalute_button);
        evaluteButton();
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.layoutBack_to_ResultListActivity);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreDetailActivity.this.finish();
            }
        });

        //商店介绍
        locate_textView= (TextView) findViewById(R.id.locate_textView);
        consumption_per_person_textView= (TextView) findViewById(R.id.consumption_per_person_textView);
        store_introduce_textView= (TextView) findViewById(R.id.store_introduce_textView);
        phone_number_textView= (TextView) findViewById(R.id.phone_number_textView);
        businessarea_textView= (TextView) findViewById(R.id.businessarea_textView);
        storeIntroduce();
        //this.onDestroy();
    }
    private void buttonSet(int choiceFlag)
    {
        switch(choiceFlag)
        {
            case 0:
                all_evalute_button.setBackgroundResource(R.drawable.button_bg);
                all_evalute_button.setTextColor(Color.parseColor("#FFA500"));
                break;
            case 1:
                good_evalute_button.setBackgroundResource(R.drawable.button_bg);
                good_evalute_button.setTextColor(Color.parseColor("#FFA500"));
                break;
            case 2:
                middle_evalute_button.setBackgroundResource(R.drawable.button_bg);
                middle_evalute_button.setTextColor(Color.parseColor("#FFA500"));
                break;
            case 3:
                bad_evalute_button.setBackgroundResource(R.drawable.button_bg);
                bad_evalute_button.setTextColor(Color.parseColor("#FFA500"));
                break;


        }
    }
    private void evaluteButton(){
        int allNum=0,goodNum=0,middleNum=0,badNum=0;
        for(int i=0;i<evaluteList.size();i++)
        {
            if(evaluteList.get(i).getOrder_state()==1) {
                allNum+=1;
                if (evaluteList.get(i).get_order_rating() >= 4)
                    goodNum += 1;
                else if (evaluteList.get(i).get_order_rating() > 1 && evaluteList.get(i).get_order_rating() < 4)
                    middleNum += 1;
                else if (evaluteList.get(i).get_order_rating() <= 1 && evaluteList.get(i).get_order_rating() >= 0)
                    badNum += 1;
            }
        }
        all_evalute_button.setText("全部"+String.valueOf(allNum));
        good_evalute_button.setText("好评"+String.valueOf(goodNum));
        middle_evalute_button.setText("中评"+String.valueOf(middleNum));
        bad_evalute_button.setText("差评"+String.valueOf(badNum));

        all_evalute_button.setTextColor(Color.parseColor("#FFFFFF"));
        all_evalute_button.setBackgroundColor(Color.parseColor("#FFA500"));

        all_evalute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSet(choiceFlag);
                choiceFlag = 0;
                all_evalute_button.setTextColor(Color.parseColor("#FFFFFF"));
                all_evalute_button.setBackgroundColor(Color.parseColor("#FFA500"));
                evalute_adapter.clear();
                evalute_listView.invalidate();
                evaluteList = order.getUserAllOrdersByStoreID(storeID);
                for(int i=0;i<evaluteList.size();i++) {
                    if(evaluteList.get(i).getOrder_state()==1)
                        part_evaluteList.add(evaluteList.get(i));
                }
                evalute_adapter = new OrderEvaluteAdapter(StoreDetailActivity.this, R.layout.evalute_detail, part_evaluteList);
                evalute_listView.setAdapter(evalute_adapter);
            }
        });

        good_evalute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSet(choiceFlag);
                choiceFlag=1;
                evalute_adapter.clear();
                good_evalute_button.setTextColor(Color.parseColor("#FFFFFF"));
                good_evalute_button.setBackgroundColor(Color.parseColor("#FFA500"));
                evalute_listView.invalidate();
                evaluteList=order.getUserAllOrdersByStoreID(storeID);
                for(int i=0;i<evaluteList.size();i++) {
                    if(evaluteList.get(i).get_order_rating()>=4&&evaluteList.get(i).getOrder_state()==1)
                        part_evaluteList.add(evaluteList.get(i));
                }
                evalute_adapter = new OrderEvaluteAdapter(StoreDetailActivity.this, R.layout.evalute_detail, part_evaluteList);
                evalute_listView.setAdapter(evalute_adapter);
            }
        });
        middle_evalute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSet(choiceFlag);
                choiceFlag = 2;
                middle_evalute_button.setTextColor(Color.parseColor("#FFFFFF"));
                middle_evalute_button.setBackgroundColor(Color.parseColor("#FFA500"));

                evalute_adapter.clear();
                evalute_listView.invalidate();
                evaluteList=order.getUserAllOrdersByStoreID(storeID);
                for(int i=0;i<evaluteList.size();i++) {
                    if(evaluteList.get(i).get_order_rating()>1&&evaluteList.get(i).get_order_rating()<4&&evaluteList.get(i).getOrder_state()==1)
                        part_evaluteList.add(evaluteList.get(i));
                }
                evalute_adapter = new OrderEvaluteAdapter(StoreDetailActivity.this, R.layout.evalute_detail, part_evaluteList);
                evalute_listView.setAdapter(evalute_adapter);
            }
        });
        bad_evalute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSet(choiceFlag);
                choiceFlag=3;
                bad_evalute_button.setTextColor(Color.parseColor("#FFFFFF"));
                bad_evalute_button.setBackgroundColor(Color.parseColor("#FFA500"));

                evalute_adapter.clear();
                evalute_listView.invalidate();
                evaluteList=order.getUserAllOrdersByStoreID(storeID);
                for(int i=0;i<evaluteList.size();i++) {
                    if(evaluteList.get(i).get_order_rating()<=1&&evaluteList.get(i).get_order_rating()>=0&&evaluteList.get(i).getOrder_state()==1)
                        part_evaluteList.add(evaluteList.get(i));
                }
                evalute_adapter = new OrderEvaluteAdapter(StoreDetailActivity.this, R.layout.evalute_detail, part_evaluteList);
                evalute_listView.setAdapter(evalute_adapter);
            }
        });
    }
    private void storeIntroduce() {
        locate_textView.setText(presentStore.getStore_addresss());
        consumption_per_person_textView.setText(String.valueOf(Math.round(presentStore.getStoreAvgcost()))+"元");
        store_introduce_textView.setText("       "+presentStore.getStore_detail());
        phone_number_textView.setText(presentStore.getPoiItem().getTel());
        businessarea_textView.setText(presentStore.getPoiItem().getBusinessArea());
    }
    @Override
    protected void onResume() {
        super.onResume();
        CommodityDB commodityDB=new CommodityDB(this,storeID);
        commodityDB.initIntroduction();
        mcommodityList=commodityDB.commodityList;
        StoreInformationAdapter adapter = new StoreInformationAdapter(StoreDetailActivity.this, R.layout.store_detail, mcommodityList);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

}

