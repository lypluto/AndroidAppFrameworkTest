package com.lltest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the operations to student DB;
 * Hides the DB from the client;
 *
 * This manager provides the interfaces for the SQL operations:
 * insert/delete/update/view...
 *
 * The other SQL operations will be done by DB helper itself:
 * create/upgrade/downgrade...
 *
 */

public class StudentDbManager {
    private static final String TAG = StudentDbManager.class.getSimpleName();

    private static StudentDbManager mInstance = null;
    private StudentDbOpenHelper mDbHelper = null;
    private SQLiteDatabase mDb = null;

    private StudentDbManager(Context context) {
        mDbHelper = StudentDbOpenHelper.getInstance(context);

        // get the data repository in write mode:
        mDb = mDbHelper.getDatabase();
    }

    public static synchronized StudentDbManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new StudentDbManager(context);
        }
        return mInstance;
    }

    public void closeDb() {
        Log.i(TAG, "closeDb()");
        if (null != mDbHelper) {
            mDbHelper.closeDatabase();
        }
    }

    public boolean addRecord(StudentData newStudent) {
        if (mDb == null) {
            Log.e(TAG, "addRecord: null DB!");
            return false;
        }
        if (newStudent == null) {
            Log.e(TAG, "addRecord: Invalid input");
            return false;
        }
        // Create a new map of values, where column names are the keys;
        // convert student instance to ContentValues, put all values into ContentValues;
        ContentValues values = newStudent.toContentValues();
        try {
            // Insert the new row, returning the primary key value of the new row:
            long newRowId = mDb.insert(StudentData.AccountTable.TABLE_NAME, null, values);
            // you can use mDb.execSQL("INSERT INTO " + TABLE_NAME_STUDENT + " VALUES('" ...
            // mDb.insert API is actually wrap the SQL command, and simplify the usage.
            Log.d(TAG, "addRecord: inserted newRowId: " + newRowId);
            return checkIfQueryResultSuccessful(newRowId);
        } catch (Exception e) {
            Log.e(TAG, "addRecord: unexpected exception while insert new record.");
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteRecordByRollNum(int inRollNum) {
        if (mDb == null) {
            Log.e(TAG, "updateRecordByRollNum: null DB!");
            return false;
        }
        if (inRollNum < 0) {
            Log.e(TAG, "deleteRecordByRollNum: invalid roll num.");
            return false;
        }
        // Issue SQL statement.
        try {
            long rowId = mDb.delete(
                    StudentData.AccountTable.TABLE_NAME,
                    StudentData.AccountTable.SELECTION_BY_ROLL_NUM_LIKE,
                    new String[]{String.valueOf(inRollNum)}
            );
            return checkIfQueryResultSuccessful(rowId);
        } catch (Exception e) {
            Log.e(TAG, "deleteRecordByRollNum: unexpected exception while delete record.");
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRecordByRollNum(int inRollNum, StudentData newStudent) {
        if (mDb == null) {
            Log.e(TAG, "updateRecordByRollNum: null DB!");
            return false;
        }
        if (inRollNum < 0) {
            Log.e(TAG, "updateRecordByRollNum: invalid roll num.");
            return false;
        }
        if (newStudent == null) {
            Log.e(TAG, "updateRecordByRollNum: Invalid input");
            return false;
        }
        // Issue SQL statement.
        try {
            int rowsAffected = mDb.update(
                    StudentData.AccountTable.TABLE_NAME,
                    newStudent.toContentValues(),
                    StudentData.AccountTable.SELECTION_BY_ROLL_NUM_LIKE,
                    new String[]{String.valueOf(inRollNum)}
            );
            return checkIfQueryResultSuccessful(rowsAffected);
        } catch (Exception e) {
            Log.e(TAG, "updateRecordByRollNum: unexpected exception while update record.");
            e.printStackTrace();
        }
        return false;
    }

    public StudentData getAccountByRollNum(int inRollNum) {
        if (mDb == null) {
            Log.e(TAG, "updateRecordByRollNum: null DB!");
            return null;
        }
        if (inRollNum < 0) {
            Log.e(TAG, "getAccountByRollNum: invalid roll num.");
            return null;
        }
        // How you want the results sorted in the resulting Cursor
        String sortOrder = StudentData.AccountTable.COL_NAME + " DESC";

        Cursor cursor = mDb.query(
                StudentData.AccountTable.TABLE_NAME,              // The table to query
                StudentData.AccountTable.PROJECTION,              // The columns to return
                StudentData.AccountTable.SELECTION_BY_ROLL_NUM,   // The columns for the WHERE clause
                new String[]{String.valueOf(inRollNum)},                // The values for the WHERE clause
                null,                                                   // don't group the rows
                null,                                                   // don't filter by row groups
                sortOrder                                               // The sort order
        );
        if (cursor == null) {
            Log.e(TAG, "failed to query by roll num: " + inRollNum);
            return null;
        }
        if (!cursor.moveToFirst()) {
            Log.e(TAG, "empty cursor.");
            return null;
        }
        return StudentData.fromCursor(cursor);
    }

    public List<StudentData> getAllAccounts() {
        if (mDb == null) {
            Log.e(TAG, "getAllAccounts: null DB!");
            return null;
        }
        List<StudentData> resultList = new ArrayList<>();

        Cursor cursor = mDb.rawQuery(StudentData.AccountTable.SELECTION_ALL, null);
        if (cursor == null) {
            Log.e(TAG, "failed to query.");
            return null;
        }
        if (cursor.getCount() == 0) {
            Log.e(TAG, "No records found.");
            return null;
        }
        while (cursor.moveToNext()) {
            resultList.add(StudentData.fromCursor(cursor));
        }
        return resultList;
    }

    private boolean checkIfQueryResultSuccessful(long row) {
        return row > 0;
    }
}
