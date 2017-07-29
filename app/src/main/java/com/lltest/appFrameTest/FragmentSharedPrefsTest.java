package com.lltest.appFrameTest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static com.lltest.util.GeneralUtil.showMessage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListenerSharedPrefs} interface
 * to handle interaction events.
 * Use the {@link FragmentSharedPrefsTest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSharedPrefsTest extends Fragment implements View.OnClickListener {
    static private final String TAG = "FragmentSharedPrefsTest";

    private OnFragmentInteractionListenerSharedPrefs mListener;
    private SharedPreferences mSharedPref;

    private View mView;
    private Button mBtnBack, mBtnView, mBtnUpdate, mBtnDelete;
    private EditText mEditNewHigh;

    public FragmentSharedPrefsTest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSharedPrefsTest newInstance() {
        Log.d(TAG, "new FragmentSharedPrefsTest");
        return new FragmentSharedPrefsTest();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNewHighSharedPrefs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_shared_prefs_test, container, false);

        mBtnBack = (Button) mView.findViewById(R.id.btnSharedPrefsBack);
        mBtnView = (Button) mView.findViewById(R.id.btnSharedPrefsView);
        mBtnUpdate = (Button) mView.findViewById(R.id.btnSharedPrefsUpdate);
        mBtnDelete = (Button) mView.findViewById(R.id.btnSharedPrefsDelete);
        mEditNewHigh = (EditText) mView.findViewById(R.id.editNewHigh);

        // register event handlers (DO NOT FORGET!!!):
        mBtnUpdate.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
        mBtnView.setOnClickListener(this);

        return mView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerSharedPrefs) {
            mListener = (OnFragmentInteractionListenerSharedPrefs) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume, reset fragment lock to false.");
        ((MainActivity)getActivity()).setFragmentLock(false);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        if (view == mBtnBack) {
            Log.d(TAG, "onClick: back");
            unloadMyself();
            return;
        }

        if (view == mBtnUpdate) {
            Log.d(TAG, "onClick: update");
            if (mEditNewHigh.getText().toString().trim().length() == 0) {
                showMessage(getContext(), "Error", "Please enter value");
                return;
            }
            String newHigh = mEditNewHigh.getText().toString().trim();
            boolean result = updateNewHigh(Long.valueOf(newHigh));
            if (result) {
                showMessage(getContext(), "Success", "New high score is updated.");
            } else {
                showMessage(getContext(), "Fail", "Failed to update new high score");
            }
            clearText();
            return;
        }

        if (view == mBtnView) {
            Log.d(TAG, "onClick: view");

            long result = getNewHigh();
            showMessage(getContext(), "New High", String.valueOf(result));
            return;
        }

        if (view == mBtnDelete) {
            Log.d(TAG, "onClick: delete");
            return;
        }
    }

    /**
     * Close current fragment
     */
    private void unloadMyself() {
        Log.d(TAG, "unloadMyself");
        // The following code cannot be put directly into button onClick(), so we use an unload function.
        this.getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListenerSharedPrefs {
        // TODO: Update argument type and name
        void OnFragmentInteractionListenerSharedPrefs(Uri uri);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListenerSharedPrefs(uri);
        }
    }


    private void initNewHighSharedPrefs() {
        Log.d(TAG, "init shared prefs");
        mSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    private long getNewHigh() {
        Log.d(TAG, "getNewHigh");
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        return mSharedPref.getLong(getString(R.string.saved_high_score), 0L);
    }

    private boolean updateNewHigh(long highScore) {
        Log.d(TAG, "updateNewHigh");
        if (null == mSharedPref) {
            Log.d(TAG, "null mSharedPref");
            return false;
        }
        SharedPreferences.Editor editor = mSharedPref.edit();
        try {
            editor.putLong(getString(R.string.saved_high_score), highScore);
            //editor.commit();
            editor.apply();     // handle it in background
        } catch (Exception e) {
            Log.d(TAG, "Exception on update long to SharedPreferences editor");
            return false;
        }
        return true;
    }

    /**
     * Clear the contents in the edit text components.
     *
     */
    private void clearText() {
        mEditNewHigh.setText("");
    }
}
