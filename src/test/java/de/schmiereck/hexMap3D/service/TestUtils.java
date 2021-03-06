package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;
import de.schmiereck.hexMap3D.service.universe.*;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;

import static de.schmiereck.hexMap3D.GridUtils.xRotArr;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {

    public static Wave createNewTestWave(final Event event, final WaveMoveDir waveMoveDir) {
        final Wave testWave = WaveService.createNewInitialWave(event, waveMoveDir, 1, 8, 128);
        event.addWave(testWave);
        return testWave;
    }

    public static void assertWaveProps(final Wave newWave, final int[][] expectedProps) {
        assertWaveProps(newWave, expectedProps, "");
    }

    public static void assertWaveProps(final Wave newWave, final int[][] expectedProps, final String msg) {
        for (int axisPos = 0; axisPos < xRotArr.length; axisPos++) {
            final Cell.Dir[] rotArr = GridUtils.xRotArr[axisPos];
            for (int pos = 0; pos < rotArr.length; pos++) {
                assertEquals(expectedProps[axisPos][pos], newWave.getMoveCalcDir(xRotArr[axisPos][pos]),
                        String.format("%sdir:\"%s\" - axisPos:\"%d\", pos:\"%d\"", msg, xRotArr[axisPos][pos].toString(), axisPos, pos));
            }
        }
    }

    @org.junit.jupiter.api.Test
    public void test1() {
        int[] dirCalcProbSumArr = new int[3];
        dirCalcProbSumArr[0] = 0;
        dirCalcProbSumArr[1] = 1;
        dirCalcProbSumArr[2] = 2;
        int[] arr = Arrays.copyOf(dirCalcProbSumArr, dirCalcProbSumArr.length);
        Assertions.assertEquals(0, arr[0]);
        Assertions.assertEquals(1, arr[1]);
        Assertions.assertEquals(2, arr[2]);
        dirCalcProbSumArr[1] = 10;
        Assertions.assertEquals(0, arr[0]);
        Assertions.assertEquals(1, arr[1]);
        Assertions.assertEquals(2, arr[2]);
        arr[2] = 20;
        Assertions.assertEquals(20, arr[2]);
        Assertions.assertEquals(2, dirCalcProbSumArr[2]);
    }
}
