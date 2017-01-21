package io.protostuff.customtag;

import java.lang.annotation.Annotation;

/**
 * 自定义层级 Tag 构造器接口, 为了方便层级 Tag 格式的自定义, 所以定义一个构造接口.
 * 只要自定义的层级 Tag 能够在调用该接口中方法后返回指定格式的数据就行.
 * <p>
 * Translate comment TODO
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
public interface CustomTagBuilder {

	/**
	 * 获取自定义的层级 Tag 类
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @return {@link Class<? extends Annotation>}
	 */
	Class<? extends Annotation> getTagAnnotation();

	/**
	 * 根据传入的自定义层级 Tag 对象, 解析出 int[] 格式的层级关系.
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param t
	 * @return {@link int[]}
	 */
	<T extends Annotation> int[] getTagValue(T t);

	/**
	 * 根据传入的自定义层级 Tag 对象, 解析出 alias
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param t
	 * @return {@link String}
	 */
	<T extends Annotation> String getAlias(T t);

	/**
	 * 根据传入的自定义层级 Tag 对象, 解析出 groupFilter
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param t
	 * @return {@link int}
	 */
	<T extends Annotation> int groupFilter(T t);
}
