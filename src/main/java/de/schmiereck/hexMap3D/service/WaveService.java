package de.schmiereck.hexMap3D.service;

public class WaveService {

    public static Wave createNextRotatedWave(final Event event, final WaveMoveCalc waveMoveCalc, final int rotationCalcPos,
                                             final int waveProb) {
        final Wave newWave = new Wave(event, waveMoveCalc, rotationCalcPos, waveProb);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNextRotatedWave2(final Event event, final WaveMoveCalc waveMoveCalc, final int rotationCalcPos,
                                             final int waveProbDenominator, final int wavePropDivisior) {
        final Wave newWave = new Wave(event, waveMoveCalc, rotationCalcPos, waveProbDenominator, wavePropDivisior);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNextMovedWave(final Wave sourceWave, final int propDivider) {
        final Event event = sourceWave.getEvent();
        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
        final WaveMoveCalc newWaveMoveCalc = WaveMoveCalcService.createNextWaveMoveCalc(sourceWaveMoveCalc);
        final Wave newWave = new Wave(event, newWaveMoveCalc, sourceWave.nextRotationCalcPos(),
                //sourceWave.getWaveProbDenominator(), sourceWave.getWaveProbDivisior() * propDivider);
                sourceWave.getWaveProb() > 1 ? sourceWave.getWaveProb() - 1 : 1);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNewWave(final Event event, final WaveMoveDir waveMoveDir, final int waveProb) {
        final WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createWaveMoveCalc(0, WaveMoveDirService.createWaveMoveDir(waveMoveDir.getMoveDirProbArr()));
        //final Wave newWave = new Wave(event, waveMoveCalc, 0, 1, 1);
        final Wave newWave = new Wave(event, waveMoveCalc, 0, waveProb);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNewWave(final Event event, final WaveMoveDir waveMoveDir) {
        return createNewWave(event, waveMoveDir, 1);
    }
}
