package com.boollean.fun2048.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.boollean.fun2048.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * 用户信息界面的Activity。
 * Created by Boollean on 2019/2/28.
 */
public class UserEditorActivity extends AppCompatActivity {

    private static final String TAG = "UserEditorActivity";
    private UserEditorFragment mUserEditorFragment;

    private User mUser = User.getInstance();
    private SharedPreferences mPreferences;

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, UserEditorActivity.class);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        FragmentManager fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.fragment_container);

        mUserEditorFragment = UserEditorFragment.newInstance();
        fm.beginTransaction().add(R.id.fragment_container, mUserEditorFragment).commit();
    }

}
