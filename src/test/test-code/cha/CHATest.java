package cha;

/**
 * Created by liture on 2021/9/20 4:25 下午
 */
class A {
    public void foo() {
        this.bar();
    }

    void bar() {
        C c = new C();
        c.bar();
    }
}

class B extends A {
    void bar() { }
}

class C extends A {

    void bar() {
        if (Math.random() > 0.5) {
            this.foo();
        }
    }

    void m() {  }
}

public class CHATest {
    public static void main(String[] args) {
        A a = new A();
        a.foo();
        int i = 0;
        int x = CHATest.foo(i);
    }

    private static int foo(int i) {
        return i + 2;
    }
}


