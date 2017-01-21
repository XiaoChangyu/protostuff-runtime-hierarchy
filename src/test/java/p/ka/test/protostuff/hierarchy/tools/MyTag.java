package p.ka.test.protostuff.hierarchy.tools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is a custom Hierarchy Tag.
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MyTag {

	/**
	 * 功能描述: Use a String to express hierachical Tag relationship, like "2-1-3"
	 * 使用一个字符串来表示层级的 Tag 关系, 比如 "2-1-3"
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @return {@link String}
	 */
	String value();

	/**
	 * Overrides the field name (useful for non-binary formats like json/xml/yaml). Optional.
	 */
	String alias() default "";

	/**
	 * A value of 0x1F means the first 5 groups (1,2,4,8,16 - bits) will include this field. A <b>negative</b> value of 0x1F means the first 5 groups will
	 * <b>exclude</b> this field. Optional.
	 */
	int groupFilter() default 0;
}