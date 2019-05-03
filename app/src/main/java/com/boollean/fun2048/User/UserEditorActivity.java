package com.boollean.fun2048.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.boollean.fun2048.R;

/**
 * 用户信息界面的Activity。
 *
 * @author Boollean
 */
public class UserEditorActivity extends AppCompatActivity {

    private UserEditorFragment userEditorFragment;

    /**
     * 获取一个新的启动UserEditorActivity的Intent
     *
     * @param context 上下文
     * @return 启动UserEditorActivity的Intent
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, UserEditorActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        FragmentManager fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.fragment_container);

        userEditorFragment = UserEditorFragment.newInstance();
        fm.beginTransaction().add(R.id.fragment_container, userEditorFragment).commit();
    }

}
