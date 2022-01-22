package org.xpectuer.ConstantPropagationAnalysis;

import soot.Local;
import soot.jimple.*;

import java.util.HashMap;


public class Pairs extends HashMap<Local, LatticeValue> {
    public LatticeValue computeValue(soot.Value svalue) {
        if (svalue instanceof Local) { // local 变量：寻找变量的常量值
            return get((Local) svalue);
        }
        if (svalue instanceof IntConstant) { // 整数常量（我们只处理整数常量）
            return LatticeValue.newConstant(((IntConstant) svalue).value);
        }
        if (svalue instanceof BinopExpr) { // 二元表达式：解释并计算值
            BinopExpr binopExpr = (BinopExpr) svalue;

            // compute operand 1
            soot.Value op1 = binopExpr.getOp1();
            // recursively get the op1's constant value
            // m(op1)
            LatticeValue lOp1 = computeValue(op1);


            // compute operand 2
            soot.Value op2 = binopExpr.getOp2();
            // recursively get the op1's constant value
            // m(op2)
            LatticeValue lOp2 = computeValue(op2);

            if (lOp2 == LatticeValue.nac || lOp1 == LatticeValue.nac) {
                return LatticeValue.nac;
            } else if (lOp1.isConst() && lOp2.isConst()) {
                if (binopExpr instanceof AddExpr) {
                    return LatticeValue.newConstant(lOp1.getValue() + lOp2.getValue());
                } else if (binopExpr instanceof SubExpr) {
                    return LatticeValue.newConstant(lOp1.getValue() + lOp2.getValue());
                } else if (binopExpr instanceof MulExpr) {
                    return LatticeValue.newConstant(lOp1.getValue() + lOp2.getValue());
                } else if (binopExpr instanceof DivExpr) {
                    return LatticeValue.newConstant(lOp1.getValue() / lOp2.getValue());
                }
            } else {
                return LatticeValue.undef;
            }
        }
        return LatticeValue.nac;
    }
}
