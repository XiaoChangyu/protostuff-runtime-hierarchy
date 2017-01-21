package p.ka.test.protostuff.hierarchy.bean.tag;

import java.util.List;

import io.protostuff.Tag;

public class Child {

	@Tag(1)
	String name;
	@Tag(2)
	int tall;
	@Tag(3)
	double weight;
	@Tag(4)
	List<Toy> toys;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight + ", toys: " + toys + "}";
	}
}
