package com.lltest.appFrameTest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.lltest.util.Constants;
import com.lltest.util.GeneralUtil;
import com.lltest.util.WebViewUtil;
import com.lltest.webview.Frag1WebViewClient;
import com.lltest.webview.Frag1WebViewListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@Link FragmentOne.OnFragmentInteractionListenerCard1} interface
 * to handle interaction events.
 * Use the {@link FragmentOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentOne extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    static private final String TAG = Constants.APP_PREFIX + "FragmentOne";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View mView;
    private Button mBtnClose;
    private Button mBtnF2;
    private Button mBtnShowUrlDialog;

    private WebView mWebView;
    private WebViewClient mWebViewClient;
    private EditText mUrlEdit;

    private Switch mLoadUrlSwitch;

    private Frag1WebViewListener mWebViewListener = new Frag1WebViewListener() {
        @Override
        public void onReceivedError(String url, int errorCode) {
            Log.d(TAG, "onReceivedError: " + url);
            //GeneralUtil.showShortToast(getActivity(), "onReceivedError");
        }

        @Override
        public void onPageFinished(String url) {
            Log.d(TAG, "page loading finished: " + url);
            //GeneralUtil.showShortToast(getActivity(), "onPageFinished");
        }

        @Override
        public void onPageStarted(String url) {
            Log.d(TAG, "page loading started: " + url);
            //GeneralUtil.showShortToast(getActivity(), "onPageStarted");
        }

        @Override
        public void onLoadResource(String url) {
            Log.d(TAG, "onLoadResource: " + url);
            //GeneralUtil.showShortToast(getActivity(), "onLoadResource");
        }

        @Override
        public void onTokenReady(String token) {
            Log.d(TAG, "onTokenReady...");
            //GeneralUtil.showShortToast(getActivity(), "onTokenReady");
        }

        @Override
        public void onPinningPrevented(String host) {
            Log.d(TAG, "onPinningPrevented...");
            //GeneralUtil.showShortToast(getActivity(), "onPinningPrevented");
        }
    };


    public FragmentOne() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentOne.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentOne newInstance(String param1, String param2) {
        /*
        if (fragment1 != null) {
            Log.d(TAG, "reuse fragment1");
            return fragment1;
        }
        */
        Log.d(TAG, "new fragment1");
        FragmentOne fragment = new FragmentOne();
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
        //return inflater.inflate(R.layout.fragment_one, container, false);
        mView = inflater.inflate(R.layout.fragment_one, container, false);
        initUI();
        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String s) {

        if (mListener != null) {
            mListener.onFragmentInteraction(s);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        Log.d(TAG, "onResume()");
        super.onResume();
        Log.d(TAG, "onResume, reset fragment lock to false.");
        ((MainActivity)getActivity()).setFragmentLock(false);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String s);
    }

    /**
     * Launch another fragment from current fragment
     * Try to use the method defained in activity.
     */
    private void loadFragmentTwo() {
        Activity activity = getActivity();
        if (activity != null && activity instanceof MainActivity) {
            ((MainActivity) activity).loadFragmentTwo();
        }
    }

    /**
     * Close current fragment
     */
    private void unloadMyself() {
        Log.d(TAG, "unloadFragmentOne");
        // The following code cannot be put directly into button onClick(), so we use an unload function.
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }


    // Update UI:
    private void initUI() {

        mUrlEdit = (EditText) mView.findViewById(R.id.url_edit);

        mLoadUrlSwitch = (Switch) mView.findViewById(R.id.load_url_switch);
        mLoadUrlSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadUrlSwitch.isChecked()) {
                    Log.d(TAG, "load URL.");
                    if (null != mWebView && null != mUrlEdit) {
                        mWebView.clearView();
                        mWebView.loadUrl(mUrlEdit.getText().toString());
                    }
                } else {
                    Log.d(TAG, "load blank URL.");
                    if (null != mWebView) {
                        mWebView.clearView();
                        mWebView.loadUrl("about:blank");
                    }
                }
            }
        });

        mWebView = (WebView) mView.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);  // TODO: need this?
        mWebViewClient = new Frag1WebViewClient(getContext(), mWebViewListener);
        mWebView.setWebViewClient(mWebViewClient);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        // handle WebView go back operation:
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //This is the filter
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mWebView != null && mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        Log.d(TAG, "WebView cannot go back. Trigger activity onBackPressed");
                        (getActivity()).onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });



        mBtnClose = (Button) mView.findViewById(R.id.one_close_btn);
        mBtnF2 = (Button) mView.findViewById(R.id.one_f2_btn);

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnClose is clicked");
                //getActivity().onBackPressed();  // this will cause the entire app goes to backend, not goes to activity page.

                //getActivity().getFragmentManager().popBackStack();

                unloadMyself();

            }
        });

        mBtnF2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnF2 is clicked");

                // start fragment 2, then remove fragment 1:
                loadFragmentTwo();
                unloadMyself();
            }
        });

        mBtnShowUrlDialog = (Button) mView.findViewById(R.id.url_dialog_btn);
        mBtnShowUrlDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnShowUrlDialog is clicked");
                String url = (mUrlEdit != null) ?
                        mUrlEdit.getText().toString() : "http://www.google.com/";
                WebViewUtil.showUrlDialog(getContext(), url, new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
            }
        });

    }

}
