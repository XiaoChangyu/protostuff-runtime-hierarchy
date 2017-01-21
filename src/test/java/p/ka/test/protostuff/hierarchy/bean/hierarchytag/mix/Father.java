package p.ka.test.protostuff.hierarchy.bean.hierarchytag.mix;

import java.util.List;

import io.protostuff.HierarchyTag;
import io.protostuff.Tag;

public class Father {

	@Tag(1)
	@HierarchyTag(1)
	String name;
	@Tag(3)
	@HierarchyTag(3)
	int tall;
	@Tag(5)
	@HierarchyTag(5)
	double weight;

	@Tag(7)
	@HierarchyTag(7)
	Child child;
	@Tag(9)
	@HierarchyTag(9)
	List<Car> cars;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight + ", child: " + child + ", cars: " + cars + "}";
	}
}
