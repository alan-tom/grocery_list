package com.example.mygrocerylist.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygrocerylist.Activities.Data.DataBaseHandler;
import com.example.mygrocerylist.Activities.Model.GroceryItem;
import com.example.mygrocerylist.R;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveButtonMake;
    private DataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DataBaseHandler(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("Table Added",String.valueOf(db.groceryCount()));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });
        byPassActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this,"You have selected Settings",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                startActivity(new Intent(MainActivity.this,ListActivity.class));
                finish();
            }
        },700);
    }

     private void byPassActivity(){
        if(db.groceryCount() > 0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
     }
}
