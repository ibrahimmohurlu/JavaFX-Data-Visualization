package sample;

public class Line {
    private String name, category;
    private int value, nextValue;

    // Creates a new line.
    public Line(String name, String category, int value) {
        this.name = name;
        this.value = value;
        this.category = category;
        this.nextValue = 0;
    }

    // Returns the name of this line.
    public String getName() {
        return name;
    }

    // Returns the category of this line.


    public String getCategory() {
        return category;
    }

    public int getValue() {
        return value;
    }

    //Returns the next value of this line.

    public int getNextValue() {
        return nextValue;
    }

    public void setNextValue(int nextValue) {
        this.nextValue = nextValue;
    }


}

