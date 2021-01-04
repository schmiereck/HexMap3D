package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

import java.util.Arrays;

import static de.schmiereck.hexMap3D.GridUtils.xRotArr;
import static de.schmiereck.hexMap3D.service.Cell.Dir.*;
import static de.schmiereck.hexMap3D.service.Cell.Dir.LG_N;
import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveFirstMiddlePositive() {
        // Arrange
        final Engine engine = null;
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.OR_P.dir()] = new WaveMoveCalcDir(100, 100);
        final Wave sourceWave = particleEvent.createWave(0, moveCalcDirArr);
        final int xRotPercent = 10;
        final int yRotPercent = 0;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
            Engine.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent);

        // Assert
        final int expectedProps[][] = {
                // Left
                { 0, 0, 0, 0},
                // Middle
                { 90, 10, 0, 0},
                // Right
                { 0, 0, 0, 0}
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveFirstMiddleNegative() {
        // Arrange
        final Engine engine = null;
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.OR_P.dir()] = new WaveMoveCalcDir(100, 100);
        final Wave sourceWave = particleEvent.createWave(0, moveCalcDirArr);
        final int xRotPercent = -10;
        final int yRotPercent = 0;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
            Engine.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent);

        // Assert
        final int expectedProps[][] = {
                // Left
                { 0, 0, 0, 0},
                // Middle
                { 90, 0, 0, 10},
                // Right
                { 0, 0, 0, 0}
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveSeccondMiddlePositive() {
        // Arrange
        final Engine engine = null;
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.GR_P.dir()] = new WaveMoveCalcDir(100, 100);
        final Wave sourceWave = particleEvent.createWave(0, moveCalcDirArr);
        final int xRotPercent = 10;
        final int yRotPercent = 0;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
            Engine.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent);

        // Assert
        final int expectedProps[][] = {
                // Left
                { 0, 0, 0, 0},
                // Middle
                { 0, 90, 10, 0},
                // Right
                { 0, 0, 0, 0}
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveFirstMiddleYPositive() {
        // Arrange
        final Engine engine = null;
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.OR_P.dir()] = new WaveMoveCalcDir(100, 100);
        final Wave sourceWave = particleEvent.createWave(0, moveCalcDirArr);
        final int xRotPercent = 0;
        final int yRotPercent = 10;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
            Engine.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent);

        // Assert
        final int expectedProps[][] = {
                // Left
                { 0, 0, 0, 10},
                // Middle
                { 90, 0, 0, 0},
                // Right
                { 0, 0, 0, 0}
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveMiddle2YPositive() {
        // Arrange
        final Engine engine = null;
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.OR_N.dir()] = new WaveMoveCalcDir(100, 100);
        final Wave sourceWave = particleEvent.createWave(0, moveCalcDirArr);
        final int xRotPercent = 0;
        final int yRotPercent = 10;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
            Engine.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent);

        // Assert
        final int expectedProps[][] = {
                { 0, 0, 0, 0}, // Left
                { 0, 0, 90, 0}, // Middle
                { 0, 10, 0, 0} // Right
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveMiddle1XPositiveYPositive() {
        // Arrange
        final Engine engine = null;
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.GR_P.dir()] = new WaveMoveCalcDir(100, 100);
        final Wave sourceWave = particleEvent.createWave(0, moveCalcDirArr);
        final int xRotPercent = 20;
        final int yRotPercent = 10;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
            Engine.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent);

        // Assert
        final int expectedXProps[][] = {
                { 0, 10, 0, 0},  //   Left: RE_P, DB_P, LB_N, LG_P
                { 0, 70, 20, 0}, // Middle: OR_P, GR_P, OR_N, GR_N
                { 0, 0, 0, 0}    //  Right: LB_P, LG_N, RE_N, DB_N
        };
        assertWaveProps(newWave, expectedXProps);
    }

    private void assertWaveProps(Wave newWave, int[][] expectedProps) {
        for (int axisPos = 0; axisPos < xRotArr.length; axisPos++) {
            final Cell.Dir[] rotArr = GridUtils.xRotArr[axisPos];
            for (int pos = 0; pos < rotArr.length; pos++) {
                assertEquals(expectedProps[axisPos][pos], newWave.getMoveCalcDir(xRotArr[axisPos][pos]).getDirCalcProp(),
                        String.format("dir:\"%s\" - axisPos:\"%d\", pos:\"%d\"", xRotArr[axisPos][pos].toString(), axisPos, pos));
            }
        }
    }

    @org.junit.jupiter.api.Test
    public void getMoveAmount() {
        // Assert
        assertEquals(100, Engine.getMoveAmount(100, 100));
        assertEquals(50, Engine.getMoveAmount(50, 100));
        assertEquals(10, Engine.getMoveAmount(10, 100));
        assertEquals(1, Engine.getMoveAmount(1, 100));

        assertEquals(-100, Engine.getMoveAmount(-100, 100));
        assertEquals(-50, Engine.getMoveAmount(-50, 100));
        assertEquals(-10, Engine.getMoveAmount(-10, 100));
        assertEquals(-1, Engine.getMoveAmount(-1, 100));

        assertEquals(50, Engine.getMoveAmount(100, 50));
        assertEquals(25, Engine.getMoveAmount(50, 50));
        assertEquals(5, Engine.getMoveAmount(10, 50));
        assertEquals(1, Engine.getMoveAmount(1, 50));

        assertEquals(-50, Engine.getMoveAmount(-100, 50));
        assertEquals(-25, Engine.getMoveAmount(-50, 50));
        assertEquals(-5, Engine.getMoveAmount(-10, 50));
        assertEquals(-1, Engine.getMoveAmount(-1, 50));
    }
}