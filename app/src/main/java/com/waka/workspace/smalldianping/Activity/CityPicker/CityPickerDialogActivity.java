package com.waka.workspace.smalldianping.Activity.CityPicker;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.waka.workspace.smalldianping.Activity.CityPicker.JavaBean.City;
import com.waka.workspace.smalldianping.Activity.CityPicker.JavaBean.District;
import com.waka.workspace.smalldianping.Activity.CityPicker.JavaBean.Province;
import com.waka.workspace.smalldianping.CustomViews.PickerView;
import com.waka.workspace.smalldianping.R;
import com.waka.workspace.smalldianping.StaticValues;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 城市选择器，对话框风格的Activity
 * create by waka
 */
public class CityPickerDialogActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CityPickerDialogActivity";

    //控件
    private PickerView mPvProvince, mPvCity, mPvDistrict;//选择器控件
    private Button mBtnConfirm, mBtnCancel;

    //数据结构
    private List<Province> mListData;//总数据
    private List<String> mListProvinceName, mListCityName, mListDistrictName;//省，市，区字符串数据
    //省，市，区对应Map;为实现三级联动需要使用Map
    private Map<String, List<City>> mMapProvinceCity;//省，市Map
    private Map<String, List<District>> mMapCityDistrict;//市，区Map

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker_dialog);
        initView();
        initData();
        initEvent();
    }

    /**
     * initView
     */
    private void initView() {
        mPvProvince = (PickerView) findViewById(R.id.pvProvince);
        mPvCity = (PickerView) findViewById(R.id.pvCity);
        mPvDistrict = (PickerView) findViewById(R.id.pvDistrict);
        mBtnConfirm = (Button) findViewById(R.id.btnConfirm);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
    }

    /**
     * initData
     */
    private void initData() {
        mMapProvinceCity = new HashMap<>();
        mMapCityDistrict = new HashMap<>();

        mListProvinceName = new ArrayList<String>();
        mListCityName = new ArrayList<String>();
        mListDistrictName = new ArrayList<String>();

        //解析省市区的XML数据
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("province_data.xml");
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();//创建一个解析xml的工厂对象
            SAXParser saxParser = saxParserFactory.newSAXParser();
            MyXmlParserHandler xmlParserHandler = new MyXmlParserHandler();
            saxParser.parse(inputStream, xmlParserHandler);
            inputStream.close();
            // 获取解析出来的数据
            mListData = xmlParserHandler.getmProvinceList();

            /**
             * 将解析出来的地址数据从放在Map中
             */
            for (int i = 0; i < mListData.size(); i++) {
                String provinceName = mListData.get(i).getProvinceName();//获得省名
                List<City> cityList = mListData.get(i).getCityList();//获得城市List
                mMapProvinceCity.put(provinceName, cityList);//对应关系放入map中
                mListProvinceName.add(provinceName);//获得省字符串数据
                for (int j = 0; j < cityList.size(); j++) {
                    String cityName = cityList.get(j).getCityName();//获得市名
                    List<District> districtList = cityList.get(j).getDistrictList();//获得地区List
                    mMapCityDistrict.put(cityName, districtList);//对应关系放入map中
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        mPvProvince.setData(mListProvinceName);
        mPvProvince.setSelected(0);
        updateCityNameList(mPvProvince.getText());
    }

    /**
     * initEvent
     */
    private void initEvent() {
        //为PickerView设置滚动监听，实现三级联动
        mPvProvince.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                updateCityNameList(text);
            }
        });
        mPvCity.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                updateDistrictNameList(text);
            }
        });
        mPvDistrict.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {

            }
        });

        //按钮点击事件
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    /**
     * 根据省名更新城市字符串List
     *
     * @param provinceName
     * @return
     */
    private void updateCityNameList(String provinceName) {
        List<City> cityList = mMapProvinceCity.get(provinceName);
        mListCityName.clear();//清空原来数据
        for (int i = 0; i < cityList.size(); i++) {
            String cityName = cityList.get(i).getCityName();
            mListCityName.add(cityName);
        }
        mPvCity.setData(mListCityName);
        mPvCity.setSelected(0);
        updateDistrictNameList(mPvCity.getText());
    }

    /**
     * 根据城市名更新地区字符串List
     *
     * @param cityName
     * @return
     */
    private void updateDistrictNameList(String cityName) {
        List<District> districtList = mMapCityDistrict.get(cityName);
        mListDistrictName.clear();
        for (int i = 0; i < districtList.size(); i++) {
            String districtName = districtList.get(i).getDistrictName();
            mListDistrictName.add(districtName);
        }
        mPvDistrict.setData(mListDistrictName);
        mPvDistrict.setSelected(0);
    }

    /**
     * 点击事件分发
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirm:
                String location = mPvProvince.getText() + "-" + mPvCity.getText() + "-" + mPvDistrict.getText();
                Intent intent = new Intent();
                intent.putExtra("city", location);
                setResult(RESULT_OK, intent);
                List<District> districtList = mMapCityDistrict.get(mPvCity.getText().toString());
                StaticValues.districtNames = new String[districtList.size()];
                for (int i = 0; i < districtList.size(); i++) {
                    StaticValues.districtNames[i] = districtList.get(i).getDistrictName();
                }
                String t = StaticValues.districtNames[0];
                StaticValues.districtNames[0] = StaticValues.districtNames[districtList.size() - 1];
                StaticValues.districtNames[districtList.size() - 1] = t;
                finish();
                break;
            case R.id.btnCancel:
                finish();
                break;
            default:
                break;
        }
    }
}
