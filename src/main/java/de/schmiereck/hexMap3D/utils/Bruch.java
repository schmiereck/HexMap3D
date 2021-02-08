package de.schmiereck.hexMap3D.utils;

public class Bruch {
    /**
     * Denominator of {@link #waveProbDenominator}/{@link #waveProbDivisior} representation of the probability.
     */
    private int waveProbDenominator;
    /**
     * Divisior of {@link #waveProbDenominator}/{@link #waveProbDivisior} representation of the probability.
     */
    private int waveProbDivisior;

    public Bruch(final int waveProbDenominator, final int waveProbDivisior) {
        this.waveProbDenominator = waveProbDenominator;
        if ((waveProbDivisior <= 0)) throw new AssertionError("waveProbDivisior <= 0");
        this.waveProbDivisior = waveProbDivisior;
    }

    public void setWaveProbFraction(final int wavePropDenominator, final int wavePropDivisior) {
        this.waveProbDenominator = wavePropDenominator;
        if ((wavePropDivisior <= 0)) throw new AssertionError("waveProbDivisior <= 0");
        this.waveProbDivisior = wavePropDivisior;
    }

    public void setWaveProbDenominator(final int waveProbDenominator) {
        this.waveProbDenominator = waveProbDenominator;
    }

    public int getWaveProbDenominator() {
        return this.waveProbDenominator;
    }

    public void setWaveProbDivisior(final int waveProbDivisior) {
        this.waveProbDivisior = waveProbDivisior;
    }

    public int getWaveProbDivisior() {
        return this.waveProbDivisior;
    }

    public static void add(final Bruch cellWave, final Bruch wave) {
        if (cellWave != null) {
            final int probDenominator = wave.getWaveProbDivisior() * cellWave.getWaveProbDenominator() +
                    cellWave.getWaveProbDivisior() * wave.getWaveProbDenominator();
            final int probDivisior = wave.getWaveProbDivisior() * cellWave.getWaveProbDivisior();
            final int factor = euclideanAlgorithm(probDenominator, probDivisior);
            cellWave.setWaveProbFraction(probDenominator / factor, probDivisior / factor);
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
