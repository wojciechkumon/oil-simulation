package org.kris.oilsimulation.model.automatonview;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.kris.oilsimulation.controller.GridCanvasController;
import org.kris.oilsimulation.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.Group;
import java.util.ArrayList;

import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;

import java.util.List;
import java.util.ResourceBundle;

public class GenerateMapView {
    private static final Color WATER_COLOR = Color.rgb(67, 183, 222);
    private static final Color LAND_COLOR = Color.rgb(245, 189, 81);
    private static final String MAPICON_PATH = "view/img/mapicon.png";
    private GridCanvasController canvas;
    private Stage newStage;
    private CellState[][] map;
    private int dim;
    private Scene scene;
    private GraphicsContext gc;
    private double cSize;
    private boolean isMousePressed;
    private double startMovePosX, startMovePosY;
    private Button reset, save;
    private ToggleGroup group;
    //private FlowPane pane1, pane2;


    public GenerateMapView(){
        this.dim = 50;
        initMap();
        //make another stage for scene2
        FlowPane pane2=new FlowPane();
        newStage = new Stage();
        this.scene = new Scene(pane2, 850, 550);
        newStage.setScene(this.scene);
        newStage.setTitle("Set initial settings");
        newStage.setMinHeight(550);
        newStage.setMinWidth(850);
        newStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(MAPICON_PATH)));
        newStage.show();
        redrawMap();
    }

    private void initMenu(Group root){
        reset = new Button("reset");
        reset.setOnAction(e-> resetButtonClicked(e));
        reset.setLayoutX(600);
        reset.setLayoutY(100);

        save = new Button("save");
        save.setOnAction(e-> saveButtonClicked(e));
        save.setLayoutX(700);
        save.setLayoutY(100);

        this.group = new ToggleGroup();

        RadioButton rb1 = new RadioButton("Land");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);
        rb1.setLayoutX(600);
        rb1.setLayoutY(200);

        RadioButton rb2 = new RadioButton("Water");
        rb2.setToggleGroup(group);
        rb2.setLayoutX(600);
        rb2.setLayoutY(230);

        RadioButton rb3 = new RadioButton("Oil leak source");
        rb3.setToggleGroup(group);
        rb3.setLayoutX(600);
        rb3.setLayoutY(260);
        rb1.setUserData("land");
        rb2.setUserData("water");
        rb3.setUserData("source");

        Slider mapSizeSlider = new Slider();
        mapSizeSlider.setMin(20);
        mapSizeSlider.setMax(180);
        mapSizeSlider.setValue(50);
        mapSizeSlider.setShowTickLabels(true);
        mapSizeSlider.setShowTickMarks(true);
        mapSizeSlider.setMajorTickUnit(40);
        mapSizeSlider.setMinorTickCount(40);
        mapSizeSlider.setBlockIncrement(20);
        mapSizeSlider.setPrefWidth(200);
        mapSizeSlider.setLayoutX(600);
        mapSizeSlider.setLayoutY(300);
        mapSizeSlider.valueProperty().addListener(
                (observable, oldvalue, newvalue) ->
                {
                    int i = newvalue.intValue();
                    //System.out.println(Integer.toString(i));
                    //System.out.println(this.dim+" ");
                    this.dim=new Double(mapSizeSlider.getValue()).intValue();
                    //System.out.println(this.dim+" ");
                    cSize=cellSize();
                    initMap();
                    drawMap(gc);
                } );

        root.getChildren().addAll(reset, save, rb1, rb2, rb3, mapSizeSlider);
    }
    public void resetButtonClicked(ActionEvent e)
    {
        initMap();
        drawMap(gc);
    }

    public void saveButtonClicked(ActionEvent e)
    {
        System.out.println("zapisujem");
    }


    private void initMap(){
        this.map = new CellState[this.dim][this.dim];
        for (int i = 0; i<this.dim; i++){
            for (int j = 0;j<this.dim;j++){
                this.map[i][j] = new WaterCellState(new ArrayList<OilParticle>());
            }
        }
    }

    private void redrawMap(){
        Canvas canvas = new Canvas(550, 550);
        cSize = cellSize();
        Group root=new Group();
        initMenu(root);
        gc = canvas.getGraphicsContext2D();
        drawMap(gc);
        addCanvasMouseClickEvent(canvas);
        addCanvasMouseReleasedEvent(canvas);
        addCanvasMouseMoveEvent(canvas);
        root.getChildren().add(canvas);
        newStage.setResizable(false);
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    private void drawMap(GraphicsContext gc) {
        double cellSize = cSize;
        for (int i = 0; i<this.dim; i++){
            for (int j = 0;j<this.dim;j++){
                gc.setFill(findColor(map[i][j]));
                gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                gc.setStroke(Color.BLACK);
                gc.strokeRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }
    }

    private double cellSize(){
        //int width = scene.getWindow
        double sceneH = scene.getHeight();
        return sceneH/dim;
    }

    private Color findColor(CellState cell){
        if(cell.isWater() && cell.getOilParticles().isEmpty()){
            return WATER_COLOR;
        }else if(!cell.isWater()){
            return LAND_COLOR;
        }else
            return Color.BLACK;
    }

    private void addCanvasMouseClickEvent(Canvas canvas){
        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent ev) {
                isMousePressed = true;
                //System.out.println("pressed");
                startMovePosX = ev.getSceneX();
                startMovePosY = ev.getSceneY();
            }
        });
    }
    private void addCanvasMouseMoveEvent(Canvas canvas){
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent ev) {
                if(isMousePressed) {
                    Double[] cord = sortAsc(ev.getSceneX(), startMovePosX);
                    if(cord[1]>dim*cSize){cord[1]=dim*cSize;}
                    double xSize = cord[1] - cord[0];//ev.getSceneX() - startMovePosX;
                    double xSt = cord[0];
                    cord = sortAsc(ev.getSceneY(), startMovePosY);
                    if(cord[1]>dim*cSize){cord[1]=dim*cSize;}
                    double ySize = cord[1] - cord[0];
                    //System.out.println("moving");
                    gc.setStroke(LAND_COLOR);
                    drawMap(gc);
                    gc.strokeRect(xSt, cord[0], xSize, ySize);
                }
            }
        });
    }
    private Double[] sortAsc(double x, double y){
        Double[] result = new Double[2];
        if(x > y) {
            result[0]=y;
            result[1]=x;
            return result;
        }
        result[0]=x;
        result[1]=y;
        return result;
    }
    private void addCanvasMouseReleasedEvent(Canvas canvas){
        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent ev) {
                //System.out.println("relased"); startMovePosX

                Double [] cord1 = sortAsc(ev.getSceneX(), startMovePosX);
                Double [] cord2 = sortAsc(ev.getSceneY(), startMovePosY);
                cord1[0] = findPos(cord1[0]);
                cord1[1] = findPos(cord1[1]);
                cord2[0] = findPos(cord2[0]);
                cord2[1] = findPos(cord2[1]);
                //System.out.println("X: " + cord1[0] + " Y: " + cord1[1] + " XCELL: " + xCell + " YCELL: " + yCell);
                for(int i = cord1[0].intValue(); i<=cord1[1].intValue(); i++){
                    for(int j = cord2[0].intValue(); j<=cord2[1].intValue(); j++){
                        if(checkCell(i, j)){

                            map[i][j] = chooseCorectCell();
                        }
                    }
                }

                isMousePressed = false;
                drawMap(gc);
            }
        });
    }

    private CellState chooseCorectCell(){
        if(group.getSelectedToggle().getUserData().toString()=="land"){
            return new LandCellState(new ArrayList<OilParticle>());
        }else if(group.getSelectedToggle().getUserData().toString()=="water"){
            return new WaterCellState(new ArrayList<OilParticle>());
        }else{
            ArrayList<OilParticle> list = new ArrayList<OilParticle>();
            list.add(new OilParticle(100,100,100,100));
            return new WaterCellState(list);
        }
    }
    private void changeCellType(double x, double y){
        Integer xCell = findPos(x).intValue();
        Integer yCell = findPos(y).intValue();

        //System.out.println("X: " + x + " Y: " + y + " XCELL: " + xCell + " YCELL: " + yCell);
        if(checkCell(xCell, yCell)){
            map[xCell][yCell] = new LandCellState(new ArrayList<OilParticle>());
        }
    }

    private Double findPos(Double l){
        return l/cSize;
    }

    private boolean checkCell(Integer x, Integer y){
        if(x>=0 && x < dim && y >= 0 && y < dim){
            return true;
        }
        return false;
    }

}
