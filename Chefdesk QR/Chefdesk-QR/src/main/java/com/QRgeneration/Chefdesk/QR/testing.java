package com.QRgeneration.Chefdesk.QR;

import java.util.Scanner;

public class testing {

    public static void main(String[] args) {
        int arr[] = new int[10];
        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < 10; i++) {
            arr[i] = sc.nextInt();
        }
        int sortedArr[] = selectionSort(arr);

        for (int i = 0; i < arr.length; i++) {
            System.out.println(sortedArr[i]);
        }
    }

    private static int[] selectionSort(int[] arr) {
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
        return arr;
    }
}
