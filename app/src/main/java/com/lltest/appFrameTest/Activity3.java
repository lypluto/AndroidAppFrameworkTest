package com.lltest.appFrameTest;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lltest.db.StudentData;
import com.lltest.db.StudentDbManager;

import java.util.List;

import static com.lltest.util.GeneralUtil.showMessage;

public class Activity3 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Activity3";
    public static final String ACT_3_RESULT_BUNDLE = "resultBundleFrom3";
    public static final String ACT_3_RESULT_STR = "resultStrFrom3";

    private Button mFinish;

    private String mResultStr = "EMPTY";

    // student DB constants:
    private static final String DB_NAME_STUDENT = "StudentDB";
    private static final String TABLE_NAME_STUDENT = "student";
    private static final String COL_ROLL_NUM = "rollno";
    private static final String COL_NAME = "name";
    private static final String COL_MARKS = "marks";


    // variables for student DB test:
    EditText mEditRollNum, mEditName, mEditMarks;
    Button mBtnAdd, mBtnDelete, mBtnModify, mBtnView, mBtnViewAll, mBtnShowInfo;
    SQLiteDatabase mDb;


    StudentDbManager mDbMgr;


    @Override
    public void onClick(View view) {

        // insert record operation:
        if (view == mBtnAdd) {
            if (mEditRollNum.getText().toString().trim().length() == 0||
                    mEditName.getText().toString().trim().length() == 0||
                    mEditMarks.getText().toString().trim().length() == 0) {
                showMessage(this, "Error", "Please enter all values");
                return;
            }
            StudentData record = new StudentData(
                    Integer.valueOf(mEditRollNum.getText().toString().trim()),
                    mEditName.getText().toString().trim(),
                    Integer.valueOf(mEditMarks.getText().toString().trim())
            );
            boolean result = mDbMgr.addRecord(record);
            if (result) {
                showMessage(this, "Success", "Record added");
            } else {
                showMessage(this, "Fail", "Failed to add the new record!");
            }

            clearText();
        }

        // delete record operation:
        if (view == mBtnDelete) {
            if (mEditRollNum.getText().toString().trim().length() == 0) {
                showMessage(this, "Error", "Please enter Roll Num!");
                return;
            }
            boolean result =
                    mDbMgr.deleteRecordByRollNum(
                            Integer.valueOf(mEditRollNum.getText().toString().trim()));
            if (result) {
                showMessage(this, "Success", "Record Deleted");
            } else {
                showMessage(this, "Fail", "Failed to delete the record!");
            }
            clearText();
        }

        // update operation:
        if (view == mBtnModify) {
            if (mEditRollNum.getText().toString().trim().length() == 0||
                    mEditName.getText().toString().trim().length() == 0||
                    mEditMarks.getText().toString().trim().length() == 0) {
                showMessage(this, "Error", "Please enter all values");
                return;
            }
            StudentData record = new StudentData(
                    Integer.valueOf(mEditRollNum.getText().toString().trim()),
                    mEditName.getText().toString().trim(),
                    Integer.valueOf(mEditMarks.getText().toString().trim())
            );
            boolean result = mDbMgr.updateRecordByRollNum(Integer.valueOf(
                    mEditRollNum.getText().toString().trim()), record);
            if (result) {
                showMessage(this, "Success", "Record updated");
            } else {
                showMessage(this, "Fail", "Failed to update the record!");
            }
            clearText();
        }

        // View one record:
        if (view == mBtnView) {
            if (mEditRollNum.getText().toString().trim().length() == 0) {
                showMessage(this, "Error", "Please enter Roll Num");
                return;
            }
            StudentData record = mDbMgr.getAccountByRollNum(
                    Integer.valueOf(mEditRollNum.getText().toString().trim()));
            if (null == record) {
                showMessage(this, "Error", "no record found");
                clearText();
                return;
            }
            mEditName.setText(record.getName());
            mEditMarks.setText(String.valueOf(record.getMark()));
        }

        // View all records:
        if (view == mBtnViewAll) {
            List<StudentData> list = mDbMgr.getAllAccounts();
            if (null == list || 0 == list.size()) {
                showMessage(this, "Student Details", "Empty");
                clearText();
                return;
            }
            showMessage(this, "Student Details", String.valueOf(list));
            mResultStr = String.valueOf(list);
        }

        // show help info:
        if (view == mBtnShowInfo) {
            showMessage(this, "Student Management System", "Developed By LL");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        // initialize DB UI variables:
        mEditRollNum=(EditText)findViewById(R.id.editRollno);
        mEditName=(EditText)findViewById(R.id.editName);
        mEditMarks=(EditText)findViewById(R.id.editMarks);
        mBtnAdd=(Button)findViewById(R.id.btnAdd);
        mBtnDelete=(Button)findViewById(R.id.btnDelete);
        mBtnModify=(Button)findViewById(R.id.btnModify);
        mBtnView=(Button)findViewById(R.id.btnView);
        mBtnViewAll=(Button)findViewById(R.id.btnViewAll);
        mBtnShowInfo=(Button)findViewById(R.id.btnShowInfo);

        // register event handlers:
        mBtnAdd.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mBtnModify.setOnClickListener(this);
        mBtnView.setOnClickListener(this);
        mBtnViewAll.setOnClickListener(this);
        mBtnShowInfo.setOnClickListener(this);


        /*
        // Create DB and table:
        // open DB "StudentDB", create it if there is no such a DB:
        // MODE_PRIVATE indicates that the database file can only be accessed
        // by the calling application or all applications sharing the same user ID.
        // The third parameter is a Cursor factory object which can be left null if not required.
        mDb = openOrCreateDatabase(DB_NAME_STUDENT, Context.MODE_PRIVATE, null);

        // execSQL() function executes any SQL command.
        // Here it is used to create the student table if it does not already exist in the database.
        // table is called student, and three columns: rollno, name, and marks;
        // VARCHAR represents variable length character string;
        mDb.execSQL("CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME_STUDENT + " (" +
                COL_ROLL_NUM + " VARCHAR," +
                COL_NAME +  " VARCHAR," +
                COL_MARKS + " VARCHAR)");
        */

        // init DB manager:
        mDbMgr = StudentDbManager.getInstance(getApplicationContext());


        mFinish = (Button) findViewById(R.id.finish3_btn);

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mAct1 is clicked");
                finishWithResult();
            }
        });

        init();
    }

    @Override
    public void onDestroy() {
        mDbMgr.closeDb();
        super.onDestroy();
    }

    // Update UI:
    private void init() {
        /*
        Bundle receiveBundle = this.getIntent().getExtras();
        final String infoFromAct1 = receiveBundle.getString(MainActivity.ACT_1_INFO_KEY);

        updateDebugLog1(infoFromAct1);
        */


    }

    /**
     * Clear the contents in the edit text components.
     *
     */
    private void clearText() {
        mEditRollNum.setText("");
        mEditName.setText("");
        mEditMarks.setText("");
        mEditRollNum.requestFocus();
    }

    private void finishWithResult() {

        Bundle resultBundle = new Bundle();
        resultBundle.putString(ACT_3_RESULT_STR, mResultStr);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(ACT_3_RESULT_BUNDLE, resultBundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
