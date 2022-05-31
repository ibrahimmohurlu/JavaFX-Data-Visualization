package sample;

public abstract class Chart {
    //Feel free to add other necessary variables
    private String title;
    private String xAxisLabel;

    public Chart(String title, String xAxisLabel) {
        this.title = title;
        this.xAxisLabel = xAxisLabel;
    }

    // Sets the caption of this chart.
    public abstract void setCaption(String caption);

    public String getTitle() {
        return title;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    // Removes all of the records from this chart.
    public abstract void reset();
//Feel free to add other necessary method
}
