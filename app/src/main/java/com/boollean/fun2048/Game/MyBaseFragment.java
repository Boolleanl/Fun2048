package com.boollean.fun2048.Game;

import android.graphics.Color;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.boollean.fun2048.Entity.NumberItem;
import com.boollean.fun2048.R;

import butterknife.BindView;

/**
 * 游戏界面的父类，定义了通用的刷新每个格子颜色和数值数据的方法。
 *
 * @author Boollean
 */
class MyBaseFragment extends Fragment {

    @BindView(R.id.score_view)
    TextView scoreView;
    @BindView(R.id.best_score_view)
    TextView bestScoreView;

    /**
     * 每次滑动后调用，刷新每个TextView里的数字。
     */
    void refreshView(String score, String bestScore, int which, NumberItem item, TextView[] mTextViews) {
        scoreView.setText(score);
        bestScoreView.setText(bestScore);
        int[][] num = item.getNumbers();
        int a = 0;
        for (int i = 0; i < which; i++) {
            for (int j = 0; j < which; j++) {
                if (num[i][j] == 2) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#FFDEAD"));
                } else if (num[i][j] == 4) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#7CCD7C"));
                } else if (num[i][j] == 8) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#FF8247"));
                } else if (num[i][j] == 16) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#FF6A6A"));
                } else if (num[i][j] == 32) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#FF6347"));
                } else if (num[i][j] == 64) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#FF3030"));
                } else if (num[i][j] == 128) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#FF6EB4"));
                } else if (num[i][j] == 256) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#8B4789"));
                } else if (num[i][j] == 512) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#9B30FF"));
                } else if (num[i][j] == 1024) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#6A5ACD"));
                } else if (num[i][j] == 2048) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#FF0000"));
                } else if (num[i][j] == 4096) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#006400"));
                } else if (num[i][j] == 8192) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#EEC900"));
                }
                //TODO:添加更多颜色
                mTextViews[a].setText(String.valueOf(num[i][j]));

                if (num[i][j] == 0) {
                    mTextViews[a].setBackgroundColor(Color.parseColor("#FAF0E6"));
                    mTextViews[a].setText("");
                }
                a++;
            }
        }
    }
}
