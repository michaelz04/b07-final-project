package com.example.b07_final_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_final_project.adapters.ItemAdapter;
import com.example.b07_final_project.classes.CurrentStoreData;
import com.example.b07_final_project.classes.CurrentUserData;
import com.example.b07_final_project.classes.Item;
import com.example.b07_final_project.classes.Store;
import com.example.b07_final_project.fragments.IndividualStoreActivityOwnerFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoreItemsListActivity extends AppCompatActivity {

    private List<Item> itemList;
    private ItemAdapter adapter;
    private List <String> itemIDs;
    private String storeId; // Store ID received from the previous activity




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_items_view);

        storeId = CurrentStoreData.getInstance().getId();
        //storeId = getIntent().getStringExtra("store_id");

        // Initialize the RecyclerView and ItemAdapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        adapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(adapter);

        if (savedInstanceState == null) {
            String type = CurrentUserData.getInstance().getAccountType();
            if (type.equals("Owners")) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragmentContainerView, IndividualStoreActivityOwnerFragment.class, null)
                        .commit();
            }
            // TODO: add other fragment for shopper, shows cart. Implement in sprint 2
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // This boolean will help in checking if there are no items in a Store.
        final boolean[] addonce = {false};

        DatabaseReference storeRef = FirebaseDatabase.getInstance("https://test-54768-default-rtdb.firebaseio.com/").getReference("Stores").child(storeId);
//        DatabaseReference storeRef = FirebaseDatabase.getInstance("https://test-54768-default-rtdb.firebaseio.com/").child(storeId);
        storeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Store store = snapshot.getValue(Store.class);
                itemIDs = store.getItems();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });


        // DatabaseReference itemsRef = FirebaseDatabase.getInstance("https://test-54768-default-rtdb.firebaseio.com/");
        DatabaseReference itemsRef = FirebaseDatabase.getInstance("https://test-54768-default-rtdb.firebaseio.com/").getReference("Items");
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String itemId = itemSnapshot.getKey();
                    if (itemIDs != null) {
                        if (itemIDs.contains(itemId)) {
                            Item item = itemSnapshot.getValue(Item.class);
                            itemList.add(item);

                        }
                    }
                }

                if (itemIDs == null && !addonce[0]) {
                    String errormsg = "No Items in the Store";
                    Item empty = new Item(errormsg, "", 0.0f, "", "", "");
                    itemList.add(empty);
                    addonce[0] = true;
                }

                // Log.d("Size", String.valueOf(itemList.size()));
https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/Banana-Single.jpg/2324px-Banana-Single.jpg
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    // Helper method to check if the store contains the given item ID

}