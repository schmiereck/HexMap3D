package de.schmiereck.hexMap3D.service.universe;

import java.util.Objects;

public class WaveMoveDirProb {
    /**
     * Probability of the wave going in this direction.
     */
    private int dirMoveProb;

    public WaveMoveDirProb(final int dirMoveProb) {
        this.dirMoveProb = dirMoveProb;
    }

    public int getDirMoveProb() {
        return this.dirMoveProb;
    }

    public void setDirMoveProb(final int dirMoveProb) {
        this.dirMoveProb = dirMoveProb;
    }

    public void addDirMoveProb(final int dirMoveProb) {
        this.dirMoveProb += dirMoveProb;
    }

    @Override
    public String toString() {
        return "{prop:" + this.dirMoveProb + "";
    }

    private int hashCode = 0;
    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = Objects.hash(this.dirMoveProb);
        }
        return hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final WaveMoveDirProb entry = (WaveMoveDirProb) obj;
        return this.dirMoveProb == entry.dirMoveProb;
    }
}
