package de.schmiereck.hexMap3D.service.reality;

public class DetectorCell {
    final int waveProbSum;
    final int[] colors;
    final int waveValue;

    public DetectorCell(final int waveProbSum, final int[] colors, final int waveValue) {
        this.waveProbSum = waveProbSum;
        this.colors = colors;
        this.waveValue = waveValue;
    }

    public int getWaveProbSum() {
        return this.waveProbSum;
    }

    public int[] getColors() {
        return this.colors;
    }

    public int getWaveValue() {
        return this.waveValue;
    }

}
