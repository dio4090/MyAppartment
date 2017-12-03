package com.sourcey.myappartment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sourcey.myappartment.model.User;


public class DBUser {
    public static String USER_ID = "id";
    public static String NAME = "name";
    public static String ADDRESS = "address";
    public static String EMAIL = "email";
    public static String PASSWORD = "password";
    public static String MOBILE_NUMBER = "mobile_number";

    private final Context mContext;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "myappartment.db";
    private static final int DATABASE_VERSION = 1;
    private static final String USER_TABLE = "UserTable";


    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " + USER_TABLE + " (" +
                    USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +NAME + " TEXT,"
                    +ADDRESS+" TEXT,"
                    +EMAIL+" TEXT NOT NULL,"
                    +PASSWORD+" TEXT NOT NULL,"
                    +MOBILE_NUMBER+" INTEGER );";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_USER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            onCreate(db);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public DBUser(Context ctx) {
        mContext = ctx;
        mDbHelper = new DatabaseHelper(mContext);
    }

    public DBUser open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    // Insert the user to the Sqlite DB
    public void insertUser(User user){
        ContentValues cv = new ContentValues();
        cv.put(NAME, user.getName());
        cv.put(ADDRESS, user.getAddess());
        cv.put(EMAIL, user.getEmail());
        cv.put(PASSWORD, user.getPassword());
        cv.put(MOBILE_NUMBER, user.getMobile_number());
        mDb.insert(USER_TABLE, null, cv);
    }

    // Get the user from SQLite DB
    // We will just get the last user we just saved for convenience...
    public User retreiveUserFromDB(User u) {
        Cursor cur = mDb.query(true, USER_TABLE, new String[]{NAME,ADDRESS,EMAIL,PASSWORD,MOBILE_NUMBER},
                null, null, null, null,
                USER_ID + " DESC", "1");

        if (cur.moveToFirst()) {
            u.setName(cur.getString(cur.getColumnIndex(NAME)));
            u.setAddress(cur.getString(cur.getColumnIndex(ADDRESS)));
            u.setEmail(cur.getString(cur.getColumnIndex(EMAIL)));
            u.setPassword(cur.getString(cur.getColumnIndex(PASSWORD)));
            u.setMobile_number(cur.getInt(cur.getColumnIndex(MOBILE_NUMBER)));
            cur.close();
            return u;
        } else {
            cur.close();
            return null;
        }
    }
}
