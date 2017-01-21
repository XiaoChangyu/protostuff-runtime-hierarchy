package p.ka.test.protostuff.hierarchy.bean.hierarchytag.mix;

import java.util.Arrays;

import io.protostuff.Tag;
import p.ka.test.protostuff.hierarchy.tools.ByteArrayTool;
import p.ka.test.protostuff.hierarchy.tools.HierarchyProtostuffTool;
import p.ka.test.protostuff.hierarchy.tools.ProtostuffTool;

/**
 * Mix Hierarchy Test. All tag of FIELD of Bean mark with {@link HierarchyTag} and {@link Tag}.
 * And build a {@link DimensionReductionFather} to store the {@link Father}'s {@link Child} dimension.
 * Then test Deserialize {@link Father}'s bytes to {@link DimensionReductionFather},
 * and Serialize {@link DimensionReductionFather} to {@link Father}'s bytes.
 * 混合层级测试. 所有的 Bean 的字段的 tag 由 {@link HierarchyTag} 和 {@link Tag} 标记.
 * 同时创建一个 {@link DimensionReductionFather} 来储存 {@link Father} 的 {@link Child} 维度.
 * 然后测试将 {@link Father} 的字节数组反序列化为 {@link DimensionReductionFather},
 * 和 将 {@link DimensionReductionFather} 序列化为 {@link Father} 的字节数组.
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
public class HierarchyTest_HierarchyTagMix {

	public static void main(String[] args) throws Throwable {
		Father father = BeanBuilder_HierarchyTagMix.getFather();
		testStuffToDimensionReductionFatherStuff(father);
	}

	/**
	 * Test ProtostuffTool serializer Bytes deserializer by HierarchyProtostuffTool to reduce dimension, then restore the dimension
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param father
	 */
	public static void testStuffToDimensionReductionFatherStuff(Father father) {
		System.out.println("==== testStuffToDimensionReductionFatherStuff ====");
		System.out.println(String.format("%-30s%s", "father:", father));
		System.out.println("---- Normal Protostuff Serialize (普通 Protostuff 序列化) ----");
		byte[] fatherBytes = ProtostuffTool.serializer(father);
		System.out.println(String.format("%-30s%s", "fatherBytes:", ByteArrayTool.toByteString(fatherBytes)));
		System.out.println(String.format("%-30s%s", "fatherBytes.length:", fatherBytes.length));
		System.out.println("---- Dimension Reduction Deserialize (降维反序列化) ----");
		DimensionReductionFather deserDRFather = HierarchyProtostuffTool.deserializer(fatherBytes, DimensionReductionFather.class);
		System.out.println(String.format("%-30s%s", "deserDRFather:", deserDRFather));
		System.out.println("---- Restore Dimension (恢复维度) ----");
		byte[] deserDRFatherBytes = HierarchyProtostuffTool.serializer(deserDRFather);
		System.out.println(String.format("%-30s%s", "deserDRFatherBytes:", ByteArrayTool.toByteString(deserDRFatherBytes)));
		System.out.println(String.format("%-30s%s", "deserDRFatherBytes.length:", deserDRFatherBytes.length));
		System.out.println("fatherBytes.equals(deserDRFatherBytes): " + Arrays.equals(fatherBytes, deserDRFatherBytes));
		System.out.println();
	}
}
