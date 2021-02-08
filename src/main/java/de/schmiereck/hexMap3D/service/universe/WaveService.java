package de.schmiereck.hexMap3D.service.universe;

public class WaveService {

    public static void calcActualWaveMoveCalcDir(final Wave wave) {
        WaveMoveCalcService.calcActualWaveMoveCalcDir(wave.getWaveMoveCalc());
    }

    public static Wave createNextRotatedWave(final Event event, final WaveMoveCalc waveMoveCalc, final int rotationCalcPos,
                                             final int waveProb) {
        final Wave newWave = new Wave(event, waveMoveCalc, rotationCalcPos, waveProb);
        return newWave;
    }

    public static Wave createNextRotatedWave(final Wave sourceWave, final int waveProb) {
        final Event event = sourceWave.getEvent();
        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
        final WaveMoveCalc newWaveMoveCalc = WaveMoveCalcService.createRotatedWaveMoveCalc(sourceWaveMoveCalc);
        final Wave newWave = new Wave(event, newWaveMoveCalc, WaveRotationService.nextRotationCalcPos(sourceWave), waveProb);
        return newWave;
    }

    public static Wave createNewInitialWave(final Event event, final WaveMoveDir waveMoveDir, final int waveProb) {
        final WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createNewInitalWaveMoveCalc(0, WaveMoveDirService.createWaveMoveDir(waveMoveDir.getMoveDirProbArr()));
        //final Wave newWave = new Wave(event, waveMoveCalc, 0, 1, 1);
        final Wave newWave = new Wave(event, waveMoveCalc, 0, waveProb);
        return newWave;
    }

}
