package com.boollean.fun2048.Rank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.boollean.fun2048.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * 排行榜界面的Activity。
 *
 * @author Boollean
 */
public class RankActivity extends AppCompatActivity {

    private static final List<String> sTitles = new ArrayList<String>();
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private RankFourFragment mRankFourFragment;
    private RankFiveFragment mRankFiveFragment;
    private RankSixFragment mRankSixFragment;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private RankPagerAdapter mRankPagerAdapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, RankActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        mRankFourFragment = new RankFourFragment();
        mRankFiveFragment = new RankFiveFragment();
        mRankSixFragment = new RankSixFragment();

        mFragments.add(0, mRankFourFragment);
        mFragments.add(1, mRankFiveFragment);
        mFragments.add(2, mRankSixFragment);

        sTitles.add("4X4模式");
        sTitles.add("5X5模式");
        sTitles.add("6X6模式");

        mTabLayout = findViewById(R.id.rank_tab_layout);
        mViewPager = findViewById(R.id.rank_view_pager);

        mRankPagerAdapter = new RankPagerAdapter(getSupportFragmentManager(), mFragments, sTitles);
        mViewPager.setAdapter(mRankPagerAdapter);
        mViewPager.setCurrentItem(0);

        mTabLayout.setupWithViewPager(mViewPager);
    }
}
