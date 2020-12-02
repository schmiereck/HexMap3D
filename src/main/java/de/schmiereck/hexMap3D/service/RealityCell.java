package de.schmiereck.hexMap3D.service;

import java.util.Arrays;

public class RealityCell {
    private int waveCount;
    private boolean barrier;
    private final int[] outputs;

    RealityCell() {
        this.waveCount = 0;
        this.barrier = false;
        this.outputs = new int[12];
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

    public int[] getOutputs() {
        return this.outputs;
    }

    public void clearReality() {
        this.setWaveCount(0);
        this.setBarrier(false);
        Arrays.stream(Cell.Dir.values()).forEach(dir -> {
            this.outputs[dir.dir()] = 0;
        });
    }
}
