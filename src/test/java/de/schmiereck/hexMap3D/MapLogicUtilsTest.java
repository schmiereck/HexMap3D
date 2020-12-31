package de.schmiereck.hexMap3D;

import java.util.concurrent.atomic.AtomicInteger;

import static de.schmiereck.hexMap3D.MapLogicUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class MapLogicUtilsTest {

    @org.junit.jupiter.api.Test
    void testLoopUp() {
        // arrange
        final int rotDir = +1;
        final int rotStartPos = 0;
        final int rotEndPos = 3;
        final int[] expectedOutput = { 0, 1, 2, 3};
        final AtomicInteger runPos = new AtomicInteger(0);
        // act & assert
        calcLoop(rotStartPos, rotEndPos, rotDir, pos -> {
            //System.out.println(pos);
            assertEquals(expectedOutput[runPos.getAndIncrement()], pos);
        });
        assertEquals(runPos.get(), 4);
    }

    @org.junit.jupiter.api.Test
    void testLoopDown() {
        // arrange
        final int rotDir = -1;
        final int rotStartPos = 3;
        final int rotEndPos = 0;
        final int[] expectedOutput = { 3, 2, 1, 0};
        final AtomicInteger runPos = new AtomicInteger(0);
        // act & assert
        calcLoop(rotStartPos, rotEndPos, rotDir, pos -> {
            //System.out.println(pos);
            assertEquals(expectedOutput[runPos.getAndIncrement()], pos);
        });
        assertEquals(runPos.get(), 4);
    }

    @org.junit.jupiter.api.Test
    void testLoopUpBreak() {
        // arrange
        final int rotDir = +1;
        final int rotStartPos = 0;
        final int rotEndPos = 3;
        final int[] expectedOutput = { 0, 1, 2, 3};
        final AtomicInteger runCount = new AtomicInteger(0);
        // act & assert
        final int retPos =
            calcBreakLoop(rotStartPos, rotEndPos, rotDir, pos -> {
                final boolean retBreak;
                //System.out.println(pos);
                assertEquals(expectedOutput[runCount.getAndIncrement()], pos);
                if (pos == 2) {
                    retBreak = true;
                } else {
                    retBreak = false;
                }
                return retBreak;
            });
        assertEquals(3, runCount.get());
        assertEquals(2, retPos);
    }

    @org.junit.jupiter.api.Test
    void testLoopUpNoBreak() {
        // arrange
        final int rotDir = +1;
        final int rotStartPos = 0;
        final int rotEndPos = 3;
        final int[] expectedOutput = { 0, 1, 2, 3};
        final AtomicInteger runCount = new AtomicInteger(0);
        // act & assert
        final int retPos =
            calcBreakLoop(rotStartPos, rotEndPos, rotDir, pos -> {
                //System.out.println(pos);
                assertEquals(expectedOutput[runCount.getAndIncrement()], pos);
                return false;
            });
        assertEquals(4, runCount.get());
        assertEquals(4, retPos);
    }

    @org.junit.jupiter.api.Test
    void testLoopUpNoBreakWrap() {
        // arrange
        final int rotDir = +1;
        final int rotStartPos = 0;
        final int rotEndPos = 3;
        final int[] expectedOutput = { 0, 1, 2, 3};
        final AtomicInteger runCount = new AtomicInteger(0);
        // act & assert
        final int retPos =
            calcBreakLoopWrap(rotStartPos, rotEndPos, rotDir, pos -> {
                //System.out.println(pos);
                assertEquals(expectedOutput[runCount.getAndIncrement()], pos);
                return false;
            });
        assertEquals(4, runCount.get());
        assertEquals(0, retPos);
    }
}