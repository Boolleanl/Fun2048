package com.boollean.fun2048.Game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.boollean.fun2048.Entity.NumberItem;
import com.boollean.fun2048.Entity.User;
import com.boollean.fun2048.Main.MainActivity;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.boollean.fun2048.Utils.OperationFactory;
import com.boollean.fun2048.Utils.OperationThread;

import org.json.JSONArray;

/**
 * 游戏界面的Activity。
 *
 * @author Boollean
 */
public class GameActivity extends AppCompatActivity {
    private static final float FLIP_DISTANCE = 48;      //滑动判定距离
    private static int whichGame;   //游戏模式标志
    private static Handler mHandler;
    private static OperationThread operationThread; //运算线程
    private User mUser = User.getInstance();
    private SoundPool mSoundPool;   //用以实现音效的声音池
    private int soundID;    //音效文件的ID
    private GameFourFragment mGameFourFragment;     //点击4X4模式后的界面
    private GameFiveFragment mGameFiveFragment;     //点击5X5模式后的界面
    private GameSixFragment mGameSixFragment;       //点击6X6模式后的界面
    private GestureDetector mDetector;      //滑动手势判断
    private SharedPreferences mPreferences;     //用以保存相关数据的SharedPreferences

    /**
     * “新游戏”启动时调用的方法。
     *
     * @param context 前文的活动
     * @param which   开启了哪个游戏模式
     * @return 游戏活动的Intent
     */
    public static Intent newIntent(Context context, int which, int bestScore) {
        whichGame = which;
        mHandler = new Handler();
        operationThread = new OperationThread(which);
        if (which == 4) {
            OperationFactory.newGameFour(bestScore);
        } else if (which == 5) {
            OperationFactory.newGameFive(bestScore);
        } else if (which == 6) {
            OperationFactory.newGameSix(bestScore);
        }
        return new Intent(context, GameActivity.class);
    }

