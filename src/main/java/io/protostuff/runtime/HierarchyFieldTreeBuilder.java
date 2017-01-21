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

import java.util.ArrayList;
import java.util.List;

/**
 * 层级 Field 树形结构构造器. 用于伪造层级关系
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 */
public class HierarchyFieldTreeBuilder {

	/**
	 * Field 树形结构端点结构
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 */
	public static class Tree {
		public java.lang.reflect.Field field;
		public String name;
		public int number;
		public List<Tree> childs = new ArrayList<>();
		public Tree(java.lang.reflect.Field field, String name, int number) {
			this.field = field;
			this.name = name;
			this.number = number;
			this.field.setAccessible(true);
		}

		/**
		 * 判断是否是叶子
		 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
		 * @return {@link boolean}
		 */
		public boolean isLeaves() {
			return childs.size() == 0;
		}

		@Override
		public String toString() {
			return "{" + number + (name == null ? "" : "," + name) + ":" + childs + "}";
		}
	}



	List<Tree> all = new ArrayList<>();

	/**
	 * 根据添加的 ids 深度, 来伪造相应深度的树形结构, 用于后期伪造相应深度的 Schema
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param field
	 * @param name
	 * @param ids
	 */
	public void add(java.lang.reflect.Field field, String name, int... ids) {
		if (ids == null || ids.length == 0) return;
		if (ids.length == 1) {
			all.add(new Tree(field, name, ids[0]));
		} else {
			List<Tree> _all = all;
			for (int i = 0; i < ids.length; i++) {
				boolean found = false;
				int a = 0;
				for (; a < _all.size(); a++) {
					Tree ta = _all.get(a);
					if (ta.number == ids[i]) {
						found = true;
						break;
					}
				}
				if (!found) _all.add(new Tree(field, name, ids[i]));
				_all = _all.get(a).childs;
			}
		}
	}

	public List<Tree> getAll() {
		return all;
	}
}
