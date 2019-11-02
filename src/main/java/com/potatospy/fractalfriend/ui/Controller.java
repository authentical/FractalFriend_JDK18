package com.potatospy.fractalfriend.ui;



/*
 *
 * I tried removing the actual algorithm code from Controller.
 * FractalGenerator class constructor needed iterations and ColorPicker values only
 *
 * makeFractal which was wholly inside FractalGenerator was passed GraphicsContext
 * but I don't think JFX likes gc calls to be made outside of the Controller.
 *
 * Todo Have to work around that.
 *
 * I guess calling the draw and setstroke methods isn't really a FractalGenerator function... its
 * a graphical one. THe draw function should be in Controller anyway, handling messing around with
 * GraphicsContext
 *
 *
 *
 *
 *
 *
 *
 *
 * */


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


public class Controller implements Initializable {


    // == Graphics and Drawing ==

    @FXML
    private Canvas fractal;     // This is the drawable area

    private GraphicsContext gc;



    // == Fields ==

    private int iterations=1;

    private double[] stepArray = new double[3]; // Stores amount to change each RGB color per iteration



    // == Input Controls ==

    @FXML
    private ColorPicker colorPickerStartColour;

    @FXML
    private ColorPicker colorPickerEndColour;

    @FXML
    private Spinner<Integer> spinnerIterations;

    @FXML
    private Slider sliderSegmentLength;

    @FXML
    private Label labelSegmentLength;

    @FXML
    private Slider sliderInitialAngle;

    @FXML
    private Label labelInitialAngle;

    @FXML
    private Slider sliderRelativeLength;

    @FXML
    private Label labelRelativeLength;

    @FXML
    private Slider sliderRelativeAngle;

    @FXML
    private Label labelRelativeAngle;

    @FXML
    private Slider sliderYPos;

    @FXML
    private Label labelYPos;

    @FXML
    private CheckBox checkboxRandomness;    // Todo



    // == Initialize ==

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // SET UP CANVAS for drawing
        gc = fractal.getGraphicsContext2D();
        clearCanvas();


        // SET INITIAL Slider, ColorPicker and Spinner values to their respective labels
        labelSegmentLength.textProperty().setValue(String.valueOf((int)sliderSegmentLength.getValue()));
        labelInitialAngle.textProperty().setValue(String.valueOf((int)sliderInitialAngle.getValue()));
        labelRelativeLength.textProperty().setValue(String.valueOf((int)sliderRelativeLength.getValue()));
        labelRelativeAngle.textProperty().setValue(String.valueOf((int)sliderRelativeAngle.getValue()));
        labelYPos.textProperty().setValue(String.valueOf((int)sliderYPos.getValue()));


