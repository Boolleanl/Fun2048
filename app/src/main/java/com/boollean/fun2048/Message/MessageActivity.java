package com.boollean.fun2048.Message;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boollean.fun2048.Entity.MessageEntity;
import com.boollean.fun2048.Entity.User;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.text.SimpleDateFormat;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
    private User mUser = User.getInstance();

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
        if (mUser.getName() == null) {
            Toast.makeText(getApplicationContext(), "发表留言之前需要先创建账号", Toast.LENGTH_SHORT).show();
            return;
        } else if (!HttpUtils.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(), "发表留言之前需要先打开网络连接", Toast.LENGTH_SHORT).show();
            return;
        }
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
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setName(mUser.getName());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String s = dateFormat.format(date);
                messageEntity.setDate(s);
                messageEntity.setAvatarPath(mUser.getBitmapPath().toString());
                messageEntity.setGender(mUser.getGender());
                messageEntity.setMessage(editText.getText().toString());
                Toast.makeText(getApplicationContext(), editText.getText(), Toast.LENGTH_SHORT).show();
                PostMessageData postMessageData = new PostMessageData(messageEntity);
                postMessageData.execute();
            }
        });
    }

    private class PostMessageData extends AsyncTask<Void, Void, Void> {
        private MessageEntity mMessageEntity;

        public PostMessageData(MessageEntity messageEntity) {
            mMessageEntity = messageEntity;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
//            String jsonString = HttpUtils.getJsonContent(HttpUtils.BASE_URL);
            HttpUtils.upLoadMessage(mUser.getName(), mMessageEntity, new HttpUtils.HttpCallbackListener() {
                @Override
                public void onFinish(String s) {
                    Log.i("message", "成功");
                }

                @Override
                public void onError(Exception e) {
                    Log.i("message", e.getMessage());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
        }
    }
}
