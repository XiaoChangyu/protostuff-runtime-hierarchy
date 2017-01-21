package p.ka.test.protostuff.hierarchy.bean.hierarchytag;

import java.util.List;

import io.protostuff.HierarchyTag;

public class Father {

	@HierarchyTag(1)
	String name;
	@HierarchyTag(3)
	int tall;
	@HierarchyTag(5)
	double weight;

	@HierarchyTag(7)
	Child child;
	@HierarchyTag(9)
	List<Car> cars;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight + ", child: " + child + ", cars: " + cars + "}";
	}
}
