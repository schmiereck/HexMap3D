package de.schmiereck.hexMap3D.service.universe;

import java.util.Objects;

/**
 * Solution 1:
 * -----------
 * Eine Welle hat eine Wahrscheinlichkeit von localProp mit 100.
 * Wird eine Welle mit 10% von ihr abgespalten wird bei dieser Welle abgelegt:
 * parentProp:100
 * localProp:10
 * und die localProp der Ausgangs-Welle auf 90 gesetzt.
 *              wave1
 *  parentProp: 100
 *  localProp:  100
 *  f():        100
 *
 *              wave1   wave1.1
 *  parentProp: 100     100
 *  localProp:  90      10
 *  f():        90      10
 *
 *              wave1   wave1.1   wave1.1.1  wave1.2
 *  parentProp: 100     100       10         90
 *  localProp:  81      9         10         10
 *  f():        81      9         1          9
 *
 *  f() = (parentProp * localProp) / 100
 *
 * Solution 2:
 * -----------
 * Wird eine Welle abgespalten wird ihr Wahrscheinlichswert waveProb
 *  wenn > 1: aufgeteilt.
 *  wenn = 1: bleibt für beide Wellen.
 * Verschmelzen zwei Wellen ändert sich für waveProbEventSum nichts.
 * Wird eine Welle glöscht, wird waveProbEventSum um waveProb reduziert.
 *              wave1
 *  waveProb:   1
 *  waveProbEventSum: 1
 *  f():        1/1
 *
 *              wave1   wave1.1
 *  waveProb:   1       1
 *  waveProbEventSum: 2
 *  f():        1/2     1/2
 *
 *              wave1   wave1.1   wave1.1.1  wave1.2
 *  waveProb:   1       1         1          2
 *  waveProbEventSum: 5
 *  f():        1/5     1/5       1/5        2/5
 *
 *              1
 *  f():        1/1
 *              1       1
 *  f():        1/2     1/2
 *              1       1       1
 *  f():        1/3     1/3     1/3
 *              1       2
 *  f():        1/3     2/3
 *              1       1       1
 *  f():        1/3     1/3     1/3
 *  ???:
 *              1       3
 *  f():        1/4     3/4
 *              0,25    0,75
 *              1       3       3
 *  f():        1/7     3/7     3/7
 *              0,14    0,42    0,42
 *
 *  f() = (waveProb / SumOfAllWaveProbs-for-Event)
 */
public class Wave  implements Comparable<Wave> {
    private final Event event;
    private WaveMoveCalc waveMoveCalc;
    /**
     * Pos in {@link WaveRotationService#rotationMatrixXYZ}.
     */
    private int rotationCalcPos;
    /**
     * {@link Cell.Dir#dir()}
     */
    private int waveDirCalcPos;
    /**
     * If two waves combines, their probabilities are added.
     * Calculation of the Probalility:
     * p = waveProb / SumOfAllWaveProbs-for-Event
     */
    private int waveProb;

    public Wave(final Event event, final WaveMoveCalc waveMoveCalc, final int rotationCalcPos, final int waveDirCalcPos,
                final int waveProb) {
        this.event = event;
        this.waveMoveCalc = waveMoveCalc;
        this.rotationCalcPos = rotationCalcPos;
        this.waveDirCalcPos = waveDirCalcPos;
        this.waveProb = waveProb;
    }

    public Event getEvent() {
        return this.event;
    }

    //public int nextDirCalcPos() {
    //    return this.waveMoveDir.nextDirCalcPos();
    //}

    //public void setDir(final Cell.Dir dir, final int dirCalcProp) {
    //    this.waveMoveDir.setDir(dir, dirCalcProp);
    //}

    public int[] getMoveCalcDirArr() {
        return this.waveMoveCalc.getWaveMoveDir().getMoveDirProbArr();
    }

    public int getMoveCalcDir(final Cell.Dir dir) {
        return this.waveMoveCalc.getWaveMoveDir().getDirMoveProb(dir);
    }

    public int getActualWaveMoveCalcDir() {
        return this.waveMoveCalc.getActualWaveMoveCalcDir();
    }

    public Cell.Dir getActualDirCalcPos() {
        return this.waveMoveCalc.getActualMoveDir();
    }

    public int getRotationCalcPos() {
        return this.rotationCalcPos;
    }

    public int getWaveDirCalcPos() {
        return this.waveDirCalcPos;
    }

    public WaveMoveDir getWaveMoveDir() {
        return this.waveMoveCalc.getWaveMoveDir();
    }

    public WaveMoveCalc getWaveMoveCalc() {
        return this.waveMoveCalc;
    }

    public int getDirCalcProbSum(final Cell.Dir dir) {
        return this.waveMoveCalc.getDirCalcProbSum(dir);
    }

    public int getDirCalcProb(Cell.Dir dir) {
        return this.getMoveCalcDirArr()[dir.dir()];
    }

    public void setWaveProb(final int waveProb) {
        this.waveProb = waveProb;
    }

    public int getWaveProb() {
        return this.waveProb;
    }

    @Override
    public int compareTo(final Wave wave) {
        int ret = this.waveProb - wave.waveProb;
        if (ret == 0) {
            ret = this.waveDirCalcPos - wave.waveDirCalcPos;
            if (ret == 0) {
                final int[] thisMoveDirProbArr = this.waveMoveCalc.getWaveMoveDir().getMoveDirProbArr();
                final int[] waveMoveDirProbArr = wave.waveMoveCalc.getWaveMoveDir().getMoveDirProbArr();
                for (int pos = 0; pos < thisMoveDirProbArr.length; pos++) {
                    final int diff = thisMoveDirProbArr[pos] - waveMoveDirProbArr[pos];
                    if (diff != 0) {
                        ret = diff;
                        break;
                    }
                }
            }
        }
        return ret;
    }

    private int hashCode = 0;
    @Override
    public int hashCode() {
        if (hashCode == 0) {
            //hashCode = Objects.hash(this.waveProb, this.waveDirCalcPos, this.event, this.waveMoveCalc);
            hashCode = Objects.hash(this.waveDirCalcPos, this.event, this.waveMoveCalc);
        }
        return hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final Wave entry = (Wave) obj;
        return //(this.waveProb == entry.waveProb) &&
                (this.waveDirCalcPos == entry.waveDirCalcPos) &&
                Objects.equals(this.event, entry.event) &&
                Objects.equals(this.waveMoveCalc, entry.waveMoveCalc);
    }
}
