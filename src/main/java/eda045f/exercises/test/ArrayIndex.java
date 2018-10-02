package eda045f.exercises.test;

public class ArrayIndex {
  public static void main(String... args) {
    int i = -1;
    int j = i + 1;
    int k = -2 + 1;

    int w = 10;

    int q = j + k;

    int[] arr = new int[2];
    arr[0] = 1;
    arr[1] = 2;

    arr[i] = 0; // err

    arr[j] = 2;

    arr[k] = 3; // err

    arr[j + k] = 4; // err

    arr[2 - 3 - 4 - 5] = 10;

    arr[q] = 11; // err

    arr[w * -1] = 10; // err

    for (int t = 2; t > -2; t--) {
      arr[t] = 10; // POS -> BOTTOM
    }

    int b = 3;
    int bb = -1;
    while (b > 0) {
      bb = bb - 1;
      b--;
    }
    arr[bb] = 19; // NEG -> NEG // throws warning

    int a = 3;
    int aa = 0;
    while (a > 0) {
      aa = aa - 1;
      a--;
    }
    arr[aa] = 19; // ZERO -> NEG // throws no warning

    int z;
    if (q > 0) {
      z = 10;
    } else {
      z = -20;
    }

    arr[z] = -1; // we don't know if z is POS or NEG
  }
}
