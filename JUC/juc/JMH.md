# JMH Java准测试工具套件

## JMH

JMH Java Microbenchmark Harness 微基准测试一套工具API

JMH is a Java harness for building, running, and analysing nano/micro/milli/macro benchmarks written in Java and other languages targetting the JVM

JMH不止能对Java语言做基准测试，还能对运行在JVM上的其他语言做基准测试。

### 官网

`http://openjdk.java.net/projects/code-tools/jmh/`

## 创建JMH测试

1. 创建Maven项目，添加依赖

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
   
       <properties>
           <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
           <encoding>UTF-8</encoding>
           <java.version>1.8</java.version>
           <maven.compiler.source>1.8</maven.compiler.source>
           <maven.compiler.target>1.8</maven.compiler.target>
       </properties>
   
       <groupId>mashibing.com</groupId>
       <artifactId>HelloJMH2</artifactId>
       <version>1.0-SNAPSHOT</version>
   
   
       <dependencies>
           <!-- https://mvnrepository.com/artifact/org.openjdk.jmh/jmh-core -->
           <dependency>
               <groupId>org.openjdk.jmh</groupId>
               <artifactId>jmh-core</artifactId>
               <version>1.21</version>
           </dependency>
   
           <!-- https://mvnrepository.com/artifact/org.openjdk.jmh/jmh-generator-annprocess -->
           <dependency>
               <groupId>org.openjdk.jmh</groupId>
               <artifactId>jmh-generator-annprocess</artifactId>
               <version>1.21</version>
               <scope>test</scope>
           </dependency>
       </dependencies>
   </project>
   ```

2. idea安装JMH插件 JMH plugin v1.0.3

3. 由于用到了注解，打开idea运行程序注解配置

   > compiler -> Annotation Processors -> Enable Annotation Processing

4. 定义需要测试类PS (ParallelStream并行流线程池)

   ```java
   package tk.deriwotua.jmh;
   
   import java.util.ArrayList;
   import java.util.List;
   import java.util.Random;
   
   /**
    * 这里并不是做对比测试
    * 而是测试相关方法的吞吐量
    */
   public class PS {
   
       static List<Integer> nums = new ArrayList<>();
       static {
           Random r = new Random();
           for (int i = 0; i < 10000; i++) nums.add(1000000 + r.nextInt(1000000));
       }
   
       /**
        * lambda表达式方式判断是否是质数
        */
       static void foreach() {
           nums.forEach(v->isPrime(v));
       }
   
       /**
        * 并行处理流判断是否是质数
        */
       static void parallel() {
           nums.parallelStream().forEach(PS::isPrime);
       }
   
       static boolean isPrime(int num) {
           for(int i=2; i<=num/2; i++) {
               if(num % i == 0) return false;
           }
           return true;
       }
   }
   ```

5. 写单元测试

   > 这个测试类一定要在test package下面

   ```java
   package tk.deriwotua.jmh;
   
   import org.openjdk.jmh.annotations.Benchmark;
   
   public class PSTest {
       @Benchmark
       public void testForEach() {
           PS.foreach();
       }
   }
   ```

6. 运行测试类，如果遇到下面的错误：

   ```text
   ERROR: org.openjdk.jmh.runner.RunnerException: ERROR: Exception while trying to acquire the JMH lock (C:\WINDOWS\/jmh.lock): C:\WINDOWS\jmh.lock (拒绝访问。), exiting. Use -Djmh.ignoreLock=true to forcefully continue.
   	at org.openjdk.jmh.runner.Runner.run(Runner.java:216)
   	at org.openjdk.jmh.Main.main(Main.java:71)
   ```

这个错误是因为JMH运行需要访问系统目录写入一个文件作为锁但系统目录有权限限制，解决办法是运行时附带上环境变量让JMH写入到TMP目录
  - 打开`RunConfiguration -> Environment Variables -> include system environment viables`

7. 阅读测试报告

运行后会生成测试报告

## JMH中的基本概念

1. Warmup
   预热，由于JVM中对于特定代码会存在优化（本地化），预热对于测试结果很重要
2. Mesurement
   总共执行多少次测试
3. Timeout
   
4. Threads
   线程数，由fork指定
5. Benchmark mode
   基准测试的模式
6. Benchmark
   测试哪一段代码

## Next

官方样例：
http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/

