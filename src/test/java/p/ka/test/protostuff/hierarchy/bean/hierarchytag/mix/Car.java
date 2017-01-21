package p.ka.test.protostuff.hierarchy.bean.hierarchytag.mix;

import io.protostuff.HierarchyTag;
import io.protostuff.Tag;

public class Car {

	public Car() {}

	public Car(String brand, String model, String color, double price) {
		this.brand = brand;
		this.model = model;
		this.color = color;
		this.price = price;
	}

	@Tag(1)
	@HierarchyTag(1)
	String brand;
	@Tag(2)
	@HierarchyTag(2)
	String model;
	@Tag(3)
	@HierarchyTag(3)
	String color;
	@Tag(4)
	@HierarchyTag(4)
	double price;

	@Override
	public String toString() {
		return "{brand: " + brand + ", model: " + model + ", color: " + color + ", price: " + price + "}";
	}
}
