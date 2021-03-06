package de.schmiereck.hexMap3D.service.universe;

public class WaveService {

    public static Wave createNewInitialWave(final Event event, final WaveMoveDir waveMoveDir, final int newWaveProb,
                                            final int waveValueDir, final int waveValueValue) {
        final WaveMoveDir newWaveMoveDir= WaveMoveDirService.createWaveMoveDir(waveMoveDir.getMoveDirProbArr());
        final WaveMoveCalc waveMoveCalc =
                WaveMoveCalcService.createNewInitalWaveMoveCalc(0, newWaveMoveDir);
        final WaveValue waveValue = new WaveValue(waveValueDir, waveValueValue);
        //final Wave newWave = new Wave(event, waveMoveCalc, 0, 1, 1);
        final Wave newWave = new Wave(event, waveMoveCalc, 0, 0, newWaveProb, waveValue);
        WaveMoveDirService.adjustMaxProb(newWave.getWaveMoveDir());
        WaveMoveCalcService.calcActualWaveMoveCalcDir(newWave.getWaveMoveCalc());
        return newWave;
    }

    public static Wave createNextMovedWave(final Event event, final WaveMoveCalc waveMoveCalc,
                                           final int rotationCalcPos, final int waveDirCalcPos,
                                           final int waveProb, final WaveValue waveValue) {
        final Wave newWave = new Wave(event, waveMoveCalc, rotationCalcPos, waveDirCalcPos, waveProb, waveValue);
        return newWave;
    }

    public static Wave createNextMovedWave(final Wave sourceWave, final int newWaveProb) {
        final Event event = sourceWave.getEvent();
        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
        final WaveMoveCalc newWaveMoveCalc = WaveMoveCalcService.createMovedWaveMoveCalc(sourceWaveMoveCalc);
        final WaveValue waveValue = sourceWave.getWaveValue();
        //final WaveValue newWaveValue = createNextWaveValue(sourceWave);
        final Wave newWave = new Wave(event, newWaveMoveCalc,
                WaveRotationService.nextRotationCalcPos(sourceWave),
                WaveRotationService.nextWaveDirCalcPos(sourceWave),
                newWaveProb,
                waveValue);
        return newWave;
    }

    public static final int MAX_WAVE_VALUE = 128;
    public static Wave createNextDistributedWave(final Wave sourceWave, final int newWaveProb) {
        final Event event = sourceWave.getEvent();
        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
        final WaveMoveCalc newWaveMoveCalc = WaveMoveCalcService.createMovedWaveMoveCalc(sourceWaveMoveCalc);
        final int newWaveValueDir;
        final WaveValue newWaveValue = createNextWaveValue(sourceWave);
        final Wave newWave = new Wave(event, newWaveMoveCalc,
                WaveRotationService.nextRotationCalcPos(sourceWave),
                sourceWave.getWaveDirCalcPos(),
                newWaveProb,
                newWaveValue);
        return newWave;
    }

    private static WaveValue createNextWaveValue(Wave sourceWave) {
        final int newWaveValueDir;
        final WaveValue waveValue = sourceWave.getWaveValue();
        final int value = waveValue.getWaveValue() + waveValue.getWaveValueDir();
        final int newValue;
        if ((value > MAX_WAVE_VALUE) || (value < -MAX_WAVE_VALUE)) {
            newWaveValueDir = -waveValue.getWaveValueDir();
            newValue = value + newWaveValueDir;
        } else {
            newWaveValueDir = waveValue.getWaveValueDir();
            newValue = value;
        }
        final WaveValue newWaveValue = new WaveValue(newWaveValueDir, newValue);
        return newWaveValue;
    }

}
