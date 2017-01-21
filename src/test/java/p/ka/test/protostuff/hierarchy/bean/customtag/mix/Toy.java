package p.ka.test.protostuff.hierarchy.bean.customtag.mix;

import io.protostuff.Tag;
import p.ka.test.protostuff.hierarchy.tools.MyTag;

public class Toy {

	public Toy() {}

	public Toy(String toyName, double price) {
		this.toyName = toyName;
		this.price = price;
	}

	@Tag(2)
	@MyTag("2")
	String toyName;
	@Tag(4)
	@MyTag("4")
	double price;

	@Override
	public String toString() {
		return "{toyName: " + toyName + ", price: " + price + "}";
	}
}
