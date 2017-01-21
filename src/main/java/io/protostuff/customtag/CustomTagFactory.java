package io.protostuff.customtag;

import java.lang.annotation.Annotation;

import io.protostuff.HierarchyTag;

/**
 * 自定义层级 Tag 工厂, 每个 ClassLoader 中只会有一个自定义层级 Tag 存在.
 * <p>
 * Translate comment TODO
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
public class CustomTagFactory {

	/**
	 * Default Custom Hierarchy Tag Builder.
	 * Default use {@link HierarchyTag}
	 */
	public final static CustomTagBuilder DEFAULT_CUSTOMTAGBUILDER = new CustomTagBuilder() {
		@Override
		public Class<? extends Annotation> getTagAnnotation() {
			return HierarchyTag.class;
		}
		@Override
		public <T extends Annotation> int groupFilter(T t) {
			if (t instanceof HierarchyTag) {
				HierarchyTag hTag = (HierarchyTag) t;
				return hTag.groupFilter();
			}
			return 0;
		}
		@Override
		public <T extends Annotation> int[] getTagValue(T t) {
			if (t instanceof HierarchyTag) {
				HierarchyTag hTag = (HierarchyTag) t;
				return hTag.value();
			}
			return null;
		}
		@Override
		public <T extends Annotation> String getAlias(T t) {
			if (t instanceof HierarchyTag) {
				HierarchyTag hTag = (HierarchyTag) t;
				return hTag.alias();
			}
			return null;
		}
	};

	private static volatile CustomTagBuilder customTagBuilder = null;

	/**
	 * 设置 ClassLoader 级别的全局 自定义层级 Tag 构造器.
	 * <p>
	 * Translate comment TODO
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param customTagBuilder
	 */
	public synchronized static void setCustomTagBuilder(CustomTagBuilder customTagBuilder) {
		CustomTagFactory.customTagBuilder = customTagBuilder;
	}

	/**
	 * 获取 ClassLoader 级别的全局 自定义层级 Tag 构造器.
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @return {@link CustomTagBuilder}
	 */
	public synchronized static CustomTagBuilder getCustomTagBuilder() {
		CustomTagBuilder _customTagBuilder = customTagBuilder;
		if (_customTagBuilder == null) {
			_customTagBuilder = DEFAULT_CUSTOMTAGBUILDER;
			setCustomTagBuilder(_customTagBuilder);
		}
		return _customTagBuilder;
	}

}
