package com.example.mygrocerylist.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mygrocerylist.Activities.Data.DataBaseHandler;
import com.example.mygrocerylist.Activities.Model.GroceryItem;
import com.example.mygrocerylist.Activities.UI.RecyclerViewAdapter;
import com.example.mygrocerylist.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<GroceryItem> groceryItemList;
    private DataBaseHandler db;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private TextView groceryItem;
    private TextView quantity;
    private Button saveButtonMake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });

        db = new DataBaseHandler(this);
        recycleView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        groceryItemList =new ArrayList<>();

        groceryItemList = db.getGroceryItems();

        recyclerViewAdapter = new RecyclerViewAdapter(this,groceryItemList);
        recycleView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public void createPopupDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_layout,null);
        groceryItem = (EditText) view.findViewById(R.id.enterName);
        quantity = (EditText) view.findViewById(R.id.enterQuantity);
        saveButtonMake = (Button) view.findViewById(R.id.saveButton);


        saveButtonMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groceryItem.getText().toString().isEmpty())
                    Snackbar.make(v, "Please Enter a Item", Snackbar.LENGTH_SHORT).show();
                else{
                    saveToDb(v);
                }
            }
        });

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void saveToDb(View v){

        GroceryItem grocery = new GroceryItem();

        String name = groceryItem.getText().toString();
        String qua = quantity.getText().toString();

        grocery.setName(name);
        grocery.setQuantity(qua);

        db.addGrocery(grocery);
        Snackbar.make(v,"Item Stored",Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                groceryItemList =new ArrayList<>();
                groceryItemList = db.getGroceryItems();
                recyclerViewAdapter = new RecyclerViewAdapter(ListActivity.this,groceryItemList);
                recycleView.setAdapter(recyclerViewAdapter);
            }
        },700);
    }

}
