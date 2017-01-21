package p.ka.test.protostuff.hierarchy.bean.hierarchytag.mix;

import java.util.ArrayList;
import java.util.List;

public class BeanBuilder_HierarchyTagMix {

	public static Child getChild() {
		Child child = new Child();
		child.name = "lucy";
		child.tall = 166;
		child.weight = 62.1;
		child.toys = getToys();
		return child;
	}

	public static Father getFather() {
		Father father = new Father();
		father.name = "jack";
		father.tall = 188;
		father.weight = 84.22;
		father.child = getChild();
		father.cars = getCars();
		return father;
	}

	private static List<Car> getCars() {
		List<Car> cars = new ArrayList<>();
		cars.add(new Car("Ferrari", "XXX", "Red", 188000.88));
		cars.add(new Car("Benz", "ZZZ", "Blue", 22000.88));
		return cars;
	}

	private static List<Toy> getToys() {
		List<Toy> toys = new ArrayList<>();
		toys.add(new Toy("batman", 100.99));
		toys.add(new Toy("superman", 200.99));
		return toys;
	}
}
