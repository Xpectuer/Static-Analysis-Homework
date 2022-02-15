package org.xpectuer.constantPropagation;

import org.xpectuer.constantPropagation.exceptions.SetValueException;

import java.util.HashMap;
import java.util.Map;

class LatticeValue {

    static final LatticeValue undef = new LatticeValue(Integer.MAX_VALUE, State.UNDEF);
    static final LatticeValue nac = new LatticeValue(Integer.MIN_VALUE, State.NAC);
    static Map<Integer, LatticeValue> cache = new HashMap<>();
    // UNDEF or NAC holds no value
    private final State state;
    private int value;

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
        if (op1.state == State.NAC) return op1;
        if (op2.state == State.NAC) return op2;

        //UNDEF meets v = v
        if (op1.state == State.UNDEF) return op2;
        if (op2.state == State.UNDEF) return op1;

        //CONSTANT meets v
        // neither op1 nor op2 can be UNDEF or NAC
        if (op1.getValue() == op2.getValue()) {
            return op1;
        } else {
            return LatticeValue.nac;
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

    public void setValue(int value) throws SetValueException {
        if (this.state == State.NAC || this.state == State.UNDEF)
            throw new SetValueException("Cannot set value for a UNDEF or NAC state value");
        this.value = value;
    }

    @Override
    public String toString() {

        if(this.isNAC()) {
            return "NAC";
        } else if(this.isUNDEF()) {
            return "UNDEF";
        } else {
            return Integer.toString(this.getValue());
        }

    }
}
