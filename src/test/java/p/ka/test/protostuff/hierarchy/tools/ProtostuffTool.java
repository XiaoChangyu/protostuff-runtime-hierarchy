package p.ka.test.protostuff.hierarchy.tools;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * The default Protostuff Tool
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
public class ProtostuffTool {

	public static <T> byte[] serializer(T message) {
		@SuppressWarnings("unchecked")
		Schema<T> schema = RuntimeSchema.getSchema((Class<T>) message.getClass());
		return ProtostuffIOUtil.toByteArray(message, schema, LinkedBuffer.allocate());
	}

	public static <T> T deserializer(byte[] data, Class<T> typeClass) {
		Schema<T> schema = RuntimeSchema.getSchema(typeClass);
		T message = schema.newMessage();
		ProtostuffIOUtil.mergeFrom(data, message, schema);
		return message;
	}
}
