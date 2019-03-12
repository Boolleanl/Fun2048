package com.boollean.fun2048.Message;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boollean.fun2048.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 留言板界面的Activity。
 * Created by Boollean on 2019/3/6.
 */
public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";

    @BindView(R.id.message_floating_action_button)
    FloatingActionButton floatingActionButton;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        return intent;
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
    void writeMessage() {
        View view = LayoutInflater.from(this).inflate(R.layout.message_edit_view, null);
        EditText editText = view.findViewById(R.id.message_edit_text);
        Button button = view.findViewById(R.id.message_submit_button);

        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("请输入留言:")
                .setView(view)
                .create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), editText.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
