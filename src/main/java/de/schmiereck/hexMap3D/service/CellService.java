package de.schmiereck.hexMap3D.service;

public class CellService {

    public static void addWave(final Cell cell, final Wave wave) {
        final Wave cellWave = cell.searchWave(wave);
        if (cellWave != null) {
            cellWave.setWaveProb(wave.getWaveProb() + cellWave.getWaveProb());
        } else {
            cell.addWave(wave);
        }
    }

    public static void addWave2(final Cell cell, final Wave wave) {
        final Wave cellWave = cell.searchWave(wave);
        if (cellWave != null) {
            final int probDenominator = wave.getWaveProbDivisior() * cellWave.getWaveProbDenominator() +
                                        cellWave.getWaveProbDivisior() * wave.getWaveProbDenominator();
            final int probDivisior = wave.getWaveProbDivisior() * cellWave.getWaveProbDivisior();
            final int factor = euclideanAlgorithm(probDenominator, probDivisior);
            cellWave.setWaveProbFraction(probDenominator / factor, probDivisior / factor);
        } else {
            cell.addWave(wave);
        }
    }

    private static int euclideanAlgorithm(final int denominator, final int divisior) {
        int a = denominator; // zaehler
        int b = divisior; // nenner
        int h;
        while (b != 0) {
            h = a % b;
            a = b;
            b = h;
        }
        return a;
    }
}
