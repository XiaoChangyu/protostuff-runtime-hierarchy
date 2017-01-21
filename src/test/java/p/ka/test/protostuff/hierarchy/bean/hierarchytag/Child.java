package p.ka.test.protostuff.hierarchy.bean.hierarchytag;

import java.util.List;

import io.protostuff.HierarchyTag;

public class Child {

	@HierarchyTag(1)
	String name;
	@HierarchyTag(2)
	int tall;
	@HierarchyTag(3)
	double weight;
	@HierarchyTag(4)
	List<Toy> toys;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight + ", toys: " + toys + "}";
	}
}
