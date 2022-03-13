package org.xpectuer.CPAwCHA;

import soot.SootMethod;
import soot.jimple.InvokeStmt;

import java.util.Objects;

/**
 * call site -> callee method sig
 */
public class JCallEdge {
    private final InvokeStmt callSite;
    private final SootMethod caller;
    private final SootMethod callee;
    private final CallType callType;

    public JCallEdge(InvokeStmt cs, SootMethod caller, SootMethod callee, CallType callType) {
        this.callSite = cs;
        this.caller = caller;
        this.callee = callee;
        this.callType = callType;
    }


    public InvokeStmt getCallSite() {
        return callSite;
    }

    public SootMethod getCaller() {
        return caller;
    }

    public SootMethod getCallee() {
        return callee;
    }

    public CallType getCallType() {
        return callType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JCallEdge callEdge = (JCallEdge) o;
        return Objects.equals(callSite, callEdge.callSite) && Objects.equals(caller, callEdge.caller) && Objects.equals(callee, callEdge.callee) && callType == callEdge.callType;
    }

    /**
     * Partial Evaluation as caller and CallType are redundant arguments in hashing
     *
     * @return int hashcode for evaluation
     */
    @Override
    public int hashCode() {
        return Objects.hash(callSite, callee);
    }

    @Override
    public String toString() {
        return "JCallEdge{" +
                "caller=" + caller +
                " -> callee=" + callee +
                '}';
    }
}
