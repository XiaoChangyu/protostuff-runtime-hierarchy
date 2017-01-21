package p.ka.test.protostuff.hierarchy.bean.hierarchytag;

import io.protostuff.HierarchyTag;
import p.ka.test.protostuff.hierarchy.tools.ByteArrayTool;
import p.ka.test.protostuff.hierarchy.tools.HierarchyProtostuffTool;
import p.ka.test.protostuff.hierarchy.tools.ProtostuffTool;

/**
 * Compatible Test. All tag of FIELD of Bean mark with {@link HierarchyTag}
 * 兼容性测试. 所有的 Bean 的字段的 tag 由 {@link HierarchyTag} 标记.
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
public class CompatibleTest_HierarchyTag {

	public static void main(String[] args) throws Throwable {
		Father father = BeanBuilder_HierarchyTag.getFather();
		testStuffToStuff(father);
		testHierarchyStuffToHierarchyStuff(father);
		testStuffToHierarchyStuff(father);
		testHierarchyStuffToStuff(father);
	}

	/**
	 * Test ProtostuffTool serializer Bytes deserializer by ProtostuffTool
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param father
	 */
	public static void testStuffToStuff(Father father) {
		System.out.println("==== testStuffToStuff ====");
		System.out.println(String.format("%-20s%s", "father:", father));
		byte[] fatherBytes = ProtostuffTool.serializer(father);
		System.out.println(String.format("%-20s%s", "fatherBytes:", ByteArrayTool.toByteString(fatherBytes)));
		System.out.println(String.format("%-20s%s", "fatherBytes.length:", fatherBytes.length));
		Father deserFather = ProtostuffTool.deserializer(fatherBytes, Father.class);
		System.out.println(String.format("%-20s%s", "deserFather:", deserFather));
		System.out.println("father.equals(deserFather): " + father.toString().equals(deserFather.toString()));
		System.out.println();
	}

	/**
	 * Test HierarchyProtostuffTool serializer Bytes deserializer by HierarchyProtostuffTool
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param father
	 */
	public static void testHierarchyStuffToHierarchyStuff(Father father) {
		System.out.println("==== testHierarchyStuffToHierarchyStuff ====");
		System.out.println(String.format("%-20s%s", "father:", father));
		byte[] fatherBytes = HierarchyProtostuffTool.serializer(father);
		System.out.println(String.format("%-20s%s", "fatherBytes:", ByteArrayTool.toByteString(fatherBytes)));
		System.out.println(String.format("%-20s%s", "fatherBytes.length:", fatherBytes.length));
		Father deserFather = HierarchyProtostuffTool.deserializer(fatherBytes, Father.class);
		System.out.println(String.format("%-20s%s", "deserFather:", deserFather));
		System.out.println("father.equals(deserFather): " + father.toString().equals(deserFather.toString()));
		System.out.println();
	}

	/**
	 * Test ProtostuffTool serializer Bytes deserializer by HierarchyProtostuffTool
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param father
	 */
	public static void testStuffToHierarchyStuff(Father father) {
		System.out.println("==== testStuffToHierarchyStuff ====");
		System.out.println("由于 @HierarchyTag 序号和实际属性顺序不同, 所以无法直接兼容, 可使用混合 @Tag 和 @HierarchyTag 标记相同序号来进行兼容, 详见 mix 子包");
		System.out.println();
	}

	/**
	 * Test HierarchyProtostuffTool serializer Bytes deserializer by ProtostuffTool
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param father
	 */
	public static void testHierarchyStuffToStuff(Father father) {
		System.out.println("==== testHierarchyStuffToStuff ====");
		System.out.println("由于 @HierarchyTag 序号和实际属性顺序不同, 所以无法直接兼容, 可使用混合 @Tag 和 @HierarchyTag 标记相同序号来进行兼容, 详见 mix 子包");
		System.out.println();
	}
}
