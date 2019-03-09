package com.boollean.fun2048;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class RankActivity extends AppCompatActivity {

    private static final List<String> sTitles = new ArrayList<String>();
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private RankFourFragment mRankFourFragment;
    private RankFourFragment mRankFourFragment1;
    private RankFourFragment mRankFourFragment2;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private MyPagerAdapter mMyPagerAdapter;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,RankActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        mRankFourFragment = new RankFourFragment();
        mRankFourFragment1 = new RankFourFragment();
        mRankFourFragment2 = new RankFourFragment();

        mFragments.add(0,mRankFourFragment);
        mFragments.add(1,mRankFourFragment1);
        mFragments.add(2,mRankFourFragment2);

        sTitles.add("4X4");
        sTitles.add("5X5");
        sTitles.add("6X6");

        mTabLayout = findViewById(R.id.rank_tab_layout);
        mViewPager = findViewById(R.id.rank_view_pager);

        mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),mFragments,sTitles);
        mViewPager.setAdapter(mMyPagerAdapter);
        mViewPager.setCurrentItem(0);

        mTabLayout.setupWithViewPager(mViewPager);
    }
}
