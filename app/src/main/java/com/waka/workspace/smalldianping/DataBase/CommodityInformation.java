package com.waka.workspace.smalldianping.DataBase;

import android.graphics.Bitmap;

/**
 * Created by WWC on 2016/1/8.
 */
public class CommodityInformation {
    private String commodity_id;
    private String store_id;
    private String name;
    private Bitmap image;
    private int price;
    private int sold;
    public CommodityInformation(String store_id,String commodity_id,String name, Bitmap image,int price,int sold)
    {
        this.store_id=store_id;
        this.commodity_id=commodity_id;
        this.name = name;
        this.image = image;
        this.price=price;
        this.sold=sold;
    }
    public String getStoreID() {   return store_id;  }
    public String getID() {   return commodity_id;  }
    public String getName() {   return name;  }
    public Bitmap getImage() {   return image;  }
    public int getPrice() {   return price;  }
    public int getSold() {   return sold;  }
    public void setSold(int sold){this.sold=sold;}
}
