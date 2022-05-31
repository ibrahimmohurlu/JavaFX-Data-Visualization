package sample;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;


public class BarChart extends Chart {
    private ArrayList<Bar> bars = new ArrayList<>();
    private String[] colorPalette = {"#003f5c", "#2f4b7c", "#665191", "#a05195", "#d45087", "#f95d6a", "#ff7c43", "#ffa600", "#79ccb3", "#868686"};
    private HashMap<String, String> categoryColor = new HashMap<>();
    Random random = new Random();

    public BarChart(String title, String xAxisLabel, ArrayList<Record> data) {
        super(title, xAxisLabel);
        dataToBars(data);
    }

    @Override
    public void setCaption(String caption) {

    }

    @Override
    public void reset() {
        this.bars.clear();
    }

    //convert data to arraylist of bars
    private void dataToBars(ArrayList<Record> data) {
        for (Record r : data) {
            String name = r.getVal("Name");
            String category = r.getVal("Category");
            int value = Integer.parseInt(r.getVal("Value"));
            this.bars.add(new Bar(name, category, value));
        }
        //sorting bars arraylist ascending order to show only 10 bars which has highest value
        Collections.sort(bars, Collections.reverseOrder());

    }

    public void drawBarChart(Canvas canvas) {
        double height = canvas.getHeight();
        double width = canvas.getWidth();
        int maxNumOfBars = 10;
        //finding range of the values because arraylist is sorted first element has the highest value
        double max = bars.get(0).getValue();
        //getting min value from end of the arraylist
        double min = bars.get(bars.size() - 1).getValue();
        //getting graphics context of the canvas to draw
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //drawing x and y axis
        gc.setStroke(Color.BLACK);
        gc.strokeLine(10, 30, 10, height - 10);
        gc.strokeLine(10, height - 10, width, height - 10);
        //writing title and xlabel
        gc.setTextAlign(TextAlignment.CENTER);
        gc.strokeText(super.getTitle(), width / 2, 10);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.strokeText(super.getxAxisLabel(), width-10, height-20);
        //positioning the bars
        double posY = (height - 150) / maxNumOfBars;
        //drawing only 10 bars to the screen
        for (int i = 0; i < 10; i++) {
            Bar b = bars.get(i);
            //calculating scale factor to fit bars into the canvas
            double scale = (b.getValue() - min) / max;
            //setting fill color from category string by using string's bytes
            byte[] bytes = b.getCategory().getBytes();
            StringBuffer hexString = new StringBuffer();
            for (int j=0;j<bytes.length;j++) {
                hexString.append(Integer.toHexString(0xFF & bytes[j]));
            }
            String finalHex = "#" + hexString.substring(0,6);
            //setting color based on the category string
            gc.setFill(Color.valueOf(finalHex).darker());
            //drawing bar itself
            gc.fillRect(10, posY * i + 150, scale * width, 30);
            gc.setStroke(Color.BLACK);
            //if we are so close to the left of the screen then aligning text to the right to prevent text being not visible
            if (scale * width < width / 4) {
                gc.setTextAlign(TextAlignment.LEFT);
            } else {
                gc.setTextAlign(TextAlignment.RIGHT);
            }
            gc.strokeText(b.getName() + "  " + b.getValue(), scale * width, posY * i + 15 + 150);


        }

    }
}