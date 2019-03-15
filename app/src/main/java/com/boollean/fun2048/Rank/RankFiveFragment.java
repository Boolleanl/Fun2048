package com.boollean.fun2048.Rank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boollean.fun2048.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 5*5排行榜界面的Fragment。
 * Created by Boollean on 2019/3/9.
 */
public class RankFiveFragment extends Fragment {

    @BindView(R.id.rank_recycler_view)
    RecyclerView mRecyclerView;

    private RankAdapter adapter;
    private ArrayList<Integer> positionList;
    private ArrayList<String> nameList;
    private ArrayList<Integer> scoreList;
    private ArrayList<Integer> genderList;

    public static RankFiveFragment newInstance() {
        return new RankFiveFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPosition();
        initName();
        initGender();
        initScore();
    }

    private void initScore() {
        if (scoreList == null) {
            scoreList = new ArrayList<>();
            Integer s1 = 555555555;
            Integer s2 = 555555;
            Integer s3 = 555;
            scoreList.add(s1);
            scoreList.add(s2);
            scoreList.add(s3);
        }
    }

    private void initGender() {
        if (genderList == null) {
            genderList = new ArrayList<>();
            Integer g1 = 1;
            Integer g2 = 2;
            Integer g3 = 0;
            genderList.add(g1);
            genderList.add(g2);
            genderList.add(g3);
        }
    }

    private void initName() {
        if (nameList == null) {
            nameList = new ArrayList<>();
            String s1 = "用户55a";
            String s2 = "用户bda3";
            String s3 = "用户cda5d";
            nameList.add(s1);
            nameList.add(s2);
            nameList.add(s3);
        }
    }

    private void initPosition() {
        if (positionList == null) {
            positionList = new ArrayList<>();
            Integer i1 = 1;
            Integer i2 = 2;
            Integer i3 = 3;
            positionList.add(i1);
            positionList.add(i2);
            positionList.add(i3);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        adapter = new RankAdapter(positionList, nameList, scoreList, genderList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }
}
