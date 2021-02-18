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
        assertEquals(2, wrapInclusive(-1, 2));
        assertEquals(1, wrapInclusive(-2, 2));
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
        assertEquals(2, wrap(-1, 3));
        assertEquals(1, wrap(-2, 3));
        assertEquals(0, wrap(-3, 3));
    }

    @org.junit.jupiter.api.Test
    void testDivide() {
        assertEquals(0, MapMathUtils.divide(0, 2, false));
        assertEquals(0, MapMathUtils.divide(0, 2, true));

        assertEquals(1, MapMathUtils.divide(1, 1, false));
        assertEquals(1, MapMathUtils.divide(1, 1, true));

        assertEquals(0, MapMathUtils.divide(1, 2, false));
        assertEquals(1, MapMathUtils.divide(1, 2, true));

        assertEquals(0, MapMathUtils.divide(1, 3, false));
        assertEquals(1, MapMathUtils.divide(1, 3, true));

        assertEquals(2, MapMathUtils.divide(2, 1, false));
        assertEquals(2, MapMathUtils.divide(2, 1, true));

        assertEquals(1, MapMathUtils.divide(2, 2, false));
        assertEquals(1, MapMathUtils.divide(2, 2, true));

        assertEquals(0, MapMathUtils.divide(2, 3, false));
        assertEquals(2, MapMathUtils.divide(2, 3, true));

        assertEquals(3, MapMathUtils.divide(3, 1, false));
        assertEquals(3, MapMathUtils.divide(3, 1, true));

        assertEquals(1, MapMathUtils.divide(3, 2, false));
        assertEquals(2, MapMathUtils.divide(3, 2, true));

        assertEquals(1, MapMathUtils.divide(3, 3, false));
        assertEquals(1, MapMathUtils.divide(3, 3, true));

        assertEquals(0, MapMathUtils.divide(3, 4, false));
        assertEquals(3, MapMathUtils.divide(3, 4, true));
    }
}