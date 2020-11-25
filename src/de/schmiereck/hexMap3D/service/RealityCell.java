package de.schmiereck.hexMap3D.service;

public class RealityCell {
    private int waveCount;
    private boolean barrier;

    RealityCell() {
        this.waveCount = 0;
        this.barrier = false;
    }

    public void addWaveCount(final int waveCount) {
        this.waveCount += waveCount;
    }

    public void setWaveCount(final int waveCount) {
        this.waveCount = waveCount;
    }

    public int getWaveCount() {
        return this.waveCount;
    }

    public void setBarrier(final boolean barrier) {
        this.barrier = barrier;
    }

    public boolean getBarrier() {
        return this.barrier;
    }

    public void clearReality() {
        this.setWaveCount(0);
        this.setBarrier(false);
    }
}
