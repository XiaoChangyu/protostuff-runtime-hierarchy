package p.ka.test.protostuff.hierarchy.bean.customtag.mix;

import io.protostuff.Tag;
import p.ka.test.protostuff.hierarchy.tools.ByteArrayTool;
import p.ka.test.protostuff.hierarchy.tools.HierarchyProtostuffTool_CustomTag;
import p.ka.test.protostuff.hierarchy.tools.MyTag;
import p.ka.test.protostuff.hierarchy.tools.ProtostuffTool;

/**
 * Compatible Test. All tag of FIELD of Bean mark with {@link MyTag} and {@link Tag}
 * 兼容性测试. 所有的 Bean 的字段的 tag 由 {@link MyTag} 和 {@link Tag} 标记.
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
public class CompatibleTest_CustomTagMix {

	public static void main(String[] args) throws Throwable {
		Father father = BeanBuilder_CustomTagMix.getFather();
		testStuffToStuff(father);
		testHierarchyCusStuffToHierarchyCusStuff(father);
		testStuffToHierarchyCusStuff(father);
		testHierarchyCusStuffToStuff(father);
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
	 * Test HierarchyProtostuffTool_CustomTag serializer Bytes deserializer by HierarchyProtostuffTool_CustomTag
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param father
	 */
	public static void testHierarchyCusStuffToHierarchyCusStuff(Father father) {
		System.out.println("==== testHierarchyCusStuffToHierarchyCusStuff ====");
		System.out.println(String.format("%-20s%s", "father:", father));
		byte[] fatherBytes = HierarchyProtostuffTool_CustomTag.serializer(father);
		System.out.println(String.format("%-20s%s", "fatherBytes:", ByteArrayTool.toByteString(fatherBytes)));
		System.out.println(String.format("%-20s%s", "fatherBytes.length:", fatherBytes.length));
		Father deserFather = HierarchyProtostuffTool_CustomTag.deserializer(fatherBytes, Father.class);
		System.out.println(String.format("%-20s%s", "deserFather:", deserFather));
		System.out.println("father.equals(deserFather): " + father.toString().equals(deserFather.toString()));
		System.out.println();
	}

	/**
	 * Test ProtostuffTool serializer Bytes deserializer by HierarchyProtostuffTool_CustomTag
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param father
	 */
	public static void testStuffToHierarchyCusStuff(Father father) {
		System.out.println("==== testStuffToHierarchyCusStuff ====");
		System.out.println(String.format("%-20s%s", "father:", father));
		byte[] fatherBytes = ProtostuffTool.serializer(father);
		System.out.println(String.format("%-20s%s", "fatherBytes:", ByteArrayTool.toByteString(fatherBytes)));
		System.out.println(String.format("%-20s%s", "fatherBytes.length:", fatherBytes.length));
		Father deserFather = HierarchyProtostuffTool_CustomTag.deserializer(fatherBytes, Father.class);
		System.out.println(String.format("%-20s%s", "deserFather:", deserFather));
		System.out.println("father.equals(deserFather): " + father.toString().equals(deserFather.toString()));
		System.out.println();
	}

	/**
	 * Test HierarchyProtostuffTool_CustomTag serializer Bytes deserializer by ProtostuffTool
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param father
	 */
	public static void testHierarchyCusStuffToStuff(Father father) {
		System.out.println("==== testHierarchyCusStuffToStuff ====");
		System.out.println(String.format("%-20s%s", "father:", father));
		byte[] fatherBytes = HierarchyProtostuffTool_CustomTag.serializer(father);
		System.out.println(String.format("%-20s%s", "fatherBytes:", ByteArrayTool.toByteString(fatherBytes)));
		System.out.println(String.format("%-20s%s", "fatherBytes.length:", fatherBytes.length));
		Father deserFather = ProtostuffTool.deserializer(fatherBytes, Father.class);
		System.out.println(String.format("%-20s%s", "deserFather:", deserFather));
		System.out.println("father.equals(deserFather): " + father.toString().equals(deserFather.toString()));
		System.out.println();
	}
}
