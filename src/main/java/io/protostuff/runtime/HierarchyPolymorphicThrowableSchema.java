package io.protostuff.runtime;

import java.io.IOException;
import java.util.List;

import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * 为了做到和 {@link RuntimeSchema} 一样的兼容度, 在处理 {@link Throwable} 这类存在自我引用的 field 的时候需要做特殊处理.
 * 而 {@link PolymorphicThrowableSchema} 中是使用 <code>schema instanceof RuntimeSchema</code> 来做前置判断,
 * 所以只能更改 {@link #tryWriteWithoutCause(Output, Object, Schema)} 方法,
 * 并在 {@link HierarchyRuntimeSchema#writeTo(Output, Object)} 方法中做执行路径判断来达到执行 {@link #tryWriteWithoutCause(Output, Object, Schema)} 方法的目的.
 * <p>
 * Translate comment TODO
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
public class HierarchyPolymorphicThrowableSchema {

	static final java.lang.reflect.Field __cause;

	static {
		java.lang.reflect.Field cause;
		try {
			cause = Throwable.class.getDeclaredField("cause");
			cause.setAccessible(true);
		} catch (Exception e) {
			cause = null;
		}
		__cause = cause;
	}

	static boolean tryWriteWithoutCause(Output output, Object value, Schema<Object> schema) throws IOException {
		if ((schema instanceof HierarchyRuntimeSchema || schema instanceof RuntimeSchema) && __cause != null) {
			int fieldCount = 0;
			List<Field<Object>> fields = null;
			// ignore the field "cause" if its references itself (cyclic)
			if (schema instanceof HierarchyRuntimeSchema) {
				HierarchyRuntimeSchema<Object> ms = (HierarchyRuntimeSchema<Object>) schema;
				fieldCount = ms.getFieldCount();
				fields = ms.getFields();
			} else if (schema instanceof RuntimeSchema) {
				RuntimeSchema<Object> ms = (RuntimeSchema<Object>) schema;
				fieldCount = ms.getFieldCount();
				fields = ms.getFields();
			}

			if (fieldCount > 1 && fields.get(1).name.equals("cause")) {
				final Object cause;
				try {
					cause = __cause.get(value);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				if (cause == value) {
					// its cyclic, skip the second field "cause"
					fields.get(0).writeTo(output, value);

					for (int i = 2, len = fieldCount; i < len; i++)
						fields.get(i).writeTo(output, value);

					return true;
				}
			}
		}

		return false;
	}
}
