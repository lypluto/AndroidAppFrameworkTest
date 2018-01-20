package com.lltest.appFrameTest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.lltest.util.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener2} interface
 * to handle interaction events.
 * Use the {@link FragmentTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTwo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    static private final String TAG = Constants.APP_PREFIX + "FragmentTwo";

    private FragmentTwo fragment2;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener2 mListener;

    private View mView;
    private Button mBtnClose;
    private Button mBtnF1;

    public FragmentTwo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTwo newInstance(String param1, String param2) {
        Log.d(TAG, "new fragment2");
        FragmentTwo fragment = new FragmentTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_two, container, false);
        mView = inflater.inflate(R.layout.fragment_two, container, false);
        initUI();
        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction2(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener2) {
            mListener = (OnFragmentInteractionListener2) context;
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
        /*
        Log.d(TAG, "onPause, reset fragment lock to false.");
        ((MainActivity)getActivity()).setFragmentLock(false);
        */
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
    public interface OnFragmentInteractionListener2 {
        // TODO: Update argument type and name
        void onFragmentInteraction2(Uri uri);
    }

    /**
     * Start fragment 1 from current fragment
     */
    private void loadFragmentOne() {
        Activity activity = getActivity();
        if (activity != null && activity instanceof MainActivity) {
            ((MainActivity) activity).loadFragmentOne();
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

    private void initUI() {
        mBtnClose = (Button) mView.findViewById(R.id.two_close_btn);
        mBtnF1 = (Button) mView.findViewById(R.id.two_f1_btn);

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnClose is clicked");
                //getActivity().onBackPressed();  // this will cause the entire app goes to backend, not goes to activity page.

                //getActivity().getFragmentManager().popBackStack();

                unloadMyself();

            }
        });

        mBtnF1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnF1 is clicked");

                // start fragment 1, and then close current fragment:
                loadFragmentOne();
                unloadMyself();     // if do not remove current fragment, then new fragment 1
                // will be on top of fragment 2.
            }
        });
    }
}
