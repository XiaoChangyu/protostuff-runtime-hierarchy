package p.ka.test.protostuff.hierarchy.bean.customtag.mix;

import java.util.List;

import io.protostuff.Tag;
import p.ka.test.protostuff.hierarchy.tools.MyTag;

public class Child {

	@Tag(1)
	@MyTag("1")
	String name;
	@Tag(2)
	@MyTag("2")
	int tall;
	@Tag(3)
	@MyTag("3")
	double weight;
	@Tag(4)
	@MyTag("4")
	List<Toy> toys;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight + ", toys: " + toys + "}";
	}
}
