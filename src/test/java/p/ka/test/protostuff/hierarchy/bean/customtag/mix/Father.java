package p.ka.test.protostuff.hierarchy.bean.customtag.mix;

import java.util.List;

import io.protostuff.Tag;
import p.ka.test.protostuff.hierarchy.tools.MyTag;

public class Father {

	@Tag(1)
	@MyTag("1")
	String name;
	@Tag(3)
	@MyTag("3")
	int tall;
	@Tag(5)
	@MyTag("5")
	double weight;

	@Tag(7)
	@MyTag("7")
	Child child;
	@Tag(9)
	@MyTag("9")
	List<Car> cars;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight + ", child: " + child + ", cars: " + cars + "}";
	}
}
