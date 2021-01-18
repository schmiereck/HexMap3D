package de.schmiereck.hexMap3D.service;

import java.util.Arrays;

public class WaveMoveCalcDir {
    /**
     * Probability of the wave going in this direction.
     */
    private int dirCalcProp;

    public WaveMoveCalcDir(final int dirCalcProp) {
        this.dirCalcProp = dirCalcProp;
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

    @Override
    public String toString() {
        return "{prop:" + this.dirCalcProp + "";
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final WaveMoveCalcDir entry = (WaveMoveCalcDir) obj;
        return this.dirCalcProp == entry.dirCalcProp;
    }
}
