package com.boollean.fun2048.Message;

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

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initName();
        initDate();
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

    private void initName() {
        String s1 = "用户a";
        String s2 = "用户b";
        String s3 = "用户c";
        String s4 = "dasdsa";
        String s5 = "31321";
        String s6 = "3413248654362867523426866305343278678563403532423424297856435.123.1fasdf";
        String s7 = ":{{}:>";
        String s8 = "/*/***/";
        String s9 = "~!@#!$@#$@!$";
        String s10 = "ewqrhifniasnfiasfnoffnwf";
        String s11 = "大家打架啊发你即使对方内外你唯亭i愤怒的随机发你夫妇能否呢如恶化发顺丰能看见你发ODKLSAFWFWEIFNJKASFAWNF";
        String s12 = "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
        nameList.add(s1);
        nameList.add(s2);
        nameList.add(s3);
        nameList.add(s4);
        nameList.add(s5);
        nameList.add(s6);
        nameList.add(s7);
        nameList.add(s8);
        nameList.add(s9);
        nameList.add(s10);
        nameList.add(s11);
        nameList.add(s12);
    }


    private void initDate() {
        String s1 = "1/01";
        String s2 = "12/16";
        String s3 = "2018/1/24";
        String s4 = "12:26";
        String s5 = "";
        String s6 = "3413248654362867523426866305343278678563403532423424297856435.123.1fasdf";
        String s7 = ":{{}:>";
        String s8 = "/*/***/";
        String s9 = "~!@#!$@#$@!$";
        String s10 = "ewqrhifniasnfiasfnoffnwf";
        String s11 = "大家打架啊发你即使对方内外你唯亭i愤怒的随机发你夫妇能否呢如恶化发顺丰能看见你发ODKLSAFWFWEIFNJKASFAWNF";
        String s12 = "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
        dateList.add(s1);
        dateList.add(s2);
        dateList.add(s3);
        dateList.add(s4);
        dateList.add(s5);
        dateList.add(s6);
        dateList.add(s7);
        dateList.add(s8);
        dateList.add(s9);
        dateList.add(s10);
        dateList.add(s11);
        dateList.add(s12);
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

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageAdapter(nameList, dateList, messageList);
        recyclerView.setAdapter(adapter);
    }
}
