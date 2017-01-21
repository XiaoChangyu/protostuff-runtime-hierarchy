package p.ka.test.protostuff.hierarchy.tools;

import java.lang.annotation.Annotation;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.customtag.CustomTagBuilder;
import io.protostuff.customtag.CustomTagFactory;
import io.protostuff.runtime.HierarchyRuntimeSchema;

/**
 * The custom Hierarchy Protostuff Tool
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
public class HierarchyProtostuffTool_CustomTag {

	static {
		CustomTagFactory.setCustomTagBuilder(new CustomTagBuilder() {

			@Override
			public Class<? extends Annotation> getTagAnnotation() {
				return MyTag.class;
			}

			@Override
			public <T extends Annotation> int groupFilter(T t) {
				if (t instanceof MyTag) {
					MyTag ct = (MyTag) t;
					return ct.groupFilter();
				}
				return 0;
			}

			@Override
			public <T extends Annotation> int[] getTagValue(T t) {
				if (t instanceof MyTag) {
					MyTag ct = (MyTag) t;
					String str = ct.value();
					/* we can define the hierarchy tag format by ourself, just to parse it to int[] on there. */
					String[] cv = str.split("-");
					int[] value = new int[cv.length];
					for (int i = 0; i < value.length; i++) {
						value[i] = Integer.parseInt(cv[i]);
					}
					return value;
				}
				return null;
			}

			@Override
			public <T extends Annotation> String getAlias(T t) {
				if (t instanceof MyTag) {
					MyTag ct = (MyTag) t;
					return ct.alias();
				}
				return "";
			}
		});
	}

	public static <T> byte[] serializer(T message) {
		@SuppressWarnings("unchecked")
		Schema<T> schema = HierarchyRuntimeSchema.getSchema((Class<T>) message.getClass());
		return ProtostuffIOUtil.toByteArray(message, schema, LinkedBuffer.allocate());
	}

	public static <T> T deserializer(byte[] data, Class<T> typeClass) {
		Schema<T> schema = HierarchyRuntimeSchema.getSchema(typeClass);
		T message = schema.newMessage();
		ProtostuffIOUtil.mergeFrom(data, message, schema);
		return message;
	}
}
