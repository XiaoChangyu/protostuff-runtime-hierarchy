package p.ka.test.protostuff.hierarchy.bean.notag;

public class Car {

	public Car() {}

	public Car(String brand, String model, String color, double price) {
		this.brand = brand;
		this.model = model;
		this.color = color;
		this.price = price;
	}

	String brand;
	String model;
	String color;
	double price;

	@Override
	public String toString() {
		return "{brand: " + brand + ", model: " + model + ", color: " + color + ", price: " + price + "}";
	}
}
