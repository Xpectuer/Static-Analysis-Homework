public class Test {
    public static void test() {
        int x = 2;
        int y = x;
        int a, b, c, d;

        a = x + 2;
        b = y + 3;
        c = x + y <<  1;
        d = a + b + c;

        System.out.println(d);

        return;
    }
}