    /**
     * “继续游戏”调用的方法
     *
     * @param context 前文的获得
     * @param which   开启了哪个游戏模式
     * @param n       从SharedPreferences处获取的上次游戏最后一步的记录。
     * @return 游戏活动的Intent
     */
    public static Intent newIntent(Context context, int which, int[][] n) {
        whichGame = which;
        if (whichGame != 0) {
            mHandler = new Handler();
            operationThread = new OperationThread(whichGame);
            OperationFactory.continueGame(whichGame, n);
        }
        return new Intent(context, GameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        FragmentManager fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.fragment_container);

        mSoundPool = new SoundPool.Builder().build();
        soundID = mSoundPool.load(this, R.raw.fusion_sound, 1);

        if (whichGame == 4) {
            mGameFourFragment = GameFourFragment.getInstance();
            fm.beginTransaction().add(R.id.fragment_container, mGameFourFragment).commit();
        } else if (whichGame == 5) {
            mGameFiveFragment = GameFiveFragment.getInstance();
            fm.beginTransaction().add(R.id.fragment_container, mGameFiveFragment).commit();
        } else if (whichGame == 6) {
            mGameSixFragment = GameSixFragment.getInstance();
            fm.beginTransaction().add(R.id.fragment_container, mGameSixFragment).commit();
        }

        //滑动方向判定。
        mDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
                if (e1.getY() - e2.getY() > e1.getX() - e2.getX()
                        && e1.getY() - e2.getY() > e2.getX() - e1.getX()
                        && e1.getY() - e2.getY() > FLIP_DISTANCE) {
                    //Up，向上滑动。
                    operationThread.setAction(1);
                    mHandler.post(operationThread);
                    playVolume();
                    return true;
                }
                if (e2.getY() - e1.getY() > e1.getX() - e2.getX()
                        && e2.getY() - e1.getY() > e2.getX() - e1.getX()
                        && e2.getY() - e1.getY() > FLIP_DISTANCE) {
                    //Down，向下滑动。
                    operationThread.setAction(2);
                    mHandler.post(operationThread);
                    playVolume();
                    return true;
                }
                if (e1.getX() - e2.getX() > e1.getY() - e2.getY()
                        && e1.getX() - e2.getX() > e2.getY() - e1.getY()
                        && e1.getX() - e2.getX() > FLIP_DISTANCE) {
                    //Left，向左滑动。
                    operationThread.setAction(3);
                    mHandler.post(operationThread);
                    playVolume();
                    return true;
                }
                if (e2.getX() - e1.getX() > e1.getY() - e2.getY()
                        && e2.getX() - e1.getX() > e2.getY() - e1.getY()
                        && e2.getX() - e1.getX() > FLIP_DISTANCE) {
                    //Right，向右滑动。
                    operationThread.setAction(4);
                    mHandler.post(operationThread);
                    playVolume();
                    return true;
                }

                return false;
            }
        });
    }

    /**
     * 播放一次音效
     */
    private void playVolume() {
        if (MainActivity.volumeSwitch) {
            mSoundPool.play(soundID, 0.2f, 0.2f, 0, 1, 1);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (whichGame == 4) {
            saveLastNumbers(NumberItem.getInstanceFour().getNumbers()); //记录最后一步的情况。
            saveBestScore(NumberItem.getInstanceFour().getBestScore()); //记录4X4模式最高分。
        } else if (whichGame == 5) {
            saveLastNumbers(NumberItem.getInstanceFive().getNumbers()); //记录最后一步的情况。
            saveBestScore(NumberItem.getInstanceFive().getBestScore()); //记录5X5模式最高分。
        } else if (whichGame == 6) {
            saveLastNumbers(NumberItem.getInstanceSix().getNumbers()); //记录最后一步的情况。
            saveBestScore(NumberItem.getInstanceSix().getBestScore()); //记录6X6模式最高分。
        }
        saveLastMode(whichGame);    //记录最后的游戏模式
    }

    /**
     * 保存最后一步的游戏模式
     *
     * @param which 游戏模式标志
     */
    private void saveLastMode(int which) {
        mPreferences = getApplicationContext().getSharedPreferences("SAVE_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt("LAST_MODE", which); //保存进SharedPreferences。
        editor.apply();
    }

    /**
     * 保存最后一步的情况，二维数组转换成JSON，用SharedPreferences保存JSON产生的String，持久化存储。
     *
     * @param n 最后一步时数字的二维数组。
     */
    private void saveLastNumbers(int[][] n) {
        mPreferences = getApplicationContext().getSharedPreferences("SAVE_DATA", MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < whichGame; i++) {
            for (int j = 0; j < whichGame; j++) {
                jsonArray.put(n[i][j]); //逐一存入JSON字段。
            }
        }

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("LAST_NUMBERS", jsonArray.toString()); //保存进SharedPreferences。
        editor.apply();
    }

    /**
     * 保存此局目前的分数，因为在启动游戏时会获取以前的最高分，
     * 并预先存入NumberItem类的BestScore里，所以BestScore里永远是最目前高分。
     *
     * @param score 目前的最高分。
     */
    private void saveBestScore(int score) {
        mPreferences = getApplicationContext().getSharedPreferences("SAVE_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        if (whichGame == 4) {
            editor.putInt("BEST_SCORE_FOR_FOUR", score);
            editor.apply();
            UpLoadBestScore upLoadBestScore = new UpLoadBestScore(whichGame, mUser.getName(), score);
            upLoadBestScore.execute();
        } else if (whichGame == 5) {
            editor.putInt("BEST_SCORE_FOR_FIVE", score);
            editor.apply();
            UpLoadBestScore upLoadBestScore = new UpLoadBestScore(whichGame, mUser.getName(), score);
            upLoadBestScore.execute();
        } else if (whichGame == 6) {
            editor.putInt("BEST_SCORE_FOR_SIX", score);
            editor.apply();
            UpLoadBestScore upLoadBestScore = new UpLoadBestScore(whichGame, mUser.getName(), score);
            upLoadBestScore.execute();
        }
    }

    /**
     * 获取最高分的记录。
     *
     * @return 目前所有游戏的最高分数。
     */
    private int getBestScore(int which) {
        mPreferences = getApplicationContext().getSharedPreferences("SAVE_DATA", MODE_PRIVATE);
        try {
            if (which == 4) {
                return mPreferences.getInt("BEST_SCORE_FOR_FOUR", 0);
            } else if (which == 5) {
                return mPreferences.getInt("BEST_SCORE_FOR_FIVE", 0);
            } else if (which == 6) {
                return mPreferences.getInt("BEST_SCORE_FOR_SIX", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    /**
     * 上传最高分的线程类
     */
    private class UpLoadBestScore extends AsyncTask<Void, Void, String> {
        private int whichGame;
        private String name;
        private int score;

        UpLoadBestScore(int whichGame, String name, int score) {
            this.whichGame = whichGame;
            this.name = name;
            this.score = score;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            try {
                String compeletedURL = HttpUtils.getURLWithParams(whichGame, name, score);
                result = HttpUtils.sendHttpRequest(compeletedURL, new HttpUtils.HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Log.i("savescore", "成功:  " + response);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("savescore", "失败");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}