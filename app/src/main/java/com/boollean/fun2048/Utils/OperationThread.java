package com.boollean.fun2048.Utils;

import com.boollean.fun2048.Game.GameFiveFragment;
import com.boollean.fun2048.Game.GameFourFragment;
import com.boollean.fun2048.Game.GameSixFragment;

/**
 * 运算线程类。
 *
 * @author Boollean
 */
public class OperationThread extends Thread {

    private GameFourFragment gameFourFragment = GameFourFragment.getInstance();
    private GameFiveFragment gameFiveFragment = GameFiveFragment.getInstance();
    private GameSixFragment gameSixFragment = GameSixFragment.getInstance();

    private int whichGame;  //游戏模式标志

    private int action = 0; //滑动方向标志，初始化为0。  上1，下2， 左3， 右4。

    public OperationThread(int which) {
        this.whichGame = which;
    }

    public void setAction(int i) {
        action = i;
    }

    @Override
    public void run() {
        switch (whichGame) {//判断是哪个游戏模式
            case 4: {
                switch (action) {
                    case 1:
                        OperationFactory.actionUp(whichGame);   //上滑调用。
                        gameFourFragment.refreshView();
                        break;
                    case 2:
                        OperationFactory.actionDown(whichGame);  //下滑调用。
                        gameFourFragment.refreshView();
                        break;
                    case 3:
                        OperationFactory.actionLeft(whichGame);  //左滑调用。
                        gameFourFragment.refreshView();
                        break;
                    case 4:
                        OperationFactory.actionRight(whichGame); //右滑调用。
                        gameFourFragment.refreshView();
                        break;
                }
                break;
            }

            case 5: {
                switch (action) {
                    case 1:
                        OperationFactory.actionUp(whichGame);   //上滑调用。
                        gameFiveFragment.refreshView();
                        break;
                    case 2:
                        OperationFactory.actionDown(whichGame);  //下滑调用。
                        gameFiveFragment.refreshView();
                        break;
                    case 3:
                        OperationFactory.actionLeft(whichGame);  //左滑调用。
                        gameFiveFragment.refreshView();
                        break;
                    case 4:
                        OperationFactory.actionRight(whichGame); //右滑调用。
                        gameFiveFragment.refreshView();
                        break;
                }
                break;
            }

            case 6: {
                switch (action) {
                    case 1:
                        OperationFactory.actionUp(whichGame);   //上滑调用。
                        gameSixFragment.refreshView();
                        break;
                    case 2:
                        OperationFactory.actionDown(whichGame);  //下滑调用。
                        gameSixFragment.refreshView();
                        break;
                    case 3:
                        OperationFactory.actionLeft(whichGame);  //左滑调用。
                        gameSixFragment.refreshView();
                        break;
                    case 4:
                        OperationFactory.actionRight(whichGame); //右滑调用。
                        gameSixFragment.refreshView();
                        break;
                }
                break;
            }
        }
    }
}
