public class Main {
    public static void main(String[] args) {
        Car car = new Car(2000, 19.999);
        Car audiCar = new AudiCar(2008, 27.900, "Romania");
        Car porscheCar = new PorscheCar(2020, 54.399, "Macan");

        System.out.println(car);
        System.out.println(audiCar);
        System.out.println(porscheCar);

        porscheCar = audiCar;

        System.out.println(porscheCar);
    }
}
