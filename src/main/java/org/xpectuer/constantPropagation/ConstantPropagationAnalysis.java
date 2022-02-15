package org.xpectuer.constantPropagation;

import soot.Local;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import java.util.HashSet;
import java.util.Set;

public class ConstantPropagationAnalysis extends ForwardFlowAnalysis<Unit, Pairs> {

    public ConstantPropagationAnalysis(UnitGraph graph) {
        super(graph);
        doAnalysis();
    }

    //TODO：before和after一样了

    // make output readable
    @Override
    public Pairs getFlowAfter(Unit s) {
        Pairs pairs = unitToAfterFlow.get(s);
        return pairs == null ? newInitialFlow() : pairs;
    }

    @Override
    public Pairs getFlowBefore(Unit s) {
        Pairs pairs = unitToBeforeFlow.get(s);
        return pairs == null ? newInitialFlow() : pairs;
    }

    @Override
    protected void flowThrough(Pairs in, Unit ut, Pairs out) {
        copy(in, out);
        if (ut instanceof AssignStmt) {
            AssignStmt as = (AssignStmt) ut;
            soot.Value lValue = as.getLeftOp();
            if (lValue instanceof Local) {
                Local defLocal = (Local) lValue;
                soot.Value rValue = as.getRightOp();
                // interpret the right expression
                LatticeValue rAbsLatticeValue = in.computeValue(rValue);
                if (rAbsLatticeValue == null) rAbsLatticeValue = LatticeValue.undef;
                out.put(defLocal, rAbsLatticeValue);
            }
        }
    }

    @Override
    protected Pairs newInitialFlow() {
        return new Pairs();
    }

    @Override
    protected void merge(Pairs src1, Pairs src2, Pairs dest) {

        Set<Local> seen = new HashSet<>();
        src1.forEach((k, v) -> {
            dest.put(k, v.meet(src2.getOrDefault(k, LatticeValue.undef)));
            seen.add(k);
        });

        src2.forEach((k, v) -> {
            if (!seen.contains(k)) {
                dest.put(k, v.meet(src1.getOrDefault(k, LatticeValue.undef)));
            }
        });

    }

    @Override
    protected void copy(Pairs src, Pairs dest) {
        dest.clear();
        dest.putAll(src);
    }


}
