package com.boollean.fun2048.About;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.boollean.fun2048.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class AboutActivity extends AppCompatActivity {
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
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
