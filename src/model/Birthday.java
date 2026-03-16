package model;

// need to check for valid input? 
// enter birthday -> 

public class Birthday {
    private int day;
    private int month;
    private int year;

    public Birthday(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String toString() {
        return "" + String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + String.format("%02d", year);
    }
    
}
