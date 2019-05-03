package com.boollean.fun2048.Message;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.boollean.fun2048.Entity.MessageEntity;
import com.boollean.fun2048.Entity.User;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.Date;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 留言板界面的Activity。
 *
 * @author Boollean
 */
public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.message_floating_action_button)
    FloatingActionButton floatingActionButton;
    private User mUser = User.getInstance();
    private MessageFragment mMessageFragment;

    /**
     * 获取一个新的启动MessageActivity的Intent
     *
     * @param context 上下文
     * @return 启动MessageActivity的Intent
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, MessageActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.fragment_message_container);

        mMessageFragment = MessageFragment.newInstance();
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

        button.setOnClickListener(v -> {
            dialog.dismiss();
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setName(mUser.getName());
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String s = dateFormat.format(date);
            messageEntity.setDate(s);
            messageEntity.setGender(mUser.getGender());
            messageEntity.setMessage(editText.getText().toString());
            PostMessageData postMessageData = new PostMessageData(messageEntity);
            postMessageData.execute();
        });
    }

    /**
     * 上传新留言的线程类
     */
    private class PostMessageData extends AsyncTask<Void, Void, String> {
        private MessageEntity mMessageEntity;

        PostMessageData(MessageEntity messageEntity) {
            mMessageEntity = messageEntity;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(Void... voids) {
            return HttpUtils.addMessage(mMessageEntity);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("fail")) {
                Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
            } else {
                //获得解析者
                JsonParser jsonParser = new JsonParser();
                //获得根节点元素
                JsonElement root = jsonParser.parse(s);
                //根据文档判断根节点属于什么类型的Gson节点对象
                JsonObject object = root.getAsJsonObject();
                String msg = object.get("msg").getAsString();
                if (msg.equals("success")) {
                    Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
                    mMessageFragment.initData();
                }
            }
        }
    }
}
