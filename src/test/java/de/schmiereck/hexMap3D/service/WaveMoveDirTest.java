package de.schmiereck.hexMap3D.service;

import java.util.Arrays;

public class WaveMoveDirTest {

    @org.junit.jupiter.api.Test
    public void test1() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.OR_P.dir()] = new WaveMoveCalcDir(75, 25 + 75);
        moveCalcDirArr[Cell.Dir.LG_N.dir()] = new WaveMoveCalcDir(25, 75);
        moveCalcDirArr[Cell.Dir.DB_P.dir()] = new WaveMoveCalcDir(0, 0);
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 100; c++) {
            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();
            printWaveMoveDir(c, waveMoveDir, 6);
        }
    }

    @org.junit.jupiter.api.Test
    public void test2_1() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.OR_P.dir()] = new WaveMoveCalcDir(40, 60 + 40);
        moveCalcDirArr[Cell.Dir.LG_N.dir()] = new WaveMoveCalcDir(30, 40);
        moveCalcDirArr[Cell.Dir.DB_P.dir()] = new WaveMoveCalcDir(30, 0);
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 800; c++) {
            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();

            printWaveMoveDir(c, waveMoveDir, 4);
        }
    }

    @org.junit.jupiter.api.Test
    public void test2_2() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.LG_N.dir()] = new WaveMoveCalcDir(30, 70 + 30);
        moveCalcDirArr[Cell.Dir.OR_P.dir()] = new WaveMoveCalcDir(40, 30);
        moveCalcDirArr[Cell.Dir.DB_P.dir()] = new WaveMoveCalcDir(30, 0);
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 800; c++) {
            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();

            printWaveMoveDir(c, waveMoveDir, 4);
        }
    }

    @org.junit.jupiter.api.Test
    public void test3() {
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.OR_P.dir()] = new WaveMoveCalcDir(87, 13 + 87);
        moveCalcDirArr[Cell.Dir.LG_N.dir()] = new WaveMoveCalcDir(13, 87);
        int dirCalcPos = 0;
        WaveMoveDir waveMoveDir = new WaveMoveDir(dirCalcPos, moveCalcDirArr);
        waveMoveDir.calcActualWaveMoveCalcDir();

        for (int c = 0; c < 800; c++) {
            waveMoveDir.calcActualDirMoved();
            waveMoveDir = new WaveMoveDir(waveMoveDir.nextDirCalcPos(), waveMoveDir.getMoveCalcDirArr());
            waveMoveDir.calcActualWaveMoveCalcDir();

            printWaveMoveDir(c, waveMoveDir, 7);
        }
    }

    private void printWaveMoveDir(int c, WaveMoveDir waveMoveDir, int i) {
        final Cell.Dir actualMoveDir = waveMoveDir.getActualMoveDir();
        final WaveMoveCalcDir actualWaveMoveCalcDir = waveMoveDir.getActualWaveMoveCalcDir();
        if (c % i == 0) System.out.println();
        System.out.printf("%s(%d), ", actualMoveDir, actualWaveMoveCalcDir.getDirCalcPropSum());
    }
}
