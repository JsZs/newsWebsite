package com.nowcoder;

import java.util.Scanner;

public class ComputeMaxProductDP {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int n = 0, k = 0, d = 0;
        int[] array = null;

        while (cin.hasNextInt()) {
            n = cin.nextInt();
            array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = cin.nextInt();
            }
            k = cin.nextInt();
            d = cin.nextInt();
        }

        System.out.println(computeMaxProduct(array, k, d));
    }

    static long max(long a, long b) {
        return a > b ? a : b;
    };

    static long min(long a, long b) {
        return a < b ? a : b;
    };

    private static long computeMaxProduct(int[] array, int k, int d) {
        long dpMax[][] = new long[array.length][k + 1];
        long dpMin[][] = new long[array.length][k + 1];
        // dpMax[i][j] 表示以数组元素A【i】作结尾时， 序列长度为j的最大乘积结果
        for (int i = 0; i < array.length; i++) {
            dpMax[i][1] = array[i];
            dpMax[i][0] = array[0];
        }

        // 状态转移方程是 dpMax[i][j] = max(dpMax[i-1][j-1]* A[i], dpMin[i-d][j-1] *
        // A[i])

        long maxSoFar = Long.MIN_VALUE;
        for (int j = 2; j <= k; j++) {
            for (int i = j - 1; i < array.length; i++) {
                dpMax[i][j] = Long.MIN_VALUE;
                dpMin[i][j] = Long.MAX_VALUE;
                for (int x = 1; x <= d && (i - x) >= j - 2; x++) {
                    // 倒数第二个元素自己加上之前的元素数量至少还要有j-1个， 所以下标i-x需呀大于j-2
                    long resMax = max(dpMax[i - x][j - 1] * array[i], dpMin[i - x][j - 1] * array[i]);
                    long resMin = min(dpMax[i - x][j - 1] * array[i], dpMin[i - x][j - 1] * array[i]);

                    if (resMax > dpMax[i][j])
                        dpMax[i][j] = resMax;
                    if (resMin < dpMin[i][j])
                        dpMin[i][j] = resMin;

                }
            }
        }

        for (int i = k-1; i < array.length; i++) {
            if (dpMax[i][k] > maxSoFar) {
                maxSoFar = dpMax[i][k];
            }
        }

        return maxSoFar;

    }

}
