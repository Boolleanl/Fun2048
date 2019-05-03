package com.boollean.fun2048.Rank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.boollean.fun2048.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 排行榜界面的Activity。
 *
 * @author Boollean
 */
public class RankActivity extends AppCompatActivity {

    private static final List<String> sTitles = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    private RankFourFragment mRankFourFragment;
    private RankFiveFragment mRankFiveFragment;
    private RankSixFragment mRankSixFragment;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    /**
     * 获取一个新的启动RankActivity的Intent
     *
     * @param context 上下文
     * @return 启动RankActivity的Intent
     */
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

        RankPagerAdapter rankPagerAdapter = new RankPagerAdapter(getSupportFragmentManager(), mFragments, sTitles);
        mViewPager.setAdapter(rankPagerAdapter);
        mViewPager.setCurrentItem(0);

        mTabLayout.setupWithViewPager(mViewPager);
    }
}
