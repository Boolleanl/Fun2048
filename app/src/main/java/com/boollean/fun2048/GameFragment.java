package com.boollean.fun2048;

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

public class GameFragment extends Fragment {

    private static final String TAG = "GameFragment";

    private static final GameFragment mGameFragment = new GameFragment();

    @BindView(R.id.score_view)
    TextView scoreView;

    private TextView[] mTextViews;
    private NumberItem mNumberItem;

    /**
     * 获取实例。
     *
     * @return 获取GameFragment实例。
     */
    public static GameFragment getInstance() {
        return mGameFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumberItem = NumberItem.getInstance(); //单例模式中，获取NumberItem的单一实例。
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
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

    /**
     * 每次滑动后调用，刷新每个TextView里的数字。
     */
    public void refreshView() {
        scoreView.setText(String.valueOf(mNumberItem.getScore()));
        int[][] num = mNumberItem.getNumbers();
        int a = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mTextViews[a].setText(String.valueOf(num[i][j]));
                a++;
            }
        }
    }
}
