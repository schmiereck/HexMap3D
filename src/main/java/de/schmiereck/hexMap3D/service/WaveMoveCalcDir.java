package de.schmiereck.hexMap3D.service;

public class WaveMoveCalcDir {
    /**
     * Diection of Wave. Wave goes in that direction if {@link #dirCalcPropSum} reaches {@link #dirCalcProp}.
     */
    //private Cell.Dir dir;
    /**
     * Probability of the wave going in this direction.
     */
    private int dirCalcProp;
    /**
     * Counter between 0 and {@link #dirCalcProp}.
     */
    private int dirCalcPropSum;

    public WaveMoveCalcDir(//final Cell.Dir dir,
                           final int dirCalcProp, final int dirCalcPropSum) {
        //this.dir = dir;
        this.dirCalcProp = dirCalcProp;
        this.dirCalcPropSum = dirCalcPropSum;
    }

    public WaveMoveCalcDir(final WaveMoveCalcDir waveMoveCalcDir) {
        //this.dir = waveMoveCalcDir.dir;
        this.dirCalcProp = waveMoveCalcDir.dirCalcProp;
        this.dirCalcPropSum = waveMoveCalcDir.dirCalcPropSum;
    }

//    public Cell.Dir getDir() {
//        return this.dir;
//    }

//    public void setDir(final Cell.Dir dir) {
//        this.dir = dir;
//    }

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
