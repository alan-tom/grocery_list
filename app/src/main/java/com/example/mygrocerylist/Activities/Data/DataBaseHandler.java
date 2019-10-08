package com.example.mygrocerylist.Activities.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mygrocerylist.Activities.Activities.MainActivity;
import com.example.mygrocerylist.Activities.Model.GroceryItem;
import com.example.mygrocerylist.Activities.Util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {

    private Context ctx;
    public DataBaseHandler(Context context) {
        super(context, Constants.DB_name,null,Constants.DB_Version);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GROCERY_TABLE = "CREATE TABLE " + Constants.TABLE_name + " (" + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_GROCERY_ITEM + " TEXT NOT NULL," + Constants.KEY_QTY_NUMBER + " TEXT," + Constants.KEY_DATE_NAME + " LONG);";
        db.execSQL(CREATE_GROCERY_TABLE);

        Log.d("Saving","Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_name);
        onCreate(db);
    }

    public void addGrocery(GroceryItem grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM,grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER,grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_name,null,values);
        Log.d("Saving","Grocery Saved");
    }
    public GroceryItem getGrocery(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_name,new String[]{Constants.KEY_ID,Constants.KEY_GROCERY_ITEM,
                        Constants.KEY_QTY_NUMBER,Constants.KEY_DATE_NAME},Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null);

        if(cursor!=null) {
            cursor.moveToFirst();

            GroceryItem groceryItem = new GroceryItem();
            groceryItem.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            groceryItem.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
            groceryItem.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

            groceryItem.setDate(formatedDate);

            return groceryItem;
        }
        return null;
    }
    public List<GroceryItem> getGroceryItems() {
        SQLiteDatabase db = getReadableDatabase();

        List<GroceryItem> groceryList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_name,new String[]{Constants.KEY_ID,Constants.KEY_GROCERY_ITEM,Constants.KEY_QTY_NUMBER,
        Constants.KEY_DATE_NAME},null,null,null,null,Constants.KEY_DATE_NAME + " DESC");
        if(cursor.moveToFirst()){
            do{
                GroceryItem item = new GroceryItem();
                item.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                item.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                item.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));
                java.text.DateFormat dateFormat = java.text.DateFormat.getInstance();

                String time = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

                item.setDate(time);

                groceryList.add(item);

            }while(cursor.moveToNext());
        }
        return groceryList;
    }
    public int updateGrocery(GroceryItem grocery){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.KEY_GROCERY_ITEM,grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER,grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME,grocery.getDate());

        return db.update(Constants.TABLE_name,values,Constants.KEY_ID + "=?",new String[]{String.valueOf(grocery.getID())});

    }
    public void deleteGrocery(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_name,Constants.KEY_ID + "=?",new String[]{String.valueOf(id)});
        db.close();
    }
    public int groceryCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_name;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }
}
