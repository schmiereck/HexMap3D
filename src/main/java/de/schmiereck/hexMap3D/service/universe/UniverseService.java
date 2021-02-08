package de.schmiereck.hexMap3D.service.universe;

import java.util.stream.IntStream;

public class UniverseService {
    public static boolean useParallel = false;

    public static void calcNext(final Universe universe) {
        UniverseService.forEachCell(universe.getXUniverseSize(), universe.getYUniverseSize(), universe.getZUniverseSize(),
                (final int xPos, final int yPos, final int zPos) -> {
                    final Cell cell = universe.getCell(xPos, yPos, zPos);
                    cell.clearWaveList();
                });
        for (final Event event : universe.getEventList()) {
            event.clearWaveList();
        }
        universe.incCalcPos();
    }

    public static void forEachCell(final int xUniverseSize, final int yUniverseSize, final int zUniverseSize, final Universe.EachCellCallback eachCellCallback) {
        final IntStream zRangeStream = IntStream.range(0, zUniverseSize);
        (useParallel ? zRangeStream.parallel() : zRangeStream).
                forEach((final int zPos) ->
                        IntStream.range(0, yUniverseSize).
                                forEach((final int yPos) ->
                                        IntStream.range(0, xUniverseSize).
                                                forEach((final int xPos) -> eachCellCallback.call(xPos, yPos, zPos))));
    }
}
