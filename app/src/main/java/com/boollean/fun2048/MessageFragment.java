package com.boollean.fun2048;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageFragment extends Fragment {

    @BindView(R.id.message_recycler_view)
    RecyclerView recyclerView;

    private MessageAdapter adapter;
    private ArrayList<String> messageList = new ArrayList<>();

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMessage();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initMessage() {
        String s1 = "第一句话!";
        String s2 = "第二句话。";
        String s3 = "第3句话";
        String s4 = "第si句话？";
        String s5 = "eqwewq";
        String s6 = "31234132";
        String s7 = ":{{}:>";
        String s8 = "/*/***/";
        String s9 = "~!@#!$@#$@!$";
        String s10 = "ewqrhifniasnfiasfnoffnwf";
        String s11 = "大家打架啊发你即使对方内外你唯亭i愤怒的随机发你夫妇能否呢如恶化发顺丰能看见你发ODKLSAFWFWEIFNJKASFAWNF";
        String s12 = "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
        messageList.add(s1);
        messageList.add(s2);
        messageList.add(s3);
        messageList.add(s4);
        messageList.add(s5);
        messageList.add(s6);
        messageList.add(s7);
        messageList.add(s8);
        messageList.add(s9);
        messageList.add(s10);
        messageList.add(s11);
        messageList.add(s12);
    }

    private void initView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(adapter);
    }
}
