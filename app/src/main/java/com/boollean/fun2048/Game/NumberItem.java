package com.boollean.fun2048.Game;

/**
 * 数据类。
 * Created by Boollean on 2019/2/28.
 */
public class NumberItem {
    private static volatile NumberItem instanceFour = null;
    private static volatile NumberItem instanceFive = null;
    private static volatile NumberItem instanceSix = null;
    private int score;  //分数。
    private int BEST_SCORE;     //最高分。
    private int[][] numbers;    //用于操作的二维数组。

    private NumberItem(int i) {
        numbers = new int[i][i];
    }

    public static NumberItem getInstanceFour() {
        synchronized (NumberItem.class) {
            if (instanceFour == null) {
                instanceFour = new NumberItem(4);
            }
        }
        return instanceFour;
    }

    public static NumberItem getInstanceFive() {
        synchronized (NumberItem.class) {
            if (instanceFive == null) {
                instanceFive = new NumberItem(5);
            }
        }
        return instanceFive;
    }

    public static NumberItem getInstanceSix() {
        synchronized (NumberItem.class) {
            if (instanceSix == null) {
                instanceSix = new NumberItem(6);
            }
        }
        return instanceSix;
    }

    public int[][] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[][] numbers) {
        this.numbers = numbers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBestScore() {
        return BEST_SCORE;
    }

    public void setBestScore(int bestScore) {
        BEST_SCORE = bestScore;
    }
}
