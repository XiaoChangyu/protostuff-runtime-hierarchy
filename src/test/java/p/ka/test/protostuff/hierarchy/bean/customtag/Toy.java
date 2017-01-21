package p.ka.test.protostuff.hierarchy.bean.customtag;

import p.ka.test.protostuff.hierarchy.tools.MyTag;

public class Toy {

	public Toy() {}

	public Toy(String toyName, double price) {
		this.toyName = toyName;
		this.price = price;
	}

	@MyTag("2")
	String toyName;
	@MyTag("4")
	double price;

	@Override
	public String toString() {
		return "{toyName: " + toyName + ", price: " + price + "}";
	}
}
