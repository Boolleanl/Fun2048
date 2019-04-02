package com.boollean.fun2048.Message;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.JsonUtils;
import com.boollean.fun2048.Utils.MessageEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 留言板界面的Fragment。
 * Created by Boollean on 2019/3/6.
 */
public class MessageFragment extends Fragment {

    @BindView(R.id.message_recycler_view)
    RecyclerView recyclerView;

    private MessageAdapter adapter;
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> dateList = new ArrayList<>();
    private ArrayList<String> messageList = new ArrayList<>();
    private ArrayList<Integer> genderList = new ArrayList<>();

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        GetMessageData getMessageData = new GetMessageData();
        getMessageData.execute();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView(List list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    private class GetMessageData extends AsyncTask<Void, Void, List<MessageEntity>> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected List<MessageEntity> doInBackground(Void... voids) {
//            String jsonString = HttpUtils.getJsonContent(HttpUtils.BASE_URL);
            String jsonString = "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"msg\": \"success\",\n" +
                    "    \"subjects\": [\n" +
                    "        {\n" +
                    "            \"name\": \"小红\",\n" +
                    "            \"gender\": 2,\n" +
                    "            \"date\": \"2017-2-5\",\n" +
                    "            \"message\": \"你好码！\" ,\n" +
                    "            \"avatar_path\": \"qwerasd.png\"\n" +
                    "        },\n" + "        {\n" +
                    "            \"name\": \"小明\",\n" +
                    "            \"gender\": 1,\n" +
                    "            \"date\": \"2018-2-5\",\n" +
                    "            \"message\": \"你好啊！\" ,\n" +
                    "            \"avatar_path\": \"qwerasd.png\"\n" +
                    "        },\n" + "        {\n" +
                    "            \"name\": \"小强\",\n" +
                    "            \"gender\": 0,\n" +
                    "            \"date\": \"2018-3-5\",\n" +
                    "            \"message\": \"你好不好\" ,\n" +
                    "            \"avatar_path\": \"qwerasd.png\"\n" +
                    "        },\n" + "        {\n" +
                    "            \"name\": \"小方\",\n" +
                    "            \"gender\": 2,\n" +
                    "            \"date\": \"2018-8-5\",\n" +
                    "            \"message\": \"dajfnafnafasjnfan打卡机你飞机喀什妇女健康好！\" ,\n" +
                    "            \"avatar_path\": \"qwerasd.png\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"name\": \"小阿三\",\n" +
                    "            \"gender\": 1,\n" +
                    "            \"date\": \"2018-9-5\",\n" +
                    "            \"message\": \"？？？？？？？？？？！\" ,\n" +
                    "            \"avatar_path\": \"qwerasd.png\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"name\": \"小去\",\n" +
                    "            \"gender\": 0,\n" +
                    "            \"date\": \"2018-12-5\",\n" +
                    "            \"message\": \"你好啊！\" ,\n" +
                    "            \"avatar_path\": \"qwerasd.png\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            Log.i("Message :  ", jsonString);
            List<MessageEntity> messageList = JsonUtils.toMessageList(jsonString);
            Log.i("Message :  ", messageList.size() + "");
            return messageList;
        }

        @Override
        protected void onPostExecute(List<MessageEntity> list) {
            Log.i("Message :  ", list.size() + "");
            initView(list);
        }
    }
}
