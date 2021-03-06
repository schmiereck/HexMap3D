package de.schmiereck.hexMap3D.view;

import de.schmiereck.hexMap3D.service.reality.Detector;
import de.schmiereck.hexMap3D.service.reality.Reality;
import de.schmiereck.hexMap3D.service.reality.RealityService;
import de.schmiereck.hexMap3D.service.universe.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.Optional;

public class GridViewApplication extends Application {
    private RunStepCallback runStepCallback;
    private Universe universe;
    private Reality reality;
    private int xSizeGrid;
    private int ySizeGrid;
    private int zSizeGrid;
    private GridViewNodeSpace nodeSpace;
    private GridViewModel gridViewModel;
    private boolean initFinished = false;
    private GridViewController gridViewController = null;

    @FunctionalInterface
    public interface RunStepCallback {
        void run();
    }

    public void init(final RunStepCallback runStepCallback, final Universe universe, final Reality reality, final int xSizeGrid, final int ySizeGrid, final int zSizeGrid) {
        this.runStepCallback = runStepCallback;
        this.universe = universe;
        this.reality = reality;
        this.xSizeGrid = xSizeGrid;
        this.ySizeGrid = ySizeGrid;
        this.zSizeGrid = zSizeGrid;
        this.nodeSpace = new GridViewNodeSpace(this.reality, this.xSizeGrid, this.ySizeGrid, this.zSizeGrid);
        this.gridViewModel = new GridViewModel();

        this.gridViewModel.showWaveMoveCalcGroupProperty().addListener((observable, oldValue, newValue) -> {
            final String toggleId = (newValue);

            final Reality.ShowWaveMoveCalc showWaveMoveCalc;
            if (GridViewModel.SWM_showActualWaveMoveCalcDirSum.equals(toggleId)) {
                showWaveMoveCalc = Reality.ShowWaveMoveCalc.ShowActualWaveMoveCalcDirSum;
            } else {
                if (GridViewModel.SWM_showAllWaveMoveCalcDirSum.equals(toggleId)) {
                    showWaveMoveCalc = Reality.ShowWaveMoveCalc.ShowAllWaveMoveCalcDirSum;
                } else {
                    if (GridViewModel.SWM_showAllWaveMoveCalcDirProb.equals(toggleId)) {
                        showWaveMoveCalc = Reality.ShowWaveMoveCalc.ShowAllWaveMoveCalcDirProb;
                    } else {
                        if (GridViewModel.SWM_showNoWaveMoveDir.equals(toggleId)) {
                            showWaveMoveCalc = Reality.ShowWaveMoveCalc.ShowNoWaveMoveDir;
                        } else {
                            throw new RuntimeException("Unexpected showWaveMoveCalc \"" + toggleId + "\".");
                        }
                    }
                }
            }
            if (this.initFinished) {
                this.setShowActualWaveMoveCalcDir(showWaveMoveCalc);
                this.calcReality();
                this.updateReality();
            }
        });
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        //-----------------------------------------------------------------------------
        primaryStage.setTitle("Hex-Map 3D V0.1.0");

        //-----------------------------------------------------------------------------
        final Group rootGroup = new Group();

        rootGroup.setTranslateX(GridViewUtils.viewGridStepA4);
        rootGroup.setTranslateY(GridViewUtils.viewGridStepA4);
        rootGroup.setTranslateZ(70);

        //-----------------------------------------------------------------------------
        //final Parent sampleGui = FXMLLoader.load(getClass().getResource("gridControll.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gridControll.fxml"));
        final Parent gridControllGui = loader.load();
        this.gridViewController = loader.<GridViewController>getController();
        gridViewController.init(this.runStepCallback, this, this.gridViewModel);
        //rootGroup.getChildren().add(sampleGui);

        //-----------------------------------------------------------------------------
        GridViewUtils.generateViewGrid(this.nodeSpace, rootGroup, xSizeGrid, ySizeGrid, zSizeGrid);

        //-----------------------------------------------------------------------------
        final PerspectiveCamera camera = new PerspectiveCamera(true);

        camera.setFarClip(8000.0D);
        camera.setFieldOfView(50.0D);
        camera.setVerticalFieldOfView(true);

        final Group cameraGroup = new Group();
        rootGroup.getChildren().add(cameraGroup);
        cameraGroup.getChildren().add(camera);

        //-----------------------------------------------------------------------------
        final Group lightGroup = new Group();

        {
            final PointLight light = new PointLight();
            light.setColor(new Color(1.0D, 1.0D, 1.0D, 1.0D));
            light.getTransforms().add(new Translate(-1000,1450,-800));
            lightGroup.getChildren().add(light);
        }
        {
            final PointLight light = new PointLight();
            light.setColor(new Color(1.0D, 1.0D, 1.0D, 0.4D));
            light.getTransforms().add(new Translate(1500,-450,1500));
            lightGroup.getChildren().add(light);
        }
        {
            final PointLight light = new PointLight();
            light.setColor(new Color(0.3D, 0.3D, 0.3D, 1.0D));
            light.getTransforms().add(new Translate(0,2450,0));
            lightGroup.getChildren().add(light);
        }
        {
            final AmbientLight light = new AmbientLight();
            light.setColor(new Color(0.5D, 0.5D, 0.5D, 0.5D));
            lightGroup.getChildren().add(light);
        }
        rootGroup.getChildren().add(lightGroup);

        //-----------------------------------------------------------------------------
        final double w1 = xSizeGrid * GridViewUtils.viewGridStepA + GridViewUtils.viewGridStepA2;
        final double h1 = ySizeGrid * GridViewUtils.viewGridStepH;
        final SubScene gridScene = new SubScene(rootGroup, w1, h1, true, SceneAntialiasing.BALANCED);
        gridScene.setPickOnBounds(true);

        gridScene.setCamera(camera);

        final double w2 = 160;
        final double h2 = 500;
        final SubScene guiScene = new SubScene(gridControllGui, w2, h2);

        final double w3 = 10.0D;
        final HBox root = new HBox(w3);
        root.setAlignment(Pos.TOP_LEFT);
        final Pane guiPane = new Pane(guiScene);
        final double w4 = 2.0D;
        //guiPane.relocate(15, 15);
        //guiPane.setTranslateX(15);
        guiPane.setPadding(new Insets(w4));
        guiPane.setBorder(new Border(new BorderStroke(Color.CORNFLOWERBLUE, BorderStrokeStyle.SOLID,  CornerRadii.EMPTY, new BorderWidths(2.0D))));
        root.getChildren().addAll(gridScene, guiPane);

        final Scene mainScene = new Scene(root,w1 + w2 + w3 + w4*2, h1);
        primaryStage.setScene(mainScene);

        //primaryStage.setScene(scene);

        //-----------------------------------------------------------------------------
        primaryStage.show();

        //-----------------------------------------------------------------------------
        final MouseLook mouseLook = new MouseLook(cameraGroup, camera);

        //cameraGroup.setTranslateX(xSizeGrid / 2 * GridViewUtils.viewGridStepA);
        //cameraGroup.setTranslateY(ySizeGrid / 2 * GridViewUtils.viewGridStepH);
        //cameraGroup.setTranslateZ(-900.0D);
        mouseLook.strafeRight(xSizeGrid / 2 * GridViewUtils.viewGridStepA);
        mouseLook.moveDown(ySizeGrid / 2 * GridViewUtils.viewGridStepH);
        if (mouseLook.getFlipYAxis()) {
            mouseLook.moveForward(-1900.0D);
            mouseLook.rotateX(180.0D);
        } else {
            mouseLook.moveForward(-945.0D);
        }

        gridScene.setOnMousePressed((event) -> {
            event.setDragDetect(true);
        });
        gridScene.setOnMouseReleased((event) -> {
            //event.setDragDetect(false);
            mouseLook.movedFinished();
        });
        //scene.setOnMouseMoved(new MouseLook(camera));
        gridScene.setOnMouseDragged((event) -> {
            mouseLook.handle(event);
            event.setDragDetect(false);
        });

        gridScene.setOnScroll((event) -> {
            mouseLook.handleMouseScrolling(event);
        });
        //-----------------------------------------------------------------------------
        this.gridViewModel.setStatisticWavesCount("---1");
        this.gridViewModel.setShowWaveMoveCalcGroup(GridViewModel.SWM_showActualWaveMoveCalcDirSum);

        this.updateReality();

        this.initFinished = true;

        //-----------------------------------------------------------------------------
    }

