package p.ka.test.protostuff.hierarchy.bean.notag;

import java.util.List;

public class Child {

	String name;
	int tall;
	double weight;
	List<Toy> toys;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight + ", toys: " + toys + "}";
	}
}
