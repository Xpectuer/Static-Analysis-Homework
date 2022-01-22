package org.xpectuer.CostantPropagation;

import org.xpectuer.ConstantPropagationAnalysis.IntraCPTransformer;
import org.xpectuer.IntraBaseTest;
import soot.Transformer;

import java.util.Collections;
import java.util.List;

public class IntraCPTest extends IntraBaseTest {
    @Override
    public List<String> getProcessDirs() {
        return Collections.singletonList("/Users/alex/projects/java_proj/static_analysis_lab/flow_analysis/src/test/test-code/constantPropagation");
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
