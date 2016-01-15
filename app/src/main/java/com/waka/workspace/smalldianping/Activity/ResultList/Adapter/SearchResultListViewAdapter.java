package com.waka.workspace.smalldianping.Activity.ResultList.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.waka.workspace.smalldianping.Activity.Main.MainActivity;
import com.waka.workspace.smalldianping.Activity.ResultList.ResultListActivity;
import com.waka.workspace.smalldianping.DataBase.DBHelper;
import com.waka.workspace.smalldianping.DataBase.StoreDB;
import com.waka.workspace.smalldianping.R;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wang_xiaojie on 2015/12/29 0029.
 */
public class SearchResultListViewAdapter extends BaseAdapter implements PoiSearch.OnPoiSearchListener {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局 /*构造函数*/
    public static List<PoiItem> poiItems;//搜索到的poi信息组
    public static List<StoreDB> storeDBs;//搜索到的对应的商家信息
    public static List<StoreDB> allStoreDBs;
    public final static int RESORT_BY_DISTANCE = 0;
    public final static int RESORT_BY_GOODEVALUATE = 1;
    public final static int RESORT_BY_AVGCOSTEXPENSIVE = 2;
    public final static int RESORT_BY_AVGCOSTCHEAP = 3;
    public static boolean searchstate;
    private DBHelper dbhelper;
    PoiSearch.Query query;
    Context mContext;
    public static int mShowResultNum;
    ResultListActivity resultListActivity;

    public SearchResultListViewAdapter(Context context, String mStrSearchKey, String mStrSearchCity, ResultListActivity resultListActivity) {

        this.resultListActivity = resultListActivity;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        dbhelper = new DBHelper(context);
        query = new PoiSearch.Query(mStrSearchKey, "餐饮服务", mStrSearchCity); //设置搜索条件
        storeDBs = new LinkedList<StoreDB>();
        poiItems = new LinkedList<PoiItem>();
        allStoreDBs = new LinkedList<StoreDB>();
        searchstate = true;
        mShowResultNum = 0;

        query.setPageSize(30);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(context, query);//初始化poiSearch对象
        poiSearch.setOnPoiSearchListener(this);//设置回调数据的监听器
        poiSearch.searchPOIAsyn();//开始搜索
    }

    @Override
    public int getCount() {
        return mShowResultNum;

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_result_list_activity, null);
            holder.mTvStoreName = (TextView) convertView.findViewById(R.id.result_store_name_textview);
            holder.mRbRating = (RatingBar) convertView.findViewById(R.id.resultlistview_ratingbar);
            holder.mTvRating = (TextView) convertView.findViewById(R.id.rating_resultlistview_textview);
            holder.mTvAvgcost = (TextView) convertView.findViewById(R.id.avecost_resultlistview_textview);
            holder.mTvStoreType = (TextView) convertView.findViewById(R.id.storetype_resultlistview_textview);
            holder.mTvDistance = (TextView) convertView.findViewById(R.id.distance_resultlistview_textview);
            holder.mIvStorePicture = (ImageView) convertView.findViewById(R.id.storepicture_resultlistview_imageview);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /*设置每一行显示的内容*/
        holder.mTvStoreName.setText(storeDBs.get(position).getStoreName());
        if (storeDBs.get(position).getStoreRating() == -1) {
            holder.mRbRating.setRating(0);
            holder.mTvRating.setText("暂未入住");
            holder.mTvAvgcost.setText("");
        } else {
            holder.mRbRating.setRating((float) storeDBs.get(position).getStoreRating());
            String tmprating = String.valueOf(storeDBs.get(position).getStoreRating());
            holder.mTvRating.setText(tmprating.substring(0, tmprating.indexOf(".") + 2) + "分");
            String tmpavgcost = String.valueOf(storeDBs.get(position).getStoreAvgcost());
            holder.mTvAvgcost.setText("人均" + tmpavgcost.substring(0, tmpavgcost.indexOf(".") + 2) + "元");
        }
        holder.mTvStoreType.setText(storeDBs.get(position).getStoreType().split(";")[2]);
        byte[] tmpimage = storeDBs.get(position).getImageBytes();
        if (tmpimage != null) {
            Bitmap tmpbitmao = BitmapFactory.decodeByteArray(tmpimage, 0, tmpimage.length);
            holder.mIvStorePicture.setImageBitmap(tmpbitmao);
        } else {

        }
        float tmpdistance = storeDBs.get(position).getDistanceToLocation();
        String strtmpdistance = String.valueOf(tmpdistance);
        holder.mTvDistance.setText(strtmpdistance.substring(0, strtmpdistance.indexOf(".") + 2) + " km");
        return convertView;
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        //可以在回调中解析result，获取POI信息
        //result.getPois()可以获取到PoiItem列表，Poi详细信息可参考PoiItem类
        //若当前城市查询不到所需Poi信息，可以通过result.getSearchSuggestionCitys()获取当前Poi搜索的建议城市
        //如果搜索关键字明显为误输入，则可通过result.getSearchSuggestionKeywords()方法得到搜索关键词建议
        //返回结果成功或者失败的响应码。0为成功，其他为失败（详细信息参见网站开发指南-错误码对照表）
        if (!searchstate)
            return;
        PoiResult poiResult = result;
        List<PoiItem> tmppoiItem = poiResult.getPois();

