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

import static io.protostuff.runtime.HierarchySerializableObjects.hierarchyBar;
import static io.protostuff.runtime.HierarchySerializableObjects.hierarchyBaz;
import static io.protostuff.runtime.HierarchySerializableObjects.hierarchyFoo;
import static io.protostuff.runtime.HierarchySerializableObjects.negativeBar;
import static io.protostuff.runtime.HierarchySerializableObjects.negativeBaz;

import java.util.LinkedHashSet;

import junit.framework.TestCase;
import io.protostuff.ComputedSizeOutput;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.HierarchyBar.Status;

/**
 * Serialization and deserialization test cases.
 * 
 * @author David Yu
 * @created Nov 18, 2009
 */
public class HierarchySerDeserTest extends TestCase
{

    static final int BUF_SIZE = 256;

    public static LinkedBuffer buf()
    {
        return LinkedBuffer.allocate(BUF_SIZE);
    }

    public <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return ProtostuffIOUtil.toByteArray(message, schema, buf());
    }

    public void testFoo() throws Exception
    {
        Schema<HierarchyFoo> schema = HierarchyRuntimeSchema.getSchema(HierarchyFoo.class);

        HierarchyFoo fooCompare = hierarchyFoo;
        HierarchyFoo dfoo = new HierarchyFoo();

        byte[] deferred = toByteArray(fooCompare, schema);

        // ComputedSizeOutput is format compatible with protobuf
        // E.g collections are not serialized ... only its members/elements are.
        if (!RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS)
            assertTrue(deferred.length == ComputedSizeOutput.getSize(
                    fooCompare, schema));

        ProtostuffIOUtil.mergeFrom(deferred, dfoo, schema);
        HierarchySerializableObjects.assertEquals(fooCompare, dfoo);
    }

    public void testBar() throws Exception
    {
        Schema<HierarchyBar> schema = HierarchyRuntimeSchema.getSchema(HierarchyBar.class);

        for (HierarchyBar barCompare : new HierarchyBar[] { hierarchyBar, negativeBar })
        {
            HierarchyBar dbar = new HierarchyBar();

            int expectedSize = ComputedSizeOutput.getSize(barCompare, schema);

            byte[] deferred = toByteArray(barCompare, schema);
            assertTrue(deferred.length == expectedSize);
            ProtostuffIOUtil.mergeFrom(deferred, dbar, schema);
            HierarchySerializableObjects.assertEquals(barCompare, dbar);
            // System.err.println(dbar.getSomeInt());
            // System.err.println(dbar.getSomeLong());
            // System.err.println(dbar.getSomeFloat());
            // System.err.println(dbar.getSomeDouble());
            // System.err.println(dbar.getSomeBytes());
            // System.err.println(dbar.getSomeString());
            // System.err.println(dbar.getSomeEnum());
            // System.err.println(dbar.getSomeBoolean());
        }
    }

    public void testBaz() throws Exception
    {
        Schema<HierarchyBaz> schema = HierarchyRuntimeSchema.getSchema(HierarchyBaz.class);

        for (HierarchyBaz bazCompare : new HierarchyBaz[] { hierarchyBaz, negativeBaz })
        {
            HierarchyBaz dbaz = new HierarchyBaz();

            int expectedSize = ComputedSizeOutput.getSize(bazCompare, schema);

            byte[] deferred = toByteArray(bazCompare, schema);
            assertTrue(deferred.length == expectedSize);
            ProtostuffIOUtil.mergeFrom(deferred, dbaz, schema);
            HierarchySerializableObjects.assertEquals(bazCompare, dbaz);
            // System.err.println(dbaz.getId());
            // System.err.println(dbaz.getName());
            // System.err.println(dbaz.getTimestamp());
        }
    }

    /**
     * HasHasBar wraps an object without a schema. That object will have to be serialized via the default java
     * serialization and it will be delimited.
     * <p>
     * HasBar wraps a message {@link HierarchyBar}.
     */
    public void testJavaSerializable() throws Exception
    {
        Schema<HierarchyHasHasBar> schema = HierarchyRuntimeSchema.getSchema(HierarchyHasHasBar.class);

        HierarchyHasHasBar hhbCompare = new HierarchyHasHasBar("hhb", new HierarchyHasBar(12345, "hb",
                HierarchySerializableObjects.hierarchyBar));
        HierarchyHasHasBar dhhb = new HierarchyHasHasBar();

        int expectedSize = ComputedSizeOutput.getSize(hhbCompare, schema);

        byte[] deferred = toByteArray(hhbCompare, schema);
        assertTrue(deferred.length == expectedSize);
        ProtostuffIOUtil.mergeFrom(deferred, dhhb, schema);
        assertEquals(hhbCompare, dhhb);
    }

    static HierarchyPojoWithArrayAndSet filledPojoWithArrayAndSet()
    {
        LinkedHashSet<Status> someEnumAsSet = new LinkedHashSet<Status>();
        someEnumAsSet.add(Status.PENDING);
        someEnumAsSet.add(Status.STARTED);
        someEnumAsSet.add(Status.COMPLETED);

        LinkedHashSet<HierarchyBar> someBarAsSet = new LinkedHashSet<HierarchyBar>();
        someBarAsSet.add(hierarchyBar);
        someBarAsSet.add(negativeBar);

        LinkedHashSet<Float> someFloatAsSet = new LinkedHashSet<Float>();
        someFloatAsSet.add(123.321f);
        someFloatAsSet.add(-456.654f);

        return new HierarchyPojoWithArrayAndSet(someEnumAsSet,
                someEnumAsSet.toArray(new Status[someEnumAsSet.size()]),
                someBarAsSet,
                someBarAsSet.toArray(new HierarchyBar[someBarAsSet.size()]),
                someFloatAsSet, someFloatAsSet.toArray(new Float[someFloatAsSet
                        .size()]), new Double[] { 112233.332211d,
                        445566.665544d }, new double[] { -112233.332211d,
                        -445566.665544d });
    }

    public void testPojoWithArrayAndSet() throws Exception
    {
        HierarchyPojoWithArrayAndSet pojoCompare = filledPojoWithArrayAndSet();

        Schema<HierarchyPojoWithArrayAndSet> schema = HierarchyRuntimeSchema
                .getSchema(HierarchyPojoWithArrayAndSet.class);

        HierarchyPojoWithArrayAndSet dpojo = new HierarchyPojoWithArrayAndSet();

        int expectedSize = ComputedSizeOutput
                .getSize(pojoCompare, schema, true);

        byte[] deferred = toByteArray(pojoCompare, schema);
        assertTrue(deferred.length == expectedSize);
        ProtostuffIOUtil.mergeFrom(deferred, dpojo, schema);
        assertEquals(pojoCompare, dpojo);
        // System.err.println(dpojo.getSomeEnumAsSet());
        // System.err.println(dpojo.getSomeFloatAsSet());
    }

    static void assertEquals(HierarchyHasHasBar h1, HierarchyHasHasBar h2)
    {
        // true if both are null
        if (h1 == h2)
            return;

        assertEquals(h1.getName(), h2.getName());
        assertEquals(h1.getHasBar(), h2.getHasBar());
    }

    static void assertEquals(HierarchyHasBar h1, HierarchyHasBar h2)
    {
        // true if both are null
        if (h1 == h2)
            return;

        assertTrue(h1.getId() == h2.getId());
        assertEquals(h1.getName(), h2.getName());
        HierarchySerializableObjects.assertEquals(h1.getBar(), h2.getBar());
    }

}
