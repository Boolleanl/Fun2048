package com.boollean.fun2048;

import android.os.AsyncTask;

/**
 * 自己的后台线程类。
 * Created by Boollean on 2018/3/10.
 */

public class OperationTask extends AsyncTask<Integer, Integer, Void> {

    /**
     * “新游戏”时调用的函数，启动一个新的线程。
     */
    public OperationTask() {
        OperationFactory.newGame();
    }

    /**
     * “继续游戏”时调用，启动一个新的线程。
     *
     * @param n 上次游戏的最后一步时的数组。
     */
    public OperationTask(int[][] n) {
        OperationFactory.continueGame(n);
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        switch (integers[0]) {
            case 1:
                OperationFactory.actionUp();   //上滑调用。
                break;
            case 2:
                OperationFactory.actionDown();  //下滑调用。
                break;
            case 3:
                OperationFactory.actionLeft();  //左滑调用。
                break;
            case 4:
                OperationFactory.actionRight(); //下滑调用。
                break;
        }
        return null;
    }
}
