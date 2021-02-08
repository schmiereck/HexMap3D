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
 * Wird eine Welle abgespalten wird ihr Wahrscheinlichkeitzähler auf das doppelte der Ausgangswelle gesetzt.
 * Wird eine Welle abgespalten wird ihr Wahrscheinlichkeitzähler um einen höher der Ausgangswelle gesetzt.
 *              wave1
 *  localProp:  1
 *  f():        100
 *
 *              wave1   wave1.1
 *  localProp:  1       2
 *  f():        50     25
 *
 *              wave1   wave1.1   wave1.1.1  wave1.2
 *  localProp:  1       2         4          2
 *  f():        25      12        6,..       12
 *
 *  f() = (100 / localProp) / count
 */
public class Wave  implements Comparable<Wave> {
    private final Event event;
    private WaveMoveCalc waveMoveCalc;
    /**
     * Pos in {@link WaveRotationService#rotationMatrixXYZ}.
     */
    private int rotationCalcPos;
    /**
     * If two waves combines, their probabilities are added.
     */
    private int waveProb;

    public Wave(final Event event, final WaveMoveCalc waveMoveCalc, final int rotationCalcPos,
                final int waveProb) {
        this.event = event;
        this.waveMoveCalc = waveMoveCalc;
        this.rotationCalcPos = rotationCalcPos;
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
        return ret;
    }

    private int hashCode = 0;
    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = Objects.hash(this.waveProb, this.event, this.waveMoveCalc);
        }
        return hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        final Wave entry = (Wave) obj;
        return (this.waveProb == entry.waveProb) &&
               (this.event.equals(entry.event)) &&
               (this.waveMoveCalc.equals(entry.waveMoveCalc));
    }
}
