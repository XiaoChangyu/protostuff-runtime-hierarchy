//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package io.protostuff.runtime;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.protostuff.Exclude;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.customtag.CustomTagBuilder;
import io.protostuff.customtag.CustomTagFactory;
import io.protostuff.runtime.HierarchyFieldTreeBuilder.Tree;
import io.protostuff.runtime.RuntimeEnv.DefaultInstantiator;
import io.protostuff.runtime.RuntimeEnv.Instantiator;

import static io.protostuff.runtime.RuntimeEnv.ID_STRATEGY;

/**
 * 对于普通 Field, 该 Schema 就是普通的 Schema. 对于 Hierarchy Field, 该 Schema 是封装了多个 Field 的 Schema, 是用外层类加上自己的Field 冒充的子类.
 * 该类的大部分方法都是 {@link RuntimeSchema} 的方法. 这里只是复制过来兼容 {@link RuntimeSchema}.
 * 其实更可靠的做法是继承. 但是因为 {@link RuntimeSchema} 是 final, 所以......
 * <p>
 * Translate comment TODO
 * ------------<p>
 * A schema that can be generated and cached at runtime for objects that have no schema. This is particularly useful for
 * pojos from 3rd party libraries.
 * 
 * @author David Yu
 */
public class HierarchyRuntimeSchema<T> implements Schema<T>, FieldMap<T> {

	public static final IdStrategy HIERARCHY_ID_STRATEGY = new HierarchyDefaultIdStrategy(ID_STRATEGY);

	public static final int MIN_TAG_VALUE = 1;
	public static final int MAX_TAG_VALUE = 536870911; // 2^29 - 1
	public static final String ERROR_TAG_VALUE = "Invalid tag number (value must be in range [1, 2^29-1])";

	private static final Set<String> NO_EXCLUSIONS = Collections.emptySet();

	public static final int MIN_TAG_FOR_HASH_FIELD_MAP = 100;

	

	private final Class<T> typeClass;
	private final FieldMap<T> fieldMap;
	private final Pipe.Schema<T> pipeSchema;
	public final Instantiator<T> instantiator;

	/**
	 * Maps the {@code baseClass} to a specific non-interface/non-abstract {@code typeClass} and registers it (this must be done on application startup).
	 * <p>
	 * With this approach, there is no overhead of writing the type metadata if a {@code baseClass} field is serialized.
	 * <p>
	 * Returns true if the baseClass does not exist.
	 * <p>
	 * NOTE: This is only supported when {@link RuntimeEnv#ID_STRATEGY} is {@link DefaultIdStrategy}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the {@code typeClass} is an interface or an abstract class.
	 */
	public static <T> boolean map(Class<? super T> baseClass, Class<T> typeClass) {
		if (ID_STRATEGY instanceof DefaultIdStrategy)
			return ((HierarchyDefaultIdStrategy) HIERARCHY_ID_STRATEGY).map(baseClass, typeClass);

		throw new RuntimeException("HierarchyRuntimeSchema.map is only supported on DefaultIdStrategy");
	}

	/**
	 * Returns true if this there is no existing one or the same schema has already been registered (this must be done on application startup).
	 * <p>
	 * NOTE: This is only supported when {@link RuntimeEnv#ID_STRATEGY} is {@link DefaultIdStrategy}.
	 */
	public static <T> boolean register(Class<T> typeClass, Schema<T> schema) {
		if (ID_STRATEGY instanceof DefaultIdStrategy)
			return ((HierarchyDefaultIdStrategy) HIERARCHY_ID_STRATEGY).registerPojo(typeClass, schema);

		throw new RuntimeException("HierarchyRuntimeSchema.register is only supported on DefaultIdStrategy");
	}

	/**
	 * Returns true if this there is no existing one or the same schema has already been registered (this must be done on application startup).
	 * <p>
	 * NOTE: This is only supported when {@link RuntimeEnv#ID_STRATEGY} is {@link DefaultIdStrategy}.
	 */
	public static <T> boolean register(Class<T> typeClass) {
		if (ID_STRATEGY instanceof DefaultIdStrategy)
			return ((HierarchyDefaultIdStrategy) HIERARCHY_ID_STRATEGY).registerPojo(typeClass);

		throw new RuntimeException("HierarchyRuntimeSchema.register is only supported on DefaultIdStrategy");
	}

