//================================================================================
//Copyright (c) 2012, David Yu
//All rights reserved.
//--------------------------------------------------------------------------------
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// 1. Redistributions of source code must retain the above copyright notice,
//    this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
// 3. Neither the name of protostuff nor the names of its contributors may be used
//    to endorse or promote products derived from this software without
//    specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
//================================================================================

package io.protostuff.runtime;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.protostuff.Tag;

/**
 * Test for runtime schemas to handle annotation-based field mapping.
 * 
 * @author Brice Jaglin
 * @author David Yu
 * @created Mar 30, 2012
 */
public class HierarchyAnnotatedFieldsTest
{

    public static class EntityFullyAnnotated
    {

        @Tag(3)
        int id;

        @Tag(5)
        String name;

        @Tag(2)
        @Deprecated
        String alias;
    }

    public static class EntityPartlyAnnotated1
    {

        @Tag(3)
        int id;

        // Missing annotation
        String name;

        @Tag(2)
        @Deprecated
        String alias;
    }

    public static class EntityPartlyAnnotated2
    {

        // Missing annotation
        int id;

        @Tag(4)
        String name;
    }

    public static class EntityInvalidAnnotated1
    {

        @Tag(-1)
        int id;
    }

    public static class EntityInvalidAnnotated2
    {

        @Tag(2)
        int id;

        @Tag(2)
        int other;
    }

    static class EntityInvalidTagNumber
    {
        @Tag(0)
        int id;
    }

    static class EntityWithFieldAlias
    {
        @Tag(400)
        double field400;

        @Tag(value = 200, alias = "f200")
        int field200;
    }

    @Test
    public void testEntityFullyAnnotated()
    {
    	HierarchyRuntimeSchema<EntityFullyAnnotated> schema = (HierarchyRuntimeSchema<EntityFullyAnnotated>) HierarchyRuntimeSchema
                .getSchema(EntityFullyAnnotated.class, RuntimeEnv.ID_STRATEGY);

        assertTrue(schema.getFieldCount() == 2);
        assertEquals(schema.getFields().get(0).name, "id");
        assertEquals(schema.getFields().get(0).number, 3);

        assertEquals(schema.getFields().get(1).name, "name");
        assertEquals(schema.getFields().get(1).number, 5);

        assertTrue(schema.getFieldNumber("alias") == 0);
        assertNull(schema.getFieldByName("alias"));
    }

    @Test
    public void testEntityPartlyAnnotated1()
    {
        try
        {
            HierarchyRuntimeSchema.getSchema(EntityPartlyAnnotated1.class);
            fail();
        }
        catch (RuntimeException e)
        {
            // expected
        }
    }

    @Test
    public void testEntityPartlyAnnotated2()
    {
        try
        {
            HierarchyRuntimeSchema.getSchema(EntityPartlyAnnotated2.class);
            fail();
        }
        catch (RuntimeException e)
        {
            // expected
        }
    }

    @Test
    public void testEntityInvalidAnnotated1()
    {
        try
        {
            HierarchyRuntimeSchema.getSchema(EntityInvalidAnnotated1.class);
            fail();
        }
        catch (RuntimeException e)
        {
            // expected
        }
    }

    @Test
    public void testEntityInvalidAnnotated2()
    {
        try
        {
            HierarchyRuntimeSchema.getSchema(EntityInvalidAnnotated1.class);
            fail();
        }
        catch (RuntimeException e)
        {
            // expected
        }
    }

    @Test
    public void testEntityInvalidTagNumber() throws Exception
    {
        try
        {
            HierarchyRuntimeSchema.getSchema(EntityInvalidTagNumber.class);
            fail();
        }
        catch (RuntimeException e)
        {
            // expected
        }
    }

    static <T> void verify(HierarchyRuntimeSchema<T> schema, int number, String name,
            int offset)
    {
        assertEquals(schema.getFields().get(offset).name, name);
        assertEquals(schema.getFields().get(offset).number, number);

        assertEquals(name, schema.getFieldName(number));
        assertEquals(number, schema.getFieldNumber(name));
    }

    @Test
    public void testEntityWithFieldAlias()
    {
    	HierarchyRuntimeSchema<EntityWithFieldAlias> schema = (HierarchyRuntimeSchema<EntityWithFieldAlias>) HierarchyRuntimeSchema
                .getSchema(EntityWithFieldAlias.class, RuntimeEnv.ID_STRATEGY);

        assertTrue(schema.getFieldCount() == 2);

        // The field with the smallest field number will be written first.
        // In this case, field200 (despite field400 being declared 1st)
        verify(schema, 200, "f200", 0);
        verify(schema, 400, "field400", 1);
    }

}