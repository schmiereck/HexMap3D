package de.schmiereck.hexMap3D.service;

public class CellService {

    public static void addWave(final Cell cell, final Wave wave) {
        final Wave cellWave = cell.searchWave(wave);
        if (cellWave != null) {
            final int denominator = wave.getWaveProp() * cellWave.getWavePropDenominator() +
                                    cellWave.getWaveProp() * wave.getWavePropDenominator();
            final int prob = wave.getWaveProp() * cellWave.getWaveProp();
            cellWave.setWaveProbFraction(denominator, prob);
        } else {
            cell.addWave(wave);
        }
    }
}
