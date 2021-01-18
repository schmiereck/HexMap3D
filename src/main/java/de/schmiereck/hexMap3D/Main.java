package de.schmiereck.hexMap3D;

import de.schmiereck.hexMap3D.service.*;
import de.schmiereck.hexMap3D.view.GridViewApplication;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Arrays;

import static java.lang.String.format;

public class Main {

    public static final boolean useParallel = true;

    public static final int xSizeGrid = 24;//16;
    public static final int ySizeGrid = 24;//16;
    public static final int zSizeGrid = 24;//16;

    public static void main(String[] args) {
        //-----------------------------------------------------------------------------

        //-----------------------------------------------------------------------------
        final Universe universe = new Universe(xSizeGrid, ySizeGrid, zSizeGrid);
        final Engine engine = new Engine(universe);

        //-----------------------------------------------------------------------------
        // Extend Test:
        {
            final Event particleEvent = new Event(engine, 1);
            final WaveMoveCalcDir[] moveCalcDirArr = new WaveMoveCalcDir[Cell.Dir.values().length];
            Arrays.stream(Cell.Dir.values()).forEach(dir -> moveCalcDirArr[dir.dir()] = WaveMoveCalcDirService.createWaveMoveCalcDir(0, 0));
            final int x, y, z;

            //moveCalcDirArr[Cell.Dir.OR_P.dir()].setDirCalcProp(50);
            //moveCalcDirArr[Cell.Dir.GR_P.dir()].setDirCalcProp(50);
            //x=0; y=0; z=8;

            //moveCalcDirArr[Cell.Dir.OR_P.dir()].setDirCalcProp(100);
            //moveCalcDirArr[Cell.Dir.LB_P.dir()].setDirCalcProp(100);
            //moveCalcDirArr[Cell.Dir.GR_P.dir()].setDirCalcProp(100);
            //moveCalcDirArr[Cell.Dir.RE_P.dir()].setDirCalcProp(100);
            //moveCalcDirArr[Cell.Dir.LG_P.dir()].setDirCalcProp(100);
            //moveCalcDirArr[Cell.Dir.DB_P.dir()].setDirCalcProp(100);
            //x=8; y=8; z=8;

            //moveCalcDirArr[Cell.Dir.OR_P.dir()].setDirCalcProp(75);
            //moveCalcDirArr[Cell.Dir.LG_N.dir()].setDirCalcProp(25);
            //moveCalcDirArr[Cell.Dir.DB_P.dir()].setDirCalcProp(0);
            //x=8; y=8; z=8;

            moveCalcDirArr[Cell.Dir.DB_P.dir()].setDirCalcProp(100);
            x=0; y=8; z=8;

            final Wave wave = WaveService.createNextMovedWave(particleEvent, moveCalcDirArr);
            wave.getWaveMoveDir().adjustMaxProp();
            wave.calcActualWaveMoveCalcDir();
            universe.addEvent(x, y, z, particleEvent);

            // For Testing:
            // Add a sharp Barrier...
            //final Event barrierEvent = new Event(engine, 0);
            // ...to the right...
            //universe.addBariere(barrierEvent, 10, 0, 0, 11, 15, 15);
            // and to the left.
            //universe.addBariere(barrierEvent, 1, 0, 0, 2, 15, 15);
        }
        //-----------------------------------------------------------------------------
        final GridViewApplication gridViewApplication = new GridViewApplication();

        gridViewApplication.init(() -> engine.run(), universe, xSizeGrid, ySizeGrid, zSizeGrid);

        Platform.startup(() -> {
            // create primary stage
            final Stage primaryStage = new Stage();

            try {
                gridViewApplication.start(primaryStage);
            } catch (Exception e) {
                throw new RuntimeException("JavaFX startup.", e);
            }
        });
         //-----------------------------------------------------------------------------
        engine.runInit();

         //-----------------------------------------------------------------------------
    }
}
