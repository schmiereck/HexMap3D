package de.schmiereck.hexMap3D.service.reality;

public class DetectorCell {
    final int waveProbSum;

    public DetectorCell(final int waveProbSum) {
        this.waveProbSum = waveProbSum;
    }

    public int getWaveProbSum() {
        return this.waveProbSum;
    }
}
