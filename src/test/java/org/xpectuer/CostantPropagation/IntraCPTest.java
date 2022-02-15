package org.xpectuer.CostantPropagation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xpectuer.constantPropagation.IntraCPTransformer;
import org.xpectuer.IntraBaseTest;
import org.xpectuer.configs.ResourceConfigHelper;
import soot.Transformer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class IntraCPTest extends IntraBaseTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<String> getProcessDirs() {
        List<String> result = Collections.singletonList("NIL");
        try {
            String str = new ResourceConfigHelper().getPropertyValue("source_path");
            result = Collections.singletonList(str);
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
        return result;
    }

    @Override
    public String getPhaseNameOfPack() {
        return "jtp";
    }

    @Override
    public String getPhaseNameOfTransformer() {
        return "jtp.intra_cp";
    }

    @Override
    public Transformer getTransformer() {
        return new IntraCPTransformer();
    }
}
