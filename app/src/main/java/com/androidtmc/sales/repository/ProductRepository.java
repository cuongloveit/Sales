package com.androidtmc.sales.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidtmc.sales.models.Product;

import java.util.ArrayList;

/**
 * Created by minhc_000 on 13/08/2015.
 */
public class ProductRepository {
    static final String TAB_PRODUCT = "Product";
    static final String COL_PRODUCTID = "ProductID";
    static final String COL_PRODUCTNAME = "ProductName";
    static final String COL_UNIT = "Unit";
    static final String COL_PRICE ="Price";

    public static String getCreateTableSQL()
    {
        String sql = "CREATE TABLE " + TAB_PRODUCT + "(" +
                COL_PRODUCTID + " integer primary key autoincrement, " +
                COL_PRODUCTNAME + " text, " +
                COL_UNIT + " text, " +
                COL_PRICE + " real)";
        return sql;
    }
    private DatabaseHelper dbhelper;
    public ProductRepository(Context context) {
        dbhelper = new DatabaseHelper(context);
    }
    private ContentValues MakeProductContentValues(Product p)
    {
        ContentValues cv = new ContentValues();
        cv.put(COL_PRODUCTNAME, p.getProductName());
        cv.put(COL_UNIT, p.getUnit());
        cv.put(COL_PRICE, p.getPrice());
        return cv;
    }
    public void insertProduct(Product p)
    {
        ContentValues cv = MakeProductContentValues(p);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long id = db.insert(TAB_PRODUCT, null, cv);
        if (id!= -1) p.setProductID(id);
    }
    public void updateProduct(Product p)
    {
        ContentValues cv = MakeProductContentValues(p);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.update(TAB_PRODUCT, cv, COL_PRODUCTID + "=?",
                new String[] { String.valueOf(p.getProductID()) });
    }public void deleteProduct(long id)
    {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(TAB_PRODUCT,COL_PRODUCTID + " = " + id,null);
    }
    public ArrayList<Product> getAllProduct()
    {
        ArrayList<Product> products = new ArrayList<Product>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TAB_PRODUCT;
        Cursor cursor = db.rawQuery(sql, null);
        int indexProductID = cursor.getColumnIndex(COL_PRODUCTID);
        int indexProductName = cursor.getColumnIndex(COL_PRODUCTNAME);
        int indexUnit = cursor.getColumnIndex(COL_UNIT);
        int indexPrice = cursor.getColumnIndex(COL_PRICE);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Product p = new Product();
                p.setProductID(cursor.getLong(indexProductID));
                p.setProductName(cursor.getString(indexProductName));
                p.setUnit(cursor.getString(indexUnit));
                p.setPrice(cursor.getDouble(indexPrice));
                products.add(p);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return products;
    }
}
