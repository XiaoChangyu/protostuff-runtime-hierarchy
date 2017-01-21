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

import io.protostuff.runtime.HierarchyFieldTreeBuilder.Tree;
import io.protostuff.runtime.RuntimeEnv.Instantiator;

/**
 * 该类纯粹是为了满足 {@link HierarchyRuntimeReflectionFieldFactory} 中构造 {@link HierarchyRuntimeReflectionFieldFactory#HIERARCHY_POJO} 的格式和参数传递.
 * 模仿的是 {@link RuntimeFieldFactory} 抽象类的抽象方法. 因为必须传递 {@link Tree} 结构. 所以直接新写了一个抽象方法来使用.
 * <p>
 * Translate comment TODO
 * ----------<p>
 * A factory to create runtime {@link Field fields} based on reflection.
 * 
 * @author David Yu
 * @created Nov 10, 2009
 */
public abstract class HierarchyRuntimeFieldFactory<V> implements Delegate<V> {

	/**
	 * Used by {@link ObjectSchema} to serialize dynamic (polymorphic) fields.
	 */
	final int id;

	public HierarchyRuntimeFieldFactory(int id) {
		this.id = id;
	}

	/**
	 * Creates a runtime {@link Field field} based on reflection.
	 */
	public abstract <T> Field<T> create(final Class<T> outerClass, final Instantiator<T> instantiator, final Tree tree, IdStrategy strategy);

}
