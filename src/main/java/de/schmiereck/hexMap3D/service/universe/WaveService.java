package de.schmiereck.hexMap3D.service.universe;

public class WaveService {

    public static Wave createNextMovedWave(final Event event, final WaveMoveCalc waveMoveCalc,
                                           final int rotationCalcPos, final int waveDirCalcPos,
                                           final int waveProb) {
        final Wave newWave = new Wave(event, waveMoveCalc, rotationCalcPos, waveDirCalcPos, waveProb);
        return newWave;
    }

    public static Wave createNextMovedWave(final Wave sourceWave, final int newWaveProb) {
        final Event event = sourceWave.getEvent();
        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
        final WaveMoveCalc newWaveMoveCalc = WaveMoveCalcService.createMovedWaveMoveCalc(sourceWaveMoveCalc);
        final Wave newWave = new Wave(event, newWaveMoveCalc,
                WaveRotationService.nextRotationCalcPos(sourceWave),
                WaveRotationService.nextWaveDirCalcPos(sourceWave),
                newWaveProb);
        return newWave;
    }

    public static Wave createNextDistributedWave(final Wave sourceWave, final int newWaveProb) {
        final Event event = sourceWave.getEvent();
        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
        final WaveMoveCalc newWaveMoveCalc = WaveMoveCalcService.createMovedWaveMoveCalc(sourceWaveMoveCalc);
        final Wave newWave = new Wave(event, newWaveMoveCalc,
                WaveRotationService.nextRotationCalcPos(sourceWave),
                sourceWave.getWaveDirCalcPos(),
                newWaveProb);
        return newWave;
    }

    public static Wave createNewInitialWave(final Event event, final WaveMoveDir waveMoveDir, final int newWaveProb) {
        final WaveMoveDir newWaveMoveDir= WaveMoveDirService.createWaveMoveDir(waveMoveDir.getMoveDirProbArr());
        final WaveMoveCalc waveMoveCalc =
                WaveMoveCalcService.createNewInitalWaveMoveCalc(0, newWaveMoveDir);
        //final Wave newWave = new Wave(event, waveMoveCalc, 0, 1, 1);
        final Wave newWave = new Wave(event, waveMoveCalc, 0, 0, newWaveProb);
        WaveMoveDirService.adjustMaxProb(newWave.getWaveMoveDir());
        WaveMoveCalcService.calcActualWaveMoveCalcDir(newWave.getWaveMoveCalc());
        return newWave;
    }

}
