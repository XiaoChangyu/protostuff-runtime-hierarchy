//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
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

import io.protostuff.ProtostuffPipeTest;
import io.protostuff.runtime.HierarchyCollectionTest.Employee;
import io.protostuff.runtime.HierarchyCollectionTest.Task;
import io.protostuff.runtime.HierarchyMathObjectsTest.Payment;

/**
 * Test case for pipes generated at runtime.
 * 
 * @author David Yu
 * @created Oct 9, 2010
 */
public class HierarchyRuntimePipeTest extends ProtostuffPipeTest
{

    static <T> HierarchyRuntimeSchema<T> getSchema(Class<T> typeClass)
    {
        return (HierarchyRuntimeSchema<T>) HierarchyRuntimeSchema.getSchema(typeClass);
    }

    @Override
    public void testFoo() throws Exception
    {
    	HierarchyRuntimeSchema<HierarchyFoo> schema = getSchema(HierarchyFoo.class);

        HierarchyFoo hierarchyFoo = HierarchySerializableObjects.hierarchyFoo;

        roundTrip(hierarchyFoo, schema, schema.getPipeSchema());
    }

    @Override
    public void testBar() throws Exception
    {
    	HierarchyRuntimeSchema<HierarchyBar> schema = getSchema(HierarchyBar.class);

        HierarchyBar hierarchyBar = HierarchySerializableObjects.hierarchyBar;

        roundTrip(hierarchyBar, schema, schema.getPipeSchema());
    }

    @Override
    public void testBaz() throws Exception
    {
    	HierarchyRuntimeSchema<HierarchyBaz> schema = getSchema(HierarchyBaz.class);

        HierarchyBaz hierarchyBaz = HierarchySerializableObjects.hierarchyBaz;

        roundTrip(hierarchyBaz, schema, schema.getPipeSchema());
    }

    public void testEmployee() throws Exception
    {
    	HierarchyRuntimeSchema<Employee> schema = getSchema(Employee.class);

        Employee emp = HierarchyCollectionTest.filledEmployee();

        roundTrip(emp, schema, schema.getPipeSchema());
    }

    public void testTask() throws Exception
    {
    	HierarchyRuntimeSchema<Task> schema = getSchema(Task.class);

        Task task = HierarchyCollectionTest.filledTask();

        roundTrip(task, schema, schema.getPipeSchema());
    }

    public void testPayment() throws Exception
    {
    	HierarchyRuntimeSchema<Payment> schema = getSchema(Payment.class);

        Payment payment = HierarchyMathObjectsTest.filledPayment();

        roundTrip(payment, schema, schema.getPipeSchema());
    }

    public void testPojoWithArrayAndSet() throws Exception
    {
    	HierarchyRuntimeSchema<HierarchyPojoWithArrayAndSet> schema = getSchema(HierarchyPojoWithArrayAndSet.class);

        HierarchyPojoWithArrayAndSet p = HierarchySerDeserTest.filledPojoWithArrayAndSet();

        roundTrip(p, schema, schema.getPipeSchema());
    }

}
