package com.boollean.fun2048.Game;

/**
 * 数据加工的工厂类，包含一些运算的算法。
 * Created by Boollean on 2019/2/27.
 */
public class OperationFactory {

    private static final String TAG = "OperationFactory";
    private static NumberItem mNumberItem;
    private static int[][] numbers;

    /**
     * 新游戏4X4，初始化各个参数
     */
    public static void newGameFour(int bestScore) {
        mNumberItem = NumberItem.getInstanceFour();
        mNumberItem.setBestScore(bestScore);
        numbers = createNumbers(4);
        mNumberItem.setNumbers(numbers);
        nowScore(mNumberItem.getNumbers(), 4);
    }

    /**
     * 新游戏5X5，初始化各个参数
     */
    public static void newGameFive(int bestScore) {
        mNumberItem = NumberItem.getInstanceFive();
        mNumberItem.setBestScore(bestScore);
        numbers = createNumbers(5);
        mNumberItem.setNumbers(numbers);
        nowScore(mNumberItem.getNumbers(), 5);
    }

    /**
     * 新游戏6X6，初始化各个参数
     */
    public static void newGameSix(int bestScore) {
        mNumberItem = NumberItem.getInstanceSix();
        mNumberItem.setBestScore(bestScore);
        numbers = createNumbers(6);
        mNumberItem.setNumbers(numbers);
        nowScore(mNumberItem.getNumbers(), 6);
    }

    /**
     * “新游戏”开始时，为数组填入起始数字。
     *
     * @param which 游戏模式标志
     * @return 起始二维数组。
     */
    public static int[][] createNumbers(int which) {
        int[][] ns = new int[which][which];
        for (int i = 0; i < which; i++) {
            for (int j = 0; j < which; j++) {
                ns[i][j] = createInitialNumber(which);
            }
        }
        return ns;
    }

    /**
     * 当前的分数，为二维数组各元素相加的总和。
     *
     * @param ns    当前一步的二维数组。
     * @param which 游戏模式标志，决定生成的二位数组
     * @return 当前分数。
     */
    public static int nowScore(int[][] ns, int which) {
        int s = 0;
        if (null != ns) {
            for (int i = 0; i < which; i++) {
                for (int j = 0; j < which; j++) {
                    s += ns[i][j];
                }
            }
        }
        mNumberItem.setScore(s);    //重新填入当前分数。
        checkAndSetBestScore(s);    //每次检查是不是比最高分高。
        return s;
    }

    /**
     * 创建最初的几个随机数字
     *
     * @param which 游戏模式标志，该参数决定不同模式下的刷新率
     * @return
     */
    private static int createInitialNumber(int which) {
        int i = (int) (Math.random() * which * 2);
        int number = 0;
        if (i == which - 2) {
            number = 2;
        } else if (i == which - 1) {
            number = 4;
        }
        return number;
    }

