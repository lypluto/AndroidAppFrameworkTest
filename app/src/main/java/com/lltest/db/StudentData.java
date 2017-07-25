package com.lltest.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * This class defines Student data;
 * This class also works as the DB contract defines the Student account DB schema;
 *
 */

public class StudentData {
    private int mRollNum;
    private String mName;
    private int mMark;

    public StudentData() {
        this.mRollNum = 0;
        this.mName = "Empty";
        this.mMark = 0;
    }

    public StudentData(int inRoll, String inName, int inMark) {
        this.mRollNum = inRoll;
        this.mName = inName;
        this.mMark = inMark;
    }

    public int getRollNum() {
        return mRollNum;
    }

    public String getName() {
        return mName;
    }

    public int getMark() {
        return mMark;
    }

    /**
     * Inner class defines the table for student accounts
     */
    public static class AccountTable implements BaseColumns {
        public static final String TABLE_NAME = "student";
        public static final String COL_ROLL_NUM = "student_rollno";
        public static final String COL_NAME = "student_name";
        public static final String COL_MARKS = "student_marks";

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        public static final String[] PROJECTION = {COL_ROLL_NUM, COL_NAME, COL_MARKS};

        //----- SQL operations ----------:

        // SQL command for filtering the records by roll number:
        public static final String SELECTION_BY_ROLL_NUM = COL_ROLL_NUM + " = ?";

        // SQL command for filtering the records by name:
        public static final String SELECTION_BY_NAME = COL_NAME + " = ?";

        // SQL command for filtering the records by roll number like:
        public static final String SELECTION_BY_ROLL_NUM_LIKE = COL_ROLL_NUM + " LIKE ?";

        // SQL command for select all records from the table:
        public static final String SELECTION_ALL = "SELECT * FROM " + TABLE_NAME;

        // SQL command for creating the DB:
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COL_ROLL_NUM + " VARCHAR PRIMARY KEY , " +
                        COL_NAME + " VARCHAR , " +
                        COL_MARKS + " INTEGER NOT NULL);";

        // SQL command for deleting the DB:
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + AccountTable.TABLE_NAME;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountTable.COL_ROLL_NUM, mRollNum);
        contentValues.put(AccountTable.COL_NAME, mName);
        contentValues.put(AccountTable.COL_MARKS, mMark);
        return contentValues;
    }

    /**
     * Convert from ContentValues instance to data instance
     *
     * @param values
     * @return
     */
    public static StudentData fromContentValues(ContentValues values) {
        if (null == values) {
            return new StudentData();
        }
        return new StudentData(
                values.getAsInteger(AccountTable.COL_ROLL_NUM),
                values.getAsString(AccountTable.COL_NAME),
                values.getAsInteger(AccountTable.COL_MARKS)
        );
    }

    /**
     * Convert cursor instance to data instance
     * @param cursor
     * @return
     */
    public static StudentData fromCursor(Cursor cursor) {
        if (null == cursor) {
            return null;
        }
        return new StudentData(
                cursor.getInt(cursor.getColumnIndexOrThrow(AccountTable.COL_ROLL_NUM)),
                cursor.getString(cursor.getColumnIndexOrThrow(AccountTable.COL_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(AccountTable.COL_MARKS))
        );
    }

    @Override
    public String toString() {
        return "Student{" +
                "Roll Num =" + mRollNum +
                ", Name ='" + mName + '\'' +
                ", Mark =" + mMark +
                '}';
    }
}
