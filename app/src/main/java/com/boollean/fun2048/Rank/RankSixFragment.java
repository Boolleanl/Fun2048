package com.boollean.fun2048.Rank;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boollean.fun2048.Entity.RankUserEntity;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.boollean.fun2048.Utils.JsonUtils;
import com.boollean.fun2048.Utils.LoadingView;

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
 * 6*6排行榜界面的Fragment。
 * Created by Boollean on 2019/3/9.
 */
public class RankSixFragment extends Fragment {
    @BindView(R.id.rank_loading_view)
    LoadingView mLoadingView;

    @BindView(R.id.rank_recycler_view)
    RecyclerView mRecyclerView;

    private RankAdapter adapter;

    public static RankSixFragment newInstance() {
        return new RankSixFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(HttpUtils.isNetworkAvailable(getActivity())){
            initData();
        }
    }

    private void initData() {
        GetData6 getData6 = new GetData6();
        getData6.execute();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        ButterKnife.bind(this, view);
        mLoadingView.setListener(new LoadingView.LoadingViewListener() {
            @Override
            public void onFailedClickListener() {
                if(HttpUtils.isNetworkAvailable(getActivity())){
                    initData();
                }else {
                    mLoadingView.showNetworkUnavailable();
                }
            }
        });
        if(!HttpUtils.isNetworkAvailable(getActivity())){
            mLoadingView.showNetworkUnavailable();
        }else {
            mLoadingView.showLoading();
        }
        return view;
    }

    private void initView(List list) {
        adapter = new RankAdapter(list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

    private class GetData6 extends AsyncTask<Void, Void, List<RankUserEntity>> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected List<RankUserEntity> doInBackground(Void... voids) {
            String jsonString = HttpUtils.getJsonContent(HttpUtils.GET_BEST_100_USERS_6);
            //如果获取Json失败，直接返回空值
            if (jsonString.equals("fail")){
                return null;
            }
            List<RankUserEntity> userList = JsonUtils.toRankUserList(jsonString);
            return userList;
        }

        @Override
        protected void onPostExecute(List<RankUserEntity> list) {
            if(list!=null){
                Log.i("Rank6", list.size() + "");
                mLoadingView.showContentView();
                initView(list);
            }else {
                mLoadingView.showFailed();
            }
        }
    }
}
