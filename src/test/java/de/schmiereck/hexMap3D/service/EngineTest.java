package de.schmiereck.hexMap3D.service;

import de.schmiereck.hexMap3D.GridUtils;

import java.util.Arrays;

import static de.schmiereck.hexMap3D.GridUtils.xRotArr;
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
                // Middle
                { 90, 10, 0, 0},
                // Left
                { 0, 0, 0, 0},
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
                // Middle
                { 90, 0, 0, 10},
                // Left
                { 0, 0, 0, 0},
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
                // Middle
                { 0, 90, 10, 0},
                // Left
                { 0, 0, 0, 0},
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
                // Middle
                { 90, 0, 0, 0},
                // Left
                { 0, 0, 0, 0},
                // Right
                { 0, 10, 0, 0}
        };
        assertWaveProps(newWave, expectedProps);
    }

    @org.junit.jupiter.api.Test
    void createMoveRotatedWaveSeccondMiddleXPositiveYPositive() {
        // Arrange
        final Engine engine = null;
        final Event particleEvent = new Event(engine, 1);
        final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
        Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = new WaveMoveCalcDir(0, 0));
        moveCalcDirArr[Cell.Dir.GR_P.dir()] = new WaveMoveCalcDir(100, 100);
        final Wave sourceWave = particleEvent.createWave(0, moveCalcDirArr);
        final int xRotPercent = 10;
        final int yRotPercent = 10;
        final int zRotPercent = 0;

        // Act
        final Wave newWave =
            Engine.createMoveRotatedWave(sourceWave, xRotPercent, yRotPercent, zRotPercent);

        // Assert
        final int expectedProps[][] = {
                // Middle
                { 0, 81, 9, 0},
                // Left
                { 0, 9, 1, 0},
                // Right
                { 0, 0, 0, 0}
        };
        assertWaveProps(newWave, expectedProps);
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
}