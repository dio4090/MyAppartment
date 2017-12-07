package com.sourcey.myappartment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sourcey.myappartment.model.Categorie;

import java.util.ArrayList;

/**
 * Created by diogo on 05/12/2017.
 */

public class DBCategorie {

    public static String CATEGORIE_ID = "id";
    public static String NAME = "name";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "myappartment.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CATEGORIE_TABLE = "categories";


    private static final String CREATE_CATEGORIE_TABLE =
            "CREATE TABLE " + CATEGORIE_TABLE + " (" +
                    CATEGORIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +NAME + " TEXT NOT NULL );";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_CATEGORIE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_CATEGORIE_TABLE);
            onCreate(db);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public DBCategorie(Context ctx) {
        Context mContext = ctx;
        mDbHelper = new DatabaseHelper(mContext);
    }

    public DBCategorie openDatabase() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void closeDatabase() {
        mDbHelper.close();
    }

    public void dropTableCategorie() {
        mDb.execSQL("DROP TABLE IF EXISTS " + CATEGORIE_TABLE);
    }

    public void createCategorieTable() {
        mDb.execSQL(CREATE_CATEGORIE_TABLE);
    }


    public void insertCategorie(Categorie categorie){
        openDatabase();

        ContentValues cv = new ContentValues();
        cv.put(CATEGORIE_ID, categorie.getId());
        cv.put(NAME, categorie.getName());
        mDb.insert(CATEGORIE_TABLE, null, cv);

        closeDatabase();
    }

    public ArrayList<Categorie> getAllCategories() {
        openDatabase();

        ArrayList<Categorie> listCategorie = new ArrayList<Categorie>();

        String queryStringAll = "SELECT * FROM "+CATEGORIE_TABLE;
        Cursor cursor = mDb.rawQuery(queryStringAll, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Categorie c = new Categorie();
                c.setId(cursor.getInt(cursor.getColumnIndex("id")));
                c.setName(cursor.getString(cursor.getColumnIndex("name")));

                listCategorie.add(c);
                cursor.moveToNext();
            }
        }

        cursor.close();
        closeDatabase();

        return listCategorie;
    }
}
