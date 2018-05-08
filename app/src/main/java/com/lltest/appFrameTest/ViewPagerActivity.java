package com.lltest.appFrameTest;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lltest.util.Constants;

import github.chenupt.springindicator.SpringIndicator;

public class ViewPagerActivity extends AppCompatActivity implements
        FragmentCard1.OnFragmentInteractionListenerCard1,
        FragmentCard2.OnFragmentInteractionListenerCard2,
        FragmentCard3.OnFragmentInteractionListenerCard3 {

    static private final String TAG = Constants.APP_PREFIX + "ViewPagerActivity";

    private Toolbar toolbar;
    private ViewPager vpPager;
    private FragmentPagerAdapter mPagerAdapter;
    private SpringIndicator springIndicator;

    private FloatingActionButton fab;

    @Override
    public void onFragmentInteractionCard1(Uri uri) {

    }

    @Override
    public void onFragmentInteractionCard2(Uri uri) {

    }

    @Override
    public void onFragmentInteractionCard3(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        initUI();           // init UI components
    }

    /**
     * Setup FragmentPagerAdapter
     */
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;   // 2 pages

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @SuppressLint("LongLogTag")
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    Log.d(TAG, "getItem: 0");
                    return FragmentCard1.newInstance(0, "Page #1");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    Log.d(TAG, "getItem: 1");
                    return FragmentCard2.newInstance(1, "Page #2");
                case 2: // Fragment # 0 - This will show FirstFragment different title
                    Log.d(TAG, "getItem: 2");
                    return FragmentCard3.newInstance(2, "Page #3");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        // The titles set there will be displayed on the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "P " + String.valueOf(position + 1);    // we want the display starts from 1
        }

    }

    private void initUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "onClick FloatingActionButton!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // init ViewPager view, and link this view with adapter:
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        springIndicator = (SpringIndicator) findViewById(R.id.indicator);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(mPagerAdapter);
        springIndicator.setViewPager(vpPager);

        // Attach the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @SuppressLint("LongLogTag")
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: " + position);
                Toast.makeText(ViewPagerActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @SuppressLint("LongLogTag")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
                //Log.d(TAG, "onPageScrolled...");
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @SuppressLint("LongLogTag")
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
                //Log.d(TAG, "onPageScrollStateChanged...");
            }
        });
    }

}
