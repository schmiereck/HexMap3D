package de.schmiereck.hexMap3D.service;

import java.util.Arrays;

import de.schmiereck.hexMap3D.service.Cell.Dir;

public class Wave {
    private final Event event;
    private Cell cell;
    //private Cell.Dir dir = null;
    private Dir[] dirArr = new Dir[3];
    private final ProbabilityCalc placePulseProbCalc;

    private boolean extendCalculated = false;

    public Wave(final Event event, final int placePulseProb) {
        this.event = event;
        this.placePulseProbCalc = new ProbabilityCalc(12, 100, placePulseProb);
    }

    /**
     * Copy and calc next Probability-Tick-Pos.
     */
    public Wave(final Event event, final ProbabilityCalc placePulseProbCalc) {
        this.event = event;
        this.placePulseProbCalc = new ProbabilityCalc(placePulseProbCalc);
    }

    public void setCell(final Cell cell) {
        this.cell = cell;
    }

    public Event getEvent() {
        return this.event;
    }

    public boolean getExtendCalculated() {
        return this.extendCalculated;
    }

    public void setExtendCalculated(final boolean extendCalculated) {
        this.extendCalculated = extendCalculated;
    }

    public int getCalcState() {
        final int calcState;
        if (this.placePulseProbCalc.getExecute()) {
            calcState = 0;
        } else {
            calcState = 1;
        }
        return calcState;
    }

    public Wave createWave() {
        final Wave wave = createWave(this.dirArr, this.placePulseProbCalc);
        this.event.addWave(wave);
        return wave;
    }

    public Wave createWave(final Dir[] dirArr) {
        final Wave wave = createWave(dirArr, this.placePulseProbCalc);
        this.event.addWave(wave);
        return wave;
    }

    /**
     * Copy and calc next Probability-Tick-Pos.
     */
    public Wave createWave(final Dir[] dirArr, final ProbabilityCalc placePulseProbCalc) {
        final Wave wave = new Wave(this.event, placePulseProbCalc);
        wave.setDirArr(dirArr);
        return wave;
    }

    public Dir getDir(final int dirNo) {
        return this.dirArr[dirNo];
    }

    public void setDirArr(final Dir[] dirArr) {
        System.arraycopy(dirArr, 0, this.dirArr, 0 , dirArr.length);
    }

    public Dir[] getDirArr() {
        return this.dirArr;
    }

    public void setDir(final int dirNo, final Dir dir) {
        this.dirArr[dirNo] = dir;
    }
}
