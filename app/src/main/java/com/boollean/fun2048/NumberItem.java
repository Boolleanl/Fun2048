package com.boollean.fun2048;

public class NumberItem {
    private static volatile NumberItem instance = null;
    private int score;  //分数。
    private int BEST_SCORE;     //最高分。
    private int[][] numbers;    //用于操作的二维数组。

    private NumberItem() {
        numbers = new int[4][4];
    }

    public static NumberItem getInstance() {
        synchronized (NumberItem.class) {
            if (instance == null) {
                instance = new NumberItem();
            }
        }
        return instance;
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
