package de.schmiereck.hexMap3D;

public class MapMathUtils {

    public static int wrap(final int pos, final int rangeExclusive) {
        final int ret;
        if (pos < 0) {
            ret = rangeExclusive + pos;
        } else {
            if (pos >= rangeExclusive) {
                ret = pos - rangeExclusive;
            } else {
                ret = pos;
            }
        }
        return ret;
    }

    public static int wrapInclusive(final int pos, final int rangeInclusive) {
        final int ret;
        if (rangeInclusive == 0) {
            ret = 0;
        } else {
            if (pos < 0) {
                ret = rangeInclusive + pos;
            } else {
                if (pos > rangeInclusive) {
                    ret = pos - rangeInclusive;
                } else {
                    ret = pos;
                }
            }
        }
        return ret;
    }
}