	/**
	 * Returns true if the {@code typeClass} was not lazily created.
	 * <p>
	 * Method overload for backwards compatibility.
	 */
	public static boolean isRegistered(Class<?> typeClass) {
		return isRegistered(typeClass, HIERARCHY_ID_STRATEGY);
	}

	/**
	 * Returns true if the {@code typeClass} was not lazily created.
	 */
	public static boolean isRegistered(Class<?> typeClass, IdStrategy strategy) {
		if (strategy.equals(ID_STRATEGY)) strategy = HIERARCHY_ID_STRATEGY;
		return strategy.isRegistered(typeClass);
	}

	/**
	 * Gets the schema that was either registered or lazily initialized at runtime.
	 * <p>
	 * Method overload for backwards compatibility.
	 */
	public static <T> Schema<T> getSchema(Class<T> typeClass) {
		return getSchema(typeClass, HIERARCHY_ID_STRATEGY);
	}

	/**
	 * Gets the schema that was either registered or lazily initialized at runtime.
	 */
	public static <T> Schema<T> getSchema(Class<T> typeClass, IdStrategy strategy) {
		if (strategy.equals(ID_STRATEGY)) strategy = HIERARCHY_ID_STRATEGY;
		return strategy.getSchemaWrapper(typeClass, true).getSchema();
	}

	/**
	 * Returns the schema wrapper.
	 * <p>
	 * Method overload for backwards compatibility.
	 */
	static <T> HasSchema<T> getSchemaWrapper(Class<T> typeClass) {
		return getSchemaWrapper(typeClass, HIERARCHY_ID_STRATEGY);
	}

	/**
	 * Returns the schema wrapper.
	 */
	static <T> HasSchema<T> getSchemaWrapper(Class<T> typeClass, IdStrategy strategy) {
		if (strategy.equals(ID_STRATEGY)) strategy = HIERARCHY_ID_STRATEGY;
		return strategy.getSchemaWrapper(typeClass, true);
	}

	/**
	 * Generates a schema from the given class.
	 * <p>
	 * Method overload for backwards compatibility.
	 */
	public static <T> HierarchyRuntimeSchema<T> createFrom(Class<T> typeClass) {
		return createFrom(typeClass, NO_EXCLUSIONS, HIERARCHY_ID_STRATEGY);
	}

	/**
	 * Generates a schema from the given class.
	 */
	public static <T> HierarchyRuntimeSchema<T> createFrom(Class<T> typeClass, IdStrategy strategy) {
		return createFrom(typeClass, NO_EXCLUSIONS, strategy);
	}

	/**
	 * Generates a schema from the given class with the exclusion of certain fields.
	 */
	public static <T> HierarchyRuntimeSchema<T> createFrom(Class<T> typeClass, String[] exclusions, IdStrategy strategy) {
		HashSet<String> set = new HashSet<String>();
		for (String exclusion : exclusions)
			set.add(exclusion);

		return createFrom(typeClass, set, strategy);
	}

