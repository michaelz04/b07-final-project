package com.example.b07_final_project.adapters;

import static android.view.View.GONE;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_final_project.ItemActivity;
import com.example.b07_final_project.R;
import com.example.b07_final_project.classes.Item;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    // ViewHolder class to hold the views for each item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView itemCard;
        TextView itemName;
        TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCard = itemView.findViewById(R.id.itemCard);
            itemName = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.price);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_in_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Item item = itemList.get(position);
            String itemName = item.getItemName();
            String errormsg= "No Items in the Store";
            if(!itemName.equals(errormsg)) {
                holder.itemName.setText(item.getItemName());
                holder.price.setText("$" + item.getPrice());

                // Set click listener
                holder.itemCard.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), ItemActivity.class);
                    intent.putExtra("item_id", item.getItemID());
                    v.getContext().startActivity(intent);
                });
            }

            else{
                holder.itemCard.setVisibility(GONE);
            }

           // Log.d("test", String.valueOf(position));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}