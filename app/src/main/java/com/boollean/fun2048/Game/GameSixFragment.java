package com.boollean.fun2048.Game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boollean.fun2048.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;

/**
 * 6*6游戏界面的Fragment。
 * Created by Boollean on 2019/3/12.
 */
public class GameSixFragment extends MyBaseFragment {
    private static final String TAG = "GameSixFragment";

    private static final GameSixFragment mGameSixFragment = new GameSixFragment();

    private String s;   //游戏分数显示区域的全局变量

    private TextView[] mTextViews;
    private NumberItem mNumberItem;

    /**
     * 获取实例。
     *
     * @return 获取GameFiveFragment实例。
     */
    public static GameSixFragment getInstance() {
        return mGameSixFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumberItem = NumberItem.getInstanceSix(); //单例模式中，获取NumberItem的单一实例。
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_six_game, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        refreshView();
        return view;
    }

    /**
     * 分配好对应的TextView，由于id不同，故手动逐一绑定。
     *
     * @param view
     */
    private void initView(View view) {
        mTextViews = new TextView[36];

        mTextViews[0] = view.findViewById(R.id.num_0_0);
        mTextViews[1] = view.findViewById(R.id.num_0_1);
        mTextViews[2] = view.findViewById(R.id.num_0_2);
        mTextViews[3] = view.findViewById(R.id.num_0_3);
        mTextViews[4] = view.findViewById(R.id.num_0_4);
        mTextViews[5] = view.findViewById(R.id.num_0_5);

        mTextViews[6] = view.findViewById(R.id.num_1_0);
        mTextViews[7] = view.findViewById(R.id.num_1_1);
        mTextViews[8] = view.findViewById(R.id.num_1_2);
        mTextViews[9] = view.findViewById(R.id.num_1_3);
        mTextViews[10] = view.findViewById(R.id.num_1_4);
        mTextViews[11] = view.findViewById(R.id.num_1_5);

        mTextViews[12] = view.findViewById(R.id.num_2_0);
        mTextViews[13] = view.findViewById(R.id.num_2_1);
        mTextViews[14] = view.findViewById(R.id.num_2_2);
        mTextViews[15] = view.findViewById(R.id.num_2_3);
        mTextViews[16] = view.findViewById(R.id.num_2_4);
        mTextViews[17] = view.findViewById(R.id.num_2_5);

        mTextViews[18] = view.findViewById(R.id.num_3_0);
        mTextViews[19] = view.findViewById(R.id.num_3_1);
        mTextViews[20] = view.findViewById(R.id.num_3_2);
        mTextViews[21] = view.findViewById(R.id.num_3_3);
        mTextViews[22] = view.findViewById(R.id.num_3_4);
        mTextViews[23] = view.findViewById(R.id.num_3_5);

        mTextViews[24] = view.findViewById(R.id.num_4_0);
        mTextViews[25] = view.findViewById(R.id.num_4_1);
        mTextViews[26] = view.findViewById(R.id.num_4_2);
        mTextViews[27] = view.findViewById(R.id.num_4_3);
        mTextViews[28] = view.findViewById(R.id.num_4_4);
        mTextViews[29] = view.findViewById(R.id.num_4_5);

        mTextViews[30] = view.findViewById(R.id.num_5_0);
        mTextViews[31] = view.findViewById(R.id.num_5_1);
        mTextViews[32] = view.findViewById(R.id.num_5_2);
        mTextViews[33] = view.findViewById(R.id.num_5_3);
        mTextViews[34] = view.findViewById(R.id.num_5_4);
        mTextViews[35] = view.findViewById(R.id.num_5_5);
    }

    /**
     * 每次滑动后调用，刷新每个TextView里的数字。
     */
    @Override
    public void refreshView() {
        s = String.valueOf(mNumberItem.getScore());
        super.refreshView(s, 6, mNumberItem, mTextViews);
    }
}
