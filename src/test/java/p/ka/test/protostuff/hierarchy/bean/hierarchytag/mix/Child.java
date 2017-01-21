package p.ka.test.protostuff.hierarchy.bean.hierarchytag.mix;

import java.util.List;

import io.protostuff.HierarchyTag;
import io.protostuff.Tag;

public class Child {

	@Tag(1)
	@HierarchyTag(1)
	String name;
	@Tag(2)
	@HierarchyTag(2)
	int tall;
	@Tag(3)
	@HierarchyTag(3)
	double weight;
	@Tag(4)
	@HierarchyTag(4)
	List<Toy> toys;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight + ", toys: " + toys + "}";
	}
}
