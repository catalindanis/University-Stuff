public class Car {
    private int year;
    private double price;

    public Car(int year, double price) {
        this.year = year;
        this.price = price;
    }

    Car() {
        this.year = 0;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Car: " +
                "year=" + year +
                ", price=" + price;
    }
}
