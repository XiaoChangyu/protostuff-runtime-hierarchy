package p.ka.test.protostuff.hierarchy.bean.customtag.mix;

import java.util.List;

import p.ka.test.protostuff.hierarchy.tools.MyTag;

public class DimensionReductionFather {

	@MyTag("1")
	public String name;
	@MyTag("3")
	public int tall;
	@MyTag("5")
	public double weight;

	@MyTag("7-1")
	public String child_name;
	@MyTag("7-2")
	public int child_tall;
	@MyTag("7-3")
	public double child_weight;
	@MyTag("7-4")
	public List<Toy> child_toys;

	@MyTag("9")
	public List<Car> cars;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight +
				", child_name: " + child_name + ", child_tall: " + child_tall + ", child_weight: " + child_weight + ", child_toys: " + child_toys +
				", cars: " + cars + "}";
	}
}
