package de.schmiereck.hexMap3D.service;

import java.util.Arrays;

import static de.schmiereck.hexMap3D.service.Engine.DIR_CALC_MAX_PROP;

public class WaveMoveDirTest {

    @org.junit.jupiter.api.Test
    public void test1() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.OR_P.dir()] = new WaveMoveCalcDir(75, DIR_CALC_MAX_PROP - 75 * 0);
        moveCalcDirArr[Cell.Dir.LG_N.dir()] = new WaveMoveCalcDir(25, DIR_CALC_MAX_PROP - 25 * 1);
        moveCalcDirArr[Cell.Dir.DB_P.dir()] = new WaveMoveCalcDir(0, 0);
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 100; c++) {
            printWaveMoveDir(c, waveMoveDir, 6);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
        }
    }

    @org.junit.jupiter.api.Test
    public void test2_1() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.OR_P.dir()] = new WaveMoveCalcDir(40, DIR_CALC_MAX_PROP - 40 * 0);
        moveCalcDirArr[Cell.Dir.LG_N.dir()] = new WaveMoveCalcDir(30, DIR_CALC_MAX_PROP - 30 * 1);
        moveCalcDirArr[Cell.Dir.DB_P.dir()] = new WaveMoveCalcDir(30, DIR_CALC_MAX_PROP - 30 * 2);
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 800; c++) {
            printWaveMoveDir(c, waveMoveDir, 4);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
        }
    }

    @org.junit.jupiter.api.Test
    public void test2_2() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.LG_N.dir()] = new WaveMoveCalcDir(30, DIR_CALC_MAX_PROP - 30 * 0);
        moveCalcDirArr[Cell.Dir.OR_P.dir()] = new WaveMoveCalcDir(40, DIR_CALC_MAX_PROP - 40 * 1);
        moveCalcDirArr[Cell.Dir.DB_P.dir()] = new WaveMoveCalcDir(30, DIR_CALC_MAX_PROP - 30 * 2);
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 800; c++) {
            printWaveMoveDir(c, waveMoveDir, 4);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
        }
    }

    @org.junit.jupiter.api.Test
    public void test3() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.OR_P.dir()] = new WaveMoveCalcDir(87, DIR_CALC_MAX_PROP - 87 * 0);
        moveCalcDirArr[Cell.Dir.LG_N.dir()] = new WaveMoveCalcDir(13, DIR_CALC_MAX_PROP - 13 * 1);
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 800; c++) {
            printWaveMoveDir(c, waveMoveDir, 7);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
        }
    }

    @org.junit.jupiter.api.Test
    public void test4() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.OR_P.dir()] = new WaveMoveCalcDir(38, 0);
        moveCalcDirArr[Cell.Dir.OR_N.dir()] = new WaveMoveCalcDir(12, 0);
        moveCalcDirArr[Cell.Dir.LG_P.dir()] = new WaveMoveCalcDir(37, 0);
        moveCalcDirArr[Cell.Dir.LG_N.dir()] = new WaveMoveCalcDir(13, 0);
        int dirCalcPos = 3; // OR_N
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.adjustDirCalcPropSum();
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 800; c++) {
            printWaveMoveDir(c, waveMoveDir, 7);

            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
        }
    }

    private void printWaveMoveDir(int c, WaveMoveDir waveMoveDir, int i) {
        final Cell.Dir actualMoveDir = waveMoveDir.getActualMoveDir();
        final WaveMoveCalcDir actualWaveMoveCalcDir = waveMoveDir.getActualWaveMoveCalcDir();
        if (c % i == 0) System.out.println();
        System.out.printf("%s(%d), ", actualMoveDir, actualWaveMoveCalcDir.getDirCalcPropSum());
    }
}
