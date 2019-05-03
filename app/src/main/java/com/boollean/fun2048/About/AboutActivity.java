package com.boollean.fun2048.About;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.boollean.fun2048.R;

/**
 * 游戏相关界面的Activity。
 *
 * @author Boollean
 */
public class AboutActivity extends AppCompatActivity {

    /**
     * 获取一个新的启动AboutActivity的Intent
     *
     * @param context 上下文
     * @return 启动AboutActivity的Intent
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, AboutActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        FragmentManager fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.fragment_container);

        AboutFragment aboutFragment = AboutFragment.newInstance();
        fm.beginTransaction().add(R.id.fragment_container, aboutFragment).commit();
    }
}
