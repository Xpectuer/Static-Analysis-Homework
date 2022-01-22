package org.xpectuer.liveVariable;

import org.xpectuer.IntraBaseTest;
import org.xpectuer.liveVariableAnalysis.IntraLVTransformer;
import soot.Transformer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by liture on 2021/9/19 4:29 下午
 */
public class IntraLVTest extends IntraBaseTest {

    @Override
    public List<String> getProcessDirs() {
        return Collections.singletonList("/Users/alex/projects/java_proj/static_analysis_lab/flow_analysis/src/test/test-code/liveVariable");
    }

    @Override
    public List<String> getExcluded() {
        List<String> excluded = new LinkedList<>(super.getExcluded());
        excluded.add("ass2.*");
        excluded.add("ass3.*");
        excluded.add("ass4.*");
        excluded.add("ass5.*");
        return excluded;
    }

    @Override
    public String getPhaseNameOfPack() {
        return "jtp";
    }

    @Override
    public String getPhaseNameOfTransformer() {
        return "jtp.intra_lv";
    }

    @Override
    public Transformer getTransformer() {
        return new IntraLVTransformer();
    }
}
