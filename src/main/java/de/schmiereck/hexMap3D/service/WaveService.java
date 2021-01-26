package de.schmiereck.hexMap3D.service;

public class WaveService {

    public static Wave createNextRotatedWave(final Event event, final WaveMoveCalc waveMoveCalc, final int rotationCalcPos,
                                             final int waveProb) {
        final Wave newWave = new Wave(event, waveMoveCalc, rotationCalcPos, waveProb);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNextMovedWave(final Wave sourceWave, final int waveProb) {
        final Event event = sourceWave.getEvent();
        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
        final WaveMoveCalc newWaveMoveCalc = WaveMoveCalcService.createNextWaveMoveCalc(sourceWaveMoveCalc);
        final Wave newWave = new Wave(event, newWaveMoveCalc, sourceWave.nextRotationCalcPos(), waveProb);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNewInitialWave(final Event event, final WaveMoveDir waveMoveDir, final int waveProb) {
        final WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createWaveMoveCalc(0, WaveMoveDirService.createWaveMoveDir(waveMoveDir.getMoveDirProbArr()));
        //final Wave newWave = new Wave(event, waveMoveCalc, 0, 1, 1);
        final Wave newWave = new Wave(event, waveMoveCalc, 0, waveProb);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNewTestWave(final Event event, final WaveMoveDir waveMoveDir) {
        return createNewInitialWave(event, waveMoveDir, 1);
    }
}
