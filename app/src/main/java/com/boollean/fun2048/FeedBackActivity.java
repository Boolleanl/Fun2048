package com.boollean.fun2048;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.ButterKnife;

public class FeedBackActivity extends AppCompatActivity {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,FeedBackActivity.class);
        return  intent;
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
