package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class Controller implements Initializable {
    private HashMap<LocalDate, ArrayList<Record>> data;
    private String title;
    private String xLabel;
    @FXML
    private Button btnFilePick;
    @FXML
    private Button btnParse;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnReplay;
    @FXML
    private Label labelFile;
    @FXML
    private Label labelProgress;
    @FXML
    private SplitPane splitPane;
    private Stage stage;
    @FXML
    private ComboBox<String> comboBoxChartType;
    @FXML
    private Canvas canvas;
    private String chartType;
    private File file;
    private Timeline animationLoop;
    private boolean play = false;

    public void btnPlayAction(ActionEvent e) {
        //we choose different file so reinitializing the animation
        if(animationLoop==null){
            if (data != null) {
                if (chartType == "Bar Chart") {
                    createBarChartAnimation();
                } else if (chartType == "Line Chart") {
                    createLineChartAnimation();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error on Chart Type");
                    alert.setContentText("Line Chart not implemented in the project.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error on Chart Type");
                    alert.setContentText("You didn't choose the chart type");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error on Parsing");
                alert.setContentText("You didn't parse the file yet");
                alert.showAndWait();
            }
        }
        if (file == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error File is Null!");
            alert.setContentText("You should first choose a file");
            alert.showAndWait();
        } else if (chartType == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on Chart Type");
            alert.setContentText("You didn't choose the chart type");
            alert.showAndWait();
        } else if (data == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on Parsing");
            alert.setContentText("You didn't parse the file yet");
            alert.showAndWait();
        } else if (!play) {
            animationLoop.play();
            play = true;
        } else {
            animationLoop.pause();
            play = false;
        }
    }

    public void btnStopAction(ActionEvent e) {
        if (file == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error File is Null!");
            alert.setContentText("You should first choose a file");
            alert.showAndWait();
        } else if (chartType == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on Chart Type");
            alert.setContentText("You didn't choose the chart type");
            alert.showAndWait();
        } else if (data == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on Parsing");
            alert.setContentText("You didn't parse the file yet");
            alert.showAndWait();
        } else {
            animationLoop.stop();
            play = false;
        }
    }

    public void btnReplayAction(ActionEvent e) {
        if (file == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error File is Null!");
            alert.setContentText("You should first choose a file");
            alert.showAndWait();
        } else if (chartType == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on Chart Type");
            alert.setContentText("You didn't choose the chart type");
            alert.showAndWait();
        } else if (data == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on Parsing");
            alert.setContentText("You didn't parse the file yet");
            alert.showAndWait();
        } else {
            animationLoop.playFromStart();
            play = true;
        }

    }


    private void createLineChartAnimation() {
        LineChart lineChart=new LineChart(title,xLabel);
        lineChart.drawLineChart(canvas);
    }

    private void createBarChartAnimation() {
        //getting graphics content to draw the graph
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double canvasHeight = canvas.getHeight();
        double canvasWidth = canvas.getWidth();
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
        gc.setLineWidth(1);
        LocalDate[] sortedDateArray = data.keySet().stream().sorted().toArray(LocalDate[]::new);

        /* longer version of the code above. Easy to read
        Collection<LocalDate> dates = data.keySet();
        Stream<LocalDate> sortedDates = dates.stream().sorted();
        LocalDate[] sortedDateArray = sortedDates.toArray(LocalDate[]::new);
        */
        //Barchart array that will be used to create animation
        BarChart[] barCharts = new BarChart[sortedDateArray.length];
        //initializing all the barcharts to animate
        for (int i = 0; i < sortedDateArray.length; i++) {
            barCharts[i] = new BarChart(title, xLabel, data.get(sortedDateArray[i]));
        }
        //creating timeline
        animationLoop = new Timeline();
        for (int i = 0; i < barCharts.length; i++) {
            int finalI = i;
            //calling draw method from barchart for every KeyFrame
            animationLoop.getKeyFrames().add(new KeyFrame(Duration.millis(i * 500), actionEvent -> {
                gc.clearRect(0, 0, canvasWidth, canvasHeight);
                gc.setStroke(Color.BLACK);
                String date = sortedDateArray[finalI].toString();
                //saving default font
                Font def = gc.getFont();
                //writing date on the screen bigger
                gc.setFont(new Font(24));
                gc.setTextAlign(TextAlignment.RIGHT);
                gc.strokeText(date, canvasWidth - 50, canvasHeight - 50);
                //setting default font
                gc.setFont(def);
                //drawing barchart on the screen
                barCharts[finalI].drawBarChart(canvas);
            }));

        }
        animationLoop.setAutoReverse(false);
    }

    public void comboBoxChartTypeAction(ActionEvent e) {
        chartType = comboBoxChartType.getValue();
        if (data != null) {
            if (chartType == "Bar Chart") {
                createBarChartAnimation();
            } else if (chartType == "Line Chart") {
                createLineChartAnimation();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Error on Chart Type");
                alert.setContentText("Line Chart not implemented in the project.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error on Chart Type");
                alert.setContentText("You didn't choose the chart type");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on Parsing");
            alert.setContentText("You didn't parse the file yet");
            alert.showAndWait();
        }


    }


    public void btnParseAction(ActionEvent e) throws IOException, ParserConfigurationException, SAXException {
        //creating parser to parse data file

        if (file == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error File is Null!");
            alert.setContentText("You should first choose a file");
            alert.showAndWait();

        }
        //we have a file
        else {
            Parser pr = new Parser(file);
            if (pr.getDataFileExtension().equals("txt") || pr.getDataFileExtension().equals("xml")) {
                //parsing
                pr.Parse();
                //getting parsed data as hashmap
                this.data = pr.getData();
                this.title = pr.getTitle();
                this.xLabel = pr.getXlabel();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error On File Format!");
                alert.setContentText("You must choose a txt or xml file");
                alert.showAndWait();
            }
            //checking data is null if its not then setting label text
            if (this.data != null) {
                labelProgress.setTextAlignment(TextAlignment.CENTER);
                labelProgress.setText("File Parsed Successfully!");
            }
        }


    }

    public void btnExitAction(ActionEvent e) {
        //asking user to really want to exit the program
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You are exiting the program");
        alert.setContentText("Are you sure?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) splitPane.getScene().getWindow();
            stage.close();
        }

    }

    public void btnFilePickAction(ActionEvent e) {
        //choosing a file
        FileChooser fc = new FileChooser();
        file = fc.showOpenDialog(null);
        //checking did we choose a file successfully
        if (file != null) {
            String fileName = file.getName();
        }

        if (file != null) {
            //setting labels text to file name to show on screen
            labelFile.setText(file.getName());
            labelProgress.setText("");
        } else {
            //error on choosing a file
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while choosing a file");
            alert.setContentText("You didn't choose a file");
            alert.showAndWait();
        }
        //if animation loop is not null it means we choose another file so make animation loop null
        if (animationLoop != null) {
            animationLoop = null;
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> list = FXCollections.observableArrayList("Bar Chart", "Line Chart");
        comboBoxChartType.setItems(list);

    }
}
