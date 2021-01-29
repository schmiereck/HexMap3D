package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static de.schmiereck.hexMap3D.MapLogicUtils.calcBreakLoopWrap;
import static de.schmiereck.hexMap3D.MapLogicUtils.calcBreakLoopWrap2;
import static de.schmiereck.hexMap3D.MapMathUtils.wrap;

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

    protected static Wave createMoveRotatedWave(final Wave sourceWave,
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
                sourceWave.nextRotationCalcPos(),
                newWaveProb);//sourceWave.getWaveProb());
                //sourceWave.getRotationCalcPos());

        return newWave;
    }
}
