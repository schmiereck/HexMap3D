package de.schmiereck.hexMap3D;

public class MapLogicUtils {

    public interface CalcLoopInterface {
        void calc(final int pos);
    }

    public interface CalcBreakLoopInterface {
        boolean calc(final int pos);
    }

    public static void calcLoop(final int startPos, final int endPos, final int dir, final CalcLoopInterface calcLoopInterface) {
        for (int pos = startPos; dir > 0 ? pos <= endPos : pos >= endPos; pos = dir > 0 ? pos + 1 : pos - 1) {
            calcLoopInterface.calc(pos);
        }
    }

    public static int calcBreakLoop(final int startPos, final int endPos, final int dir, final CalcBreakLoopInterface calcBreakLoopInterface) {
        int pos = endPos;
        for (pos = startPos; dir > 0 ? pos <= endPos : pos >= endPos; pos = dir > 0 ? pos + 1 : pos - 1) {
            if (calcBreakLoopInterface.calc(pos)) {
                break;
            }
        }
        return pos;
    }

    public static int calcBreakLoopWrap(final int startPos, final int endPos, final int dir, final CalcBreakLoopInterface calcBreakLoopInterface) {
        int retPos = startPos;
        for (int pos = startPos; dir > 0 ? pos <= endPos : pos >= endPos; pos = dir > 0 ? pos + 1 : pos - 1) {
            if (calcBreakLoopInterface.calc(pos)) {
                retPos = pos;
                break;
            }
        }
        return retPos;
    }

    public static int calcBreakLoopWrap2(final int startPos, final int length, final int dir, final CalcBreakLoopInterface calcBreakLoopInterface) {
        int retPos = startPos;
        int pos = startPos;
        for (int count = 0; count < length; count++) {
            //dir > 0 ? pos <= endPos : pos >= endPos;
            if (calcBreakLoopInterface.calc(pos)) {
                retPos = pos;
                break;
            }
            if (dir > 0) {
                if ((pos + 1) < length) {
                    pos++;
                } else {
                    pos = 0;
                }
            } else {
                if ((pos - 1) > 0) {
                    pos--;
                } else {
                    pos = length - 1;
                }
            }
        }
        return retPos;
    }
}