    public void setShowGrid(final boolean showGrid) {
        RealityService.setShowGrid(this.reality, showGrid);
    }

    public void setShowActualWaveMoveCalcDir(final Reality.ShowWaveMoveCalc showWaveMoveCalc) {
        RealityService.setShowWaveMoveCalc(this.reality, showWaveMoveCalc);
    }

    public void calcReality() {
        RealityService.calcReality(this.universe, this.reality);
    }

    public void updateReality() {
        this.nodeSpace.updateReality();

        this.gridViewModel.setStatisticWavesCount(String.format("%,d", RealityService.getStatisticWaveCount(this.reality)));
        this.gridViewModel.setStatisticCalcRunTime(String.format("%.2f s", RealityService.getStatisticCalcRunTime(this.reality) / 1000.0F));
        this.gridViewModel.setStatisticCalcStepCount(Long.toString(RealityService.getStatisticCalcStepCount(this.reality)));

        final Optional<Detector> optionalDetector = this.reality.getDetectorStream().findFirst();
        if (optionalDetector.isPresent()) {
            this.gridViewController.updateDetector(optionalDetector.get());
        }

        System.out.println("Cache: " +
                "CS:" + CellStateService.getCellStateCacheSize() + "(" + CellStateService.getCellStateCacheHitCount() + "), " +
                        "next:" + CellStateService.getNextCellStateCacheSize() + "(" + CellStateService.getNextCellStateCacheHitCount() + ")" +
                " | " +
                "MC:" + WaveMoveCalcService.getWaveMoveCalcCacheSize() + "(" + WaveMoveCalcService.getWaveMoveCalcCacheHitCount() + ")" +
                " | " +
                "MD:" + WaveMoveDirService.getWaveMoveDirCacheMapSize() + "(" + WaveMoveDirService.getWaveMoveDirCacheHitCount() + "), " +
                        "rotate:" + WaveMoveDirService.getRotateMoveDirCacheMapSize() + "(" + WaveMoveDirService.getRotateMoveDirCacheHitCount() + ")");
    }

    public void addDetector(final Detector detector) {
        this.gridViewController.addDetector(detector);
    }
}