        if (rCode == 0) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                //根据搜索到的商家，到数据库调出相应信息，保存在静态变量中
                if (tmppoiItem != null && tmppoiItem.size() > 0) {
                    for (int i = 0; i < tmppoiItem.size(); i++) {

                        SQLiteDatabase db = dbhelper.getReadableDatabase();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher)
                                .compress(Bitmap.CompressFormat.PNG, 100, baos);
                        StoreDB.addStore(tmppoiItem.get(i), Math.random() * 200, baos.toByteArray(), db);//数据库添加信息
                        db.close();

                        SQLiteDatabase db2 = dbhelper.getReadableDatabase();
                        LatLng tmpLatLng = new LatLng(tmppoiItem.get(i).getLatLonPoint().getLatitude(),
                                tmppoiItem.get(i).getLatLonPoint().getLongitude());
                        float tmpdistance = AMapUtils.calculateLineDistance(MainActivity.loactionLatLng, tmpLatLng) / 1000;
                        StoreDB tmpstore = new StoreDB(tmppoiItem.get(i), db2);
                        tmpstore.setDistanceToLocation(tmpdistance);
                        if (searchstate)
                            allStoreDBs.add(tmpstore);//添加到静态变量
                        else return;
                        db2.close();
                    }
                }
            }
        }
        if (result.getPageCount() != result.getQuery().getPageNum() && searchstate) {
            query.setPageNum(result.getQuery().getPageNum() + 1);//设置查询页码
            PoiSearch poiSearch = new PoiSearch(mContext, query);//初始化poiSearch对象
            poiSearch.setOnPoiSearchListener(this);//设置回调数据的监听器
            poiSearch.searchPOIAsyn();//开始搜索
        } else {

            if (allStoreDBs.size() < 15) {
                mShowResultNum = allStoreDBs.size();
            } else {
                mShowResultNum = 15;
            }
            if (mShowResultNum == 0) {
                this.resultListActivity.mTvLoading.setText("没有符合条件的结果！");
                return;
            }
            storeDBs.addAll(allStoreDBs);
            resort(RESORT_BY_DISTANCE);
            this.resultListActivity.mTvLoading.setVisibility(TextView.GONE);
            this.resultListActivity.mListViewSearchResult.setVisibility(ListView.VISIBLE);
            searchstate = false;
            this.notifyDataSetChanged();
        }
    }

    public List<StoreDB> resort(int resrotType) {
        int size;
        switch (resrotType) {
            case RESORT_BY_DISTANCE://离我最近
                size = storeDBs.size();
                for (int i = 0; i < size - 1; i++) {
                    for (int j = 0; j < size - i - 1; j++) {
                        if (storeDBs.get(j).getDistanceToLocation() > storeDBs.get(j + 1).getDistanceToLocation()) {
                            StoreDB tmpstoredb = storeDBs.get(j);
                            storeDBs.remove(j);
                            storeDBs.add(j + 1, tmpstoredb);
                        }
                    }
                }
                break;
            case RESORT_BY_GOODEVALUATE://好评优先
                size = storeDBs.size();
                for (int i = 0; i < size - 1; i++) {
                    for (int j = 0; j < size - i - 1; j++) {
                        if (storeDBs.get(j).getStoreRating() < storeDBs.get(j + 1).getStoreRating()) {
                            StoreDB tmpstoredb = storeDBs.get(j);
                            storeDBs.remove(j);
                            storeDBs.add(j + 1, tmpstoredb);
                        }
                    }
                }
                break;
            case RESORT_BY_AVGCOSTCHEAP://人均最低
                size = storeDBs.size();
                for (int i = 0; i < size - 1; i++) {
                    for (int j = 0; j < size - i - 1; j++) {
                        if (storeDBs.get(j).getStoreAvgcost() > storeDBs.get(j + 1).getStoreAvgcost()
                                || storeDBs.get(j).getStoreAvgcost() == -1 && storeDBs.get(j + 1).getStoreAvgcost() != -1) {
                            StoreDB tmpstoredb = storeDBs.get(j);
                            storeDBs.remove(j);
                            storeDBs.add(j + 1, tmpstoredb);
                        }
                    }
                }
                break;
            case RESORT_BY_AVGCOSTEXPENSIVE://人均最高
                size = storeDBs.size();
                for (int i = 0; i < size - 1; i++) {
                    for (int j = 0; j < size - i - 1; j++) {
                        if (storeDBs.get(j).getStoreAvgcost() < storeDBs.get(j + 1).getStoreAvgcost()) {
                            StoreDB tmpstoredb = storeDBs.get(j);
                            storeDBs.remove(j);
                            storeDBs.add(j + 1, tmpstoredb);
                        }
                    }
                }
                break;
        }
        return storeDBs;
    }
}

final class ViewHolder {
    public TextView mTvStoreName;
    public RatingBar mRbRating;
    public TextView mTvRating;
    public TextView mTvAvgcost;
    public TextView mTvStoreType;
    public TextView mTvDistance;
    public ImageView mIvStorePicture;

}
