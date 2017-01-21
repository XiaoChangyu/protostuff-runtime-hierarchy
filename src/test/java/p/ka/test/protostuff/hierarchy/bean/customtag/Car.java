package p.ka.test.protostuff.hierarchy.bean.customtag;

import p.ka.test.protostuff.hierarchy.tools.MyTag;

public class Car {

	public Car() {}

	public Car(String brand, String model, String color, double price) {
		this.brand = brand;
		this.model = model;
		this.color = color;
		this.price = price;
	}

	@MyTag("1")
	String brand;
	@MyTag("2")
	String model;
	@MyTag("3")
	String color;
	@MyTag("4")
	double price;

	@Override
	public String toString() {
		return "{brand: " + brand + ", model: " + model + ", color: " + color + ", price: " + price + "}";
	}
}
