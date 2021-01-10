package de.schmiereck.hexMap3D;

public class MapMathUtils {

    public static int wrap(final int pos, final int rangeExclusive) {
        final int ret;
        if (rangeExclusive <= 1) {
            ret = 0;
        } else {
            if (pos < 0) {
                ret = (rangeExclusive + (pos % rangeExclusive)) % rangeExclusive;
            } else {
                if (pos >= rangeExclusive) {
                    ret = pos % rangeExclusive;
                } else {
                    ret = pos;
                }
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
                ret = ((rangeInclusive + 1) + (pos % (rangeInclusive + 1))) % (rangeInclusive + 1);
            } else {
                if (pos > rangeInclusive) {
                    ret = pos % (rangeInclusive + 1);
                } else {
                    ret = pos;
                }
            }
        }
        return ret;
    }
}
