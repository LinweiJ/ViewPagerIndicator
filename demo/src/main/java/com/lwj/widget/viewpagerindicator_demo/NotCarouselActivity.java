package com.lwj.widget.viewpagerindicator_demo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lwj.widget.viewpagerindicator.ViewPagerIndicator;

import java.util.ArrayList;

public class NotCarouselActivity extends AppCompatActivity {

    private ViewPager mViewpager;
    private ViewPagerAdapter2 adapter;
    private ViewPagerIndicator mIndicatorDefault;
    private ViewPagerIndicator mIndicatorCircle;
    private ViewPagerIndicator mIndicatorLine;
    private ViewPagerIndicator mIndicatorCircleLine;
    private ViewPagerIndicator mIndicatorBazier;
    private ViewPagerIndicator mIndicatorSpring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_carousel);

        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("NO.1");
        strings.add("NO.2");
        strings.add("NO.3");
        strings.add("NO.4");
        strings.add("NO.5");
//        strings.add("NO.6");
//        strings.add("NO.7");
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i=0;i<strings.size();i++){
            Fragment fragment = new BlankFragment();
            Bundle bundle = new Bundle();
            bundle.putString("key",strings.get(i));
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        ViewPagerAdapter2 adapter =  new ViewPagerAdapter2(getSupportFragmentManager(),fragments);
        mViewpager.setAdapter(adapter);

        mIndicatorDefault = (ViewPagerIndicator) findViewById(R.id.indicator_default);
        mIndicatorCircle = (ViewPagerIndicator) findViewById(R.id.indicator_circle);
        mIndicatorLine = (ViewPagerIndicator) findViewById(R.id.indicator_line);
        mIndicatorCircleLine = (ViewPagerIndicator) findViewById(R.id.indicator_circle_line);
        mIndicatorBazier = (ViewPagerIndicator) findViewById(R.id.indicator_bezier);
        mIndicatorSpring = (ViewPagerIndicator) findViewById(R.id.indicator_spring);

        mIndicatorDefault.setViewPager(mViewpager);
        mIndicatorCircle.setViewPager(mViewpager);
        mIndicatorLine.setViewPager(mViewpager);
        mIndicatorCircleLine.setViewPager(mViewpager);
        mIndicatorBazier.setViewPager(mViewpager);
        mIndicatorSpring.setViewPager(mViewpager);
    }


    class ViewPagerAdapter2 extends FragmentPagerAdapter {

        private FragmentManager mSupportFragmentManager;
        private ArrayList<Fragment> mFragments;


        public ViewPagerAdapter2(FragmentManager supportFragmentManager, ArrayList<Fragment> fragments) {
            super(supportFragmentManager);
            mSupportFragmentManager = supportFragmentManager;
            mFragments = fragments;

        }

        @Override
        public Fragment getItem(int position) {
            return   mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

}
