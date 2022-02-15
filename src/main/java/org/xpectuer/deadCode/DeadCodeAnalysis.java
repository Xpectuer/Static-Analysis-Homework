package org.xpectuer.deadCode;

import soot.Unit;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import java.util.HashSet;
import java.util.Set;


public class DeadCodeAnalysis extends ForwardFlowAnalysis<Unit, Set<Unit> > {

    public DeadCodeAnalysis(UnitGraph graph) {
        super(graph);
        doAnalysis();
    }


    @Override
    protected void flowThrough(Set<Unit> src, Unit unit, Set<Unit> dest) {

    }

    @Override
    protected Set<Unit> newInitialFlow() {
        return new HashSet<>();
    }

    @Override
    protected void merge(Set<Unit> src1, Set<Unit> src2, Set<Unit> dest) {

    }

    @Override
    protected void copy(Set<Unit> src, Set<Unit> dest) {
        dest.addAll(src);
    }
}
