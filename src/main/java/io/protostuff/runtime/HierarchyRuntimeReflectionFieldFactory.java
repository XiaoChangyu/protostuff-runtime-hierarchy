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

import static io.protostuff.runtime.RuntimeFieldFactory.ID_POJO;

import java.io.IOException;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Tag;
import io.protostuff.WireFormat.FieldType;
import io.protostuff.runtime.HierarchyFieldTreeBuilder.Tree;
import io.protostuff.runtime.RuntimeEnv.Instantiator;

/**
 * 这个类的内容其实只是想扩展 {@link RuntimeReflectionFieldFactory} 类.
 * 但是由于 {@link RuntimeReflectionFieldFactory} 被标记为 final, 所以......
 * <p>
 * Translate comment TODO
 * ---------<p>
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
public class HierarchyRuntimeReflectionFieldFactory {

	private HierarchyRuntimeReflectionFieldFactory() {}

	static final HierarchyRuntimeFieldFactory<Object> HIERARCHY_POJO = new HierarchyRuntimeFieldFactory<Object>(ID_POJO) {
		@Override
		public <T> Field<T> create(final Class<T> outerClass, final Instantiator<T> instantiator, final Tree tree, final IdStrategy strategy) {
			HasSchema<T> hasSchema = new HierarchyDefaultIdStrategy.HierarchyLazy<>(outerClass, instantiator, tree, strategy);
			return new RuntimeMessageField<T, T>(outerClass, hasSchema, FieldType.MESSAGE, tree.number, tree.name, false, tree.field.getAnnotation(Tag.class)) {
				{
					tree.field.setAccessible(true);
				}

				public void mergeFrom(Input input, T message) throws IOException {
					try {
						input.mergeObject(message, getSchema());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}

				public void writeTo(Output output, T message) throws IOException {
					final T existing;
					try {
						existing = message;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}

					if (existing != null)
						output.writeObject(number, existing, getSchema(), false);
				}

				public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
					output.writeObject(number, pipe, getPipeSchema(), repeated);
				}
			};
		}

		@Override
		public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object readFrom(Input input) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void writeTo(Output output, int number, Object value, boolean repeated) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public FieldType getFieldType() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Class<?> typeClass() {
			throw new UnsupportedOperationException();
		}
	};
}
