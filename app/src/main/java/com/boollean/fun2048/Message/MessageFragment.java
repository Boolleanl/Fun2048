package com.boollean.fun2048.Message;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boollean.fun2048.Entity.MessageEntity;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.boollean.fun2048.Utils.JsonUtils;
import com.boollean.fun2048.Utils.LoadingView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 留言板界面的Fragment。
 *
 * @author Boollean
 */
public class MessageFragment extends Fragment {
    @BindView(R.id.message_loading_view)
    LoadingView mLoadingView;

    @BindView(R.id.message_recycler_view)
    RecyclerView recyclerView;

    /**
     * 获取一个新的MessageFragment对象
     *
     * @return 新的MessageFragment对象
     */
    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (HttpUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            initData();
        }
    }

    /**
     * 初始化数据
     */
    void initData() {
        GetMessageData getMessageData = new GetMessageData();
        getMessageData.execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        mLoadingView.setListener(() -> {
            if (HttpUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                initData();
            } else {
                mLoadingView.showNetworkUnavailable();
            }
        });
        if (!HttpUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            mLoadingView.showNetworkUnavailable();
        } else {
            mLoadingView.showLoading();
        }
        return view;
    }

    private void initView(List<MessageEntity> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        MessageAdapter adapter = new MessageAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 获取留言信息的线程类
     */
    private class GetMessageData extends AsyncTask<Void, Void, List<MessageEntity>> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected List<MessageEntity> doInBackground(Void... voids) {
            String jsonString = HttpUtils.getJsonContent(HttpUtils.GET_MESSAGES);
            //如果获取Json失败，直接返回空值
            if (jsonString.equals("fail")) {
                return null;
            }

            return JsonUtils.toMessageList(jsonString);
        }

        @Override
        protected void onPostExecute(List<MessageEntity> list) {
            if (list != null) {
                mLoadingView.showContentView();
                initView(list);
            } else {
                mLoadingView.showFailed();
            }
        }
    }
}
