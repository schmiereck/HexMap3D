package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static de.schmiereck.hexMap3D.MapLogicUtils.calcBreakLoopWrap;
import static de.schmiereck.hexMap3D.MapLogicUtils.calcBreakLoopWrap2;
import static de.schmiereck.hexMap3D.MapMathUtils.wrap;
import static de.schmiereck.hexMap3D.service.WaveMoveDirService.createMoveRotatedWaveMoveDir;

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

    public static boolean useRotateMoveDirCache = true;

    private static class RotateMoveDirCacheEntry {
        final WaveMoveDir inWaveMoveDir;
        WaveMoveDir outWaveMoveDir;
        final int xRotPercent;
        final int yRotPercent;
        final int zRotPercent;

        private RotateMoveDirCacheEntry(final WaveMoveDir inWaveMoveDir, final int xRotPercent, final int yRotPercent, final int zRotPercent) {
            this.inWaveMoveDir = inWaveMoveDir;
            this.xRotPercent = xRotPercent;
            this.yRotPercent = yRotPercent;
            this.zRotPercent = zRotPercent;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.inWaveMoveDir, this.xRotPercent, this.yRotPercent, this.zRotPercent);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (this.getClass() != obj.getClass()) return false;
            final RotateMoveDirCacheEntry entry = (RotateMoveDirCacheEntry) obj;
            return this.xRotPercent == entry.xRotPercent &&
                    this.yRotPercent == entry.yRotPercent &&
                    this.zRotPercent == entry.zRotPercent &&
                    (this.inWaveMoveDir.equals(entry.inWaveMoveDir));
        }
    }

    private static final Map<RotateMoveDirCacheEntry, RotateMoveDirCacheEntry> rotateMoveDirCacheSet = new HashMap<>();

    protected static Wave createMoveRotatedWave(final Wave sourceWave,
                                                final int xRotPercent,
                                                final int yRotPercent,
                                                final int zRotPercent,
                                                final int newWaveProb) {
        final Wave newWave;

        final WaveMoveCalc sourceWaveMoveCalc = sourceWave.getWaveMoveCalc();
        final WaveMoveDir sourceWaveMoveDir = sourceWaveMoveCalc.getWaveMoveDir();
        final WaveMoveDir newWaveMoveDir;

        if (useRotateMoveDirCache) {
            final RotateMoveDirCacheEntry newRotateMoveDirCacheEntry = new RotateMoveDirCacheEntry(sourceWaveMoveDir, xRotPercent, yRotPercent, zRotPercent);
            final RotateMoveDirCacheEntry rotateMoveDirCacheEntry = rotateMoveDirCacheSet.get(newRotateMoveDirCacheEntry);
            if (rotateMoveDirCacheEntry != null) {
                newWaveMoveDir = rotateMoveDirCacheEntry.outWaveMoveDir;
            } else {
                newWaveMoveDir = createMoveRotatedWaveMoveDir(sourceWaveMoveDir, xRotPercent, yRotPercent, zRotPercent);
                newRotateMoveDirCacheEntry.outWaveMoveDir = newWaveMoveDir;
                rotateMoveDirCacheSet.put(newRotateMoveDirCacheEntry, newRotateMoveDirCacheEntry);
            }
        } else {
            newWaveMoveDir = createMoveRotatedWaveMoveDir(sourceWaveMoveDir, xRotPercent, yRotPercent, zRotPercent);
        }

        final WaveMoveCalc newWaveMoveCalc = WaveMoveCalcService.createRotatedWaveMoveCalc(sourceWaveMoveCalc, newWaveMoveDir);

        newWave = WaveService.createNextRotatedWave(sourceWave.getEvent(), newWaveMoveCalc,
                sourceWave.nextRotationCalcPos(),
                newWaveProb);//sourceWave.getWaveProb());
                //sourceWave.getRotationCalcPos());

        return newWave;
    }
}
