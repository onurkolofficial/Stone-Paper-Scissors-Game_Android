package com.onurkolofficial.spsgame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.onurkolofficial.spsgame.R;
import com.onurkolofficial.spsgame.classes.SinglePlayerGame;
import com.onurkolofficial.spsgame.classes.StoreListAdapter;
import com.onurkolofficial.spsgame.data.StoreData;

import java.util.ArrayList;

import static com.onurkolofficial.spsgame.classes.GameSound.startButtonClickSound;
import static com.onurkolofficial.spsgame.data.GamePlayerData.getPlayerData;
import static com.onurkolofficial.spsgame.data.GamePlayerData.getPlayerDataPreferences;

public class StoreActivity extends AppCompatActivity {

    TextView backBtn,playerMoney;
    ListView itemList;

    ArrayList<StoreData> itemDataList;
    StoreListAdapter itemListAdapter;

    public static int playerStoreMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        // Get View Elements
        backBtn=findViewById(R.id.backButton);
        playerMoney=findViewById(R.id.storePlayerMoney);
        itemList=findViewById(R.id.storeItemList);

        // Get Player Money
        playerStoreMoney=Integer.parseInt(getPlayerData(getPlayerDataPreferences(),"Game.Player.Money"));

        // Button Events
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Close This Activity
                finish();
            }
        });

        // Print Data
        playerMoney.setText(String.valueOf(playerStoreMoney));

        // Set List Adapter
        initializeListData();
        addStoreItems();
    }

    public void initializeListData(){
        itemDataList=new ArrayList<StoreData>();
        itemListAdapter=new StoreListAdapter(this,itemDataList);
        itemList.setAdapter(itemListAdapter);
    }

    public void addStoreItems(){
        // Get 'store_data.xml' to Adding store list view.
        String[] itemNameArray=getResources().getStringArray(R.array.item_name);
        int[] itemIdArray=getResources().getIntArray(R.array.item_id);
        int[] itemCountArray=getResources().getIntArray(R.array.item_count);
        int[] itemMoneyArray=getResources().getIntArray(R.array.item_money);

        // Adding Items in Store
        for(int i = 0; i < itemNameArray.length; i++) {
            StoreData item=new StoreData(itemNameArray[i],itemIdArray[i],itemCountArray[i],itemMoneyArray[i]);
            itemListAdapter.add(item);
        }
    }
}