    /**
     * 生成随机数，0、2、4三个数。在滑动后没有相加并且有空位的情况下调用。
     *
     * @param which 游戏模式标志，该参数决定不同模式下的刷新率
     * @return 0、2或者4。
     */
    public static int createRandomNumber(int which) {
        int i = (int) (Math.random() * which);
        if (which == 5) {
            i = (int) (Math.random() * which * 4);
        } else if (which == 6) {
            i = (int) (Math.random() * which * 4);
        }
        int number = 0;
        if (i == which - 2) {
            number = 2;
        } else if (i == which - 1) {
            number = 4;
        }
        return number;
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
     * @param which 游戏模式
     * @param n     上次最后一步的二维数组。
     */
    public static void continueGame(int which, int[][] n) {
        if (which == 4) {
            mNumberItem = NumberItem.getInstanceFour();
        } else if (which == 5) {
            mNumberItem = NumberItem.getInstanceFive();
        } else if (which == 6) {
            mNumberItem = NumberItem.getInstanceSix();
        }
        mNumberItem.setNumbers(n);
        nowScore(mNumberItem.getNumbers(), which);
    }

    /**
     * 向上滑动时调用的方法。
     *
     * @param which 游戏模式标志，该参数决定运算的算法
     */
    public static void actionUp(int which) {
        int[][] numbers = mNumberItem.getNumbers();
        int[][] ns = null;
        if (which == 4) {
            ns = calculateFourNumbers(numbers, numbers);
        } else if (which == 5) {
            ns = wipeFive(numbers);
            ns = calculateFiveNumbers(ns);
        } else if (which == 6) {
            ns = wipeSix(numbers);
            ns = calculateSixNumbers(ns);
        }
        mNumberItem.setNumbers(ns);
    }

    /**
     * 5X5模式下的去除空位的算法，将所有不为0的数往一个方向依序移动
     *
     * @param nums 需要移动的二维数组
     * @return 移动后的二维数组
     */
    private static int[][] wipeFive(int[][] nums) {
        int[][] r = new int[5][5];
        if (null != nums) {
            for (int i = 0; i < 5; i++) {
                int k = 0;
                if (nums[0][i] == 0 && nums[1][i] == 0 && nums[2][i] == 0 && nums[3][i] == 0 && nums[4][i] == 0) {
                    continue;
                }
                for (int j = 0; j < 5; j++) {
                    if (nums[j][i] == 0) {
                        continue;
                    } else {
                        r[k][i] = nums[j][i];
                        k++;
                    }
                }
            }
        }
        return r;
    }

    /**
     * 5X5模式下的去除空位的算法，将所有不为0的数往一个方向依序移动
     *
     * @param nums 需要移动的二维数组
     * @return 移动后的二维数组
     */
    private static int[][] wipeSix(int[][] nums) {
        int[][] r = new int[6][6];
        if (null != nums) {
            for (int i = 0; i < 6; i++) {
                int k = 0;
                if (nums[0][i] == 0 && nums[1][i] == 0 && nums[2][i] == 0 && nums[3][i] == 0 && nums[4][i] == 0 && nums[5][i] == 0) {
                    continue;
                }
                for (int j = 0; j < 6; j++) {
                    if (nums[j][i] == 0) {
                        continue;
                    } else {
                        r[k][i] = nums[j][i];
                        k++;
                    }
                }
            }
        }
        return r;
    }

    /**
     * 5X5模式下的运算算法，若两个数相邻并相等则相加。
     *
     * @param nums 需要运算的二维数组
     * @return 运算完成后的二维数组
     */
    private static int[][] calculateSixNumbers(int[][] nums) {
        boolean hasDouble;  //相加标志，某一排只要有相加就为true
        int[][] result = new int[6][6];
        if (null != nums) {
            for (int i = 0; i < 6; i++) {
                hasDouble = false;  //每一排初开始运算时始化为false
                int k = 0;
                if (nums[0][i] == 0 && nums[1][i] == 0 && nums[2][i] == 0 && nums[3][i] == 0 && nums[4][i] == 0 && nums[5][i] == 0) {
                    result[0][i] = createRandomNumber(6); //此排全为0则新生成一个2或4
                }
                for (int j = 0; j < 6; j++) {
                    if (nums[j][i] != 0) {
                        if (j <= 4 && nums[j][i] == nums[j + 1][i]) {
                            result[k][i] = nums[j][i] * 2;
                            hasDouble = true;
                            j++;
                            k++;
                        } else {
                            result[k][i] = nums[j][i];
                            k++;
                        }
                    }
                }
                if (!hasDouble) {//如果一排没有相加
                    if (nums[0][i] != nums[1][i] && nums[1][i] != nums[2][i] && nums[2][i] != nums[3][i]
                            && nums[3][i] != nums[4][i] && nums[4][i] != nums[5][i] && nums[5][i] != 0) {
                        continue;//一排全不为零且相邻两个数均不相等，跳出此排的此次循环
                    }
                    for (int m = 0; m < 6; m++) {
                        if (result[m][i] == 0) {//此排第一个为零的位置生成一个新的2或4
                            result[m][i] = createRandomNumber(6);
                            break;
                        }
                    }
                }
            }
        }

        nowScore(result, 6);    //刷新分数
        return result;
    }

    /**
     * 5X5模式下的运算算法，若两个数相邻并相等则相加。
     *
     * @param nums 需要运算的二维数组
     * @return 运算完成后的二维数组
     */
    private static int[][] calculateFiveNumbers(int[][] nums) {
        boolean hasDouble;  //相加标志，某一排只要有相加就为true
        int[][] result = new int[5][5];
        if (null != nums) {
            for (int i = 0; i < 5; i++) {
                hasDouble = false;  //每一排初开始运算时始化为false
                int k = 0;
                if (nums[0][i] == 0 && nums[1][i] == 0 && nums[2][i] == 0 && nums[3][i] == 0 && nums[4][i] == 0) {
                    result[0][i] = createRandomNumber(5); //此排全为0则新生成一个2或4
                }
                for (int j = 0; j < 5; j++) {
                    if (nums[j][i] != 0) {
                        if (j <= 3 && nums[j][i] == nums[j + 1][i]) {
                            result[k][i] = nums[j][i] * 2;
                            hasDouble = true;
                            j++;
                            k++;
                        } else {
                            result[k][i] = nums[j][i];
                            k++;
                        }
                    }
                }
                if (!hasDouble) {//如果一排没有相加
                    if (nums[0][i] != nums[1][i] && nums[1][i] != nums[2][i] && nums[2][i] != nums[3][i] && nums[3][i] != nums[4][i] && nums[4][i] != 0) {
                        continue;//一排全不为零且相邻两个数均不相等，跳出此排的此次循环
                    }
                    for (int m = 0; m < 5; m++) {
                        if (result[m][i] == 0) {//此排第一个为零的位置生成一个新的2或4
                            result[m][i] = createRandomNumber(5);
                            break;
                        }
                    }
                }
            }
        }

        nowScore(result, 5);    //刷新分数
        return result;
    }

    /**
     * 运算算法，分别根据穷举的情况来选择是否相加，是否移动，是否生成新的2或4。
     *
     * @param preNumbers 需要运算的二维数组。
     * @return 运算后的二维数组。
     */
    private static int[][] calculateFourNumbers(int[][] preNumbers, int[][] nums) {
        if (preNumbers != null) {
            for (int i = 0; i < 4; i++) {
                if (preNumbers[0][i] == preNumbers[1][i] && preNumbers[2][i] == preNumbers[3][i]) {
                    if (preNumbers[0][i] == 0) {
                        nums[0][i] = preNumbers[2][i] * 2;
                        nums[1][i] = 0;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[2][i] != 0) {
                        nums[0][i] = preNumbers[0][i] * 2;
                        nums[1][i] = preNumbers[2][i] * 2;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[2][i] == 0) {
                        nums[0][i] = preNumbers[0][i] * 2;
                        nums[1][i] = 0;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    }
                } else if (preNumbers[0][i] == preNumbers[1][i] && preNumbers[2][i] != preNumbers[3][i]) {
                    if (preNumbers[0][i] == 0 && preNumbers[2][i] == 0) {
                        nums[0][i] = preNumbers[3][i];
                        nums[1][i] = createRandomNumber(nums.length);
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0 && preNumbers[3][i] == 0) {
                        nums[0][i] = preNumbers[2][i];
                        nums[1][i] = createRandomNumber(nums.length);
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0 && preNumbers[3][i] != 0) {
                        nums[0][i] = preNumbers[2][i];
                        nums[1][i] = preNumbers[3][i];
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[2][i] == 0) {
                        nums[0][i] = preNumbers[0][i] * 2;
                        nums[1][i] = preNumbers[3][i];
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[2][i] != 0 && preNumbers[3][i] == 0) {
                        nums[0][i] = preNumbers[0][i] * 2;
                        nums[1][i] = preNumbers[2][i];
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else {
                        nums[0][i] = preNumbers[0][i] * 2;
                        nums[1][i] = preNumbers[2][i];
                        nums[2][i] = preNumbers[3][i];
                        nums[3][i] = 0;
                    }
                } else if (preNumbers[0][i] != preNumbers[1][i] && preNumbers[1][i] == preNumbers[2][i]) {
                    if (preNumbers[0][i] == 0 && preNumbers[3][i] == 0) {
                        nums[0][i] = preNumbers[1][i] * 2;
                        nums[1][i] = 0;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[3][i] != 0) {
                        nums[0][i] = preNumbers[1][i] * 2;
                        nums[1][i] = preNumbers[3][i];
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] == 0 && preNumbers[2][i] == 0 && preNumbers[3][i] == 0) {
                        nums[1][i] = createRandomNumber(nums.length);
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] == 0 && preNumbers[3][i] == 0) {
                        nums[1][i] = createRandomNumber(nums.length);
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] == 0 && preNumbers[0][i] == preNumbers[3][i]) {
                        nums[0][i] = preNumbers[0][i] * 2;
                        nums[1][i] = 0;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] == 0 && preNumbers[0][i] != preNumbers[3][i]) {
                        nums[1][i] = preNumbers[3][i];
                        nums[2][i] = createRandomNumber(nums.length);
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] != 0 && preNumbers[3][i] == 0) {
                        nums[1][i] = preNumbers[1][i] * 2;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else {
                        nums[1][i] = preNumbers[1][i] * 2;
                        nums[2][i] = preNumbers[3][i];
                        nums[3][i] = 0;
                    }
                } else if (preNumbers[0][i] != preNumbers[1][i] && preNumbers[1][i] != preNumbers[2][i]
                        && preNumbers[2][i] == preNumbers[3][i]) {
                    if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0) {
                        nums[0][i] = preNumbers[1][i];
                        nums[1][i] = preNumbers[2][i] * 2;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] == 0) {
                        nums[0][i] = preNumbers[1][i];
                        nums[1][i] = createRandomNumber(nums.length);
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] != 0 && preNumbers[2][i] == 0) {
                        nums[2][i] = createRandomNumber(nums.length);
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] != 0 && preNumbers[2][i] != 0) {
                        nums[2][i] = preNumbers[2][i] * 2;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == preNumbers[2][i]) {
                        nums[0][i] = preNumbers[0][i] * 2;
                        nums[1][i] = preNumbers[3][i];
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else {
                        nums[1][i] = preNumbers[2][i] * 2;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    }
                } else if (preNumbers[0][i] != preNumbers[1][i] && preNumbers[1][i] != preNumbers[2][i]
                        && preNumbers[2][i] != preNumbers[3][i]) {
                    if (preNumbers[0][i] == 0 && preNumbers[2][i] == 0 && preNumbers[1][i] == preNumbers[3][i]) {
                        nums[0][i] = preNumbers[1][i] * 2;
                        nums[1][i] = 0;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] == 0 && preNumbers[1][i] != preNumbers[3][i]) {
                        nums[0][i] = preNumbers[1][i];
                        nums[1][i] = preNumbers[3][i];
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0 && preNumbers[3][i] == 0) {
                        nums[0][i] = preNumbers[1][i];
                        nums[1][i] = preNumbers[2][i];
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0 && preNumbers[3][i] != 0) {
                        nums[0][i] = preNumbers[1][i];
                        nums[1][i] = preNumbers[2][i];
                        nums[2][i] = preNumbers[3][i];
                        nums[3][i] = 0;
                    } else if (preNumbers[1][i] == 0 && preNumbers[0][i] == preNumbers[2][i] && preNumbers[3][i] == 0) {
                        nums[0][i] = preNumbers[0][i] * 2;
                        nums[1][i] = 0;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[1][i] == 0 && preNumbers[0][i] == preNumbers[2][i] && preNumbers[3][i] != 0) {
                        nums[0][i] = preNumbers[0][i] * 2;
                        nums[1][i] = preNumbers[3][i];
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[1][i] == 0 && preNumbers[0][i] != preNumbers[2][i]) {
                        nums[1][i] = preNumbers[2][i];
                        nums[2][i] = preNumbers[3][i];
                        nums[3][i] = 0;
                    } else if (preNumbers[2][i] == 0 && preNumbers[1][i] == preNumbers[3][i]) {
                        nums[1][i] = preNumbers[1][i] * 2;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[2][i] == 0 && preNumbers[1][i] != preNumbers[3][i]) {
                        nums[2][i] = preNumbers[3][i];
                        nums[3][i] = createRandomNumber(nums.length);
                    } else if (preNumbers[1][i] == 0 && preNumbers[3][i] == 0 && preNumbers[0][i] != preNumbers[2][i]) {
                        nums[1][i] = preNumbers[2][i];
                        nums[2][i] = createRandomNumber(nums.length);
                    } else if (preNumbers[0][i] == 0 && preNumbers[3][i] == 0) {
                        nums[0][i] = preNumbers[1][i];
                        nums[1][i] = preNumbers[2][i];
                        nums[2][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] != 0 && preNumbers[2][i] != 0 && preNumbers[3][i] == 0) {
                        nums[3][i] = createRandomNumber(nums.length);
                    } else {
                    }
                }
            }
        }

        nowScore(nums, 4);  //刷新分数
        return nums;
    }

    /**
     * 向下滑动时调用的方法。
     *
     * @param which 游戏模式标志
     */
    public static void actionDown(int which) {
        int[][] ns = mNumberItem.getNumbers();
        int[][] numbers;
        numbers = transformDown(ns, which); //矩阵转置。
        if (which == 4) {
            ns = calculateFourNumbers(numbers, numbers);
        } else if (which == 5) {
            ns = wipeFive(numbers);
            ns = calculateFiveNumbers(ns);
        } else if (which == 6) {
            ns = wipeSix(numbers);
            ns = calculateSixNumbers(ns);
        }

        numbers = transformDown(ns, which);   //运算结束后转置回来。
        mNumberItem.setNumbers(numbers);
    }

    /**
     * 从下转化成上。
     *
     * @param n     需要转置的二维数组。
     * @param which 游戏模式标志，该参数决定生成的二维数组
     * @return 转置后的数组。
     */
    private static int[][] transformDown(int[][] n, int which) {
        int[][] result = new int[which][which];
        if (which == 4) {
            for (int i = 0; i < 4; i++) {
                result[0][i] = n[3][i];
                result[1][i] = n[2][i];
                result[2][i] = n[1][i];
                result[3][i] = n[0][i];
            }
        } else if (which == 5) {
            for (int i = 0; i < 5; i++) {
                result[0][i] = n[4][i];
                result[1][i] = n[3][i];
                result[2][i] = n[2][i];
                result[3][i] = n[1][i];
                result[4][i] = n[0][i];
            }
        } else if (which == 6) {
            for (int i = 0; i < 6; i++) {
                result[0][i] = n[5][i];
                result[1][i] = n[4][i];
                result[2][i] = n[3][i];
                result[3][i] = n[2][i];
                result[4][i] = n[1][i];
                result[5][i] = n[0][i];
            }
        }
        return result;
    }

    /**
     * 想做滑动时调用。
     */
    public static void actionLeft(int which) {
        int[][] ns = mNumberItem.getNumbers();
        int[][] numbers;
        numbers = transformToLeft(ns, which);   //矩阵转置。

        if (which == 4) {
            ns = calculateFourNumbers(numbers, numbers);
        } else if (which == 5) {
            ns = wipeFive(numbers);
            ns = calculateFiveNumbers(ns);
        } else if (which == 6) {
            ns = wipeSix(numbers);
            ns = calculateSixNumbers(ns);
        }

        numbers = transformFromLeft(ns, which);   //转换回来。
        mNumberItem.setNumbers(numbers);
    }

    /**
     * 从左转化成上。
     *
     * @param n     需要转置的二维数组，
     * @param which 游戏模式标志，该标志决定生成的二维数组
     * @return 转置后的数组。
     */
    private static int[][] transformToLeft(int[][] n, int which) {
        int[][] result = new int[which][which];
        if (which == 4) {
            for (int i = 0; i < 4; i++) {
                result[i][0] = n[3][i];
                result[i][1] = n[2][i];
                result[i][2] = n[1][i];
                result[i][3] = n[0][i];
            }
        } else if (which == 5) {
            for (int i = 0; i < 5; i++) {
                result[i][0] = n[4][i];
                result[i][1] = n[3][i];
                result[i][2] = n[2][i];
                result[i][3] = n[1][i];
                result[i][4] = n[0][i];
            }
        } else if (which == 6) {
            for (int i = 0; i < 6; i++) {
                result[i][0] = n[5][i];
                result[i][1] = n[4][i];
                result[i][2] = n[3][i];
                result[i][3] = n[2][i];
                result[i][4] = n[1][i];
                result[i][5] = n[0][i];
            }
        }
        return result;
    }

    /**
     * 从上转化成左。进过运算后，转置回来以便装入TextView显示。
     *
     * @param n 需要转置的二维数组。
     * @return 转置后的数组。
     */
    private static int[][] transformFromLeft(int[][] n, int which) {
        int[][] result = new int[which][which];
        if (which == 4) {
            for (int i = 0; i < 4; i++) {
                result[0][i] = n[i][3];
                result[1][i] = n[i][2];
                result[2][i] = n[i][1];
                result[3][i] = n[i][0];
            }
        } else if (which == 5) {
            for (int i = 0; i < 5; i++) {
                result[0][i] = n[i][4];
                result[1][i] = n[i][3];
                result[2][i] = n[i][2];
                result[3][i] = n[i][1];
                result[4][i] = n[i][0];
            }
        } else if (which == 6) {
            for (int i = 0; i < 6; i++) {
                result[0][i] = n[i][5];
                result[1][i] = n[i][4];
                result[2][i] = n[i][3];
                result[3][i] = n[i][2];
                result[4][i] = n[i][1];
                result[5][i] = n[i][0];
            }
        }
        return result;
    }

    /**
     * 向下滑动时调用。
     */
    public static void actionRight(int which) {
        int[][] ns = mNumberItem.getNumbers();
        int[][] numbers;
        numbers = transformToRight(ns, which);  //矩阵转置

        if (which == 4) {
            ns = calculateFourNumbers(numbers, numbers);
        } else if (which == 5) {
            ns = wipeFive(numbers);
            ns = calculateFiveNumbers(ns);
        } else if (which == 6) {
            ns = wipeSix(numbers);
            ns = calculateSixNumbers(ns);
        }

        numbers = transformFromRight(ns, which);  //转置回来。
        mNumberItem.setNumbers(numbers);
    }

    /**
     * 从右转化成上。
     *
     * @param n     需要转置的二维数组。
     * @param which 游戏模式标志，该参数决定了生成的二维数组。
     * @return 转置后的数组。
     */
    private static int[][] transformToRight(int[][] n, int which) {
        int[][] result = new int[which][which];
        if (which == 4) {
            for (int i = 0; i < 4; i++) {
                result[0][i] = n[i][3];
                result[1][i] = n[i][2];
                result[2][i] = n[i][1];
                result[3][i] = n[i][0];
            }
        } else if (which == 5) {
            for (int i = 0; i < 5; i++) {
                result[0][i] = n[i][4];
                result[1][i] = n[i][3];
                result[2][i] = n[i][2];
                result[3][i] = n[i][1];
                result[4][i] = n[i][0];
            }
        } else if (which == 6) {
            for (int i = 0; i < 6; i++) {
                result[0][i] = n[i][5];
                result[1][i] = n[i][4];
                result[2][i] = n[i][3];
                result[3][i] = n[i][2];
                result[4][i] = n[i][1];
                result[5][i] = n[i][0];
            }
        }
        return result;
    }

    /**
     * 从上转化成右。进过运算后，转置回来以便装入TextView显示。
     *
     * @param n     需要转置的二维数组。
     * @param which 游戏模式标志，该参数决定了生成的二维数组
     * @return 转置后的数组。
     */
    private static int[][] transformFromRight(int[][] n, int which) {
        int[][] result = new int[which][which];
        if (which == 4) {
            for (int i = 0; i < 4; i++) {
                result[i][0] = n[3][i];
                result[i][1] = n[2][i];
                result[i][2] = n[1][i];
                result[i][3] = n[0][i];
            }
        } else if (which == 5) {
            for (int i = 0; i < 5; i++) {
                result[i][0] = n[4][i];
                result[i][1] = n[3][i];
                result[i][2] = n[2][i];
                result[i][3] = n[1][i];
                result[i][4] = n[0][i];
            }
        } else if (which == 6) {
            for (int i = 0; i < 6; i++) {
                result[i][0] = n[5][i];
                result[i][1] = n[4][i];
                result[i][2] = n[3][i];
                result[i][3] = n[2][i];
                result[i][4] = n[1][i];
                result[i][5] = n[0][i];
            }
        }
        return result;
    }
}