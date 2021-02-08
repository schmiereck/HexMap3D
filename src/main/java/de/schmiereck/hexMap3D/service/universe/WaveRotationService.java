package de.schmiereck.hexMap3D.service.universe;

import de.schmiereck.hexMap3D.MapMathUtils;

public class WaveRotationService {

    // Rotation-Matrix X, Y, Z:
    public static final int[][] rotationMatrixXYZ =
            {
              // X   Y   Z
              {  1,  0,  0 },
              {  1,  1,  0 },
              {  0,  1,  0 },
              {  0,  1,  1 },
              {  0,  0,  1 },
              {  1,  0,  1 },

              { -1,  0,  0 },
              { -1, -1,  0 },
              {  0, -1,  0 },
              {  0, -1, -1 },
              {  0,  0, -1 },
              { -1,  0, -1 },

              /*
              { -1,  0,  1 },
              {  1,  0, -1 }
              */
            };

    public static Wave createMoveRotatedWave(final Wave sourceWave,
                                             final int xRotPercent,
                                             final int yRotPercent,
                                             final int zRotPercent,
                                             final int newWaveProb) {
        final Wave newWave;

        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
        final WaveMoveDir sourceWaveMoveDir = sourceWaveMoveCalc.getWaveMoveDir();
        final WaveMoveDir newWaveMoveDir =
                WaveMoveDirService.createRotatedWaveMoveDir(sourceWaveMoveDir, xRotPercent, yRotPercent, zRotPercent);

        final WaveMoveCalc newWaveMoveCalc = WaveMoveCalcService.createRotatedWaveMoveCalc(sourceWaveMoveCalc, newWaveMoveDir);

        newWave = WaveService.createNextRotatedWave(sourceWave.getEvent(), newWaveMoveCalc,
                nextRotationCalcPos(sourceWave),
                newWaveProb);//sourceWave.getWaveProb());
                //sourceWave.getRotationCalcPos());

        return newWave;
    }

    public static int nextRotationCalcPos(final Wave wave) {
        return MapMathUtils.wrap(wave.getRotationCalcPos() + 1, WaveRotationService.rotationMatrixXYZ.length);
    }
}
