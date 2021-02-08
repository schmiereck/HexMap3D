package de.schmiereck.hexMap3D.service.reality;

import de.schmiereck.hexMap3D.service.universe.Cell;

import java.util.Arrays;

public class RealityCell {
    private int waveCount;
    private int waveProb;
    private boolean barrier;
    private boolean showGrid;
    private final int[] outputs;

    RealityCell() {
        this.waveCount = 0;
        this.waveProb = 0;
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

    public void setWaveProb(final int waveProb) {
        this.waveProb = waveProb;
    }

    public int getWaveProb() {
        return this.waveProb;
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

    public void setShowGrid(final boolean showGrid) {
        this.showGrid = showGrid;
    }

    public boolean getShowGrid() {
        return this.showGrid;
    }
}
