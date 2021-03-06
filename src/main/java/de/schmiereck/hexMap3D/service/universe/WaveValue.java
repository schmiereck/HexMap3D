package de.schmiereck.hexMap3D.service.universe;

public class WaveValue {

    /**
     * The direction and amount of the change of {@link #waveValue}.
     */
    private final int waveValueDir;

    /**
     * Value is changing between a max and a min value.
     * The direction and amount of the change depends on {@link #waveValueDir}.
     */
    private final int waveValue;

    public WaveValue(final int waveValueDir, final int waveValue) {
        this.waveValueDir = waveValueDir;
        this.waveValue = waveValue;
    }

    public int getWaveValueDir() {
        return this.waveValueDir;
    }

    public int getWaveValue() {
        return this.waveValue;
    }
}
