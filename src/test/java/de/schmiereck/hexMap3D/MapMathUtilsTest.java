package de.schmiereck.hexMap3D;

import static de.schmiereck.hexMap3D.MapMathUtils.wrap;
import static de.schmiereck.hexMap3D.MapMathUtils.wrapInclusive;
import static org.junit.jupiter.api.Assertions.*;

class MapMathUtilsTest {

    @org.junit.jupiter.api.Test
    void testWrapInclusive() {
        assertEquals(0, wrapInclusive(0, 0));
        assertEquals(0, wrapInclusive(1, 0));
        assertEquals(0, wrapInclusive(2, 0));
        assertEquals(0, wrapInclusive(3, 0));
        assertEquals(0, wrapInclusive(-1, 0));
        assertEquals(0, wrapInclusive(-2, 0));
        assertEquals(0, wrapInclusive(-3, 0));

        assertEquals(0, wrapInclusive(0, 1));
        assertEquals(1, wrapInclusive(1, 1));
        assertEquals(0, wrapInclusive(2, 1));
        assertEquals(1, wrapInclusive(3, 1));
        assertEquals(1, wrapInclusive(-1, 1));
        assertEquals(0, wrapInclusive(-2, 1));
        assertEquals(1, wrapInclusive(-3, 1));

        assertEquals(0, wrapInclusive(0, 2));
        assertEquals(1, wrapInclusive(1, 2));
        assertEquals(2, wrapInclusive(2, 2));
        assertEquals(0, wrapInclusive(3, 2));
        assertEquals(1, wrapInclusive(-1, 2));
        assertEquals(2, wrapInclusive(-2, 2));
        assertEquals(0, wrapInclusive(-3, 2));
    }

    @org.junit.jupiter.api.Test
    void testWrap() {
        assertEquals(0, wrap(0, 1));
        assertEquals(0, wrap(1, 1));
        assertEquals(0, wrap(2, 1));
        assertEquals(0, wrap(3, 1));
        assertEquals(0, wrap(-1, 1));
        assertEquals(0, wrap(-2, 1));
        assertEquals(0, wrap(-3, 1));

        assertEquals(0, wrap(0, 2));
        assertEquals(1, wrap(1, 2));
        assertEquals(0, wrap(2, 2));
        assertEquals(1, wrap(3, 2));
        assertEquals(1, wrap(-1, 2));
        assertEquals(0, wrap(-2, 2));
        assertEquals(1, wrap(-3, 2));

        assertEquals(0, wrap(0, 3));
        assertEquals(1, wrap(1, 3));
        assertEquals(2, wrap(2, 3));
        assertEquals(0, wrap(3, 3));
        assertEquals(1, wrap(-1, 3));
        assertEquals(2, wrap(-2, 3));
        assertEquals(0, wrap(-3, 3));
    }
}