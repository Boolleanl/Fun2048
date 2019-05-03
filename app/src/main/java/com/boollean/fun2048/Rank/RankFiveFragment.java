package com.boollean.fun2048.Rank;

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

import com.boollean.fun2048.Entity.RankUserEntity;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.boollean.fun2048.Utils.JsonUtils;
import com.boollean.fun2048.Utils.LoadingView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 5*5排行榜界面的Fragment。
 *
 * @author Boollean
 */
public class RankFiveFragment extends Fragment {
    @BindView(R.id.rank_loading_view)
    LoadingView mLoadingView;
    @BindView(R.id.rank_recycler_view)
    RecyclerView mRecyclerView;

    /**
     * 获取一个新的RankFiveFragment对象
     *
     * @return 新的RankFiveFragment对象
     */
    public static RankFiveFragment newInstance() {
        return new RankFiveFragment();
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
    private void initData() {
        GetData5 getData5 = new GetData5();
        getData5.execute();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
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

    private void initView(List<RankUserEntity> list) {
        RankAdapter adapter = new RankAdapter(list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 初始化此模式游戏数据的线程类
     */
    private class GetData5 extends AsyncTask<Void, Void, List<RankUserEntity>> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected List<RankUserEntity> doInBackground(Void... voids) {
            String jsonString = HttpUtils.getJsonContent(HttpUtils.GET_BEST_100_USERS_5);
            //如果获取Json失败，直接返回空值
            if (jsonString.equals("fail")) {
                return null;
            }
            return JsonUtils.toRankUserList(jsonString);
        }

        @Override
        protected void onPostExecute(List<RankUserEntity> list) {
            if (list != null) {
                mLoadingView.showContentView();
                initView(list);
            } else {
                mLoadingView.showFailed();
            }
        }
    }
}
