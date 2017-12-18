package com.sourcey.myappartment.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.sourcey.myappartment.model.Project;

import java.util.ArrayList;

public class DBProject {

    public static String PROJECT_ID = "id";
    public static String NAME = "name";
    public static String CREATED_AT = "created_at";
    public static String UPDATED_AT = "updated_at";
    public static String CATEGORIE_ID = "categorie_id";
    public static String DESCRIPTION = "description";
    public static String IS_ENABLED = "is_enabled";
    public static String IS_PRIVATE = "is_private";
    public static String IMAGE_ID = "image_id";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "myappartment.db";
    private static final int DATABASE_VERSION = 1;
    private static final String PROJECT_TABLE = "projects";


    private static final String CREATE_PROJECT_TABLE =
            "CREATE TABLE " + PROJECT_TABLE + " (" +
                    PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +NAME + " TEXT NOT NULL, "
                    +CREATED_AT+" INTEGER, "
                    +UPDATED_AT+" INTEGER, "
                    +CATEGORIE_ID+" INTEGER, "
                    +IMAGE_ID+" INTEGER, "
                    +DESCRIPTION+" TEXT, "
                    +IS_ENABLED+" BOOLEAN NOT NULL, "
                    +IS_PRIVATE+" BOOLEAN, "
                    +"FOREIGN KEY ("+CATEGORIE_ID+") REFERENCES categories(id), "
                    +"FOREIGN KEY ("+IMAGE_ID+") REFERENCES images(id) );";

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

    public DBProject open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void dropTableProject() {
        mDb.execSQL("DROP TABLE IF EXISTS " + PROJECT_TABLE);
    }

    public void createProjectTable() {
        mDb.execSQL(CREATE_PROJECT_TABLE);
    }

    public boolean insertProject(Project project){
        open();

        try {
            ContentValues cv = new ContentValues();
            cv.put(NAME, project.getName());
            cv.put(CREATED_AT, project.getCreated_at());
            cv.put(UPDATED_AT, project.getUpdated_at());
            cv.put(CATEGORIE_ID, project.getCategorie_id());
            cv.put(IMAGE_ID, project.getImage_id());
            cv.put(DESCRIPTION, project.getDescription());
            cv.put(IS_ENABLED, project.isIs_enabled());
            cv.put(IS_PRIVATE, false);
            mDb.insert(PROJECT_TABLE, null, cv);
            close();
            return true;

        } catch(SQLiteException e) {
            System.out.print(e.getMessage());
            close();
            return false;
        }

    }

    public ArrayList<Project> getAllProjects() {
        open();

        ArrayList<Project> listProject = new ArrayList<Project>();

        String queryStringAll = "SELECT * FROM "+PROJECT_TABLE;
        Cursor cursor = mDb.rawQuery(queryStringAll, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Project p = new Project();
                p.setProject_id(cursor.getInt(cursor.getColumnIndex(PROJECT_ID)));
                p.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                p.setCategorie_id(cursor.getInt(cursor.getColumnIndex(CATEGORIE_ID)));
                p.setImage_id(cursor.getInt(cursor.getColumnIndex(IMAGE_ID)));
                //p.setIs_enabled(cursor.(cursor.getColumnIndex(IS_ENABLED)));
                cursor.moveToNext();
            }
        }

        cursor.close();
        close();
    }


}
