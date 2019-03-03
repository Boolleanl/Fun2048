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

    public static void newGameFour() {
        mNumberItem = NumberItem.getInstanceFour();
        numbers = createNumbers(4);
        mNumberItem.setNumbers(numbers);
        nowScore(mNumberItem.getNumbers(),4);
    }

    public static void newGameFive() {
        mNumberItem = NumberItem.getInstanceFive();
        numbers = createNumbers(5);
        mNumberItem.setNumbers(numbers);
        nowScore(mNumberItem.getNumbers(),5);
    }

    /**
     * “新游戏”开始时，为数组填入起始数字。
     *
     * @return 起始二维数组。
     */
    public static int[][] createNumbers(int which) {
        int[][] ns = new int[which][which];
        for (int i = 0; i < which; i++) {
            for (int j = 0; j < which; j++) {
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
    public static int nowScore(int[][] ns,int which) {
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
     * 生成随机数，0、2、4三个数。在起始生成数组时，或者滑动后有空位的情况下调用。
     *
     * @return 0、2或者4。
     */
    public static int createRandomNumber() {
        int i = (int) (Math.random() * 4);
        if (i == 0||i==1) {
            num = 0;
        } else if (i == 2) {
            num = 2;
        } else if (i == 3) {
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
        mNumberItem = NumberItem.getInstanceFour();
        mNumberItem.setNumbers(n);
    }

    /**
     * 向上滑动时调用的方法。
     */
    public static void actionUp(int which) {
        int[][] numbers = mNumberItem.getNumbers();
        int[][] ns = null;
        if(which == 4){
            ns = calculateFourNumbers(numbers,numbers);
        }else if(which == 5){
            ns = wipe(numbers);
        }
        mNumberItem.setNumbers(ns);
    }

    private static int[][] wipe(int[][] nums){
        if (null != nums) {
            for (int i = 0; i < 5; i++) {
                    if (nums[0][i] == 0 && nums[1][i] == 0 && nums[2][i] == 0 && nums[3][i] == 0 && nums[4][i] == 0) {
                        break;
                    } else if (nums[0][i] == 0) {
                        nums[0][i] = nums[1][i];
                        nums[1][i] = nums[2][i];
                        nums[2][i] = nums[3][i];
                        nums[3][i] = nums[4][i];
                        nums[4][i] = 0;
                        wipe(nums);
                    }
                    if (nums[1][i] == 0 && nums[2][i] == 0 && nums[3][i] == 0 && nums[4][i] == 0) {
                        break;
                    } else if (nums[1][i] == 0) {
                        nums[1][i] = nums[2][i];
                        nums[2][i] = nums[3][i];
                        nums[3][i] = nums[4][i];
                        nums[4][i] = 0;
                        wipe(nums);
                    }
                    if (nums[2][i] == 0 && nums[3][i] == 0 && nums[4][i] == 0) {
                        break;
                    } else if (nums[2][i] == 0) {
                        nums[2][i] = nums[3][i];
                        nums[3][i] = nums[4][i];
                        nums[4][i] = 0;
                        wipe(nums);
                    }
                    if (nums[3][i] == 0 && nums[4][i] == 0) {
                        break;
                    } else if (nums[3][i] == 0) {
                        nums[3][i] = nums[4][i];
                        nums[4][i] = 0;
                    }

            }
        }
        return nums;
    }

//    private static int[][] calculateFiveNumbers(int[][] preNumbers, int[][] nums) {
//        //TODO:完善5X5的算法。
//        if (null != preNumbers) {
//            for (int i = 0; i < 5; i++) {
//
//
//                //0____
//                if(preNumbers[0][i]==0){
//                    //00___
//                    if(preNumbers[1][i]==0){
//                        //000__
//                        if(preNumbers[2][i]==0){
//                            //0000_
//                            if(preNumbers[3][i]==0){
//                                //00000
//                                if(preNumbers[4][i]==0){
//                                    nums[0][i]=createRandomNumber();
//                                }else {//0000A
//                                    nums[0][i]=preNumbers[4][i];
//                                    nums[4][i]=createRandomNumber();
//                                }
//                            }else {//000A0 000AA 000AB
//                                if(preNumbers[3][i]==preNumbers[4][i]){//000AA
//                                    nums[0][i] = doubleValue(preNumbers[3][i]);
//                                    nums[3][i] = 0;
//                                    nums[4][i] = 0;
//                                }else {//000A0 000AB
//                                    nums[0][i] = preNumbers[3][i];
//                                    nums[1][i] = preNumbers[4][i];
//                                    nums[3][i] = 0;
//                                    nums[4][i] = 0;
//                                }
//                            }
//                        }else {//00___
//                            if(preNumbers[3][i]==0&&preNumbers[4][i]==0){//00A00
//                                nums[0][i]= preNumbers[2][i];
//                                nums[1][i] = createRandomNumber();
//                                nums[2][i] = 0;
//                            }else if(preNumbers[2][i]==preNumbers[3][i]||(preNumbers[2][i]==preNumbers[4][i]&&preNumbers[3][i]==0)){
//                                //00AA0 00AAA 00AAB 00A0A
//                                nums[0][i]=doubleValue(preNumbers[2][i]);
//                                nums[1][i]=preNumbers[4][i];
//                                nums[2][i]=0;
//                                nums[3][i]=0;
//                                nums[4][i]=0;
//                            }else if(preNumbers[2][i]==preNumbers[4][i]&&preNumbers[3][i]!=0){// 00ABA
//
//                            }
//                        }
//
//                    }
//                }
//            }
//        }
//                return nums;
//    }

    /**
     * 运算算法，分别根据穷举的情况来选择是否相加，是否移动，是否生成新的2或4。
     *
     * @param preNumbers    需要运算的二维数组。
     * @return 运算后的二维数组。
     */
    private static int[][] calculateFourNumbers(int[][] preNumbers,int[][] nums) {
        if (preNumbers != null) {
            for (int i = 0; i < 4; i++) {

                if (preNumbers[0][i] == preNumbers[1][i] && preNumbers[2][i] == preNumbers[3][i]) {
                    if (preNumbers[0][i] == 0) {
                        nums[0][i] = doubleValue(preNumbers[2][i]);
                        //
                        nums[1][i] = 0;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[2][i] != 0) {
                        nums[0][i] = doubleValue(preNumbers[0][i]);
                        nums[1][i] = doubleValue(preNumbers[2][i]);
//
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[2][i] == 0) {
                        nums[0][i] = doubleValue(preNumbers[0][i]);
                        //
                        nums[1][i] = 0;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    }
                } else if (preNumbers[0][i] == preNumbers[1][i] && preNumbers[2][i] != preNumbers[3][i]) {
                    if (preNumbers[0][i] == 0 && preNumbers[2][i] == 0) {
                        nums[0][i] = preNumbers[3][i];
                        nums[1][i] = createRandomNumber();
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0 && preNumbers[3][i] == 0) {
                        nums[0][i] = preNumbers[2][i];
                        nums[1][i] = createRandomNumber();
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0 && preNumbers[3][i] != 0) {
                        nums[0][i] = preNumbers[2][i];
                        nums[1][i] = preNumbers[3][i];
//
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[2][i] == 0) {
                        nums[0][i] = doubleValue(preNumbers[0][i]);
                        nums[1][i] = preNumbers[3][i];
//
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[2][i] != 0 && preNumbers[3][i] == 0) {
                        nums[0][i] = doubleValue(preNumbers[0][i]);
                        nums[1][i] = preNumbers[2][i];
//
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else {
                        nums[0][i] = doubleValue(preNumbers[0][i]);
                        nums[1][i] = preNumbers[2][i];
                        nums[2][i] = preNumbers[3][i];
//
                        nums[3][i] = 0;
                    }
                } else if (preNumbers[0][i] != preNumbers[1][i] && preNumbers[1][i] == preNumbers[2][i]) {
                    if (preNumbers[0][i] == 0 && preNumbers[3][i] == 0) {
                        nums[0][i] = doubleValue(preNumbers[1][i]);
                        //
                        nums[1][i] = 0;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[3][i] != 0) {
                        nums[0][i] = doubleValue(preNumbers[1][i]);
                        nums[1][i] = preNumbers[3][i];
//
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] == 0 && preNumbers[2][i] == 0 && preNumbers[3][i] == 0) {
                        nums[1][i] = createRandomNumber();
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] == 0 && preNumbers[3][i] == 0) {
                        nums[1][i] = createRandomNumber();
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] == 0 && preNumbers[0][i] == preNumbers[3][i]) {
                        nums[0][i] = doubleValue(preNumbers[0][i]);
                        //
                        nums[1][i] = 0;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] == 0 && preNumbers[0][i] != preNumbers[3][i]) {
                        nums[1][i] = preNumbers[3][i];
                        nums[2][i] = createRandomNumber();
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] != 0 && preNumbers[3][i] == 0) {
                        nums[1][i] = doubleValue(preNumbers[1][i]);
                        //
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else {
                        nums[1][i] = doubleValue(preNumbers[1][i]);
                        nums[2][i] = preNumbers[3][i];
//
                        nums[3][i] = 0;
                    }
                } else if (preNumbers[0][i] != preNumbers[1][i] && preNumbers[1][i] != preNumbers[2][i]
                        && preNumbers[2][i] == preNumbers[3][i]) {
                    if (preNumbers[0][i] == 0 && preNumbers[2][i] != 0) {
                        nums[0][i] = preNumbers[1][i];
                        nums[1][i] = doubleValue(preNumbers[2][i]);
//
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] == 0) {
                        nums[0][i] = preNumbers[1][i];
                        nums[1][i] = createRandomNumber();
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] != 0 && preNumbers[2][i] == 0) {
                        nums[2][i] = createRandomNumber();
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] != 0 && preNumbers[2][i] != 0) {
                        nums[2][i] = doubleValue(preNumbers[2][i]);
                        //
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == preNumbers[2][i]) {
                        nums[0][i] = doubleValue(preNumbers[0][i]);
                        nums[1][i] = preNumbers[3][i];
//
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else {
                        nums[1][i] = doubleValue(preNumbers[2][i]);
                        //
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    }
                } else if (preNumbers[0][i] != preNumbers[1][i] && preNumbers[1][i] != preNumbers[2][i]
                        && preNumbers[2][i] != preNumbers[3][i]) {
                    if (preNumbers[0][i] == 0 && preNumbers[2][i] == 0 && preNumbers[1][i] == preNumbers[3][i]) {
                        nums[0][i] = doubleValue(preNumbers[1][i]);
                        //
                        nums[1][i] = 0;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[0][i] == 0 && preNumbers[2][i] == 0 && preNumbers[1][i] != preNumbers[3][i]) {
                        nums[0][i] = preNumbers[1][i];
                        nums[1][i] = preNumbers[3][i];
//
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
//
                        nums[3][i] = 0;
                    } else if (preNumbers[1][i] == 0 && preNumbers[0][i] == preNumbers[2][i] && preNumbers[3][i] == 0) {
                        nums[0][i] = doubleValue(preNumbers[0][i]);
                        //
                        nums[1][i] = 0;
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[1][i] == 0 && preNumbers[0][i] == preNumbers[2][i] && preNumbers[3][i] != 0) {
                        nums[0][i] = doubleValue(preNumbers[0][i]);
                        nums[1][i] = preNumbers[3][i];
//
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[1][i] == 0 && preNumbers[0][i] != preNumbers[2][i]) {
                        nums[1][i] = preNumbers[2][i];
                        nums[2][i] = preNumbers[3][i];
//
                        nums[3][i] = 0;
                    } else if (preNumbers[2][i] == 0 && preNumbers[1][i] == preNumbers[3][i]) {
                        nums[1][i] = doubleValue(preNumbers[1][i]);
                        //
                        nums[2][i] = 0;
                        nums[3][i] = 0;
                    } else if (preNumbers[2][i] == 0 && preNumbers[1][i] != preNumbers[3][i]) {
                        nums[2][i] = preNumbers[3][i];
                        nums[3][i] = createRandomNumber();
                    } else if (preNumbers[1][i] == 0 && preNumbers[3][i] == 0 && preNumbers[0][i] != preNumbers[2][i]) {
                        nums[1][i] = preNumbers[2][i];
                        nums[2][i] = createRandomNumber();
                    } else if (preNumbers[0][i] == 0 && preNumbers[3][i] == 0) {
                        nums[0][i] = preNumbers[1][i];
                        nums[1][i] = preNumbers[2][i];
//
                        nums[2][i] = 0;
                    } else if (preNumbers[0][i] != 0 && preNumbers[1][i] != 0 && preNumbers[2][i] != 0 && preNumbers[3][i] == 0) {
                        nums[3][i] = createRandomNumber();
                    } else {
                    }
                }
            }
        }

        nowScore(nums,4);

        return nums;
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
    public static void actionDown(int which) {
        int[][] preNumbers = new int[which][which];
        int[][] ns = mNumberItem.getNumbers();
        preNumbers = transformDown(preNumbers, ns,which); //矩阵转置。
        if(which == 4){
            ns = calculateFourNumbers(preNumbers,preNumbers);
        }else if(which == 5){
            ns = wipe(preNumbers);
        }

        int[][] numbers = new int[which][which];
        numbers = transformDown(numbers, ns,which);   //运算结束后转置回来。

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
    private static int[][] transformDown(int[][] preNumbers, int[][] n,int which) {
        if (which == 4) {
            for (int i = 0; i < 4; i++) {
                preNumbers[0][i] = n[3][i];
                preNumbers[1][i] = n[2][i];
                preNumbers[2][i] = n[1][i];
                preNumbers[3][i] = n[0][i];
            }
        }else if(which ==5){
            for (int i = 0; i < 5; i++) {
                preNumbers[0][i] = n[4][i];
                preNumbers[1][i] = n[3][i];
                preNumbers[2][i] = n[2][i];
                preNumbers[3][i] = n[1][i];
                preNumbers[4][i] = n[0][i];
            }
        }
        return preNumbers;
    }

    /**
     * 想做滑动时调用。
     */
    public static void actionLeft(int which) {

        int[][] preNumbers = new int[which][which];
        int[][] ns = mNumberItem.getNumbers();
        preNumbers = transformToLeft(preNumbers, ns,which);   //矩阵转置。

        if(which == 4){
            ns = calculateFourNumbers(preNumbers,preNumbers);
        }else if(which == 5){
            ns = wipe(preNumbers);
        }

        int[][] numbers = new int[which][which];
        numbers = transformFromLeft(numbers, ns,which);   //转换回来。

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
    private static int[][] transformToLeft(int[][] preNumbers, int[][] n,int which) {
        if(which == 4){
            for (int i = 0; i < 4; i++) {
                preNumbers[i][0] = n[3][i];
                preNumbers[i][1] = n[2][i];
                preNumbers[i][2] = n[1][i];
                preNumbers[i][3] = n[0][i];
            }
        }else if(which == 5){
            for (int i = 0; i < 5; i++) {
                preNumbers[i][0] = n[4][i];
                preNumbers[i][1] = n[3][i];
                preNumbers[i][2] = n[2][i];
                preNumbers[i][3] = n[1][i];
                preNumbers[i][4] = n[0][i];
            }
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
    private static int[][] transformFromLeft(int[][] preNumbers, int[][] n,int which) {
        if(which==4){
            for (int i = 0; i < 4; i++) {
                preNumbers[0][i] = n[i][3];
                preNumbers[1][i] = n[i][2];
                preNumbers[2][i] = n[i][1];
                preNumbers[3][i] = n[i][0];
            }
        }else if(which == 5){
            for (int i = 0; i < 5; i++) {
                preNumbers[0][i] = n[i][4];
                preNumbers[1][i] = n[i][3];
                preNumbers[2][i] = n[i][2];
                preNumbers[3][i] = n[i][1];
                preNumbers[4][i] = n[i][0];
            }
        }
        return preNumbers;
    }

    /**
     * 向下滑动时调用。
     */
    public static void actionRight(int which) {
        int[][] preNumbers = new int[which][which];
        int[][] ns = mNumberItem.getNumbers();
        preNumbers = transformToRight(preNumbers, ns,which);  //矩阵转置

        if(which == 4){
            ns = calculateFourNumbers(preNumbers,preNumbers);
        }else if(which == 5){
            ns = wipe(preNumbers);
        }

        int[][] numbers = new int[which][which];
        numbers = transformFromRight(numbers, ns,which);  //转置回来。

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
    private static int[][] transformToRight(int[][] preNumbers, int[][] n,int which) {
        if(which == 4){
            for (int i = 0; i < 4; i++) {
                preNumbers[0][i] = n[i][3];
                preNumbers[1][i] = n[i][2];
                preNumbers[2][i] = n[i][1];
                preNumbers[3][i] = n[i][0];
            }
        }else if(which==5){
            for (int i = 0; i < 5; i++) {
                preNumbers[0][i] = n[i][4];
                preNumbers[1][i] = n[i][3];
                preNumbers[2][i] = n[i][2];
                preNumbers[3][i] = n[i][1];
                preNumbers[4][i] = n[i][0];
            }
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
    private static int[][] transformFromRight(int[][] preNumbers, int[][] n,int which) {
        if(which == 4){
            for (int i = 0; i < 4; i++) {
                preNumbers[i][0] = n[3][i];
                preNumbers[i][1] = n[2][i];
                preNumbers[i][2] = n[1][i];
                preNumbers[i][3] = n[0][i];
            }
        }else if(which == 5){
            for (int i = 0; i < 5; i++) {
                preNumbers[i][0] = n[4][i];
                preNumbers[i][1] = n[3][i];
                preNumbers[i][2] = n[2][i];
                preNumbers[i][3] = n[1][i];
                preNumbers[i][4] = n[0][i];
            }
        }
        return preNumbers;
    }
}