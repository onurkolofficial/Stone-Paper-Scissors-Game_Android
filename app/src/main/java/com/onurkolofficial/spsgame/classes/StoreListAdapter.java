package com.onurkolofficial.spsgame.classes;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.reflect.TypeToken;
import com.onurkolofficial.spsgame.R;
import com.onurkolofficial.spsgame.data.StoreData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.onurkolofficial.spsgame.activity.SinglePlayerActivity.buyItemCount;
import static com.onurkolofficial.spsgame.activity.SinglePlayerActivity.buyItemList;
import static com.onurkolofficial.spsgame.activity.StoreActivity.playerStoreMoney;
import static com.onurkolofficial.spsgame.classes.GameAnimations.ShakeAnimation;
import static com.onurkolofficial.spsgame.classes.GameSound.startButtonClickSound;
import static com.onurkolofficial.spsgame.classes.LoadSaveData.gson;
import static com.onurkolofficial.spsgame.data.GamePlayerData.getPlayerData;
import static com.onurkolofficial.spsgame.data.GamePlayerData.getPlayerDataPreferences;
import static com.onurkolofficial.spsgame.data.GamePlayerData.setPlayerData;

public class StoreListAdapter extends ArrayAdapter<StoreData> {
    private final LayoutInflater inflater;
    private final Activity context;
    private ViewHolder holder;
    private final ArrayList<StoreData> dataList;
    public StoreListAdapter(Activity context, ArrayList<StoreData> dataList){
        super(context,0,dataList);
        this.context=context;
        this.dataList=dataList;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            convertView=inflater.inflate(R.layout.store_items, null);
            holder=new ViewHolder();
            holder.itemName=convertView.findViewById(R.id.item_name);
            holder.itemMoney=convertView.findViewById(R.id.item_money);
            holder.itemCount=convertView.findViewById(R.id.item_count);
            holder.itemImage=convertView.findViewById(R.id.item_image);
            holder.buyBtn=convertView.findViewById(R.id.buyButton);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }
        // Get Activity View Element
        TextView storeMoney=context.findViewById(R.id.storePlayerMoney);
        // Get Current Item Position (Check History Data)
        final StoreData data=dataList.get(position);
        // Get Current Item Data
        String iName=data.getItemName();
        int iId=data.getItemID();
        int iCount=data.getItemCount();
        int iMoney=data.getItemMoney();
        // Set Values
        holder.itemName.setText(iName);
        holder.itemMoney.setText(String.valueOf("$"+iMoney));
        holder.itemCount.setText(String.valueOf("x"+iCount));
        // Check Item image
        Resources res=context.getResources();
        // If new item adds to get item integer and set item image.
        if(iId==res.getInteger(R.integer.ITEM_IRON))
            holder.itemImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.iron_32));

        // Buy Button
        holder.buyBtn.setOnClickListener(view -> {
            // Sound Effect
            startButtonClickSound();
            // Check Money
            if(playerStoreMoney<iMoney){
                // Player Money Not Enought.
                Toast.makeText(context, res.getString(R.string.not_enought_money), Toast.LENGTH_SHORT).show();
                // Money Animation
                storeMoney.startAnimation(ShakeAnimation());
            }
            else{
                // Adding Item to array.
                // If player close store to check buy items in 'onResume'.
                // Note! if earlier purchased same item, increase count to earlier item.
                if(buyItemList.size()==0){
                    // Adding new Item
                    buyItemList.add(iId);
                    buyItemCount.add(iCount);
                }
                else {
                    for (int i=0; i<buyItemList.size(); i++) {
                        if (buyItemList.get(i).equals(iId)){
                            // Note! if used this item, increase new count to remaining item count.
                            // Because 'buyItemCount' updated for every use.
                            int newCount;
                            // Get Item Count
                            newCount = buyItemCount.get(i);
                            // Count New Items
                            newCount += iCount;
                            // Update New Count
                            buyItemCount.set(i, newCount);
                        }
                        else{
                            // Adding new Item
                            buyItemList.add(iId);
                            buyItemCount.add(iCount);
                            break;
                        }
                    }
                }
                // Update Money
                playerStoreMoney-=iMoney;
                // Print new Money
                storeMoney.setText(String.valueOf(playerStoreMoney));
                // Save Preference Data
                String saveItemList=gson.toJson(buyItemList);
                String saveItemListCount=gson.toJson(buyItemCount);
                setPlayerData(getPlayerDataPreferences(),"Game.Player.Store.Items",saveItemList);
                setPlayerData(getPlayerDataPreferences(),"Game.Player.Store.ItemsCount",saveItemListCount);
                setPlayerData(getPlayerDataPreferences(),"Game.Player.Money", String.valueOf(playerStoreMoney));

                // Go 'SinglePlayerActivity.java', 'onResume'.
                Toast.makeText(context, res.getString(R.string.success_buy_text), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    //View Holder Pattern for better performance
    private static class ViewHolder {
        TextView itemName,itemMoney,itemCount;
        ImageView itemImage;
        ImageButton buyBtn;
    }
}
