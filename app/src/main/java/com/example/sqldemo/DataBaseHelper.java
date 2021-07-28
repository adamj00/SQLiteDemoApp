package com.example.sqldemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // define static values for each column name in the database
    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";
    public static final String COLUMN_ID = "ID";

    // constructor
    public DataBaseHelper(@Nullable Context context) {
        // due to the docs factory can be null
        super(context, "customer.db", null, 1);
    }

    // this function executes when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create a new table
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUSTOMER_AGE + " INT, " + COLUMN_ACTIVE_CUSTOMER + " BOOL)";
        db.execSQL(createTableStatement);
    }

    // this function executes when the structure of the database is changed
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    // this method adds new customer to the database
    public boolean addOne(CustomerModel customerModel) {

        // getWritableDatabase - insert actions
        // getReadableDatabase - select (read) actions
        SQLiteDatabase db = this.getWritableDatabase();

        // CV is a list of pairs
        ContentValues cv = new ContentValues();

        // putting values from customerModel to the corresponding columns in cv
        cv.put(COLUMN_CUSTOMER_NAME, customerModel.getName());
        cv.put(COLUMN_CUSTOMER_AGE, customerModel.getAge());
        cv.put(COLUMN_ACTIVE_CUSTOMER, customerModel.isActive());
        // we don't have to put the ID number because it is auto-incrementing in the database

        // inserting data in cv into the database
        long insert = db.insert(CUSTOMER_TABLE, null, cv);
        // it returns -1 if the insert failed
        if (insert == -1)
            return false;
        return true;
    }

    // method that will SELECT all records from the table
    public List<CustomerModel> getEveryOne() {
        List<CustomerModel> returnList = new ArrayList<>();

        // get data from the database
        // the query that will select all records from the table
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;

        // we choose getReadableDatabase because it doesn't lock the database
        SQLiteDatabase db = this.getReadableDatabase();

        // cursor is the result set from a SQL statement
        Cursor cursor = db.rawQuery(queryString, null);

        // moveToFirst() returns true if there were items selected
        if (cursor.moveToFirst()) {
            // loop through the cursor and create a new customer objects and insert them to the list
            do {
               int customerID = cursor.getInt(0);
               String customerName = cursor.getString(1);
               int customerAge = cursor.getInt(2);
               boolean customerActive = cursor.getInt(3) == 1;

               CustomerModel newCostumer = new CustomerModel(customerID, customerName, customerAge, customerActive);
               returnList.add(newCostumer);
            } while(cursor.moveToNext());
        }
        else {
            // there are no results
        }

        // close the cursor and db
        cursor.close();
        db.close();

        // return the list
        return returnList;
    }

    // this method deletes customer from the table
    public boolean deleteOne(CustomerModel customerModel) {

        SQLiteDatabase db = this.getWritableDatabase();

        // query for deleting customer with the given id from the table
        String queryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_ID + " = " + customerModel.getId();

        // execute the query
        Cursor cursor = db.rawQuery(queryString, null);

        // return true if the customer was deleted, false otherwise
        return cursor.moveToFirst();
    }

}
