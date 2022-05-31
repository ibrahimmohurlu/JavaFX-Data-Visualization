package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;


import java.util.ArrayList;
import java.util.Collections;

public class LineChart extends Chart {
    private ArrayList<ArrayList<Record>> records = new ArrayList<>();
    private ArrayList<ArrayList<Line>> lines = new ArrayList<>();

    public LineChart(String title, String xAxisLabel) {
        super(title, xAxisLabel);

    }

    public void drawLineChart(Canvas canvas) {
        double height = canvas.getHeight();
        double width = canvas.getWidth();
        GraphicsContext gc= canvas.getGraphicsContext2D();
        gc.clearRect(0,0,height,width);
        //drawing x and y axis
        gc.setStroke(Color.BLACK);
        gc.strokeLine(10, 30, 10, height - 10);
        gc.strokeLine(10, height - 10, width, height - 10);
        //writing title and xlabel
        gc.setTextAlign(TextAlignment.CENTER);
        gc.strokeText(super.getTitle(), width / 2, 10);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.strokeText(super.getxAxisLabel(), width-10, height-20);
    }

    public void addData(ArrayList<Record> data) {
        Collections.sort(data, Collections.reverseOrder());
        records.add(data);
    }

    public void setNextValues() {

    }

    @Override
    public void setCaption(String caption) {

    }

    @Override
    public void reset() {

    }
}
