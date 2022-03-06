package org.xpectuer.constantPropagation;

import java.util.HashMap;
import java.util.Map;

public class LatticeValue {

    static final LatticeValue UNDEF = new LatticeValue(Integer.MAX_VALUE, State.UNDEF) {
        @Override
        public int hashCode() {
            return Integer.MAX_VALUE;
        }
    };
    static final LatticeValue NAC = new LatticeValue(Integer.MIN_VALUE, State.NAC) {
        @Override
        public int hashCode() {
            return Integer.MIN_VALUE;
        }
    };

    static Map<Integer, LatticeValue> cache = new HashMap<>();
    // UNDEF or NAC holds no value
    private final State state;
    private final int value;

    public LatticeValue(int value, State state) {
        this.state = state;
        this.value = value;
    }

    public static LatticeValue newConstant(int intConstant) {
        if (LatticeValue.cache.containsKey(intConstant)) {
            return LatticeValue.cache.get(intConstant);
        }
        LatticeValue.cache.put(intConstant, new LatticeValue(intConstant, State.CONST));
        return cache.get(intConstant);

    }

    /**
     * @param op2
     * @return result of meet
     * <p>
     * implementation of meet operator
     * @this op1
     */
    public LatticeValue meet(LatticeValue op2) {
        LatticeValue op1 = this;
        // NAC meets v = NAC
        if (op1.isNAC()) return op1;
        else if (op2.isNAC()) return op2;

            //UNDEF meets v = v
        else if (op1.isUNDEF()) return op2;
        else if (op2.isUNDEF()) return op1;

            //CONSTANT meets v
            // neither op1 nor op2 can be UNDEF or NAC
        else if (op1.getValue() == op2.getValue()) {
            return op1;
        } else {
            return LatticeValue.NAC;
        }

    }

    public boolean isNAC() {
        return this.state == State.NAC;
    }

    public boolean isUNDEF() {
        return this.state == State.UNDEF;
    }

    public boolean isConst() {
        return this.state == State.CONST;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {

        if (this.isNAC()) {
            return "NAC";
        } else if (this.isUNDEF()) {
            return "UNDEF";
        } else {
            return Integer.toString(this.getValue());
        }

    }
}