        // SET UP INPUT OBSERVERS (Makes slider values appear in text fields below each one)
        sliderSegmentLength.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                labelSegmentLength.textProperty().setValue(String.valueOf((int)sliderSegmentLength.getValue()));
            }
        });
        sliderInitialAngle.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                labelInitialAngle.textProperty().setValue(String.valueOf((int)sliderInitialAngle.getValue()));
            }
        });
        sliderRelativeLength.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                labelRelativeLength.textProperty().setValue(String.valueOf((int)sliderRelativeLength.getValue()));
            }
        });
        sliderRelativeAngle.valueProperty().addListener(new ChangeListener<Number>() {  ////////// Todo Rel angle
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                labelRelativeAngle.textProperty().setValue(String.valueOf((int)sliderRelativeAngle.getValue()));
            }
        });
        sliderYPos.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                labelYPos.textProperty().setValue(String.valueOf((int)sliderYPos.getValue()));
            }
        });
    }



    // == Button Handlers ==

    @FXML
    private void clearCanvas(){
        gc.setFill(Color.WHITE);    // Background color Todo
        gc.fillRect(0,0,1000,700);
    }


    // drawTree - Gather all input data from input controls and draw
    @FXML
    private void drawTree(){

        iterations = spinnerIterations.getValue();
        double segmentLength = sliderSegmentLength.getValue();
        double initialAngle = Math.toRadians(sliderInitialAngle.getValue());//270* Math.PI/180;
        double nextIterationRelativeLength=(sliderRelativeLength.getValue()/100);  // Shorten or lengthen each proceeding child segment
        double nextIterationAngle=sliderRelativeAngle.getValue()* Math.PI/180;
        double endOfLastSegmentY= -sliderYPos.getValue() + 700;
        double endOfLastSegmentX= 450;
        stepArray = initializeColourSteps(iterations);


        makeFractal(gc,
                endOfLastSegmentX, endOfLastSegmentY, segmentLength,
                initialAngle, iterations,
                nextIterationRelativeLength, nextIterationAngle);

    }


    // makeFractal draws 1 iteration of the fractal and calls two more makeFractal
    public void makeFractal(GraphicsContext gc,
                            double endOfLastSegmentX, double endOfLastSegmentY,
                            double segmentLength,
                            double segmentAngle,
                            int iterationsRemaining,
                            double nextIterationRelativeLength, double nextIterationAngle){

        // If there are no iterations remaining to draw, stop.
        if(iterationsRemaining<0){ return; }
        iterationsRemaining--;

        // Set segment line width
        gc.setLineWidth(iterationsRemaining);

        // Set segment's colour
        gc.setStroke(getNextColour(iterationsRemaining));

        // Set end of current segment
        double endOfCurrentSegmentX = segmentLength*Math.cos(segmentAngle)+endOfLastSegmentX;
        double endOfCurrentSegmentY = segmentLength*Math.sin(segmentAngle)+endOfLastSegmentY;

        // Draw this segment
        gc.strokeLine(endOfLastSegmentX, endOfLastSegmentY, endOfCurrentSegmentX, endOfCurrentSegmentY);


        // Calculate and run next iterations
        makeFractal(gc,
                endOfCurrentSegmentX,endOfCurrentSegmentY,
                getNextSegmentLength(segmentLength,nextIterationRelativeLength,iterationsRemaining),
                getNextIterationAngle(segmentAngle, nextIterationAngle, true),
                iterationsRemaining,
                nextIterationRelativeLength, nextIterationAngle);
        makeFractal(gc,
                endOfCurrentSegmentX,endOfCurrentSegmentY,
                getNextSegmentLength(segmentLength,nextIterationRelativeLength,iterationsRemaining),
                getNextIterationAngle(segmentAngle, nextIterationAngle, false),
                iterationsRemaining,
                nextIterationRelativeLength, nextIterationAngle);

    }

    // getNextIterationAngle is a utility method that adds randomness to the segment draw angle
    public double getNextIterationAngle(double segmentAngle,
                                        double nextIterationAngle,
                                        boolean clockwise){

        Random random = new Random();

        double clockwiseRandomModifier = 1+ random.nextFloat()/25;
        double anticlockwiseRandomModifier = 1+ random.nextFloat()/25;


        return segmentAngle + (clockwise? 1 *clockwiseRandomModifier: -1 *anticlockwiseRandomModifier) * nextIterationAngle;
    }

    // getNextSegmentLength is a utility method that adds randomness to the segment length
    public double getNextSegmentLength(double segmentLength, double nextIterationRelativeLength,
                                       int iterationsRemaining){

        Random random = new Random();



        return segmentLength*nextIterationRelativeLength*(1+random.nextFloat()/(25-iterationsRemaining));
    }


    // Determine how much red, green and blue will be added to or subtracted from each iteration
    // so that the color of the first iteration changes evenly to the color of the last iteration
    // as required by startColor and endColor
    public double[] initializeColourSteps(int iterations){
        double startRed = colorPickerStartColour.getValue().getRed();
        double endRed = colorPickerEndColour.getValue().getRed();
        double startGreen = colorPickerStartColour.getValue().getGreen();
        double endGreen = colorPickerEndColour.getValue().getGreen();
        double startBlue = colorPickerStartColour.getValue().getBlue();
        double endBlue = colorPickerEndColour.getValue().getBlue();
        double redStep=0.0d;
        double greenStep=0.0d;
        double blueStep=0.0d;


        // Red
        redStep = (endRed-startRed)/iterations;

        // Green
        greenStep = (endGreen-startGreen)/iterations;

        // Blue
        blueStep = (endBlue-startBlue)/iterations;

        stepArray[0] = redStep;
        stepArray[1] = greenStep;
        stepArray[2] = blueStep;


        return stepArray;

    }


    // Add or subtract color from the color used by the segment of the last iteration
    public Color getNextColour(int iterationsRemaining){


        double red = (iterations-iterationsRemaining-1)*stepArray[0] + colorPickerStartColour.getValue().getRed();
        double green = (iterations-iterationsRemaining-1)*stepArray[1] + colorPickerStartColour.getValue().getGreen();
        double blue = (iterations-iterationsRemaining-1)*stepArray[2] + colorPickerStartColour.getValue().getBlue();


        return Color.color(red, green, blue);
    }
}
