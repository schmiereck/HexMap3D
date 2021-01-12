package de.schmiereck.hexMap3D.service;

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
}
