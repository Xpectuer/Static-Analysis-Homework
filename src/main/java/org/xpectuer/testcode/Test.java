package org.xpectuer.testcode;

public class Test {
    public void test() {
        int x = 2;
        int y = x;
        int a, b, c, d;

        a = x + 2;
        b = y + 3;
        c = x + y <<  1;
        d = a + b + c;

        System.out.println(d);
        int[] arr = new int[10];
        for(int i=0;i<10;i++) {
            arr[i] = i;
            a = arr[i];
        }



    }
}
