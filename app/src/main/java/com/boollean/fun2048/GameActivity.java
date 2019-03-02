package com.boollean.fun2048;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import org.json.JSONArray;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * 游戏界面的Activity。
 * Created by Boollean on 2018/2/27.
 */

public class GameActivity extends AppCompatActivity {

    private static int whichGame;
    private static final float FLIP_DISTANCE = 48;      //滑动判定距离
    private static final String TAG = "GameActivity";

    private GameFourFragment mGameFourFragment;
    private GameFiveFragment mGameFiveFragment;
    private GestureDetector mDetector;

    private static Handler mHandler;
    private static OperationThread operationThread;
    private SharedPreferences mPreferences;

    /**
     * “新游戏”启动时调用的方法。
     *
     * @param context
     * @return 游戏界面的Activity，重新初始化NumberItem。
     */

    public static Intent newIntent(Context context,int which) {
        whichGame = which;
        mHandler = new Handler();
        operationThread = new OperationThread(which);
        if(which == 4){
            OperationFactory.newGameFour();
        }else if(which == 5){
            OperationFactory.newGameFive();
        }
        Intent i = new Intent(context, GameActivity.class);
        return i;
    }

    /**
     * @param context
     * @param n       从SharedPreferences处获取的上次游戏最后一步的记录。
     * @return 游戏界面的Activity，紧接上次游戏的进程。
     */
    public static Intent newIntent(Context context, int[][] n) {
        mHandler = new Handler();
        operationThread = new OperationThread(whichGame);
        OperationFactory.continueGame(n);
        Intent i = new Intent(context, GameActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        FragmentManager fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.fragment_container);

        if(whichGame == 4){
            mGameFourFragment = GameFourFragment.getInstance();
            fm.beginTransaction().add(R.id.fragment_container, mGameFourFragment).commit();
        }else if(whichGame == 5){
            mGameFiveFragment = GameFiveFragment.getInstance();
            fm.beginTransaction().add(R.id.fragment_container, mGameFiveFragment).commit();
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
                    return true;
                }
                if (e2.getY() - e1.getY() > e1.getX() - e2.getX()
                        && e2.getY() - e1.getY() > e2.getX() - e1.getX()
                        && e2.getY() - e1.getY() > FLIP_DISTANCE) {
                    //Down，向下滑动。
                    operationThread.setAction(2);
                    mHandler.post(operationThread);
                    return true;
                }
                if (e1.getX() - e2.getX() > e1.getY() - e2.getY()
                        && e1.getX() - e2.getX() > e2.getY() - e1.getY()
                        && e1.getX() - e2.getX() > FLIP_DISTANCE) {
                    //Left，向左滑动。
                    operationThread.setAction(3);
                    mHandler.post(operationThread);
                    return true;
                }
                if (e2.getX() - e1.getX() > e1.getY() - e2.getY()
                        && e2.getX() - e1.getX() > e2.getY() - e1.getY()
                        && e2.getX() - e1.getX() > FLIP_DISTANCE) {
                    //Right，向右滑动。
                    operationThread.setAction(4);
                    mHandler.post(operationThread);
                    return true;
                }

                Log.d("TAG", e2.getX() + " " + e2.getY());
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveLastNumbers(NumberItem.getInstanceFour().getNumbers()); //记录最后一步的情况。
        saveBestScore(NumberItem.getInstanceFour().getBestScore()); //记录最高分。
    }

    /**
     * 保存最后一步的情况，二维数组转换成JSON，用SharedPreferences保存JSON产生的String，持久化存储。
     *
     * @param n 最后一步时数字的二维数组。
     */
    private void saveLastNumbers(int[][] n) {
        mPreferences = getApplicationContext().getSharedPreferences("SAVE_LAST_NUMBERS", MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                jsonArray.put(n[i][j]); //逐一存入JSON字段。
            }
        }

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("LAST_NUMBERS", jsonArray.toString()); //保存进SharedPreferences。
        editor.commit();
        Log.i(TAG, jsonArray.toString());
    }

//    public void showGameOverDialog() {
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setMessage("GAME OVER")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        System.exit(0);
//                    }
//                })
//                .create();
//        dialog.show();
//    }

    /**
     * 保存此局目前的分数，因为在启动游戏时会获取以前的最高分，
     * 并预先存入NumberItem类的BestScore里，所以BestScore里永远是最目前高分。
     *
     * @param s 目前的最高分。
     */
    private void saveBestScore(int s) {
        mPreferences = getApplicationContext().getSharedPreferences("SAVE_BEST_SCORE", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt("BEST_SCORE", s);
        editor.commit();

        Log.i(TAG, String.valueOf(s));
    }

    /**
     * 每次滑动后，调用刷新函数。
     */
    private void refreshView() {
        mGameFourFragment.refreshView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }
}