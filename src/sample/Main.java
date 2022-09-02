package sample;/*
Name - Tahzib Yafi
NSID - tay642
Student id - 11255718
CMPT 381-03
 */

//package com.example.assignment1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Circle swatch;
    private static Circle circle1, circle2, circle3;
    private Color currentColor;
    private static ColorSlider redSlider, greenSlider, blueSlider;
    private static int addPalettePressed = 0;       // Keeps track of the number of times "Add to Palette" button has been clicked

    /**
     * ColorSlider contains the label of the slider(red,green or blue), slider and the value of the slider
     * */
    static class ColorSlider extends Pane {
        private Label title;
        public Slider slider;
        private Label colorValue;

        /**
         * Construct a slider
         * @param title label of the slider
         * */
        ColorSlider(String title){
            this.title = new Label();
            this.title.setText(title);
            HBox Title = new HBox(this.title);  // HBox for containing label of the slider
            Title.setPrefWidth(35);
            slider = new Slider(0,255,50);
            this.colorValue = new Label();
            colorValue.setText(Integer.toString(getValue()));   // Obtain value from the slider

            HBox sliderInfo = new HBox();
            sliderInfo.setSpacing(10);
            sliderInfo.getChildren().addAll(Title,slider,colorValue);   // HBox contains slider label, slider, slider value
            this.getChildren().addAll(sliderInfo);
        }

        /**
         * return the current value of slider
         * */
        public int getValue(){
            return (int)slider.getValue();
        }

        /**
         * set the text for the slider value
         * @param val current value of the slider
         * */
        public void setValue(int val){
            colorValue.setText(String.valueOf(val));
        }
    }

    /**
     * PaletteView contains three Circle objects in an HBox and a reference to a ColoPalette object
     * */
    static class PaletteView extends Pane{
        HBox circles = new HBox();
        ColorPalette colorPalette;
        /**
         * Default constructor when no parameter is passed in
         * */
        PaletteView(){
            circles.setPadding(new Insets(0,10,0,10));
            circles.getChildren().addAll(circle1,circle2,circle3);
            this.getChildren().add(circles);
        }

        /**
         * Places three color palettes in an HBox
         * @param cp ColorPalette object
         * */
        PaletteView(ColorPalette cp){
            Circle c1,c2,c3;
            circles.setPadding(new Insets(0,10,0,10));

            if(cp==null){       // Places three blank color palettes in the list
                c1 = new Circle(circle1.getRadius(),Color.WHITE);
                c2 = new Circle(circle2.getRadius(),Color.WHITE);
                c3 = new Circle(circle3.getRadius(),Color.WHITE);
            }
            else{               // Grabs present color palettes and places them in the list
                colorPalette = cp;
                c1 = new Circle(circle1.getRadius(),colorPalette.colorLeft);
                c2 = new Circle(circle2.getRadius(),colorPalette.colorMiddle);
                c3 = new Circle(circle3.getRadius(),colorPalette.colorRight);
            }

            c1.setStroke(Color.BLACK);
            c2.setStroke(Color.BLACK);
            c3.setStroke(Color.BLACK);
            circles.getChildren().addAll(c1,c2,c3);  // Put all three circles in an HBox
            this.getChildren().add(circles);
        }

    }

    /**
     * PaletteCell displays PaletteView object
     * */
    static class PaletteCell extends ListCell<ColorPalette>{
        public void updateItem(ColorPalette item, boolean empty){
            super.updateItem(item,empty);
            PaletteView pv = new PaletteView(item);
            if(item==null){
                setGraphic(null);
            }
            else{
                setGraphic(pv);
            }
        }
    }

    /**
     * ColorPalette contains three color objects
     * */
    static class ColorPalette{
        private Color colorLeft = Color.WHITE;
        private Color colorMiddle = Color.WHITE;
        private Color colorRight = Color.WHITE;

        /**
         * Adds the color to one of the three color palettes
         * @param c color to be placed
         * */
        public void addColor(Color c){
            if(addPalettePressed%3==1){
                colorLeft = c;
                circle1.setFill(c);
            }
            else if(addPalettePressed%3==2){
                colorMiddle = c;
                circle2.setFill(c);
            }
            else{
                colorRight = c;
                circle3.setFill(c);
            }
        }
    }

    /**
     * Sets the color of the swatch and the sliders
     * */
    public void setColor(){
        int redSliderVal = redSlider.getValue();
        int greenSliderVal = greenSlider.getValue();
        int blueSliderVal = blueSlider.getValue();

        currentColor = Color.rgb(redSliderVal,greenSliderVal,blueSliderVal);
        swatch.setFill(currentColor);
        redSlider.setValue(redSliderVal);
        greenSlider.setValue(greenSliderVal);
        blueSlider.setValue(blueSliderVal);
    }

    @Override
    public void start(Stage stage) throws IOException {
        HBox root = new HBox();

        // Red, green and blue sliders
        redSlider = new ColorSlider("Red:");
        greenSlider = new ColorSlider("Green:");
        blueSlider = new ColorSlider("Blue:");

        // Listener for setting the color of the swatch and the values of the sliders
        redSlider.slider.valueProperty().addListener((observable,oldValue,newValue)->setColor());
        greenSlider.slider.valueProperty().addListener((observable,oldValue,newValue)-> setColor());
        blueSlider.slider.valueProperty().addListener((observable,oldValue,newValue)-> setColor());

        // VBox for containing all 3 ColorSlider object
        VBox sliders = new VBox();
        sliders.setSpacing(20);
        sliders.setPadding(new Insets(0,10,0,10));
        sliders.getChildren().addAll(redSlider,greenSlider,blueSlider);

        // Top color swatch with 70px radius
        swatch = new Circle(70,Color.rgb(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
        VBox topCircle = new VBox(swatch);
        topCircle.setAlignment(Pos.BASELINE_CENTER);
        topCircle.setPadding(new Insets(10));

        // Add to palette button and a VBox for aligning it along the center
        ColorPalette cp = new ColorPalette();

        Button addToPalette = new Button("Add to Palette");
        addToPalette.setOnAction(event->{
            addPalettePressed++;
            cp.addColor(currentColor);
        });

        VBox addPalette = new VBox(addToPalette);
        addPalette.setAlignment(Pos.BASELINE_CENTER);

        // Add to list button and a VBox for aligning it along the center
        Button addToList = new Button("Add to List");

        ObservableList<ColorPalette> colorPaletteList = FXCollections.observableArrayList();
        ListView<ColorPalette> paletteList = new ListView<>(colorPaletteList);
        paletteList.setPrefWidth(290);
        addToList.setOnAction(event-> {     // Adds group of three palettes to the list
            paletteList.getItems().add(cp);
            paletteList.setCellFactory(listItem -> new PaletteCell());
        });

        VBox addList = new VBox(addToList);
        addList.setAlignment(Pos.BASELINE_CENTER);


        // Three horizontal bottom palettes
        circle1 = new Circle(40,Color.WHITE);
        circle2 = new Circle(40,Color.WHITE);
        circle3 = new Circle(40,Color.WHITE);
        circle1.setStroke(Color.BLACK);
        circle2.setStroke(Color.BLACK);
        circle3.setStroke(Color.BLACK);

        // ColorPalette
        VBox buttonsWithPalettes = new VBox();
        buttonsWithPalettes.setSpacing(15);
        buttonsWithPalettes.setPadding(new Insets(10,0,10,0));
        buttonsWithPalettes.getChildren().addAll(addPalette, new PaletteView(),addList);

        // Left side of the widget
        VBox leftWidget = new VBox();
        leftWidget.getChildren().addAll(topCircle,sliders,buttonsWithPalettes);

        // Right side of the widget containing palette list
        HBox PaletteList = new HBox(paletteList);

        // Entire widget
        root.getChildren().addAll(leftWidget,PaletteList);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}



