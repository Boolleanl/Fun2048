package com.boollean.fun2048;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";

    @BindView(R.id.message_floating_action_button)
    FloatingActionButton floatingActionButton;

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, MessageActivity.class);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.fragment_message_container);

        MessageFragment mMessageFragment = MessageFragment.newInstance();
        fm.beginTransaction().add(R.id.fragment_message_container, mMessageFragment).commit();
    }


    @OnClick(R.id.message_floating_action_button)
    void writeMessage(){
        Toast.makeText(this, "写留言", Toast.LENGTH_SHORT).show();
    }
}
