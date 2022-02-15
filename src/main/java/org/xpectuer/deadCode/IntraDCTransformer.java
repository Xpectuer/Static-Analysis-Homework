package org.xpectuer.deadCode;

import org.xpectuer.constantPropagation.ConstantPropagationAnalysis;
import org.xpectuer.liveVariable.LiveVariableAnalysis;
import soot.Body;
import soot.BodyTransformer;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.Map;

public class IntraDCTransformer extends BodyTransformer {

    @Override
    protected void internalTransform(Body body, String s, Map<String, String> map) {
        UnitGraph g = new BriefUnitGraph(body);

        ConstantPropagationAnalysis constantPropagation = new ConstantPropagationAnalysis(g);
        LiveVariableAnalysis liveVariableAnalysis = new LiveVariableAnalysis(g);
        DeadCodeAnalysis deadCode = new DeadCodeAnalysis(g);

    }
}
