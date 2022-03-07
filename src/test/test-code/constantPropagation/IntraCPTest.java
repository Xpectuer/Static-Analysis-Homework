package constantPorpagation;

/**
 * Created by liture on 2021/9/19 4:21 下午
 */
public class IntraCPTest {

    public int nonDistributiveTest(int k) {
        int a;
        int b;
        a = 2;
        b = 0;
        if (Math.random() > 0.5 && k > 0) {
            a = 1;
            b = 9;
        } else if(k == 0) {
            a = 9;
            b = 1;
        } else {
            b = 8;
        }
        int c = a + b;
        return c;
    }

    public void sameValueMeetTest() {
        int a;
        int b;
        if (Math.random() > 0.5) {
            a = 1; // a: same value as false branch
            b = 2; // b: not same value as false branch
        }
        else {
            a = 1;
            b = 1;
        }
        int c = a + b;
        int d = a + a;
        int e = b + b;

        switch(e) {
            case 0:
                e = e+1;break;
            case 1:
                e = e+2;break;
            default:

        }


    }

    public void testParam(int x) {
        int a;
        a = x; // x is undefined
        int b = a; // b is undefined
    }
}