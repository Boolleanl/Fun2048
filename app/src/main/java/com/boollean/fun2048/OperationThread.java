package com.boollean.fun2048;

public class OperationThread extends Thread {

    private GameFourFragment gameFourFragment = GameFourFragment.getInstance();
    private GameFiveFragment gameFiveFragment = GameFiveFragment.getInstance();

    private int whichGame;

    private int action = 0;

    public OperationThread(int which) {
        this.whichGame = which;
    }

    public void setAction(int i) {
        action = i;
    }

    @Override
    public void run() {
        switch (whichGame){
            case 4:{
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
        }
    }
}
