package de.schmiereck.hexMap3D.service;

public class Wave {
    private final Event event;
    private Cell cell;
    private Cell.Dir dir = null;
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
        final Wave wave = createWave(this.dir, this.placePulseProbCalc);
        this.event.addWave(wave);
        return wave;
    }

    public Wave createWave(final Cell.Dir dir) {
        final Wave wave = createWave(dir, this.placePulseProbCalc);
        this.event.addWave(wave);
        return wave;
    }

    /**
     * Copy and calc next Probability-Tick-Pos.
     */
    public Wave createWave(final Cell.Dir dir, final ProbabilityCalc placePulseProbCalc) {
        final Wave wave = new Wave(this.event, placePulseProbCalc);
        wave.setDir(dir);
        return wave;
    }

    public Cell.Dir getDir() {
        return this.dir;
    }

    public void setDir(final Cell.Dir dir) {
        this.dir = dir;
    }
}
