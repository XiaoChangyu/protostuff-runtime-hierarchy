protostuff-runtime-hierarchy
============================


**This is a tool that coding on Protostuff-runtime to extend serialize mode.**

**这是一个在 Protostuff-runtime 的基础上扩展序列化方式的工具.**

**扩展的序列化方式是:**

在兼容原 Protostuff-runtime 的基础上, 可以将多个维度层级的 Bean 嵌套关系的序列化字节数组反序列化成低维度的 Bean 嵌套关系. 同时, 可以反向序列化还原.

**Let's look an example:**

**The tool's ability looks like make Bean_A serialize to Protostuff byte array, then the byte array can deserialize to Bean_B:**

**这个工具的能力看起来像是能把 Bean_A 序列化为 Protostuff 字节数组, 然后这个字节数组还能反序列化成 Bean_B:**

Bean_A
* f_a_100
* f_a_200
* f_obj_300
	* f_a_310
	* f_obj_320
		* f_a_321
		* f_a_322
		* f_a_323
* f_obj_400
	* f_a_410
	* f_a_420
* f_a_500

**(Bean_A serialize to Protostuff byte array, then the byte array deserialize to Bean_B)**

Bean_B
* f_a_100
* f_a_200
* f_a_310
* f_a_321
* f_a_321
* f_a_322
* f_a_410
* f_a_420
* f_a_500

**Absolutely, It can serialize Bean_B to Protostuff byte array (actually, this byte array just is Bean_A's Protostuff byte array), then deserialize it to Bean_A**

**当然, 它也可以序列化 Bean_B 到 Protostuff 字节数组(事实上, 这个字节数组其实就是 Bean_A 的 Protostuff 字节数组), 然后再反序列化到 Bean_A**

Bean_A
* f_a_100
* f_a_200
* f_obj_300
	* f_a_310
	* f_obj_320
		* f_a_321
		* f_a_322
		* f_a_323
* f_obj_400
	* f_a_410
	* f_a_420
* f_a_500

**Of course, we can serialize Bean_A to Protostuff byte array, then deserialize it back to Bean_A**

We can do
---------
**I called protostuff-runtime-hierarchy PRH in the below.**
* **1. If you use normal Protostuff to serialize Bean_A or Bean_B, you can get normal Protostuff byte array of Bean_A or Bean_B.**
* **2. Protostuff-Runtime support what, PRH can support it too. Example reduce field.**
* **3. PRH can serialize Bean_B to Protostuff byte array with Bean_A's style. Original Protostuff can not support it.**
* **4. PRH used custom tag to mark field, just look like original Protostuff tag, and you can design custom tag with yourself style.**
* **5. PRH didn't design new serialize byte style, it just change serialize mode. so you can mix PRH and Original Protostuff.**

**Please read the test code in the src/test/java/p/ka/test/protostuff/hierarchy.**

**The code below of src/test/java/io/protostuff/runtime just for test compatibility with Protostuff. And the compatibility is good.**

**在下面, 我把 protostuff-runtime-hierarchy 称之为 PRH.**
* **1. 如果你使用普通的 Protostuff 去序列化 Bean_A 或 Bean_B, 你能得到普通的 Bean_A 或 Bean_B 的 Protostuff 字节数组.**
* **2. Protostuff-runtime-hierarchy 支持什么, PRH 也能支持什么. 例如缩减字段.**
* **3. PRH 能够序列化 Bean_B 到 Bean_A 的样式的 Protostuff 字节数组. 原始的 Protostuff 不能支持这个.**
* **4. PRH 使用自定义的 tag 来标记 field, 看起来和原始的 Protostuff tag 很像, 而且你还能定义自己样式的自定义 tag.**
* **5. PRH 并没有定义新的序列化字节样式, 它只是更改了序列化模式. 所以你可以混合 PRH 和原始的 Protostuff.**

**请阅读 src/test/java/p/ka/test/protostuff/hierarchy 下的测试代码.**

**在 src/test/java/io/protostuff/runtime 下的代码只是为了测试和 Protostuff 的兼容性. 当然, 兼容性没有问题.**

Usage (maven)
-------------

1. When you generate schemas for your classes
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-core</artifactId>
    <version>1.5.2</version>
  </dependency>
  ```

2. Runtime schemas
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-runtime</artifactId>
    <version>1.5.2</version>
  </dependency>
  ```
3. Hierarchy Runtime schemas
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId><!-- Could I use this GroupId? -->
    <artifactId>protostuff-runtime-hierarchy</artifactId>
    <version>1.5.2</version><!-- I am write this on version 1.5.2 of protostuff-runtime -->
  </dependency>
  ```

Requirements
------------

Java 1.6 or higher (Because I am write it on version 1.5.2 of protostuff-runtime)

Build Requirements
------------------

Maven 3.2.3 or higher (Because I am write it on version 1.5.2 of protostuff-runtime)


Compatibility Test
------------------

I copied all java test file of the io.protostuff.runtime below src/test/java in protostuff-runtime to protostuff-runtime-hierarchy.
And then changed all file name (Add a prefix Hierarchy),
and then replaced all RuntimeSchema to HierarchyRuntimeSchema in every file,
and test them all,
and they are all right ;-) (include test-map-collection-register.sh(bat) and test-null-array-element.sh(bat)).
So I think there is no compatibility problem.

How To Use
----------

Please look at the demo in src/test/java/p/ka/test/protostuff/hierarchy, you will know how to use it.

In the p.ka.test.protostuff.hierarchy.tools package, I wrote some tools.

In the p.ka.test.protostuff.hierarchy.bean.notag package, I am already wrote some Bean, like Car, Child, Toy, Father.

The BeanBuilder_NoTag use to build Father Object.

The CompatibleTest_NoTag use to run some compatibility test.

Same as package p.ka.test.protostuff.hierarchy.bean.tag, p.ka.test.protostuff.hierarchy.bean.hierarchytag and p.ka.test.protostuff.hierarchy.bean.customtag.

The different is Beans of notag haven't annotation, and Beans of tag are annotate with @Tag, and Beans of hierarchytag are annotate with @HierarchyTag, and Beans of customtag are annotate with @MyTag (@MyTag is a custom Hierarchy Tag define under p.ka.test.protostuff.hierarchy.tools path).

Under the p.ka.test.protostuff.hierarchy.bean.hierarchytag path, I add a mix subpath to test mixed state with @Tag and @HierarchyTag.

The detail in the p.ka.test.protostuff.hierarchy.bean.hierarchytag.mix.HierarchyTest_HierarchyTagMix.java file, the same as p.ka.test.protostuff.hierarchy.bean.customtag.mix.HierarchyTest_CustomTagMix.java.


****
### The below is original Protostuff README.md (以下是 Protostuff 的 README.md)
### The Protostuff github site is https://github.com/protostuff/protostuff

![Protostuff](http://www.protostuff.io/images/protostuff_300x100.png)

A serialization library with built-in support for forward-backward compatibility (schema evolution) and validation.

Documentation:

1. http://www.protostuff.io/

Benchmarks:

1. http://hperadin.github.io/jvm-serializers-report/report.html

Usage (maven)
-------------

1. When you generate schemas for your classes
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-core</artifactId>
    <version>1.5.2</version>
  </dependency>
  ```

2. Runtime schemas
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-runtime</artifactId>
    <version>1.5.2</version>
  </dependency>
  ```

Questions/Concerns/Suggestions
------------------------------

- subscribe to http://groups.google.com/group/protostuff/

Requirements
------------

Java 1.6 or higher

Build Requirements
------------------

Maven 3.2.3 or higher
