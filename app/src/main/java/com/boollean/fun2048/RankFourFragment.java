package com.boollean.fun2048;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RankFourFragment extends Fragment {


    @BindView(R.id.rank_list_view)
    ListView mListView;
    @BindView(R.id.rank_more_text_view)
    TextView mTextView;

    private RankAdapter adapter;
    private ArrayList<Integer> positionList;
    private ArrayList<String> nameList;
    private ArrayList<Integer> scoreList;

    public static RankFourFragment newInstance() {
        return new RankFourFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPosition();
        initName();
        initScore();
    }

    private void initScore() {
        if(scoreList == null){
            scoreList = new ArrayList<>();
            Integer s1 = 88888888;
            Integer s2 = 77777;
            Integer s3 = 333;
            scoreList.add(s1);
            scoreList.add(s2);
            scoreList.add(s3);
        }
    }

    private void initName() {
        if(nameList == null){
            nameList = new ArrayList<>();
            String s1 = "用户a";
            String s2 = "用户b";
            String s3 = "用户c";
            nameList.add(s1);
            nameList.add(s2);
            nameList.add(s3);
        }
    }

    private void initPosition() {
        if(positionList == null){
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
        View view = inflater.inflate(R.layout.fragment_rank,container,false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        adapter = new RankAdapter(positionList,nameList,scoreList);
        mListView.setAdapter(adapter);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
