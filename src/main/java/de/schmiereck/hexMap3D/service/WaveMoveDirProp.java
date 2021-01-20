package de.schmiereck.hexMap3D.service;

import java.util.Objects;

public class WaveMoveDirProp {
    /**
     * Probability of the wave going in this direction.
     */
    private int dirMoveProp;

    public WaveMoveDirProp(final int dirMoveProp) {
        this.dirMoveProp = dirMoveProp;
    }

    public int getDirMoveProp() {
        return this.dirMoveProp;
    }

    public void setDirMoveProp(final int dirMoveProp) {
        this.dirMoveProp = dirMoveProp;
    }

    public void addDirMoveProp(final int dirMoveProp) {
        this.dirMoveProp += dirMoveProp;
    }

    @Override
    public String toString() {
        return "{prop:" + this.dirMoveProp + "";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dirMoveProp);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final WaveMoveDirProp entry = (WaveMoveDirProp) obj;
        return this.dirMoveProp == entry.dirMoveProp;
    }
}
