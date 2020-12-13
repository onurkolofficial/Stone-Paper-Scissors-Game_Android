package com.onurkolofficial.spsgame.data;

import android.graphics.drawable.Drawable;

public class StoreData {
    private String STORE_ITEM_NAME;
    private int STORE_ITEM_MONEY,STORE_ITEM_COUNT,STORE_ITEM_ID;

    public StoreData(String ITEM_NAME, int ITEM_ID, int ITEM_COUNT, int ITEM_MONEY){
        this.STORE_ITEM_NAME=ITEM_NAME;
        this.STORE_ITEM_MONEY=ITEM_MONEY;
        this.STORE_ITEM_COUNT=ITEM_COUNT;
        this.STORE_ITEM_ID=ITEM_ID;
    }

    public String getItemName(){
        return STORE_ITEM_NAME;
    }
    public int getItemMoney(){
        return STORE_ITEM_MONEY;
    }
    public int getItemID(){
        return STORE_ITEM_ID;
    }
    public int getItemCount(){
        return STORE_ITEM_COUNT;
    }
}
