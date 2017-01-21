package p.ka.test.protostuff.hierarchy.bean.notag;

public class Toy {

	public Toy() {}

	public Toy(String toyName, double price) {
		this.toyName = toyName;
		this.price = price;
	}

	String toyName;
	double price;

	@Override
	public String toString() {
		return "{toyName: " + toyName + ", price: " + price + "}";
	}
}