	/**
	 * Generates a schema from the given class with the exclusion of certain fields.
	 */
	public static <T> HierarchyRuntimeSchema<T> createFrom(Class<T> typeClass, Set<String> exclusions, IdStrategy strategy) {
		if (strategy.equals(ID_STRATEGY)) strategy = HIERARCHY_ID_STRATEGY;
		if (typeClass.isInterface() || Modifier.isAbstract(typeClass.getModifiers())) {
			throw new RuntimeException("The root object can neither be an abstract " + "class nor interface: \"" + typeClass.getName());
		}

		final Map<String, java.lang.reflect.Field> fieldMap = findInstanceFields(typeClass);
		final ArrayList<Field<T>> fields = new ArrayList<Field<T>>(fieldMap.size());
		final HierarchyFieldTreeBuilder hierarchyFieldTreeBuilder = new HierarchyFieldTreeBuilder();
		int i = 0;
		boolean annotated = false;
		CustomTagBuilder customTagBuilder = CustomTagFactory.getCustomTagBuilder();
		Class<? extends Annotation> tagClass = null;
		if (customTagBuilder != null) {
			tagClass = customTagBuilder.getTagAnnotation();
		}
		for (java.lang.reflect.Field f : fieldMap.values()) {
			if (!exclusions.contains(f.getName())) {
				if (f.getAnnotation(Deprecated.class) != null) {
					// this field should be ignored by ProtoStuff.
					// preserve its field number for backward-forward compat
					i++;
					continue;
				}

				final Tag tag = f.getAnnotation(Tag.class);
				final int fieldMapping;
				final Annotation hTag;
				if (tagClass != null) {
					hTag = f.getAnnotation(tagClass);
				} else {
					hTag = null;
				}
				final int[] hFieldMapping;
				final String name;
				if (tag == null && hTag == null) {
					// Fields gets assigned mapping tags according to their
					// definition order
					if (annotated) {
						String className = typeClass.getCanonicalName();
						String fieldName = f.getName();
						String message = tagClass != null ?
								String.format("%s#%s is not annotated with @Tag OR @%s", className, fieldName, tagClass.getSimpleName())
								:
								String.format("%s#%s is not annotated with @Tag", className, fieldName);
						throw new RuntimeException(message);
					}
					fieldMapping = ++i;

					name = f.getName();

					final Field<T> field = RuntimeFieldFactory.getFieldFactory(f.getType(), strategy).create(fieldMapping, name, f, strategy);
					fields.add(field);
				} else {
					// Fields gets assigned mapping tags according to their
					// annotation
					if (!annotated && !fields.isEmpty()) {
						throw new RuntimeException(
								tagClass != null ? "When using annotation-based mapping, all fields must be annotated with @" +
										Tag.class.getSimpleName() + " OR @" + tagClass.getSimpleName()
								: "When using annotation-based mapping, all fields must be annotated with @" + Tag.class.getSimpleName());
					}
					annotated = true;
					Field<T> field = null;

					if (tag != null) {
						fieldMapping = tag.value();

						if (fieldMapping < MIN_TAG_VALUE || fieldMapping > MAX_TAG_VALUE) {
							throw new IllegalArgumentException(ERROR_TAG_VALUE + ": " + fieldMapping + " on " + typeClass);
						}
						name = tag.alias().isEmpty() ? f.getName() : tag.alias();

						field = RuntimeFieldFactory.getFieldFactory(f.getType(), strategy).create(fieldMapping, name, f, strategy);
					} else if (hTag != null) {
						String alias = customTagBuilder.getAlias(hTag);
						name = alias == null || alias.isEmpty() ? f.getName() : alias;
					} else {
						name = f.getName();
					}

					if (hTag != null) {
						field = null;
						hFieldMapping = customTagBuilder.getTagValue(hTag);
						if (hFieldMapping == null || hFieldMapping.length == 0) {
							String className = typeClass.getCanonicalName();
							String fieldName = f.getName();
							String message = String.format("%s#%s is annotated with empty @HierarchyTag", className, fieldName);
							throw new RuntimeException(message);
						}
						for (int hTagValue : hFieldMapping) {
							if (hTagValue < MIN_TAG_VALUE || hTagValue > MAX_TAG_VALUE) {
								throw new IllegalArgumentException(ERROR_TAG_VALUE + ": " + hTagValue + " on " + typeClass + "." + f.getName());
							}
						}
						hierarchyFieldTreeBuilder.add(f, name, hFieldMapping);
					}

					if (field != null)
						fields.add(field);
				}
			}
		}

		Instantiator<T> instantiator = RuntimeEnv.newInstantiator(typeClass);

		List<Tree> treeList = hierarchyFieldTreeBuilder.getAll();
		if (treeList.size() > 0) {
			parseTreeListToFillFieldList(typeClass, instantiator, treeList, fields, strategy);
		}

		return new HierarchyRuntimeSchema<>(typeClass, fields, instantiator);
	}

