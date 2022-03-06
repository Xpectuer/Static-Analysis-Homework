package org.xpectuer;

import org.junit.Test;
import org.xpectuer.configs.ResourceConfigHelper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MiscTest {
    public Integer x = null;

    @Test
    public void test() throws Exception {


       A a1 = new A();
       A a2 = new A();

       Set<A> set = new HashSet<>();
       set.add(a1);
       set.add(a2);

       System.out.println(set.contains(a1));


    }
}

class A {
    int a;
    int b;
}
