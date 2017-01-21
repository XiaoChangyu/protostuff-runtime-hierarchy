package p.ka.test.protostuff.hierarchy.tools;

/**
 * 功能描述: 字节数组工具包
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
public class ByteArrayTool {

	/**
	 * 功能描述: 将 字节数组 转换为 定义字符串, 如: {33,75,10,-44,29}
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param bytes
	 * @return String
	 */
	public static String toByteString(byte[] bytes) {
		if (bytes == null || bytes.length == 0) return null;
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < bytes.length; i++) {
			sb.append(bytes[i]);
			if (i < bytes.length - 1) sb.append(",");
		}
		sb.append("}");
		return sb.toString();
	}

	 /**
	 * 功能描述: 转换为字节数组的字符串形式, 如: [33, 75, 10, -44, 29]
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param bytes
	 * @return String
	 */
	public static String toByteArrayString(byte[] bytes) {
		if (bytes == null || bytes.length == 0) return null;
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < bytes.length; i++) {
			sb.append(bytes[i]);
			if (i < bytes.length - 1) sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}
}
