package p.ka.test.protostuff.hierarchy.bean.notag;

import java.util.List;

public class Father {

	String name;
	int tall;
	double weight;

	Child child;
	List<Car> cars;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight + ", child: " + child + ", cars: " + cars + "}";
	}
}
