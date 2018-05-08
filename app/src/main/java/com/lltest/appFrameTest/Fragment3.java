package com.lltest.appFrameTest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lltest.util.Constants;

/**
 * This is a fragment demonstrates hint dialog process.
 *
 */

public class Fragment3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    static private final String TAG = Constants.APP_PREFIX + "Fragment3";

    private Fragment3 fragment3;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener3 mListener;

    private View mView;

    private View mLinearLayout;

    private CountDownTimer mTimerHint;
    private final int TICK_TIME_HINT = 1000;
    private final int FINISH_TIME_HINT = 2000;

    private float mInitHintPositionY = 5000F;
    private float mFinalHintPositionY = 5000F;

    public Fragment3() {
        // Required empty public constructor
    }

    public static Fragment3 newInstance(String param1, String param2) {
        Log.d(TAG, "new fragment3");
        Fragment3 fragment = new Fragment3();
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
        mView = inflater.inflate(R.layout.hint_main_view, container, false);
        initUI();
        return mView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction3(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener3) {
            mListener = (OnFragmentInteractionListener3) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListenerCard1");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();
        mListener = null;

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume, reset fragment lock to false.");
        ((MainActivity)getActivity()).setFragmentLock(false);
        showHint();
        mTimerHint.start();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    public interface OnFragmentInteractionListener3 {
        // TODO: Update argument type and name
        void onFragmentInteraction3(Uri uri);
    }



    private void unloadMyself() {
        Log.d(TAG, "unloadMyself");
        // The following code cannot be put directly into button onClick(), so we use an unload function.

        this.getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .commitAllowingStateLoss();

        //this.getActivity().getSupportFragmentManager().popBackStack();
    }

    private void initUI() {
        mView.setClickable(true);

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mInitHintPositionY = ((float)metrics.heightPixels) * 2F;
        mFinalHintPositionY = ((float)metrics.heightPixels) * 0.5F;


        mLinearLayout = (LinearLayout) mView.findViewById(R.id.linear_layout_hint_dialog);
        mLinearLayout.setY(mInitHintPositionY);
    }


    private void showHint() {
        Log.v(TAG, "showHint()");
        showHintAnim();
        mTimerHint = new CountDownTimer(FINISH_TIME_HINT, TICK_TIME_HINT) {
            public void onTick(long millisUntilFinished) {
                // nothing
            }

            public void onFinish() {
                Log.v(TAG, "timer onFinish.");
                //showVcoInfoDialog();
                //mTimerHintDismiss.start();
                //mTimer2.start();
                dismissHintAnim();
            }
        };
    }

    private void showHintAnim() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(mLinearLayout, View.TRANSLATION_Y, mFinalHintPositionY);
        animation.setDuration(1000);
        animation.start();
    }

    private void dismissHintAnim() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(mLinearLayout, View.TRANSLATION_Y, mInitHintPositionY);
        animation.setDuration(1000);
        animation.start();

        animation.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.v(TAG, "onAnimationStart()");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.v(TAG, "onAnimationEnd, unload myself");
                unloadMyself();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
