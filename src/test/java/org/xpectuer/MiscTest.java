package org.xpectuer;

import org.junit.Test;
import org.xpectuer.configs.ResourceConfigHelper;

import java.io.IOException;

public class MiscTest {
    public Integer x = null;
    @Test
    public void test() throws Exception{

        try {
            System.out.println(new ResourceConfigHelper().getPropertyValue("source_path"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
