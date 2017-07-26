package com.lltest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * SQLite open helper for Student DB;
 * The students DB is a Writable Database;
 *
 * This helper will handle the creation/upgrade/downgrade operations of the SQL DB;
 *
 * Other SQL operations will be handled through DB manager;
 *
 */

public class StudentDbOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "StudentDbOpenHelper";

    private static StudentDbOpenHelper mInstance = null;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "studentsDB.db";

    private static final int GET_WRITABLE_DB_RETRY_MAX_COUNT = 5;   // TODO: necessary?
    private static final boolean isEnableDatabaseEncryption = false;

    private SQLiteDatabase mDb = null;      // SQL DB object

    private int mDatabaseOpenCount;         // TODO: necessary?
    private final Object mDatabaseLock;     // TODO: necessary?

    /**
     * Factory method to create StudentDbOpenHelper instance.
     *
     * @param context
     * @return
     */
    public static synchronized StudentDbOpenHelper getInstance(Context context) {
        Log.d(TAG, "getInstance");
        if (mInstance == null) {
            // assume isEnableDatabaseEncryption always false:
            mInstance = new StudentDbOpenHelper(context, DATABASE_NAME, isEnableDatabaseEncryption);
            Log.d(TAG, "getInstance, create plain db");
        }
        return mInstance;
    }

    private StudentDbOpenHelper(Context context, String name, boolean isSecureDb) {
        super(context, name, null, DATABASE_VERSION);

        Log.d(TAG, "EcomDbOpenHelper, isSecureDb " + isSecureDb);
        mDatabaseOpenCount = 0;
        mDatabaseLock = new Object();
        initializeDb(isSecureDb);
    }

    private void initializeDb(boolean isSecureDb) {
        try {
            // assume isEnableDatabaseEncryption always false:
            Log.d(TAG, "StudentDbOpenHelper, initializeDb plain db");
            mDb = getWritableDatabase();
            Log.d(TAG, "StudentDbOpenHelper, db " + mDb);
        } catch (Exception e) {
            Log.d(TAG, "Unexpected exception on getWritableDatabase: " + e.getMessage());
            e.printStackTrace();
        }
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        // create tables
        Log.d(TAG, "initialize ecom db, create tables...");
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade");
        // to simply to discard the data and start over
        db.execSQL(StudentData.AccountTable.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Creates a SQLite DB
     *
     * @param db Target DB.
     */
    private void createTables(SQLiteDatabase db) {
        if (db == null) {
            Log.e(TAG, "failed to create tables, db is not initialized.");
            return;
        }
        try {
            // One DB can hold multiple tables;
            // thus, here, you can create tables one by one inside db;
            Log.d(TAG, "create student accounts table in db.");
            db.execSQL(StudentData.AccountTable.SQL_CREATE_ENTRIES);

            // TODO: can create more tables here in the future if necessary.
        } catch(Exception e) {
            Log.e(TAG, "unexpected exception while creating tables.");
            e.printStackTrace();
        }
    }

    /**
     * DB manager needs to call this to get the SQL DB instance.
     *
     * @return
     */
    public SQLiteDatabase getDatabase() {
        if (mDb == null) {
            mDb = getWritableDatabase();
        }
        return mDb;
    }

    public void closeDatabase() {
        Log.d(TAG, "closeDatabase.");
        if (mDb != null && mDb.isOpen()) {
            Log.d(TAG, "mDb.close()");
            mDb.close();
            mDb = null;
        }
    }

}
