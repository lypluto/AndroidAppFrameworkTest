package com.lltest.ui;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lltest.appFrameTest.R;

import static com.lltest.util.Constants.FRAGMENT_LOGIN_TAG;
import static com.lltest.util.Constants.FRAGMENT_REGISTER_TAG;

/**
 * Created by louis.lu on 5/15/18.
 */

public class LoginActivity extends BaseActivity implements
        View.OnClickListener,
        LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener,
        SetupProfileFragment.OnSetupProfileFragmentInteractionListener
{

    private static final String TAG = LoginActivity.class.getSimpleName();

    private ImageView mImgCover = null;
    private Button mBtnLogin = null;
    private Button mBtnRegister = null;

    @Override
    public void onLoginFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRegisterFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSetupProfileFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * Initialization
     *
     */
    private void init() {
        mImgCover = findViewById(R.id.login_cover_image);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnRegister = findViewById(R.id.btn_register);

        // register button onClick listeners:
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_login:
                Log.d(TAG, "try to load login fragment");
                loadFragmentLogin();
                break;
            case R.id.btn_register:
                Log.d(TAG, "try to load register fragment");
                loadFragmentRegister();
                break;
            default:
                // do nothing
        }
    }

    private void loadFragmentLogin() {
        Log.d(TAG, "loadFragmentLogin()");
        Fragment loginFragment = getFragmentManager().
                findFragmentByTag(FRAGMENT_LOGIN_TAG);

        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance("arg1", "arg2");
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, loginFragment, FRAGMENT_LOGIN_TAG)
                    .addToBackStack(FRAGMENT_LOGIN_TAG)
                    .commitAllowingStateLoss();
        }
    }

    private void loadFragmentRegister() {
        Log.d(TAG, "loadFragmentRegister()");
        Fragment regFragment = getFragmentManager().
                findFragmentByTag(FRAGMENT_REGISTER_TAG);

        if (regFragment == null) {
            regFragment = RegisterFragment.newInstance("arg1", "arg2");
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, regFragment, FRAGMENT_REGISTER_TAG)
                    .addToBackStack(FRAGMENT_REGISTER_TAG)
                    .commitAllowingStateLoss();
        }
    }
}
