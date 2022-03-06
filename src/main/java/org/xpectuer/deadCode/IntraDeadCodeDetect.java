package org.xpectuer.deadCode;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xpectuer.constantPropagation.ConstantPropagationAnalysis;
import org.xpectuer.constantPropagation.Pairs;
import org.xpectuer.liveVariable.LiveVariableAnalysis;
import soot.*;
import soot.JastAddJ.BreakStmt;
import soot.jimple.*;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;

import java.util.*;

public class IntraDeadCodeDetect extends BodyTransformer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    // TODO: only applied for Integers compare statements
    private final Set<Unit> dead_codes = new HashSet<>();
    private final Set<Integer> dead_codes_lines = new HashSet<>();


    @Override
    protected void internalTransform(Body body, String s, Map<String, String> map) {


        // Detecting nop used for trap


        // do live variable analysis
        // result: Unit -> Quote

        UnitGraph graph = new BriefUnitGraph(body);

        LiveVariableAnalysis liveVariableAnalysis = new LiveVariableAnalysis(graph);
        ConstantPropagationAnalysis constantPropagation = new ConstantPropagationAnalysis(graph);
        Map<Unit, Set<Value>> lv_results = liveVariableAnalysis.getResult();
        Map<Unit, Pairs> cp_results = constantPropagation.getResult();

        detect_dead_assignments(body, lv_results);
        detect_unreachable_branch(body, cp_results);

        System.out.println(getResults());

    }


    private void detect_unreachable_branch(Body body, Map<Unit, Pairs> cp_results) {
        // ------------- init --------------
        UnitGraph graph = new BriefUnitGraph(body);
        List<Unit> entries = graph.getHeads();
        Deque<Unit> dq = new ArrayDeque<>();
        Set<Unit> seen = new HashSet<>();

        for (Unit entry : entries) {
            dq.addLast(entry);
        }


        // ----------- body --------------
        while (!dq.isEmpty()) {
            // may analysis
            // TODO: maybe to be refactor?
            boolean certain = false;
            Unit first = dq.pollFirst();
            seen.add(first);
            if (!is_End(first)) {
                List<Unit> succ = graph.getSuccsOf(first);
                // visit(first)
                Pairs pairs = cp_results.get(first);
                if (first instanceof IfStmt) {
                    IfStmt ifStmt = (IfStmt) first;
                    logger.info(String.format("if statement:%s", ifStmt));

                    if (ifStmt.getCondition() instanceof ConditionExpr) {
                        ConditionExpr cond_expr = (ConditionExpr) ifStmt.getCondition();
                        Value ope1 = cond_expr.getUseBoxes().get(0).getValue();
                        Value ope2 = cond_expr.getUseBoxes().get(1).getValue();
                        // parse ope1 and ope2
                        int v1 = 0, v2 = 0;
                        boolean is_const1 = false, is_const2 = false;

                        // ---parse ope1---
                        if (ope1 instanceof Local) {
                            // TODO: may cause bug???
                            is_const1 = pairs.get(ope1).isConst();
                            if (is_const1) {
                                v1 = pairs.get(ope1).getValue();
                            }
                        } else if (ope1 instanceof Constant) {
                            Constant constant = (Constant) ope1;
                            v1 = Integer.parseInt(constant.toString());
                        } else {
                            logger.error(String.format("Expect: Local or Constant.\n Provided %s ", ope1.getType()));
                        }

                        // ---parse ope2---
                        if (ope2 instanceof Local) {
                            is_const2 = !(pairs.get(ope2).isNAC() || pairs.get(ope2).isUNDEF());
                            if (is_const2) {
                                v1 = pairs.get(ope2).getValue();
                            }
                        } else if (ope2 instanceof Constant) {
                            Constant constant = (Constant) ope2;
                            v2 = Integer.parseInt(constant.toString());
                        } else {
                            logger.error(String.format("Expect: Local or Constant.\n Provided %s ", ope2.getType()));
                        }

                        // do add true branching
                        if (is_const1 && is_const2) {        // if ope1 and ope2 are constants simultaneously

                            boolean constant_result = interpretBool(cond_expr, v1, v2);
                            if (constant_result) {
                                dq.addLast(ifStmt.getTarget());
                                certain = true;
                            } else {
                                for (Unit unit : succ) {
                                    if (unit != ifStmt.getTarget()) {
                                        dq.addLast(unit);
                                    }
                                }
                            }

                        }

                    }
                } else if (first instanceof SwitchStmt) { // switch
                    SwitchStmt switchStmt = (SwitchStmt) first;
                    logger.info(String.format("Switch branching:%s", switchStmt));
                    Value v = switchStmt.getKey();
                    if (v instanceof Local) {
                        Local local = (Local) v;
                        boolean is_const = pairs.get(v).isConst();
                        if (is_const) {
                            certain = true;
                            int c = pairs.get(v).getValue();
                            dq.addLast(switchStmt.getTarget(c));
                        }
                    }
                }

                if (!certain) { // not a branching or ending
                    for (Unit unit : succ) {
                        if (!is_End(unit)) {
                            dq.addLast(unit);
                        }
                    }
                }


            } else {
                logger.info(String.format("Control Flow Unreachable: %s", first));
            }


        }

        // set dead code

        for (Unit stmt : body.getUnits()) {
            if (!seen.contains(stmt)) {
                dead_codes.add(stmt);
            }
        }


    }

    private boolean interpretBool(ConditionExpr cond_expr, Integer const1, Integer const2) {
        if (cond_expr instanceof EqExpr) {
            return (Objects.equals(const1, const2));
        } else if (cond_expr instanceof LtExpr) {
            return (const1 < const2);
        } else if (cond_expr instanceof LeExpr) {
            return (const1 <= const2);
        } else if (cond_expr instanceof GtExpr) {
            return (const1 > const2);
        } else if (cond_expr instanceof GeExpr) {
            return (const1 >= const2);
        } else {
            return false;
        }
    }


    private boolean is_End(Unit stmt) {
        return (stmt instanceof ReturnStmt ||
                stmt instanceof GotoStmt ||
                //stmt instanceof RetStmt ||
                //stmt instanceof ReturnVoidStmt ||
                stmt instanceof BreakStmt ||
                stmt instanceof IfStmt ||
                stmt instanceof SwitchStmt

        );
    }


    private void detect_dead_assignments(Body body, Map<Unit, Set<Value>> lv_results) {
        Set<Unit> trap = this.getTrap(body);
        final Chain<Unit> units = body.getUnits();
        for (Unit stmt : units) {

            Set<Value> stmt_lv_result = lv_results.get(stmt);
            // Mark NopStmt Except for Trap
            if (stmt instanceof NopStmt) {
                if (!trap.contains(stmt)) {
                    dead_codes.add(stmt);
                }
            } else if (stmt instanceof AssignStmt) {

                AssignStmt astmt = (AssignStmt) stmt;

                Value lValue = astmt.getLeftOp();
                Value rValue = astmt.getRightOp();

                if (lValue == rValue) {
                    dead_codes.add(stmt);
                }
                // live variable check
                else if (lValue instanceof Local) {

                    Local local = (Local) lValue;
                    if (!stmt_lv_result.contains(local)) {
                        dead_codes.add(stmt);
                    }
                }
            }
        }
    }

    public Set<Integer> getResultsLines() {
        for(Unit units:dead_codes) {
            dead_codes_lines.add(units.getJavaSourceStartLineNumber());
        }
        return dead_codes_lines;
    }

    public Set<Unit> getResults() {
        return dead_codes;
    }


    private Set<Unit> getTrap(Body body) {
        Set<Unit> trap = new HashSet<>();
        for (Trap t : body.getTraps()) {
            trap.add(t.getEndUnit());
        }
        return trap;
    }
}