	static Map<String, java.lang.reflect.Field> findInstanceFields(Class<?> typeClass) {
		LinkedHashMap<String, java.lang.reflect.Field> fieldMap = new LinkedHashMap<String, java.lang.reflect.Field>();
		fill(fieldMap, typeClass);
		return fieldMap;
	}

	static void fill(Map<String, java.lang.reflect.Field> fieldMap, Class<?> typeClass) {
		if (Object.class != typeClass.getSuperclass())
			fill(fieldMap, typeClass.getSuperclass());

		for (java.lang.reflect.Field f : typeClass.getDeclaredFields()) {
			int mod = f.getModifiers();
			if (!Modifier.isStatic(mod) && !Modifier.isTransient(mod) && f.getAnnotation(Exclude.class) == null)
				fieldMap.put(f.getName(), f);
		}
	}

	static <T> void parseTreeListToFillFieldList(Class<T> outerClass, Instantiator<T> instantiator, List<Tree> treeList, List<Field<T>> fields, IdStrategy strategy) {
		List<Field<T>> leavesFields = new ArrayList<>();
		for (int gi = treeList.size() - 1; gi >= 0; gi--) {
			if (treeList.get(gi).isLeaves()) {
				Tree leaves = treeList.remove(gi);
				Field<T> leavesField = RuntimeFieldFactory.getFieldFactory(leaves.field.getType(), strategy).create(leaves.number, leaves.name, leaves.field, strategy);
				leavesFields.add(leavesField);
			}
		}
		if (!leavesFields.isEmpty()) fields.addAll(leavesFields);

		List<Field<T>> treeFields = new ArrayList<>();
		for (int ti = 0; ti < treeList.size(); ti++) {
			Field<T> treeField = HierarchyRuntimeReflectionFieldFactory.HIERARCHY_POJO.create(outerClass, instantiator, treeList.get(ti), strategy);
			treeFields.add(treeField);
		}
		if (!treeFields.isEmpty()) fields.addAll(treeFields);
	}













	public HierarchyRuntimeSchema(Class<T> typeClass, Collection<Field<T>> fields, Constructor<T> constructor) {
		this(typeClass, fields, new DefaultInstantiator<T>(constructor));
	}

	public HierarchyRuntimeSchema(Class<T> typeClass, Collection<Field<T>> fields, Instantiator<T> instantiator) {
		this(typeClass, createFieldMap(fields), instantiator);
	}

	public HierarchyRuntimeSchema(Class<T> typeClass, FieldMap<T> fieldMap, Instantiator<T> instantiator) {
		this.typeClass = typeClass;
		this.fieldMap = fieldMap;
		this.instantiator = instantiator;
		this.pipeSchema = new RuntimePipeSchema<T>(this, fieldMap);
	}

	public HierarchyRuntimeSchema(Class<T> outerClass, Instantiator<T> instantiator, Tree tree, IdStrategy strategy) {
		this(outerClass, createFieldMap(outerClass, instantiator, tree, strategy), instantiator);
	}


	private static <T> FieldMap<T> createFieldMap(Collection<Field<T>> fields) {
		int lastFieldNumber = 0;
		for (Field<T> field : fields) {
			if (field.number > lastFieldNumber) {
				lastFieldNumber = field.number;
			}
		}
		if (preferHashFieldMap(fields, lastFieldNumber)) {
			return new HashFieldMap<T>(fields);
		}
		// array field map should be more efficient
		return new ArrayFieldMap<T>(fields, lastFieldNumber);
	}

	private static <T> boolean preferHashFieldMap(Collection<Field<T>> fields, int lastFieldNumber) {
		return lastFieldNumber > MIN_TAG_FOR_HASH_FIELD_MAP && lastFieldNumber >= 2 * fields.size();
	}

