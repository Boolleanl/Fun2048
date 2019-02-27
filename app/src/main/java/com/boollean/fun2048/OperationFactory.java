package com.boollean.fun2048;

/**
 * 数据加工的工厂类，包含一些运算的算法。
 * Created by Boollean on 2018/3/3.
 */

public class OperationFactory {

    private static final String TAG = "OperationFactory";
    private static NumberItem mNumberItem;
    private static int[][] numbers;
    private static int num;

    /**
     * “新游戏”启动时调用的构造函数。
     */
    public OperationFactory() {
    }

    public static void newGame() {
        mNumberItem = NumberItem.getInstance();
        numbers = createNumbers();
        mNumberItem.setNumbers(numbers);
        nowScore(mNumberItem.getNumbers());
    }

    /**
     * “新游戏”开始时，为数组填入起始数字。
     *
     * @return 起始二维数组。
     */
    public static int[][] createNumbers() {
        int[][] ns = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ns[i][j] = createRandomNumber();
            }
        }
        return ns;
    }

    /**
     * 当前的分数，为二维数组各元素相加的总和。
     *
     * @param ns 当前一步的二维数组。
     * @return 当前分数。
     */
    public static int nowScore(int[][] ns) {
        int s = 0;
        if (null != ns) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    s += ns[i][j];
                }
            }
        }
        mNumberItem.setScore(s);    //重新填入当前分数。
        checkAndSetBestScore(s);    //每次检查是不是比最高分高。
        return s;
    }

    /**
     * 生成随机数，0、2、4三个数。在起始生成数组时，或者滑动后有空位的情况下调用。
     *
     * @return 0、2或者4。
     */
    public static int createRandomNumber() {
        int i = (int) (Math.random() * 3);
        if (i == 0) {
            num = 0;
        } else if (i == 1) {
            num = 2;
        } else if (i == 2) {
            num = 4;
        }
        return num;
    }

    /**
     * 检查是否比最高分还高，若是，则设置为新的最高分。
     *
     * @param score 当前分数。
     */
    public static void checkAndSetBestScore(int score) {
        if (score > mNumberItem.getBestScore()) {
            mNumberItem.setBestScore(score);
        }
    }

    /**
     * “继续游戏”调用的构造函数。
     *
     * @param n 上次最后一步的二维数组。
     */
    public static void continueGame(int[][] n) {
        mNumberItem = NumberItem.getInstance();
        mNumberItem.setNumbers(n);
    }

    /**
     * 向上滑动时调用的方法。
     */
    public static void actionUp() {
        int[][] numbers = mNumberItem.getNumbers();
        int[][] ns = calculate(numbers, numbers);

        mNumberItem.setNumbers(ns);
    }

    /**
     * 运算算法，分别根据穷举的情况来选择是否相加，是否移动，是否生成新的2或4。
     *
     * @param numbers    需要运算的二维数组。
     * @param preNumbers 与前一个参数一样。
     * @return 运算后的二维数组。
     */
    private static int[][] calculate(int[][] numbers, int[][] preNumbers) {
        if (preNumbers != null) {
            for (int i = 0; i < 4; i++) {

                if (preNumbers[0][i] == preNumbers[1][i] && preNumbers[2][i] == preNumbers[3][i]) {
                    if (preNumbers[0][i] == 0) {
                        numbers[0][i] = doubleValue(preNumbers[2][i]);
                        numbers[1][i] = createRandomNumber();
                        numbers[2][i] = 0;
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[2][i] != 0) {
                        numbers[0][i] = doubleValue(preNumbers[0][i]);
                        numbers[1][i] = doubleValue(preNumbers[2][i]);
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[2][i] == 0) {
                        numbers[0][i] = doubleValue(preNumbers[0][i]);
                        numbers[1][i] = createRandomNumber();
                        numbers[2][i] = 0;
                        numbers[3][i] = 0;
                    }
                } else if (preNumbers[0][i] == preNumbers[1][i] && preNumbers[2][i] != preNumbers[3][i]) {
                    if (preNumbers[0][i] == 0 && preNumbers[2][i] == 0) {
                        numbers[0][i] = preNumbers[3][i];
                        numbers[1][i] = createRandomNumber();
                        numbers[2][i] = 0;
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0 && preNumbers[3][i] == 0) {
                        numbers[0][i] = preNumbers[2][i];
                        numbers[1][i] = createRandomNumber();
                        numbers[2][i] = 0;
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0 && preNumbers[3][i] != 0) {
                        numbers[0][i] = preNumbers[2][i];
                        numbers[1][i] = preNumbers[3][i];
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[2][i] == 0) {
                        numbers[0][i] = doubleValue(preNumbers[0][i]);
                        numbers[1][i] = preNumbers[3][i];
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[2][i] != 0 && preNumbers[3][i] == 0) {
                        numbers[0][i] = doubleValue(preNumbers[0][i]);
                        numbers[1][i] = preNumbers[2][i];
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else {
                        numbers[0][i] = doubleValue(preNumbers[0][i]);
                        numbers[1][i] = preNumbers[2][i];
                        numbers[2][i] = preNumbers[3][i];
                        numbers[3][i] = createRandomNumber();
                    }
                } else if (preNumbers[0][i] != preNumbers[1][i] && preNumbers[1][i] == preNumbers[2][i]) {
                    if (preNumbers[0][i] == 0 && preNumbers[3][i] == 0) {
                        numbers[0][i] = doubleValue(preNumbers[1][i]);
                        numbers[1][i] = createRandomNumber();
                        numbers[2][i] = 0;
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[3][i] != 0) {
                        numbers[0][i] = doubleValue(preNumbers[1][i]);
                        numbers[1][i] = preNumbers[3][i];
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] == 0 && preNumbers[2][i] == 0 && preNumbers[3][i] == 0) {
                        numbers[1][i] = createRandomNumber();
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] == 0 && preNumbers[3][i] == 0) {
                        numbers[1][i] = createRandomNumber();
                        numbers[2][i] = 0;
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] == 0 && preNumbers[0][i] == preNumbers[3][i]) {
                        numbers[0][i] = doubleValue(preNumbers[0][i]);
                        numbers[1][i] = createRandomNumber();
                        numbers[2][i] = 0;
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] == 0 && preNumbers[0][i] != preNumbers[3][i]) {
                        numbers[1][i] = preNumbers[3][i];
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] != 0 && preNumbers[3][i] == 0) {
                        numbers[1][i] = doubleValue(preNumbers[1][i]);
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else {
                        numbers[1][i] = doubleValue(preNumbers[1][i]);
                        numbers[2][i] = preNumbers[3][i];
                        numbers[3][i] = createRandomNumber();
                    }
                } else if (preNumbers[0][i] != preNumbers[1][i] && preNumbers[1][i] != preNumbers[2][i]
                        && preNumbers[2][i] == preNumbers[3][i]) {
                    if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0) {
                        numbers[0][i] = preNumbers[1][i];
                        numbers[1][i] = doubleValue(preNumbers[2][i]);
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] == 0) {
                        numbers[0][i] = preNumbers[1][i];
                        numbers[1][i] = createRandomNumber();
                        numbers[2][i] = 0;
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] != 0 && preNumbers[2][i] == 0) {
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] != 0 && preNumbers[2][i] != 0) {
                        numbers[2][i] = doubleValue(preNumbers[2][i]);
                        numbers[3][i] = createRandomNumber();
                    } else if (preNumbers[0][i] == preNumbers[2][i]) {
                        numbers[0][i] = doubleValue(preNumbers[0][i]);
                        numbers[1][i] = preNumbers[3][i];
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else {
                        numbers[1][i] = doubleValue(preNumbers[2][i]);
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    }
                } else if (preNumbers[0][i] != preNumbers[1][i] && preNumbers[1][i] != preNumbers[2][i]
                        && preNumbers[2][i] != preNumbers[3][i]) {
                    if (preNumbers[0][i] == 0 && preNumbers[2][i] == 0 && preNumbers[1][i] == preNumbers[3][i]) {
                        numbers[0][i] = doubleValue(preNumbers[1][i]);
                        numbers[1][i] = createRandomNumber();
                        numbers[2][i] = 0;
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] == 0 && preNumbers[1][i] != preNumbers[3][i]) {
                        numbers[0][i] = preNumbers[1][i];
                        numbers[1][i] = preNumbers[3][i];
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0 && preNumbers[3][i] == 0) {
                        numbers[0][i] = preNumbers[1][i];
                        numbers[1][i] = preNumbers[2][i];
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0 && preNumbers[3][i] != 0) {
                        numbers[0][i] = preNumbers[1][i];
                        numbers[1][i] = preNumbers[2][i];
                        numbers[2][i] = preNumbers[3][i];
                        numbers[3][i] = createRandomNumber();
                    } else if (preNumbers[1][i] == 0 && preNumbers[0][i] == preNumbers[2][i] && preNumbers[3][i] == 0) {
                        numbers[0][i] = doubleValue(preNumbers[0][i]);
                        numbers[1][i] = createRandomNumber();
                        numbers[2][i] = 0;
                        numbers[3][i] = 0;
                    } else if (preNumbers[1][i] == 0 && preNumbers[0][i] == preNumbers[2][i] && preNumbers[3][i] != 0) {
                        numbers[0][i] = doubleValue(preNumbers[0][i]);
                        numbers[1][i] = preNumbers[3][i];
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else if (preNumbers[1][i] == 0 && preNumbers[0][i] != preNumbers[2][i]) {
                        numbers[1][i] = preNumbers[2][i];
                        numbers[2][i] = preNumbers[3][i];
                        numbers[3][i] = createRandomNumber();
                    } else if (preNumbers[2][i] == 0 && preNumbers[1][i] == preNumbers[3][i]) {
                        numbers[1][i] = doubleValue(preNumbers[1][i]);
                        numbers[2][i] = createRandomNumber();
                        numbers[3][i] = 0;
                    } else if (preNumbers[2][i] == 0 && preNumbers[1][i] != preNumbers[3][i]) {
                        numbers[2][i] = preNumbers[3][i];
                        numbers[3][i] = createRandomNumber();
                    } else if (preNumbers[1][i] == 0 && preNumbers[3][i] == 0 && preNumbers[0][i] != preNumbers[2][i]) {
                        numbers[1][i] = preNumbers[2][i];
                        numbers[2][i] = createRandomNumber();
                    } else if (preNumbers[0][i] == 0 && preNumbers[3][i] == 0) {
                        numbers[0][i] = preNumbers[1][i];
                        numbers[1][i] = preNumbers[2][i];
                        numbers[2][i] = createRandomNumber();
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] != 0 && preNumbers[2][i] != 0 && preNumbers[3][i] == 0) {
                        numbers[3][i] = createRandomNumber();
                    } else {
                    }
                }
            }
        }

        nowScore(numbers);

        return numbers;
    }

    /**
     * 相同的数字，在特定条件下加起来
     *
     * @param number 需要乘2的数字。
     * @return 运算后的数字。
     */
    private static int doubleValue(int number) {
        return number * 2;
    }

    /**
     * 向下滑动时调用的方法。
     */
    public static void actionDown() {
        int[][] preNumbers = new int[4][4];
        int[][] ns = mNumberItem.getNumbers();
        preNumbers = transformDown(preNumbers, ns); //矩阵转置。

        int[][] numbers = preNumbers;
        ns = calculate(preNumbers, numbers);    //判定运算的算法。
        numbers = new int[4][4];
        numbers = transformDown(numbers, ns);   //运算结束后转置回来。

        mNumberItem.setNumbers(numbers);
    }

    /**
     * 从下转化成上。
     *
     * @param preNumbers 需要转置的二位数组。
     * @param n          需要转置的二维数组，其实与preNumbers一样。
     *                   但有时不用改变数字，所以保持不变能避免一些运算。
     * @return 转置后的数组。
     */
    private static int[][] transformDown(int[][] preNumbers, int[][] n) {
        for (int i = 0; i < 4; i++) {
            preNumbers[0][i] = n[3][i];
            preNumbers[1][i] = n[2][i];
            preNumbers[2][i] = n[1][i];
            preNumbers[3][i] = n[0][i];
        }
        return preNumbers;
    }

    /**
     * 想做滑动时调用。
     */
    public static void actionLeft() {

        int[][] preNumbers = new int[4][4];
        int[][] ns = mNumberItem.getNumbers();
        preNumbers = transformToLeft(preNumbers, ns);   //矩阵转置。

        int[][] numbers = preNumbers;
        ns = calculate(preNumbers, numbers);    //运算。
        numbers = new int[4][4];
        numbers = transformFromLeft(numbers, ns);   //转换回来。

        mNumberItem.setNumbers(numbers);
    }

    /**
     * 从左转化成上。
     *
     * @param preNumbers 需要转置的二位数组。
     * @param n          需要转置的二维数组，其实与preNumbers一样。
     *                   但有时不用改变数字，所以保持不变能避免一些运算。
     * @return 转置后的数组。
     */
    private static int[][] transformToLeft(int[][] preNumbers, int[][] n) {
        for (int i = 0; i < 4; i++) {
            preNumbers[i][0] = n[3][i];
            preNumbers[i][1] = n[2][i];
            preNumbers[i][2] = n[1][i];
            preNumbers[i][3] = n[0][i];
        }
        return preNumbers;
    }

    /**
     * 从上转化成左。进过运算后，转置回来以便装入TextView显示。
     *
     * @param preNumbers 需要转置的二位数组。
     * @param n          需要转置的二维数组，其实与preNumbers一样。
     *                   但有时不用改变数字，所以保持不变能避免一些运算。
     * @return 转置后的数组。
     */
    private static int[][] transformFromLeft(int[][] preNumbers, int[][] n) {
        for (int i = 0; i < 4; i++) {
            preNumbers[0][i] = n[i][3];
            preNumbers[1][i] = n[i][2];
            preNumbers[2][i] = n[i][1];
            preNumbers[3][i] = n[i][0];
        }
        return preNumbers;
    }

    /**
     * 向下滑动时调用。
     */
    public static void actionRight() {
        int[][] preNumbers = new int[4][4];
        int[][] ns = mNumberItem.getNumbers();
        preNumbers = transformToRight(preNumbers, ns);  //矩阵转置

        int[][] numbers = preNumbers;
        ns = calculate(numbers, preNumbers);    //运算
        numbers = new int[4][4];
        numbers = transformFromRight(numbers, ns);  //转置回来。

        mNumberItem.setNumbers(numbers);
    }

    /**
     * 从右转化成上。
     *
     * @param preNumbers 需要转置的二位数组。
     * @param n          需要转置的二维数组，其实与preNumbers一样。
     *                   但有时不用改变数字，所以保持不变能避免一些运算。
     * @return 转置后的数组。
     */
    private static int[][] transformToRight(int[][] preNumbers, int[][] n) {
        for (int i = 0; i < 4; i++) {
            preNumbers[0][i] = n[i][3];
            preNumbers[1][i] = n[i][2];
            preNumbers[2][i] = n[i][1];
            preNumbers[3][i] = n[i][0];
        }
        return preNumbers;
    }

    /**
     * 从上转化成右。进过运算后，转置回来以便装入TextView显示。
     *
     * @param preNumbers 需要转置的二位数组。
     * @param n          需要转置的二维数组，其实与preNumbers一样。
     *                   但有时不用改变数字，所以保持不变能避免一些运算。
     * @return 转置后的数组。
     */
    private static int[][] transformFromRight(int[][] preNumbers, int[][] n) {
        for (int i = 0; i < 4; i++) {
            preNumbers[i][0] = n[3][i];
            preNumbers[i][1] = n[2][i];
            preNumbers[i][2] = n[1][i];
            preNumbers[i][3] = n[0][i];
        }
        return preNumbers;
    }
}