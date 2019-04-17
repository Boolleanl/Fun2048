package com.boollean.fun2048.Message;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boollean.fun2048.Entity.MessageEntity;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.boollean.fun2048.Utils.JsonUtils;
import com.boollean.fun2048.Utils.LoadingView;

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
    @BindView(R.id.loading_view)
    LoadingView mLoadingView;

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

    public void initData() {
        GetMessageData getMessageData = new GetMessageData();
        getMessageData.execute();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        mLoadingView.setListener(new LoadingView.LoadingViewListener() {
            @Override
            public void onFailedClickListener() {

            }
        });
        mLoadingView.showLoading();
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
            String jsonString = HttpUtils.getJsonContent(HttpUtils.GET_MESSAGES);
            Log.i("Message", jsonString);
            if (jsonString.equals("fail")){
                return null;
            }
            List<MessageEntity> messageList = JsonUtils.toMessageList(jsonString);
            Log.i("Message", messageList.size() + "");
            return messageList;
        }

        @Override
        protected void onPostExecute(List<MessageEntity> list) {
            if(list!=null){
                Log.i("Message", list.size() + "");
                mLoadingView.showContentView();
                initView(list);
            }else {
                Toast.makeText(getActivity(), "服务器异常，无法获取信息", Toast.LENGTH_LONG).show();
                mLoadingView.showFailed();
            }
        }
    }
}
