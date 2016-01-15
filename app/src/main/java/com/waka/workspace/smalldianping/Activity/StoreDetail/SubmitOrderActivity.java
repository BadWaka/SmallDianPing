package com.waka.workspace.smalldianping.Activity.StoreDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waka.workspace.smalldianping.Activity.Main.MainActivity;
import com.waka.workspace.smalldianping.DataBase.CommodityDB;
import com.waka.workspace.smalldianping.DataBase.CommodityInformation;
import com.waka.workspace.smalldianping.DataBase.Order;
import com.waka.workspace.smalldianping.R;

import java.util.Timer;
import java.util.TimerTask;

public class SubmitOrderActivity extends AppCompatActivity {
    public static CommodityInformation commodityInformation;
    private TextView commodity_name_textView;
    private TextView price_textView;
    private TextView submit_number_textView;
    private TextView subtotal_submit_textview;
    private TextView total_submit_textview;
    private TextView submit_prompt_textView;
    private ImageView sub_submit_imageView;
    private ImageView add_submit_imageView;
    private Button submit_button;
    private CommodityDB commodityDB;
    private Context context;
    boolean flag=false;//订单是否有提交
    Handler handler;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);
        context=this;
        submit_prompt_textView= (TextView) findViewById(R.id.submit_prompt_textView);
        commodity_name_textView=(TextView)findViewById(R.id.commodity_name_submit_textView);
        commodity_name_textView.setText(commodityInformation.getName());
        price_textView=(TextView)findViewById(R.id.commodity_price_submit_textview);
        price_textView.setText(String.valueOf("¥ " + commodityInformation.getPrice()));
        sub_submit_imageView=(ImageView)findViewById(R.id.sub_submit_imageView);
        total_submit_textview=(TextView)findViewById(R.id.total_submit_textview);
        subtotal_submit_textview=(TextView)findViewById(R.id.subtotal_submit_textview);
        submit_number_textView=(TextView)findViewById(R.id.submit_number_textView);
        total_submit_textview.setText("¥" + String.valueOf(commodityInformation.getPrice()));
        subtotal_submit_textview.setText("¥" + String.valueOf(commodityInformation.getPrice()));
        sub_submit_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = submit_number_textView.getText().toString();
                if (Integer.valueOf(text) > 0) {
                    submit_number_textView.setText(String.valueOf(Integer.valueOf(text) - 1));
                    total_submit_textview.setText("¥" + String.valueOf((Integer.valueOf(text) - 1) * commodityInformation.getPrice()));
                    subtotal_submit_textview.setText("¥" + String.valueOf((Integer.valueOf(text) - 1) * commodityInformation.getPrice()));
                }
                if(Integer.valueOf(text)==1)
                    sub_submit_imageView.setColorFilter(Color.BLACK);
            }
        });
        add_submit_imageView=(ImageView) this.findViewById(R.id.add_submit_imageView);
        add_submit_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = submit_number_textView.getText().toString();
                submit_number_textView.setText(String.valueOf(Integer.valueOf(text) + 1));
                total_submit_textview.setText("¥" + String.valueOf((Integer.valueOf(text) + 1) * commodityInformation.getPrice()));
                subtotal_submit_textview.setText("¥" + String.valueOf((Integer.valueOf(text) + 1) * commodityInformation.getPrice()));
                sub_submit_imageView.setColorFilter(Color.GREEN);
            }
        });
        submit_button=(Button)findViewById(R.id.submit_button);//订单提交按钮
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==false) {
                    commodityDB = new CommodityDB(context, commodityInformation.getID(), Integer.valueOf(submit_number_textView.getText().toString()));
                    commodityDB.updateSold();
                    flag=true;
                    submit_prompt_textView.setBackgroundColor(Color.BLACK);
                    submit_prompt_textView.setTextColor(Color.WHITE);
                    CommodityDetailActivity.commodityInformation.setSold(CommodityDetailActivity.commodityInformation.getSold()+Integer.valueOf(submit_number_textView.getText().toString()));
                    //将订单信息写入数据库
                    Order order=new Order(
                            MainActivity.login_user_id,
                            commodityInformation.getStoreID(),
                            commodityInformation.getID(),
                            Integer.valueOf(submit_number_textView.getText().toString()),
                            (Double.valueOf(submit_number_textView.getText().toString()) ) * commodityInformation.getPrice());
                    order.addOrder();
                    //测试使用评价信息
                    timer();
                    submit_prompt_textView.setText(" 订单提交成功！");
                }
                else
                    submit_prompt_textView.setText(" 您已经提交过订单！"); ;
            }
        });
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.layoutBack);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitOrderActivity.this.finish();
            }
        });
    }
    private void timer(){
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Message msg=new Message();
                msg.what=1;
                handler.sendMessage(msg);
            }
        };
        timer=new Timer();
        timer.schedule(timerTask,1000,1000);
        handler=new Handler() {
            @Override
            public void handleMessage (android.os.Message msg)
            {
                switch (msg.what)
                {
                    case 1:
                        SubmitOrderActivity.this.finish() ;
                        break;
                }
            }
        };
    }
}
