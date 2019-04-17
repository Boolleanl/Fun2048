package com.boollean.fun2048.Rank;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boollean.fun2048.Entity.RankUserEntity;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.boollean.fun2048.Utils.JsonUtils;

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
 * 4*4排行榜界面的Fragment。
 * Created by Boollean on 2019/3/8.
 */
public class RankFourFragment extends Fragment {
    @BindView(R.id.rank_recycler_view)
    RecyclerView mRecyclerView;

    private RankAdapter adapter;

    public static RankFourFragment newInstance() {
        return new RankFourFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        GetData4 getData = new GetData4();
        getData.execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView(List list) {
        adapter = new RankAdapter(list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

    private class GetData4 extends AsyncTask<Void, Void, List<RankUserEntity>> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected List<RankUserEntity> doInBackground(Void... voids) {
            String jsonString = HttpUtils.getJsonContent(HttpUtils.GET_BEST_100_USERS_4);
            Log.i("Rank4:  ", jsonString);
            if (jsonString.equals("fail")){
                return null;
            }
            List<RankUserEntity> userList = JsonUtils.toRankUserList(jsonString);
            Log.i("Rank4:  ", userList.size() + "");
            return userList;
        }

        @Override
        protected void onPostExecute(List<RankUserEntity> list) {
            if(list!=null){
                Log.i("Rank4", list.size() + "");
                initView(list);
            }else {
                Toast.makeText(getActivity(), "服务器异常，无法获取信息", Toast.LENGTH_LONG).show();
            }
        }
    }
}
