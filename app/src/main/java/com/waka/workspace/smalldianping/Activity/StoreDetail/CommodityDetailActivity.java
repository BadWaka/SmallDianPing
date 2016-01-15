package com.waka.workspace.smalldianping.Activity.StoreDetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waka.workspace.smalldianping.Activity.Main.MainActivity;
import com.waka.workspace.smalldianping.Activity.StoreDetail.Adapter.OrderEvaluteAdapter;
import com.waka.workspace.smalldianping.DataBase.CommodityInformation;
import com.waka.workspace.smalldianping.DataBase.Order;
import com.waka.workspace.smalldianping.R;

import java.util.ArrayList;
import java.util.List;

public class CommodityDetailActivity extends Activity {

    public static CommodityInformation commodityInformation;
    TextView sold_textView;
    private Button commodity_all_evalute_button;
    private Button commodity_good_evalute_button;
    private Button commodity_bad_evalute_button;
    private Button commodity_middle_evalute_button;
    private OrderEvaluteAdapter evalute_adapter;
    private Order order;
    private int choiceFlag = 0;
    private List<Order> evaluteList = new ArrayList<Order>();
    private List<Order> part_evaluteList = new ArrayList<Order>();
    private ListView commodity_evalute_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_detail);
        ImageView topcommodityimageView = (ImageView) findViewById(R.id.topcommodityimageView);
