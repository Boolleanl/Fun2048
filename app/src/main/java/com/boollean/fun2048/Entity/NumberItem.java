package com.boollean.fun2048.Entity;

/**
 * 运算和分数相关的数据类。
 *
 * @author Boollean
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

    /**
     * 获取唯一的4*4模式游戏数据对象
     *
     * @return 4*4模式的游戏数据对象
     */
    public static NumberItem getInstanceFour() {
        synchronized (NumberItem.class) {
            if (instanceFour == null) {
                instanceFour = new NumberItem(4);
            }
        }
        return instanceFour;
    }

    /**
     * 获取唯一的5*5模式游戏数据对象
     *
     * @return 5*5模式的游戏数据对象
     */
    public static NumberItem getInstanceFive() {
        synchronized (NumberItem.class) {
            if (instanceFive == null) {
                instanceFive = new NumberItem(5);
            }
        }
        return instanceFive;
    }

    /**
     * 获取唯一的6*6模式游戏数据对象
     *
     * @return 6*6模式的游戏数据对象
     */
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
