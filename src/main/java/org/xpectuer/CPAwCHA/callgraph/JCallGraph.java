package org.xpectuer.CPAwCHA;

import soot.SootClass;
import soot.SootMethod;
import soot.jimple.InvokeStmt;
import soot.util.Chain;

import java.util.*;

public class JCallGraph {

    /**
     * entries
     */
    private List<SootMethod> entries;

    /**
     * reachables
     */
    private Set<SootMethod> reachableMethods;

    /**
     * callee -> caller
     * for returning from callee
     */
    private Map<SootMethod, Set<JCallEdge>> caller2callee;

    /**
     * caller -> callees
     * for the initilization of callsite jump
     */
    private Map<SootMethod, Set<JCallEdge>> callee2caller;

    /**
     * callsites -> caller
     * TODO: No Need for this table for good edge structure design
     */
    //private Map<Unit, SootMethod> cs2caller;


    // --- <init> ----
    public JCallGraph(Chain<SootClass> allClazzes, SootClass mainClazz) {
        // ---- init maps ---
        int size = 0;
        for (SootClass clazz : allClazzes) {
            size = size + clazz.getMethods().size();
        }
        size = size << 1;

        this.reachableMethods = new HashSet<>(size);
        this.caller2callee = new HashMap<>(size);
        this.callee2caller = new HashMap<>(size);
        //this.cs2caller = new HashMap<>(size);

//        // ---- map cs->caller ----
//        for (SootClass clazz : Scene.v().getApplicationClasses()) {
//            for (SootMethod method : clazz.getMethods()) {
//                if (method.isConcrete()) {
//                    // diff from getActiveBody()
//                    // create one if null
//                    @NotNull
//                    Body body = method.retrieveActiveBody();
//                    for (Unit unit : body.getUnits()) {
//                        // TODO: test to reduce the Set size
//                        if (unit instanceof InvokeStmt) {
//                            cs2caller.put(unit, method);
//                        }
//                    }
//                }
//            }
//        }

        // -----init entries-------
        this.entries = getOnceEntryMethods(mainClazz);
    }


    /**
     * set entries once
     *
     * @param mainClazz main classes
     * @return main methods
     */
    public List<SootMethod> getOnceEntryMethods(SootClass mainClazz) {
        // Use Optional To Prevent Propagation of NULL
        Optional<List<SootMethod>> optional = Optional.ofNullable(entries);
        if (optional.isPresent()) {
            return optional.get();
        }

        entries = new LinkedList<SootMethod>();

        for (SootMethod method : mainClazz.getMethods()) {
            if (method.getName().equals("main")) {
                entries.add(method);
            }
        }

        return entries;
    }


    /**
     * limited access to the "reachable" set
     *
     * @param method SootMethod
     * @return true or false
     */
    public boolean isReachable(SootMethod method) {
        return reachableMethods.contains(method);
    }

    public void addReachable(SootMethod method) {
        reachableMethods.add(method);
    }

    /**
     * @param cs     call site
     * @param callee callee
     * @return true if has call edge to callee
     */
    public boolean hasEdge(InvokeStmt cs, SootMethod callee) {
        Set<JCallEdge> callEdgeTable = getCallEdgesByCallee(callee);
        for (JCallEdge callEdge : callEdgeTable) {
            if (callEdge.getCallSite() == cs) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param cs       call site
     * @param callee   callee
     * @param callType callType{invokevirtual, invokestatic, invokespecial}
     */
    public void addEdge(InvokeStmt cs, SootMethod callee, SootMethod caller, CallType callType) throws CallEdgeAddException {
        JCallEdge callEdge = new JCallEdge(cs, caller, callee, callType);

        // lazy load
        Set<JCallEdge> callers = callee2caller.computeIfAbsent(callee, (k) -> new HashSet<>());
        if (!callers.add(callEdge)) {
            throw new CallEdgeAddException(String.format("call edge add failed. cause: callee2caller \n dump: call edge table: %s\n", callers));
        }


        Set<JCallEdge> callees = caller2callee.computeIfAbsent(caller, k -> new HashSet<>());
        if (!callees.add(callEdge)) {
            throw new CallEdgeAddException(String.format("call edge add failed. cause: caller2callee \n dump: call edge table: %s\n", callees));
        }
    }

    /**
     * lazy loader of table
     *
     * @param callee callee
     * @return table of call edges
     */
    private Set<JCallEdge> getCallEdgesByCallee(SootMethod callee) {
        // lazy
        Set<JCallEdge> result = callee2caller.computeIfAbsent(callee, k -> new HashSet<>());
        return Collections.unmodifiableSet(result);
    }


    /**
     * lazy loader of table
     *
     * @param caller caller
     * @return table of call edges
     */
    private Set<JCallEdge> getCallEdgesByCaller(SootMethod caller) {
        // lazy
        Set<JCallEdge> result = caller2callee.computeIfAbsent(caller, k -> new HashSet<>());
        return Collections.unmodifiableSet(result);
    }

}
