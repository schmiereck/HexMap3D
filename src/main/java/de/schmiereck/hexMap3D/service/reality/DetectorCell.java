package de.schmiereck.hexMap3D.service.reality;

public class DetectorCell {
    final int waveProbSum;
    final int[] colors;

    public DetectorCell(final int waveProbSum, final int[] colors) {
        this.waveProbSum = waveProbSum;
        this.colors = colors;
    }

    public int getWaveProbSum() {
        return this.waveProbSum;
    }

    public int[] getColors() {
        return this.colors;
    }
}
