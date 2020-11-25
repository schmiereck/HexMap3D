package de.schmiereck.hexMap3D.view;

import de.schmiereck.hexMap3D.service.Universe;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
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
        //rootGroup.getChildren().add(sampleGui);

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
        Group lightGroup = new Group();

        {
            PointLight light = new PointLight();
            light.setColor(Color.WHITE);
            light.setRotate(45);
            light.getTransforms().add(new Translate(0,-50,50));
            lightGroup.getChildren().add(light);
        }
        {
            final AmbientLight light = new AmbientLight();
            light.setColor(Color.DARKGRAY);
            lightGroup.getChildren().add(light);
        }
        rootGroup.getChildren().add(lightGroup);

        //-----------------------------------------------------------------------------
        final double w1 = xSizeGrid * GridViewUtils.viewGridStepA + GridViewUtils.viewGridStepA2;
        final double h1 = ySizeGrid * GridViewUtils.viewGridStepH;
        final SubScene gridScene = new SubScene(rootGroup, w1, h1, true, SceneAntialiasing.BALANCED);
        gridScene.setPickOnBounds(true);

        gridScene.setCamera(camera);

        final double w2 = 120;
        final double h2 = 100;
        SubScene guiScene = new SubScene(sampleGui,w2,h2);

        final double w3 = 10.0D;
        HBox root = new HBox(w3);
        root.setAlignment(Pos.TOP_LEFT);
        Pane guiPane = new Pane(guiScene);
        final double w4 = 2.0D;
        //guiPane.relocate(15, 15);
        //guiPane.setTranslateX(15);
        guiPane.setPadding(new Insets(w4));
        guiPane.setBorder(new Border(new BorderStroke(Color.CORNFLOWERBLUE, BorderStrokeStyle.SOLID,  CornerRadii.EMPTY, new BorderWidths(2.0D))));
        root.getChildren().addAll(gridScene, guiPane);

        Scene mainScene = new Scene(root,w1 + w2 + w3 + w4*2, h1);
        primaryStage.setScene(mainScene);

        //primaryStage.setScene(scene);

        //-----------------------------------------------------------------------------
        primaryStage.show();

        //-----------------------------------------------------------------------------
        final MouseLook mouseLook = new MouseLook(camera);
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
    }

    public void updateReality() {
        this.nodeSpace.updateReality();
    }
}
