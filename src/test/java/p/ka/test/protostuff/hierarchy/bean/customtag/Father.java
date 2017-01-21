package p.ka.test.protostuff.hierarchy.bean.customtag;

import java.util.List;

import p.ka.test.protostuff.hierarchy.tools.MyTag;

public class Father {

	@MyTag("1")
	String name;
	@MyTag("3")
	int tall;
	@MyTag("5")
	double weight;

	@MyTag("7")
	Child child;
	@MyTag("9")
	List<Car> cars;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight + ", child: " + child + ", cars: " + cars + "}";
	}
}
