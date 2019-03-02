package com.boollean.fun2048;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GameFiveFragment extends MyBaseFragment {

    private static final String TAG = "GameFiveFragment";

    private static final GameFiveFragment M_GAME_FIVE_FRAGMENT = new GameFiveFragment();

    private String s;

    private TextView[] mTextViews;
    private NumberItem mNumberItem;

    /**
     * 获取实例。
     *
     * @return 获取GameFragment实例。
     */
    public static GameFiveFragment getInstance() {
        return M_GAME_FIVE_FRAGMENT;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumberItem = NumberItem.getInstanceFive(); //单例模式中，获取NumberItem的单一实例。
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_five_game, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        refreshView(s,5,mNumberItem,mTextViews);
        return view;
    }

    /**
     * 分配好对应的TextView，由于id不同，故手动逐一绑定。
     *
     * @param view
     */
    private void initView(View view) {
        mTextViews = new TextView[25];

        mTextViews[0] = view.findViewById(R.id.num_0_0);
        mTextViews[1] = view.findViewById(R.id.num_0_1);
        mTextViews[2] = view.findViewById(R.id.num_0_2);
        mTextViews[3] = view.findViewById(R.id.num_0_3);
        mTextViews[4] = view.findViewById(R.id.num_0_4);

        mTextViews[5] = view.findViewById(R.id.num_1_0);
        mTextViews[6] = view.findViewById(R.id.num_1_1);
        mTextViews[7] = view.findViewById(R.id.num_1_2);
        mTextViews[8] = view.findViewById(R.id.num_1_3);
        mTextViews[9] = view.findViewById(R.id.num_1_4);

        mTextViews[10] = view.findViewById(R.id.num_2_0);
        mTextViews[11] = view.findViewById(R.id.num_2_1);
        mTextViews[12] = view.findViewById(R.id.num_2_2);
        mTextViews[13] = view.findViewById(R.id.num_2_3);
        mTextViews[14] = view.findViewById(R.id.num_2_4);

        mTextViews[15] = view.findViewById(R.id.num_3_0);
        mTextViews[16] = view.findViewById(R.id.num_3_1);
        mTextViews[17] = view.findViewById(R.id.num_3_2);
        mTextViews[18] = view.findViewById(R.id.num_3_3);
        mTextViews[19] = view.findViewById(R.id.num_3_4);

        mTextViews[20] = view.findViewById(R.id.num_4_0);
        mTextViews[21] = view.findViewById(R.id.num_4_1);
        mTextViews[22] = view.findViewById(R.id.num_4_2);
        mTextViews[23] = view.findViewById(R.id.num_4_3);
        mTextViews[24] = view.findViewById(R.id.num_4_4);
    }

    /**
     * 每次滑动后调用，刷新每个TextView里的数字。
     */
    @Override
    public void refreshView(String s, int which, NumberItem item, TextView[] mTextViews) {
        s = String.valueOf(mNumberItem.getScore());
        super.refreshView(s, which, item, mTextViews);
    }
}
