package org.xpectuer.liveVariable;

import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;

import java.util.HashSet;
import java.util.Set;


/**
 Partial order is subset inclusion
 Merge operator is union
*/

public class LiveVariableAnalysis extends BackwardFlowAnalysis<Unit, Set> {

    public LiveVariableAnalysis(DirectedGraph<Unit> graph) {
        super(graph);
        doAnalysis();
    }

    @Override
    protected Set newInitialFlow() {
        return new HashSet();
    }

    @Override
    protected Set entryInitialFlow() {
        return new HashSet();
    }

    // merge joins two IN sets to make a OUT set
    @Override
    protected void merge(Set src1, Set src2, Set dest) {
        dest.clear();
        dest.addAll(src1);
        dest.addAll(src2);

    }


    // copy() brings IN set to OUT set
    @Override
    protected void copy(Set src, Set dest) {
        dest.clear();
        dest.addAll(src);
    }

    @Override
    protected void flowThrough(Set srcValue, Unit ut, Set destValue) {
        // take out kill set
        for(ValueBox box : ut.getDefBoxes()) {
            Value value = box.getValue();
            if(value instanceof Local) destValue.remove(value);
        }
        // Add gen set
        for(ValueBox box: ut.getUseBoxes()) {
            Value value = box.getValue();
            if(value instanceof Local) destValue.add(value);
        }
    }
}
