package de.schmiereck.hexMap3D.service;

public class CellService {

    public static void addWave(final CellState cellState, final Wave wave) {
        final Wave cellWave = cellState.searchWave(wave);
        if (cellWave != null) {
            cellWave.setWaveProb(wave.getWaveProb() + cellWave.getWaveProb());
        } else {
            cellState.addWave(wave);
        }
    }
}
