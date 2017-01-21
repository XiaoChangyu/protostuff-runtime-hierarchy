package p.ka.test.protostuff.hierarchy.bean.hierarchytag;

import io.protostuff.HierarchyTag;

public class Toy {

	public Toy() {}

	public Toy(String toyName, double price) {
		this.toyName = toyName;
		this.price = price;
	}

	@HierarchyTag(2)
	String toyName;
	@HierarchyTag(4)
	double price;

	@Override
	public String toString() {
		return "{toyName: " + toyName + ", price: " + price + "}";
	}
}
