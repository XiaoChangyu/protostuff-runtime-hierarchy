package p.ka.test.protostuff.hierarchy.bean.hierarchytag.mix;

import java.util.List;

import io.protostuff.HierarchyTag;

public class DimensionReductionFather {

	@HierarchyTag(1)
	public String name;
	@HierarchyTag(3)
	public int tall;
	@HierarchyTag(5)
	public double weight;

	@HierarchyTag({7,1})
	public String child_name;
	@HierarchyTag({7,2})
	public int child_tall;
	@HierarchyTag({7,3})
	public double child_weight;
	@HierarchyTag({7,4})
	public List<Toy> child_toys;

	@HierarchyTag(9)
	public List<Car> cars;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight +
				", child_name: " + child_name + ", child_tall: " + child_tall + ", child_weight: " + child_weight + ", child_toys: " + child_toys +
				", cars: " + cars + "}";
	}
}
