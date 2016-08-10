package com.example.ubun17.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ubun17 on 8/9/16.
 */
public class ProductsDBHandler extends SQLiteOpenHelper {
    private static final String TAG = "ProductsDBHandler";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";

    public static final String TABLE_MARKIT = ProductsContract.Products.TABLE_MARKIT;
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_NAME = ProductsContract.Products.COLUMN_NAME;
    public static final String COLUMN_CHANGE = ProductsContract.Products.COLUMN_CHANGE;

    private static ProductsDBHandler mInstance;

    public static ProductsDBHandler getInstance(Context context){
        if(mInstance == null){
            mInstance = new ProductsDBHandler(context);
        }
        return mInstance;
    }

    private ProductsDBHandler(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE "
                + TABLE_MARKIT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_CHANGE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKIT);
        onCreate(sqLiteDatabase);
    }

    public long addProduct(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        long insertedRow = db.insert(TABLE_MARKIT, null, values);
        db.close();
        return insertedRow;
    }

    public Cursor findProduct(String selection, String[] selectionArgs, String sortOrder, String id){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = null;

        if(id == null)
            cursor = db.query(TABLE_MARKIT,null,selection,selectionArgs,null,null,sortOrder);
        else
            cursor = db.query(TABLE_MARKIT,null,COLUMN_ID+" = ?",new String[]{id},null,null,sortOrder);

        return cursor;
    }

}