	private static <T> FieldMap<T> createFieldMap(Class<T> outerClass, Instantiator<T> instantiator, Tree tree, IdStrategy strategy) {
		List<Field<T>> fields = new ArrayList<>();
		if (tree.isLeaves()) {
			throw new RuntimeException("不能直接用叶子创建.");
		} else {
			parseTreeListToFillFieldList(outerClass, instantiator, tree.childs, fields, strategy);
		}
		return createFieldMap(fields);
	}


	/**
	 * Returns the pipe schema linked to this.
	 */
	public Pipe.Schema<T> getPipeSchema() {
		return pipeSchema;
	}


	@Override
	public Field<T> getFieldByNumber(int n) {
		return fieldMap.getFieldByNumber(n);
	}

	@Override
	public Field<T> getFieldByName(String fieldName) {
		return fieldMap.getFieldByName(fieldName);
	}

	@Override
	public int getFieldCount() {
		return fieldMap.getFieldCount();
	}

	@Override
	public List<Field<T>> getFields() {
		return fieldMap.getFields();
	}

	@Override
	public Class<T> typeClass() {
		return typeClass;
	}

	@Override
	public String messageName() {
		return typeClass.getSimpleName();
	}

	@Override
	public String messageFullName() {
		return typeClass.getName();
	}

	@Override
	public String getFieldName(int number) {
		final Field<T> field = getFieldByNumber(number);
		return field == null ? null : field.name;
	}

	@Override
	public int getFieldNumber(String name) {
		final Field<T> field = getFieldByName(name);
		return field == null ? 0 : field.number;
	}


	@Override
	public void mergeFrom(Input input, T message) throws IOException {
		for (int n = input.readFieldNumber(this); n != 0; n = input.readFieldNumber(this)) {
			final Field<T> field = getFieldByNumber(n);
			if (field == null) {
				input.handleUnknownField(n, this);
			} else {
				field.mergeFrom(input, message);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeTo(Output output, T message) throws IOException {
		/*
		 * 为了兼容 Throwable 这种自引用的序列化, 需要在执行流上调用 HierarchyPolymorphicThrowableSchema 的 tryWriteWithoutCause 方法.
		 * 所以特此在这里进行执行流判断, 去执行调整过的 tryWriteWithoutCause 方法.
		 * 当然, 这样做会降低序列化过程中的性能, 可以优化的方式是在创建 Schema 的时候记录 Throwable 是否存在, 若不存在这里可以直接跳过.
		 */
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (stackTrace != null && stackTrace.length > 2) {
			StackTraceElement ste = stackTrace[2];
			if ("io.protostuff.runtime.PolymorphicThrowableSchema".equals(ste.getClassName())
					&& "writeObjectTo".equals(ste.getMethodName())) {
				if (HierarchyPolymorphicThrowableSchema.tryWriteWithoutCause(output, message, (Schema<Object>) this)) {
					return;
				}
			}
		}
		for (Field<T> f : getFields())
			f.writeTo(output, message);
	}


	@Override
	public boolean isInitialized(T message) {
		return true;
	}

	@Override
	public T newMessage() {
		return instantiator.newInstance();
	}


	/**
	 * Invoked only when applications are having pipe io operations.
	 */
	@SuppressWarnings("unchecked")
	static <T> Pipe.Schema<T> resolvePipeSchema(Schema<T> schema, Class<? super T> clazz, boolean throwIfNone) {
		if (Message.class.isAssignableFrom(clazz)) {
			try {
				// use the pipe schema of code-generated messages if available.
				java.lang.reflect.Method m = clazz.getDeclaredMethod("getPipeSchema");
				return (Pipe.Schema<T>) m.invoke(null);
			} catch (Exception e) {
				// ignore
			}
		}

		if (HierarchyRuntimeSchema.class.isAssignableFrom(schema.getClass()))
			return ((HierarchyRuntimeSchema<T>) schema).getPipeSchema();

		if (RuntimeSchema.class.isAssignableFrom(schema.getClass()))
			return ((RuntimeSchema<T>) schema).getPipeSchema();

		if (throwIfNone)
			throw new RuntimeException("No pipe schema for: " + clazz);

		return null;
	}
}
