package p.ka.test.protostuff.hierarchy.bean.hierarchytag;

import io.protostuff.HierarchyTag;

public class Car {

	public Car() {}

	public Car(String brand, String model, String color, double price) {
		this.brand = brand;
		this.model = model;
		this.color = color;
		this.price = price;
	}

	@HierarchyTag(1)
	String brand;
	@HierarchyTag(2)
	String model;
	@HierarchyTag(3)
	String color;
	@HierarchyTag(4)
	double price;

	@Override
	public String toString() {
		return "{brand: " + brand + ", model: " + model + ", color: " + color + ", price: " + price + "}";
	}
}
