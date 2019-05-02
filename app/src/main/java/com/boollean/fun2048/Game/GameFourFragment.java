package com.boollean.fun2048.Game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boollean.fun2048.Entity.NumberItem;
import com.boollean.fun2048.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;

/**
 * 4*4游戏界面的Fragment。
 *
 * @author Boollean
 */
public class GameFourFragment extends MyBaseFragment {

    private static final String TAG = "GameFourFragment";

    private static final GameFourFragment mGameFourFragment = new GameFourFragment();

    private TextView[] mTextViews;
    private NumberItem mNumberItem;

    /**
     * 获取实例。
     *
     * @return 获取GameFourFragment实例。
     */
    public static GameFourFragment getInstance() {
        return mGameFourFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumberItem = NumberItem.getInstanceFour(); //单例模式中，获取NumberItem的单一实例。
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_four_game, container, false);
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
        mTextViews = new TextView[16];

        mTextViews[0] = view.findViewById(R.id.num_0_0);
        mTextViews[1] = view.findViewById(R.id.num_0_1);
        mTextViews[2] = view.findViewById(R.id.num_0_2);
        mTextViews[3] = view.findViewById(R.id.num_0_3);

        mTextViews[4] = view.findViewById(R.id.num_1_0);
        mTextViews[5] = view.findViewById(R.id.num_1_1);
        mTextViews[6] = view.findViewById(R.id.num_1_2);
        mTextViews[7] = view.findViewById(R.id.num_1_3);

        mTextViews[8] = view.findViewById(R.id.num_2_0);
        mTextViews[9] = view.findViewById(R.id.num_2_1);
        mTextViews[10] = view.findViewById(R.id.num_2_2);
        mTextViews[11] = view.findViewById(R.id.num_2_3);

        mTextViews[12] = view.findViewById(R.id.num_3_0);
        mTextViews[13] = view.findViewById(R.id.num_3_1);
        mTextViews[14] = view.findViewById(R.id.num_3_2);
        mTextViews[15] = view.findViewById(R.id.num_3_3);
    }


    @Override
    public void refreshView() {
        String score = String.valueOf(mNumberItem.getScore());
        String bestScore = String.valueOf(mNumberItem.getBestScore());
        super.refreshView(score, bestScore, 4, mNumberItem, mTextViews);
    }
}
