package com.boollean.fun2048;

public class OperationThread extends Thread {

    private GameFragment gameFragment = GameFragment.getInstance();

    private int action = 0;

    public void setAction(int i) {
        action = i;
    }

    @Override
    public void run() {
        switch (action) {
            case 1:
                OperationFactory.actionUp();   //上滑调用。
                gameFragment.refreshView();
                break;
            case 2:
                OperationFactory.actionDown();  //下滑调用。
                gameFragment.refreshView();
                break;
            case 3:
                OperationFactory.actionLeft();  //左滑调用。
                gameFragment.refreshView();
                break;
            case 4:
                OperationFactory.actionRight(); //右滑调用。
                gameFragment.refreshView();
                break;
        }
    }
}
