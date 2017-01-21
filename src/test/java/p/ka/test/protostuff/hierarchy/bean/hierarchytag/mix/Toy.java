package p.ka.test.protostuff.hierarchy.bean.hierarchytag.mix;

import io.protostuff.HierarchyTag;
import io.protostuff.Tag;

public class Toy {

	public Toy() {}

	public Toy(String toyName, double price) {
		this.toyName = toyName;
		this.price = price;
	}

	@Tag(2)
	@HierarchyTag(2)
	String toyName;
	@Tag(4)
	@HierarchyTag(4)
	double price;

	@Override
	public String toString() {
		return "{toyName: " + toyName + ", price: " + price + "}";
	}
}
