package org.xpectuer.ConstantPropagationAnalysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xpectuer.liveVariableAnalysis.LiveVariableAnalysis;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.Map;

public class IntraCPTransformer extends BodyTransformer {

    Logger logger = LoggerFactory.getLogger(IntraCPTransformer.class);
    @Override
    protected void internalTransform(Body body, String s, Map<String, String> map) {
        UnitGraph unitGraph = new BriefUnitGraph(body);
        ConstantPropagationAnalysis cp = new ConstantPropagationAnalysis(unitGraph);
        System.out.print(String.format("____Method %s____\n",body.getMethod().getName()));
        for(Unit unit :body.getUnits()) {
            System.out.println(String.format("Before %s:%s",unit,cp.getFlowBefore(unit)));
            System.out.println(String.format("After %s:%s",unit,cp.getFlowAfter(unit)));
            System.out.println("==================================\n");
        }
    }
}
