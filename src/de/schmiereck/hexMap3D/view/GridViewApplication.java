package de.schmiereck.hexMap3D.view;

import de.schmiereck.hexMap3D.service.Universe;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class GridViewApplication extends Application {
    private Universe universe;
    private int xSizeGrid;
    private int ySizeGrid;
    private int zSizeGrid;
    private GridViewNodeSpace nodeSpace;

    public void init(final Universe universe, final int xSizeGrid, final int ySizeGrid, final int zSizeGrid) {
        this.universe = universe;
        this.xSizeGrid = xSizeGrid;
        this.ySizeGrid = ySizeGrid;
        this.zSizeGrid = zSizeGrid;
        this.nodeSpace = new GridViewNodeSpace(universe, xSizeGrid, ySizeGrid, zSizeGrid);
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
        //final Parent sampleGui = FXMLLoader.load(getClass().getResource("sample.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        final Parent sampleGui = loader.load();
        final GridViewController gridViewController = loader.<GridViewController>getController();
        gridViewController.setGridViewApplication(this);
        rootGroup.getChildren().add(sampleGui);

        //-----------------------------------------------------------------------------
        GridViewUtils.generateViewGrid(this.nodeSpace, rootGroup, xSizeGrid, ySizeGrid, zSizeGrid);

        //-----------------------------------------------------------------------------
        final PerspectiveCamera camera = new PerspectiveCamera(true);

        camera.setTranslateX(xSizeGrid / 2 * GridViewUtils.viewGridStepA);
        camera.setTranslateY(ySizeGrid / 2 * GridViewUtils.viewGridStepH);
        camera.setTranslateZ(-900.0D);

        camera.setRotationAxis(Rotate.Y_AXIS);
        camera.setRotate(0.0D);

        camera.setFarClip(8000.0D);
        camera.setFieldOfView(50.0D);

        //-----------------------------------------------------------------------------
        final Scene scene = new Scene(rootGroup,
                xSizeGrid * GridViewUtils.viewGridStepA + GridViewUtils.viewGridStepA2, ySizeGrid * GridViewUtils.viewGridStepH,
                true, SceneAntialiasing.BALANCED);

        scene.setCamera(camera);
        primaryStage.setScene(scene);

        //-----------------------------------------------------------------------------
        primaryStage.show();

        //-----------------------------------------------------------------------------
        final MouseLook mouseLook = new MouseLook(camera);
        scene.setOnMousePressed((event) -> {
            event.setDragDetect(true);
        });
        scene.setOnMouseReleased((event) -> {
            //event.setDragDetect(false);
            mouseLook.movedFinished();
        });
        //scene.setOnMouseMoved(new MouseLook(camera));
        scene.setOnMouseDragged((event) -> {
            mouseLook.handle(event);
            event.setDragDetect(false);
        });

        scene.setOnScroll((event) -> {
            mouseLook.handleMouseScrolling(event);
        });
        //-----------------------------------------------------------------------------
    }

    public void updateReality() {
        this.nodeSpace.updateReality();
    }
}
