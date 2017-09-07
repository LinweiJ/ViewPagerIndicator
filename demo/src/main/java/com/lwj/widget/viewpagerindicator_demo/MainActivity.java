package com.lwj.widget.viewpagerindicator_demo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lwj.widget.viewpagerindicator.ViewPagerIndicator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewpager;
    private ViewPagerIndicator mIndicatorDefault;
    private ViewPagerIndicator mIndicatorCircle;
    private ViewPagerIndicator mIndicatorLine;
    private ViewPagerIndicator mIndicatorCircleLine;
    private ViewPagerIndicator mIndicatorBazier;
    private ViewPagerIndicator mIndicatorSpring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("NO.1");
        strings.add("NO.2");
        strings.add("NO.3");
        strings.add("NO.4");
//        strings.add("NO.5");
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


        int allNum=100000;
        final int num= strings.size();  // 100/7=14..2    7*7=49  20/7=2..6   7   30/7=4..2    14   40/7=5..5   14
        int firstIndex=0;
        if(num>0)
        {
            firstIndex=allNum/num/2*num;
        }


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),strings,num,allNum);
        mViewpager.setAdapter(adapter);
        mViewpager.setCurrentItem(firstIndex);


        mIndicatorDefault = (ViewPagerIndicator) findViewById(R.id.indicator_default);
        mIndicatorCircle = (ViewPagerIndicator) findViewById(R.id.indicator_circle);
        mIndicatorLine = (ViewPagerIndicator) findViewById(R.id.indicator_line);
        mIndicatorCircleLine = (ViewPagerIndicator) findViewById(R.id.indicator_circle_line);
        mIndicatorBazier = (ViewPagerIndicator) findViewById(R.id.indicator_bezier);
        mIndicatorSpring = (ViewPagerIndicator) findViewById(R.id.indicator_spring);

        mIndicatorDefault.setViewPager(mViewpager,num);
        mIndicatorCircle.setViewPager(mViewpager,num);
        mIndicatorLine.setViewPager(mViewpager,num);
        mIndicatorCircleLine.setViewPager(mViewpager,num);
        mIndicatorBazier.setViewPager(mViewpager,num);
        mIndicatorSpring.setViewPager(mViewpager,num);

    }





    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private FragmentManager mSupportFragmentManager;
        private ArrayList<String> mStrings;
        private  int mNum;
        private  int mAllNum;


        public ViewPagerAdapter(FragmentManager supportFragmentManager, ArrayList  strings, int num, int allNum) {
            super(supportFragmentManager);
            mSupportFragmentManager = supportFragmentManager;
            mStrings = strings;
            mNum = num;
            mAllNum = allNum;
        }


        @Override
        public Fragment getItem(int position) {

            BlankFragment blankFragment=new BlankFragment();
            Bundle bundle = new Bundle();
            if(mNum>0)
            {
                bundle.putString("key",mStrings.get(position%mNum));
            }
            blankFragment.setArguments(bundle);
            return   blankFragment;
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mAllNum;
        }
    }

    public void not_carousel(View v)
    {
        startActivity(new Intent(this,NotCarouselActivity.class));
    }
}
