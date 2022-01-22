package org.xpectuer.liveVariableAnalysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.Map;

public class IntraLVTransformer extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(IntraLVTransformer.class);
    protected void internalTransform(Body body, String s, Map<String, String> map) {

        UnitGraph unitGraph = new BriefUnitGraph(body);
        LiveVariableAnalysis lv = new LiveVariableAnalysis(unitGraph);
        System.out.print(String.format("____Method %s____\n",body.getMethod().getName()));
        for(Unit unit :body.getUnits()) {
            logger.info(String.format("Before %s:%s",unit,lv.getFlowBefore(unit)));
            logger.info(String.format("After %s:%s",unit,lv.getFlowAfter(unit)));
            logger.info("==================================\n");
        }
    }
}
