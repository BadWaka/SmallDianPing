package com.waka.workspace.smalldianping.Activity.ResultMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.PoiOverlay;
import com.waka.workspace.smalldianping.Activity.Main.MainActivity;
import com.waka.workspace.smalldianping.Activity.ResultList.Adapter.SearchResultListViewAdapter;
import com.waka.workspace.smalldianping.Activity.StoreDetail.StoreDetailActivity;
import com.waka.workspace.smalldianping.Constant;
import com.waka.workspace.smalldianping.DataBase.StoreDB;
import com.waka.workspace.smalldianping.R;

/**
 * 搜索结果地图
 * Created by wang_xiaojie on 2015/12/29 0029.
 */
public class ResultMapActivity extends AppCompatActivity implements AMap.OnMarkerClickListener, View.OnTouchListener, View.OnClickListener ,AMap.OnMapLoadedListener{

    private MapView mapView;//地图控件
    private AMap aMap;
    private LinearLayout bottomlayout;//底部布局
    private TextView tvstorename;//商家名称
    private TextView tvstoreaddress;//商家地址
    private UiSettings uisettings;//地图UI设置
    private PoiOverlay poiOverlay;//poi展示类
    private ImageButton IBback;//返回按钮
    private RatingBar ratingBar;//商家星级bar
    private TextView tvavgcost;//商家人均消费
    private TextView tvrating;//商家星级textview
    private  LinearLayout mLinearLayoutBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_map);

        //对象初始化
        tvstorename = (TextView) findViewById(R.id.storename);
        mapView = (MapView) findViewById(R.id.map);
        bottomlayout = (LinearLayout) findViewById(R.id.bottomlayout);
        IBback = (ImageButton) findViewById(R.id.imagebutton_back);
        tvstoreaddress = (TextView) findViewById(R.id.storeaddress);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        tvrating = (TextView) findViewById(R.id.textview_rating);
        tvavgcost = (TextView) findViewById(R.id.textview_avecost);
        mLinearLayoutBottom = (LinearLayout) findViewById(R.id.bottomlayout);
        mapView.onCreate(savedInstanceState);// 必须要写

        aMap = mapView.getMap();
        aMap.setOnMapLoadedListener(this);
        uisettings = aMap.getUiSettings();//获得地图ui设置对象
        uisettings.setZoomPosition(1);//设置放大缩小按钮在中间

        mLinearLayoutBottom.setOnClickListener(this);
        IBback.setOnTouchListener(this);//按钮设置响应
        aMap.setOnMarkerClickListener(this);//设置地图标记事件监听


        //在地图上显示当前定位
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(MainActivity.loactionLatLng);
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_resultmapactivity));
        aMap.addMarker(markerOption);


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        bottomlayout.setVisibility(View.VISIBLE);//设置底部布局可见（默认不可见）
        tvstorename.setText(marker.getTitle());//设置商家名称
        tvstoreaddress.setText(marker.getSnippet());//设置商家地址
        //找到数据库中对应的商家信息（评论星级等）
        for (int i = 0; i < SearchResultListViewAdapter.storeDBs.size(); i++) {
            StoreDB tmpsdb = (SearchResultListViewAdapter.storeDBs.get(i));
            //由于marker中没有poiid，所以只能判断店名地址是否一致来确认商家信息一致
            if (tmpsdb.getStoreName().equals(marker.getTitle()) &&
                    tmpsdb.getStoreSnippet().equals(marker.getSnippet())) {
                //暂未入住
                if (tmpsdb.getStoreRating() == -1) {
                    tvrating.setText("暂未入住");
                    tvavgcost.setText("");
                    ratingBar.setRating(0);
                }
                //已入住但是还没有评价星级
                else if (tmpsdb.getStoreRating() == 0) {
                    //字符串处理，保留小数点后一位
                    String avgcost = String.valueOf(tmpsdb.getStoreAvgcost());
                    avgcost = avgcost.substring(0, avgcost.indexOf(".") + 2);

                    tvrating.setText("暂无评价");
                    tvavgcost.setText(avgcost + "元");
                    ratingBar.setRating(0);
                }
                //已入住，有评价星级
                else {
                    //字符串处理，保留小数点后一位
                    String avgcost = String.valueOf(tmpsdb.getStoreAvgcost());
                    avgcost = avgcost.substring(0, avgcost.indexOf(".") + 2);
                    String rating = String.valueOf(tmpsdb.getStoreRating());
                    rating = rating.substring(0, rating.indexOf(".") + 2);

                    tvrating.setText(rating + "分");
                    tvavgcost.setText(avgcost + "元");
                    ratingBar.setRating((float) tmpsdb.getStoreRating());
                }
                break;
            }
        }
        return true;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.imagebutton_back) {
            //按下按钮时，改变图片
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                IBback.setBackgroundResource(R.mipmap.ic_resultmapactivity_back_pressed);
            }
            //松开按钮时，改变图片，跳转activity
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                IBback.setBackgroundResource(R.mipmap.ic_resultmapactivity_back);
                this.finish();
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        for(int i=0;i<SearchResultListViewAdapter.mShowResultNum;i++){
            if(SearchResultListViewAdapter.storeDBs.get(i).getStoreName().equals(tvstorename.getText())
                    && SearchResultListViewAdapter.storeDBs.get(i).getStoreSnippet().equals(tvstoreaddress.getText())){
                StoreDetailActivity.presentStore=SearchResultListViewAdapter.storeDBs.get(i);
                Intent intent = new Intent(this, StoreDetailActivity.class);
                startActivityForResult(intent, Constant.STORE_DETAIL_ACTIVITY_REQUEST_CODE);
                break;
            }
        }
    }

    @Override
    public void onMapLoaded() {
        poiOverlay = new PoiOverlay(aMap, SearchResultListViewAdapter.poiItems);//设置poi点在地图的显示
        poiOverlay.removeFromMap();
        poiOverlay.addToMap();//添加到地图中
        poiOverlay.zoomToSpan();//设置地图视图范围位置到合适值
    }
}
