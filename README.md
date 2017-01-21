![protostuff-runtime-hierarchy]

一个在 Protostuff-runtime 的基础上扩展序列化方式的工具.
扩展的序列化方式是:
在兼容原 Protostuff-runtime 的基础上, 可以将多个维度层级的 Bean 嵌套关系的序列化字节数组反序列化成低维度的 Bean 嵌套关系. 同时, 可以反向序列化还原.
Usage (maven)

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

Please look at the demo in src/test/java, there has a subpath, p.ka.test.protostuff.hierarchy, to show how to use it.
In the p.ka.test.protostuff.hierarchy.tools path, I wrote some tools.
In the p.ka.test.protostuff.hierarchy.bean.notag path, I am already wrote some Bean, like Car, Child, Toy, Father.
The BeanBuilder_NoTag use to build Father Object.
The CompatibleTest_NoTag use to run some compatibility test.
The same as path p.ka.test.protostuff.hierarchy.bean.tag, p.ka.test.protostuff.hierarchy.bean.hierarchytag and p.ka.test.protostuff.hierarchy.bean.customtag.
The different is Beans of notag haven't annotation, and Beans of tag are annotate with @Tag, and Beans of hierarchytag are annotate with @HierarchyTag, and Beans of customtag are annotate with @MyTag (@MyTag is a custom Hierarchy Tag define under p.ka.test.protostuff.hierarchy.tools path).

Under the p.ka.test.protostuff.hierarchy.bean.hierarchytag path, I add a mix subpath to test mixed state with @Tag and @HierarchyTag.
The detail in the p.ka.test.protostuff.hierarchy.bean.hierarchytag.mix.HierarchyTest_HierarchyTagMix.java file, the same as p.ka.test.protostuff.hierarchy.bean.customtag.mix.HierarchyTest_CustomTagMix.java.



------------------------------- The below is original Protostuff README.md (以下是 Protostuff 的 README.md) -------------------------------

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
