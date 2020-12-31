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
}
