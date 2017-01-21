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

import java.io.Serializable;

/**
 * Ser/deser test object that wraps a message {@link HierarchyBar}.
 * 
 * @author David Yu
 * @created Nov 12, 2009
 */
public class HierarchyHasBar implements Serializable
{

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private HierarchyBar hierarchyBar;

    public HierarchyHasBar()
    {

    }

    public HierarchyHasBar(int id, String name, HierarchyBar hierarchyBar)
    {
        this.id = id;
        this.name = name;
        this.hierarchyBar = hierarchyBar;
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the bar
     */
    public HierarchyBar getBar()
    {
        return hierarchyBar;
    }

    /**
     * @param hierarchyBar
     *            the bar to set
     */
    public void setBar(HierarchyBar hierarchyBar)
    {
        this.hierarchyBar = hierarchyBar;
    }

}
