package io.protostuff.runtime;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import io.protostuff.CollectionSchema;
import io.protostuff.CollectionSchema.MessageFactory;
import io.protostuff.Input;
import io.protostuff.MapSchema;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy.Lazy;
import io.protostuff.runtime.DefaultIdStrategy.LazyRegister;
import io.protostuff.runtime.DefaultIdStrategy.Mapped;
import io.protostuff.runtime.DefaultIdStrategy.Registered;
import io.protostuff.runtime.HierarchyFieldTreeBuilder.Tree;
import io.protostuff.runtime.RuntimeEnv.Instantiator;

/**
 * 为了做到和 {@link RuntimeSchema} 一样的兼容度, 需要修改 {@link DefaultIdStrategy} 中的部分方法和内部类. 但是因为 final 标记...
 * 所以只好使用部分代理的方式进行更改处理.
 * 另外, 很关键的必须对 {@link DefaultIdStrategy.Lazy} 做更改. 所以直接改造为 {@link HierarchyDefaultIdStrategy.HierarchyLazy}.
 * <p>
 * Translate comment TODO
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
public class HierarchyDefaultIdStrategy extends IdStrategy {

	final ConcurrentHashMap<String, HasSchema<?>> pojoMapping = new ConcurrentHashMap<String, HasSchema<?>>();

	private IdStrategy idStrategy;

	protected HierarchyDefaultIdStrategy(IdStrategy idStrategy) {
		super(DEFAULT_FLAGS, idStrategy.primaryGroup, idStrategy.groupId);
		this.idStrategy = idStrategy;
	}

	@Override
	protected <T> Schema<T> newSchema(Class<T> typeClass) {
		if (idStrategy.primaryGroup == null)
			return HierarchyRuntimeSchema.createFrom(typeClass, this);
		final Schema<T> s = idStrategy.newSchema(typeClass);
		// only pojos created by Hierarchy runtime schema support groups
		if (!(s instanceof HierarchyRuntimeSchema))
			return s;

		final HierarchyRuntimeSchema<T> hrs = (HierarchyRuntimeSchema<T>) s;
		// check if we need to filter
		if (hrs.getFieldCount() == 0)
			return hrs;

		final ArrayList<Field<T>> fields = new ArrayList<Field<T>>(hrs.getFieldCount());

		for (Field<T> f : hrs.getFields()) {
			final int groupFilter = f.groupFilter;
			if (groupFilter != 0) {
				final int set; // set for exclusion
				if (groupFilter > 0) {
					// inclusion
					set = ~groupFilter & 0x7FFFFFFF;
				} else {
					// exclusion
					set = -groupFilter;
				}

				if (0 != (groupId & set)) {
					// this field is excluded on the current group id
					continue;
				}
			}

			fields.add(f);
		}

		// The behavior has been changed to always allow messages with zero fields
		// regardless if it has a primary group or not.
		/*
		 * if (fields.size() == 0) { throw new RuntimeException("All fields were excluded for " + rs.messageFullName() + " on group " + groupId); }
		 */

		return fields.size() == hrs.getFieldCount() ? hrs : new HierarchyRuntimeSchema<T>(typeClass, fields, hrs.instantiator);
	}



	/**
	 * Registers a pojo. Returns true if registration is successful or if the same exact schema was previously registered. Used by
	 * {@link RuntimeSchema#register(Class, Schema)}.
	 */
	public <T> boolean registerPojo(Class<T> typeClass, Schema<T> schema) {
		assert typeClass != null && schema != null;

		final HasSchema<?> last = pojoMapping.putIfAbsent(typeClass.getName(), new Registered<T>(schema, this));

		return last == null || (last instanceof Registered<?> && ((Registered<?>) last).schema == schema);
	}

	/**
	 * Registers a pojo. Returns true if registration is successful or if the same exact schema was previously registered.
	 */
	public <T> boolean registerPojo(Class<T> typeClass) {
		assert typeClass != null;

		final HasSchema<?> last = pojoMapping.putIfAbsent(typeClass.getName(), new LazyRegister<T>(typeClass, this));

		return last == null || (last instanceof LazyRegister);
	}

	/**
	 * Registers an enum. Returns true if registration is successful.
	 */
	public <T extends Enum<T>> boolean registerEnum(Class<T> enumClass) {
		if (idStrategy instanceof DefaultIdStrategy)
			return ((DefaultIdStrategy) idStrategy).registerEnum(enumClass);

		throw new RuntimeException("HierarchyDefaultIdStrategy.registerEnum is only supported on DefaultIdStrategy");
	}

	/**
	 * Registers a delegate. Returns true if registration is successful.
	 */
	public <T> boolean registerDelegate(Delegate<T> delegate) {
		if (idStrategy instanceof DefaultIdStrategy)
			return ((DefaultIdStrategy) idStrategy).registerDelegate(delegate);

		throw new RuntimeException("HierarchyDefaultIdStrategy.registerDelegate is only supported on DefaultIdStrategy");
	}

	/**
	 * Registers a collection. Returns true if registration is successful.
	 */
	public boolean registerCollection(CollectionSchema.MessageFactory factory) {
		if (idStrategy instanceof DefaultIdStrategy)
			return ((DefaultIdStrategy) idStrategy).registerCollection(factory);

		throw new RuntimeException("HierarchyDefaultIdStrategy.registerCollection is only supported on DefaultIdStrategy");
	}

	/**
	 * Registers a map. Returns true if registration is successful.
	 */
	public boolean registerMap(MapSchema.MessageFactory factory) {
		if (idStrategy instanceof DefaultIdStrategy)
			return ((DefaultIdStrategy) idStrategy).registerMap(factory);

		throw new RuntimeException("HierarchyDefaultIdStrategy.registerMap is only supported on DefaultIdStrategy");
	}

	/**
	 * Used by {@link RuntimeSchema#map(Class, Class)}.
	 */
	public <T> boolean map(Class<? super T> baseClass, Class<T> typeClass) {
		assert baseClass != null && typeClass != null;

		if (typeClass.isInterface() || Modifier.isAbstract(typeClass.getModifiers())) {
			throw new IllegalArgumentException(typeClass + " cannot be an interface/abstract class.");
		}

		final HasSchema<?> last = pojoMapping.putIfAbsent(baseClass.getName(), new Mapped<T>(baseClass, typeClass, this));

		return last == null || (last instanceof Mapped<?> && ((Mapped<?>) last).typeClass == typeClass);
	}



	@Override
	public boolean isDelegateRegistered(Class<?> typeClass) {
		return idStrategy.isDelegateRegistered(typeClass);
	}

	@Override
	public <T> HasDelegate<T> getDelegateWrapper(Class<? super T> typeClass) {
		return idStrategy.getDelegateWrapper(typeClass);
	}

	@Override
	public <T> Delegate<T> getDelegate(Class<? super T> typeClass) {
		return idStrategy.getDelegate(typeClass);
	}

	@Override
	public boolean isRegistered(Class<?> typeClass) {
		final HasSchema<?> last = pojoMapping.get(typeClass.getName());
		return last != null && !(last instanceof Lazy<?> || last instanceof HierarchyLazy<?>);
	}

	@SuppressWarnings("unchecked")
	private <T> HasSchema<T> getSchemaWrapper(String className, boolean load) {
		HasSchema<T> hs = (HasSchema<T>) pojoMapping.get(className);
		if (hs == null) {
			if (!load)
				return null;

			final Class<T> typeClass = RuntimeEnv.loadClass(className);

			hs = new HierarchyLazy<T>(typeClass, this);
			final HasSchema<T> last = (HasSchema<T>) pojoMapping.putIfAbsent(typeClass.getName(), hs);
			if (last != null)
				hs = last;
		}

		return hs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> HasSchema<T> getSchemaWrapper(Class<T> typeClass, boolean create) {
		HasSchema<T> hs = (HasSchema<T>) pojoMapping.get(typeClass.getName());
		if (hs == null && create) {
			hs = new HierarchyLazy<T>(typeClass, this);
			final HasSchema<T> last = (HasSchema<T>) pojoMapping.putIfAbsent(typeClass.getName(), hs);
			if (last != null)
				hs = last;
		}

		return hs;
	}

	@Override
	protected EnumIO<? extends Enum<?>> getEnumIO(Class<?> enumClass) {
		return idStrategy.getEnumIO(enumClass);
	}

	@Override
	protected MessageFactory getCollectionFactory(Class<?> clazz) {
		return idStrategy.getCollectionFactory(clazz);
	}

	@Override
	protected io.protostuff.MapSchema.MessageFactory getMapFactory(Class<?> clazz) {
		return idStrategy.getMapFactory(clazz);
	}

	@Override
	protected void writeCollectionIdTo(Output output, int fieldNumber, Class<?> clazz) throws IOException {
		idStrategy.writeCollectionIdTo(output, fieldNumber, clazz);
	}

	@Override
	protected void transferCollectionId(Input input, Output output, int fieldNumber) throws IOException {
		idStrategy.transferCollectionId(input, output, fieldNumber);
	}

	@Override
	protected MessageFactory resolveCollectionFrom(Input input) throws IOException {
		return idStrategy.resolveCollectionFrom(input);
	}

	@Override
	protected void writeMapIdTo(Output output, int fieldNumber, Class<?> clazz) throws IOException {
		idStrategy.writeMapIdTo(output, fieldNumber, clazz);
	}

	@Override
	protected void transferMapId(Input input, Output output, int fieldNumber) throws IOException {
		idStrategy.transferMapId(input, output, fieldNumber);
	}

	@Override
	protected io.protostuff.MapSchema.MessageFactory resolveMapFrom(Input input) throws IOException {
		return idStrategy.resolveMapFrom(input);
	}

	@Override
	protected void writeEnumIdTo(Output output, int fieldNumber, Class<?> clazz) throws IOException {
		idStrategy.writeEnumIdTo(output, fieldNumber, clazz);
	}

	@Override
	protected void transferEnumId(Input input, Output output, int fieldNumber) throws IOException {
		idStrategy.transferEnumId(input, output, fieldNumber);
	}

	@Override
	protected EnumIO<?> resolveEnumFrom(Input input) throws IOException {
		return idStrategy.resolveEnumFrom(input);
	}

	@Override
	protected <T> HasSchema<T> tryWritePojoIdTo(Output output, int fieldNumber, Class<T> clazz, boolean registered) throws IOException {
		HasSchema<T> hs = getSchemaWrapper(clazz, false);
		if (hs == null || (registered && hs instanceof Lazy<?>))
			return null;

		output.writeString(fieldNumber, clazz.getName(), false);

		return hs;
	}

	@Override
	protected <T> HasSchema<T> writePojoIdTo(Output output, int fieldNumber, Class<T> clazz) throws IOException {
		output.writeString(fieldNumber, clazz.getName(), false);

		// it is important to return the schema initialized (if it hasn't been).
		return getSchemaWrapper(clazz, true);
	}

	@Override
	protected <T> HasSchema<T> transferPojoId(Input input, Output output, int fieldNumber) throws IOException {
		final String className = input.readString();

		final HasSchema<T> wrapper = getSchemaWrapper(className, 0 != (AUTO_LOAD_POLYMORPHIC_CLASSES & flags));
		if (wrapper == null) {
			throw new ProtostuffException("polymorphic pojo not registered: " + className);
		}

		output.writeString(fieldNumber, className, false);

		return wrapper;
	}

	@Override
	protected <T> HasSchema<T> resolvePojoFrom(Input input, int fieldNumber) throws IOException {
		final String className = input.readString();

		final HasSchema<T> wrapper = getSchemaWrapper(className, 0 != (AUTO_LOAD_POLYMORPHIC_CLASSES & flags));
		if (wrapper == null)
			throw new ProtostuffException("polymorphic pojo not registered: " + className);

		return wrapper;
	}

	@Override
	protected <T> Schema<T> writeMessageIdTo(Output output, int fieldNumber, Message<T> message) throws IOException {
		return idStrategy.writeMessageIdTo(output, fieldNumber, message);
	}

	@Override
	protected <T> HasDelegate<T> tryWriteDelegateIdTo(Output output, int fieldNumber, Class<T> clazz) throws IOException {
		return idStrategy.tryWriteDelegateIdTo(output, fieldNumber, clazz);
	}

	@Override
	protected <T> HasDelegate<T> transferDelegateId(Input input, Output output, int fieldNumber) throws IOException {
		return idStrategy.transferDelegateId(input, output, fieldNumber);
	}

	@Override
	protected <T> HasDelegate<T> resolveDelegateFrom(Input input) throws IOException {
		return idStrategy.resolveDelegateFrom(input);
	}

	@Override
	protected void writeArrayIdTo(Output output, Class<?> componentType) throws IOException {
		idStrategy.writeArrayIdTo(output, componentType);
	}

	@Override
	protected void transferArrayId(Input input, Output output, int fieldNumber, boolean mapped) throws IOException {
		idStrategy.transferArrayId(input, output, fieldNumber, mapped);
	}

	@Override
	protected Class<?> resolveArrayComponentTypeFrom(Input input, boolean mapped) throws IOException {
		return idStrategy.resolveArrayComponentTypeFrom(input, mapped);
	}

	@Override
	protected void writeClassIdTo(Output output, Class<?> componentType, boolean array) throws IOException {
		idStrategy.writeClassIdTo(output, componentType, array);
	}

	@Override
	protected void transferClassId(Input input, Output output, int fieldNumber, boolean mapped, boolean array) throws IOException {
		idStrategy.transferClassId(input, output, fieldNumber, mapped, array);
	}

	@Override
	protected Class<?> resolveClassFrom(Input input, boolean mapped, boolean array) throws IOException {
		return idStrategy.resolveClassFrom(input, mapped, array);
	}


	static final class HierarchyLazy<T> extends HasSchema<T> {
		final Class<T> typeClass;
		private volatile Schema<T> schema;
		private volatile Pipe.Schema<T> pipeSchema;
		private Instantiator<T> instantiator;
		private Tree tree;
		boolean useHierarchy = false;

		HierarchyLazy(Class<T> outerClass, Instantiator<T> instantiator, Tree tree, IdStrategy strategy) {
			super(strategy);
			this.typeClass = outerClass;
			this.instantiator = instantiator;
			this.tree = tree;
		}

		public HierarchyLazy(Class<T> typeClass, IdStrategy strategy) {
			super(strategy);
			this.typeClass = typeClass;
			this.useHierarchy = true;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Schema<T> getSchema() {
			Schema<T> schema = this.schema;
			if (schema == null) {
				synchronized (this) {
					if ((schema = this.schema) == null) {
						if (Message.class.isAssignableFrom(typeClass)) {
							// use the message's schema.
							Message<T> m = (Message<T>) createMessageInstance(typeClass);
							this.schema = schema = m.cachedSchema();
						} else if (useHierarchy) {
							this.schema = schema = HierarchyRuntimeSchema.createFrom(typeClass, strategy);
						} else if ((tree == null || instantiator == null)) {
							this.schema = schema = strategy.newSchema(typeClass);
						} else {
							this.schema = schema = new HierarchyRuntimeSchema<T>(typeClass, instantiator, tree, strategy);
						}
					}
				}
			}

			return schema;
		}

		@Override
		public Pipe.Schema<T> getPipeSchema() {
			Pipe.Schema<T> pipeSchema = this.pipeSchema;
			if (pipeSchema == null) {
				synchronized (this) {
					if ((pipeSchema = this.pipeSchema) == null) {
						this.pipeSchema = pipeSchema = HierarchyRuntimeSchema.resolvePipeSchema(getSchema(), typeClass, true);
					}
				}
			}
			return pipeSchema;
		}
	}
}
