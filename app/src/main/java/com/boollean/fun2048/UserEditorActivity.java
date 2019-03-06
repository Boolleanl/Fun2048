package com.boollean.fun2048;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class UserEditorActivity extends AppCompatActivity {

    private static final String TAG = "UserEditorActivity";
    private UserEditorFragment mUserEditorFragment;

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
