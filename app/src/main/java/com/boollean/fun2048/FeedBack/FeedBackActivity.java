package com.boollean.fun2048.FeedBack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.boollean.fun2048.R;

/**
 * 意见反馈界面的Activity。
 *
 * @author Boollean
 */
public class FeedBackActivity extends AppCompatActivity {

    /**
     * 获取一个新的启动FeedBackActivity的Intent
     *
     * @param context 上下文
     * @return 启动FeedBackActivity的Intent
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, FeedBackActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        FragmentManager fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.fragment_container);

        FeedBackFragment mFeedBackFragment = FeedBackFragment.newInstance();
        fm.beginTransaction().add(R.id.fragment_container, mFeedBackFragment).commit();
    }
}
