package com.example.ubun17.myapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by ubun17 on 8/9/16.
 */
public class ProductsContentProvider extends ContentProvider {
    private ProductsDBHandler mProductsDBHandler;

    public static final int PRODUCTS = 1;
    public static final int PRODUCTS_ID = 2;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(ProductsContract.AUTHORITY, ProductsDBHandler.TABLE_MARKIT, PRODUCTS);
        sURIMatcher.addURI(ProductsContract.AUTHORITY, ProductsDBHandler.TABLE_MARKIT + "/#", PRODUCTS_ID);
    }

    @Override
    public boolean onCreate() {
        mProductsDBHandler = ProductsDBHandler.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int uriType = sURIMatcher.match(uri);
        switch (uriType){
            case PRODUCTS:
                return ProductsContract.Products.CONTENT_TYPE;
            case PRODUCTS_ID:
                return ProductsContract.Products.CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues ) {
        int uriType = sURIMatcher.match(uri);

        long id = 0;
        switch (uriType) {
            case PRODUCTS:
                id = mProductsDBHandler.addProduct(contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(ProductsContract.Products.CONTENT_URI,id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
//        int uriType = sURIMatcher.match(uri);
//        int rowsDeleted = 0;
//
//        switch (uriType) {
//            case PRODUCTS:
//                rowsDeleted = mProductsDBHandler.deleteProduct(selection,selectionArgs,null);
//                break;
//            case PRODUCTS_ID:
//                String id = uri.getLastPathSegment();
//                rowsDeleted = mProductsDBHandler.deleteProduct(null,null,id);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
//        int uriType = sURIMatcher.match(uri);
//        int rowsUpdated = 0;
//
//        switch (uriType) {
//            case PRODUCTS:
//                rowsUpdated = mProductsDBHandler.updateProduct(values,selection,selectionArgs,null);
//                break;
//            case PRODUCTS_ID:
//                String id = uri.getLastPathSegment();
//                rowsUpdated = mProductsDBHandler.updateProduct(values,null,null,id);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }
}
