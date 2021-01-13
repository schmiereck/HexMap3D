package de.schmiereck.hexMap3D.service;

import java.util.Arrays;

public class WaveMoveCalcDir {
    /**
     * Probability of the wave going in this direction.
     */
    private int dirCalcProp;
    /**
     * Counter between 0 and {@link #dirCalcProp}.
     */
    private int dirCalcPropSum;

    public WaveMoveCalcDir(final int dirCalcProp, final int dirCalcPropSum) {
        this.dirCalcProp = dirCalcProp;
        this.dirCalcPropSum = dirCalcPropSum;
    }

    public int getDirCalcProp() {
        return this.dirCalcProp;
    }

    public void setDirCalcProp(final int dirCalcProp) {
        this.dirCalcProp = dirCalcProp;
    }

    public void addDirCalcProp(final int dirCalcProp) {
        this.dirCalcProp += dirCalcProp;
    }

    public int getDirCalcPropSum() {
        return this.dirCalcPropSum;
    }

    public void setDirCalcPropSum(final int dirCalcPropSum) {
        this.dirCalcPropSum = dirCalcPropSum;
    }

    public void addDirCalcPropSum(final int dirCalcPropSum) {
        this.dirCalcPropSum += dirCalcPropSum;
    }

    @Override
    public String toString() {
        return "{prop:" + this.dirCalcProp + ", sum:" + this.dirCalcPropSum + "}";
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final WaveMoveCalcDir entry = (WaveMoveCalcDir) obj;
        return this.dirCalcProp == entry.dirCalcProp &&
                this.dirCalcPropSum == entry.dirCalcPropSum;
    }
}