//        topcommodityimageView.setImageBitmap(commodityInformation.getImage());
        topcommodityimageView.setBackground(new BitmapDrawable(commodityInformation.getImage()));
        TextView commodity_name_textView = (TextView) findViewById(R.id.commodity_name_textView);
        commodity_name_textView.setText(commodityInformation.getName());
        TextView price_textView = (TextView) findViewById(R.id.price_textView);
        price_textView.setText(String.valueOf("¥ " + commodityInformation.getPrice()));
        sold_textView = (TextView) findViewById(R.id.sold_textView);
        sold_textView.setText(String.valueOf(" 已售" + commodityInformation.getSold() + "份"));
        Button button = (Button) findViewById(R.id.purchase_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.login_user_id == "") {
                    Toast.makeText(CommodityDetailActivity.this, "您还没有登录，请登录后购买！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(CommodityDetailActivity.this, SubmitOrderActivity.class);
                SubmitOrderActivity.commodityInformation = commodityInformation;
                startActivity(intent);
            }
        });
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layoutBack1);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommodityDetailActivity.this.finish();
            }
        });

        //评价信息显示
        order = new Order("", "", "", 0, 0.0);
        evaluteList = order.getUserAllOrdersByGoodsID(commodityInformation.getID());
        for (int i = 0; i < evaluteList.size(); i++) {
            if (evaluteList.get(i).getOrder_state() == 1)
                part_evaluteList.add(evaluteList.get(i));
        }
        evalute_adapter = new OrderEvaluteAdapter(CommodityDetailActivity.this, R.layout.evalute_detail, part_evaluteList);
        commodity_evalute_listview = (ListView) findViewById(R.id.commodity_evalute_listview);
        commodity_evalute_listview.setAdapter(evalute_adapter);
        commodity_all_evalute_button = (Button) findViewById(R.id.commodity_all_evalute_button);
        commodity_good_evalute_button = (Button) findViewById(R.id.commodity_good_evalute_button);
        commodity_middle_evalute_button = (Button) findViewById(R.id.commodity_middle_evalute_button);
        commodity_bad_evalute_button = (Button) findViewById(R.id.commodity_bad_evalute_button);
        evaluteButton();
    }

    private void buttonSet(int choiceFlag) {
        switch (choiceFlag) {
            case 0:
                commodity_all_evalute_button.setBackgroundResource(R.drawable.button_bg);
                commodity_all_evalute_button.setTextColor(Color.parseColor("#FFA500"));
                break;
            case 1:
                commodity_good_evalute_button.setBackgroundResource(R.drawable.button_bg);
                commodity_good_evalute_button.setTextColor(Color.parseColor("#FFA500"));
                break;
            case 2:
                commodity_middle_evalute_button.setBackgroundResource(R.drawable.button_bg);
                commodity_middle_evalute_button.setTextColor(Color.parseColor("#FFA500"));
                break;
            case 3:
                commodity_bad_evalute_button.setBackgroundResource(R.drawable.button_bg);
                commodity_bad_evalute_button.setTextColor(Color.parseColor("#FFA500"));
                break;


        }
    }

    private void evaluteButton() {
        int allNum = 0, goodNum = 0, middleNum = 0, badNum = 0;
        for (int i = 0; i < evaluteList.size(); i++) {
            if (evaluteList.get(i).getOrder_state() == 1) {
                allNum += 1;
                if (evaluteList.get(i).get_order_rating() >= 4)
                    goodNum += 1;
                else if (evaluteList.get(i).get_order_rating() > 1 && evaluteList.get(i).get_order_rating() < 4)
                    middleNum += 1;
                else if (evaluteList.get(i).get_order_rating() <= 1 && evaluteList.get(i).get_order_rating() >= 0)
                    badNum += 1;
            }
        }
        commodity_all_evalute_button.setText("全部" + String.valueOf(allNum));
        commodity_good_evalute_button.setText("好评" + String.valueOf(goodNum));
        commodity_middle_evalute_button.setText("中评" + String.valueOf(middleNum));
        commodity_bad_evalute_button.setText("差评" + String.valueOf(badNum));
        commodity_all_evalute_button.setTextColor(Color.parseColor("#FFFFFF"));
        commodity_all_evalute_button.setBackgroundColor(Color.parseColor("#FFA500"));

        commodity_all_evalute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSet(choiceFlag);
                choiceFlag = 0;
                commodity_all_evalute_button.setTextColor(Color.parseColor("#FFFFFF"));
                commodity_all_evalute_button.setBackgroundColor(Color.parseColor("#FFA500"));
                evalute_adapter.clear();
                commodity_evalute_listview.invalidate();
                evaluteList = order.getUserAllOrdersByGoodsID(commodityInformation.getID());
                for (int i = 0; i < evaluteList.size(); i++) {
                    if (evaluteList.get(i).getOrder_state() == 1)
                        part_evaluteList.add(evaluteList.get(i));
                }
                evalute_adapter = new OrderEvaluteAdapter(CommodityDetailActivity.this, R.layout.evalute_detail, part_evaluteList);
                commodity_evalute_listview.setAdapter(evalute_adapter);
            }
        });

        commodity_good_evalute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSet(choiceFlag);
                choiceFlag = 1;
                evalute_adapter.clear();
                commodity_good_evalute_button.setTextColor(Color.parseColor("#FFFFFF"));
                commodity_good_evalute_button.setBackgroundColor(Color.parseColor("#FFA500"));
                commodity_evalute_listview.invalidate();
                evaluteList = order.getUserAllOrdersByGoodsID(commodityInformation.getID());
                for (int i = 0; i < evaluteList.size(); i++) {
                    if (evaluteList.get(i).get_order_rating() >= 4 && evaluteList.get(i).getOrder_state() == 1)
                        part_evaluteList.add(evaluteList.get(i));
                }
                evalute_adapter = new OrderEvaluteAdapter(CommodityDetailActivity.this, R.layout.evalute_detail, part_evaluteList);
                commodity_evalute_listview.setAdapter(evalute_adapter);
            }
        });
        commodity_middle_evalute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSet(choiceFlag);
                choiceFlag = 2;
                commodity_middle_evalute_button.setTextColor(Color.parseColor("#FFFFFF"));
                commodity_middle_evalute_button.setBackgroundColor(Color.parseColor("#FFA500"));

                evalute_adapter.clear();
                commodity_evalute_listview.invalidate();
                evaluteList = order.getUserAllOrdersByGoodsID(commodityInformation.getID());
                for (int i = 0; i < evaluteList.size(); i++) {
                    if (evaluteList.get(i).get_order_rating() > 1 && evaluteList.get(i).get_order_rating() < 4 && evaluteList.get(i).getOrder_state() == 1)
                        part_evaluteList.add(evaluteList.get(i));
                }
                evalute_adapter = new OrderEvaluteAdapter(CommodityDetailActivity.this, R.layout.evalute_detail, part_evaluteList);
                commodity_evalute_listview.setAdapter(evalute_adapter);
            }
        });
        commodity_bad_evalute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSet(choiceFlag);
                choiceFlag = 3;
                commodity_bad_evalute_button.setTextColor(Color.parseColor("#FFFFFF"));
                commodity_bad_evalute_button.setBackgroundColor(Color.parseColor("#FFA500"));

                evalute_adapter.clear();
                commodity_evalute_listview.invalidate();
                evaluteList = order.getUserAllOrdersByGoodsID(commodityInformation.getID());
                for (int i = 0; i < evaluteList.size(); i++) {
                    if (evaluteList.get(i).get_order_rating() <= 1 && evaluteList.get(i).get_order_rating() >= 0 && evaluteList.get(i).getOrder_state() == 1)
                        part_evaluteList.add(evaluteList.get(i));
                }
                evalute_adapter = new OrderEvaluteAdapter(CommodityDetailActivity.this, R.layout.evalute_detail, part_evaluteList);
                commodity_evalute_listview.setAdapter(evalute_adapter);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sold_textView.setText(String.valueOf(" 已售" + commodityInformation.getSold() + "份"));
    }
}
