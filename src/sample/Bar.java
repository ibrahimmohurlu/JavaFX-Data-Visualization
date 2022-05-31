package sample;

public class Bar implements Comparable<Bar> {
    private String name,category;
    private int value;
    // Creates a new bar.
    public Bar(String name, String category, int value) {
        this.name=name;
        this.category=category;
        this.value=value;
    }

    // Returns the name of this bar.
    public String getName() {
        return this.name;
    }

    // Returns the category of this bar.
    public String getCategory() {
        return this.category;
    }

    // Returns the value of this bar.
    public int getValue() {
        return this.value;
    }


    // Compares two bars by value.
    public int compareTo(Bar other) {
        int result;
        if (this.value > other.value) {
            result = 1;
        } else if (this.value < other.value) {
            result = -1;
        } else {
            result = 0;
        }
        return result;
    }
//Feel free to add other necessary method
}
