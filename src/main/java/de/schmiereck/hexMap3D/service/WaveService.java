package de.schmiereck.hexMap3D.service;

public class WaveService {

    public static Wave createNextRotatedWave(final Event event, final WaveMoveCalc waveMoveCalc, final int rotationCalcPos,
                                             final int waveProbDenominator, final int waveProp) {
        final Wave newWave = new Wave(event, waveMoveCalc, rotationCalcPos, waveProbDenominator, waveProp);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNextMovedWave(final Wave sourceWave, final int propDivider) {
        final Event event = sourceWave.getEvent();
        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
        final WaveMoveCalc newWaveMoveCalc = WaveMoveCalcService.createNextWaveMoveCalc(sourceWaveMoveCalc);
        final Wave newWave = new Wave(event, newWaveMoveCalc, sourceWave.nextRotationCalcPos(),
                sourceWave.getWavePropDenominator(), sourceWave.getWaveProp() * propDivider);
        event.addWave(newWave);
        return newWave;
    }

    public static Wave createNewWave(final Event event, final WaveMoveDir waveMoveDir) {
        final WaveMoveCalc waveMoveCalc = WaveMoveCalcService.createWaveMoveCalc(0, WaveMoveDirService.createWaveMoveDir(waveMoveDir.getMoveDirPropArr()));
        final Wave newWave = new Wave(event, waveMoveCalc, 0, 1, 1);
        event.addWave(newWave);
        return newWave;
    }
}
