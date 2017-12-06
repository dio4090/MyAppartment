package com.sourcey.myappartment.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sourcey.myappartment.model.Project;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBProject {

    public static String PROJECT_ID = "id";
    public static String NAME = "name";
    public static String CREATED_AT = "created_at";
    public static String UPDATED_AT = "updated_at";
    public static String CATEGORIE_ID = "categorie_id";
    public static String DESCRIPTION = "description";
    public static String IS_ENABLED = "is_enabled";
    public static String IS_PRIVATE = "is_private";
    public static String PROJECT_IMAGE = "project_image";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "myappartment.db";
    private static final int DATABASE_VERSION = 1;
    private static final String PROJECT_TABLE = "projects";


    private static final String CREATE_PROJECT_TABLE =
            "CREATE TABLE " + PROJECT_TABLE + " (" +
                    PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +NAME + " TEXT NOT NULL,"
                    +CREATED_AT+" INTEGER,"
                    +UPDATED_AT+" INTEGER,"
                    +CATEGORIE_ID+" INTEGER,"
                    +DESCRIPTION+" TEXT,"
                    +IS_ENABLED+" BOOLEAN NOT NULL,"
                    +IS_PRIVATE+" BOOLEAN,"
                    +PROJECT_IMAGE+" BLOB,"
                    +"FOREIGN KEY "+CATEGORIE_ID+" REFERENCES categories(id) );";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_PROJECT_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_PROJECT_TABLE);
            onCreate(db);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public DBProject(Context ctx) {
        Context mContext = ctx;
        mDbHelper = new DatabaseHelper(mContext);
    }

    public DBProject openDatabase() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void closeDatabase() {
        mDbHelper.close();
    }

    public void dropTableProject() {
        mDb.execSQL("DROP TABLE IF EXISTS " + PROJECT_TABLE);
    }

    public void createProjectTable() {
        mDb.execSQL(CREATE_PROJECT_TABLE);
    }

    public void insertProject(Project project){
        openDatabase();

        ContentValues cv = new ContentValues();
        cv.put(NAME, project.getName());
        cv.put(CREATED_AT, project.getCategorie_id());
        cv.put(UPDATED_AT, project.getUpdated_at());
        cv.put(CATEGORIE_ID, project.getCategorie_id());
        cv.put(DESCRIPTION, project.getDescription());
        cv.put(IS_ENABLED, project.isIs_enabled());
        mDb.insert(PROJECT_TABLE, null, cv);

        closeDatabase();
    }

    public void getAllProjects() {
        openDatabase();

        String queryStringAll = "SELECT * FROM "+PROJECT_TABLE;

        Cursor cursor = mDb.rawQuery(queryStringAll, null);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultDate;


        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                System.out.println("Usuário "+cursor.getInt(cursor.getColumnIndex("id")) );
                System.out.println("Nome: "+cursor.getString(cursor.getColumnIndex("name")));
                System.out.println("Email: "+cursor.getString(cursor.getColumnIndex("email")));
                System.out.println("Address: "+cursor.getString(cursor.getColumnIndex("address")));

                resultDate = new Date(cursor.getInt(cursor.getColumnIndex("login_updated_at")));
                System.out.println("Updated_at"+sdf.format(resultDate));

                cursor.moveToNext();
            }
        }

        cursor.close();
        closeDatabase();
    }

    // Insert the image to the Sqlite DB
    public void insertImage(byte[] imageBytes) {
        ContentValues cv = new ContentValues();
        cv.put(PROJECT_IMAGE, imageBytes);
        mDb.insert(PROJECT_TABLE, null, cv);
    }

    // Get the image from SQLite DB
    // We will just get the last image we just saved for convenience...
    public byte[] retreiveImageFromDB() {
        Cursor cur = mDb.query(true, PROJECT_TABLE, new String[]{PROJECT_IMAGE,},
                null, null, null, null,
                PROJECT_ID + " DESC", "1");
        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex(PROJECT_IMAGE));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }

}
