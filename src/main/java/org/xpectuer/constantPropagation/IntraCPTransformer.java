package org.xpectuer.constantPropagation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.Map;

public class IntraCPTransformer extends BodyTransformer {

    Logger logger = LoggerFactory.getLogger(IntraCPTransformer.class);

    private void print_analysis_before(ConstantPropagationAnalysis cp, Body body) {
        System.out.println("===============Before==============");
        for(Unit unit :body.getUnits()) {
            System.out.printf("%s:%s\n",unit,cp.getFlowBefore(unit));
        }
        System.out.println("==================================");
    }

    private void print_analysis_after(ConstantPropagationAnalysis cp, Body body) {
        System.out.println("===============After==============");
        for(Unit unit :body.getUnits()) {
            System.out.printf("%s:%s\n",unit,cp.getFlowAfter(unit));
        }
        System.out.println("==================================");
    }

    @Override
    protected void internalTransform(Body body, String s, Map<String, String> map) {
        UnitGraph unitGraph = new BriefUnitGraph(body);
        // analysis
        ConstantPropagationAnalysis cp = new ConstantPropagationAnalysis(unitGraph);
        System.out.printf("____Method %s____\n",body.getMethod().getName());

        // output
        for(Unit unit: body.getUnits()) {
            System.out.printf("Before:[%s:%s]\n",unit,cp.getFlowBefore(unit));
            System.out.printf("After:[%s:%s]\n",unit,cp.getFlowAfter(unit));
            System.out.println("================================");
        }
    }
}