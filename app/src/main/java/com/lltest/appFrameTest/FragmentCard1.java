package com.lltest.appFrameTest;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lltest.util.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListenerCard1} interface
 * to handle interaction events.
 * Use the {@link FragmentCard1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCard1 extends Fragment {

    static private final String TAG = Constants.APP_PREFIX + "FragmentCard1";

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
    public interface OnFragmentInteractionListenerCard1 {
        // TODO: Update argument type and name
        void onFragmentInteractionCard1(Uri uri);
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Store instance variables
    private View mView;
    private String title;
    private int page;

    private TextView mTxt1;
    private ImageView mImg;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentCard1.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCard1 newInstance(int page, String title) {
        FragmentCard1 fragment = new FragmentCard1();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListenerCard1 mListener;

    public FragmentCard1() {
        // Required empty public constructor
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionCard1(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerCard1) {
            mListener = (OnFragmentInteractionListenerCard1) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListenerCard1");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
            page = getArguments().getInt(ARG_PARAM1, 0);
            title = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_fragment_card1, container, false);
        initUI();
        return mView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initUI() {
        Log.d(TAG, "initUI 1");
        //mTxt1 = (TextView) mView.findViewById(R.id.txt_info_1);
        //mTxt1.setText(page + " -- " + title);
    }
}
