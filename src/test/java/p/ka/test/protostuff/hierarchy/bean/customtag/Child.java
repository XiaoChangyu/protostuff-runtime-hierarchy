package p.ka.test.protostuff.hierarchy.bean.customtag;

import java.util.List;

import p.ka.test.protostuff.hierarchy.tools.MyTag;

public class Child {

	@MyTag("1")
	String name;
	@MyTag("2")
	int tall;
	@MyTag("3")
	double weight;
	@MyTag("4")
	List<Toy> toys;

	@Override
	public String toString() {
		return "{name: " + name + ", tall: " + tall + ", weight: " + weight + ", toys: " + toys + "}";
	}
}
