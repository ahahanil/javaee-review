[TOC]


# JVM运行参数、JVM内存模型、dump文件分析、jstack死锁问题、visualjvm使用

了解下我们为什么要学习JVM优化

掌握jvm的运行参数以及参数的设置

掌握jvm的内存模型（堆内存）

掌握jamp命令的使用以及通过MAT工具进行分析

掌握定位分析内存溢出的方法

掌握jstack命令的使用

掌握VisualJVM工具的使用

## 1、为什么要对jvm做优化？
在本地开发环境中我们很少会遇到需要对jvm进行优化的需求，但是到了生产环境，可能将有下面的需求：
- 运行的应用“卡住了”，日志不输出，程序没有反应
- 服务器的CPU负载突然升高
- 在多线程应用下，如何分配线程的数量？
- ……
这里将对jvm有更深入的学习，不仅要让程序能跑起来，而且是可以跑的更快！可以分析解决在生产环境中所遇到的各种“棘手”的问题。
> 说明：使用的jdk版本为1.8。

## 2、jvm的运行参数
在jvm中有很多的参数可以进行设置，这样可以让jvm在各种环境中都能够高效的运行。绝大部分的参数保持默认即可。
### 2.1、三种参数类型
`jvm`的参数类型分为三类
- 标准参数
  - `-help`
  - `-version`
- `-X参数` （非标准参数）
  - `-Xint`
  - `-Xcomp`
- `-XX参数`（非标准参数使用率较高）
  - `-XX:newSize`
  - `-XX:+UseSerialGC`

### 2.2、标准参数
`jvm`的标准参数，一般都是很稳定的，在未来的JVM版本中不会改变，可以使用`java -help`检索出所有的标准参数。
```shell script
[root@node01 ~]# java -help
用法: java [-options] class [args...]
            (执行类)
    或 java [-options] -jar jarfile [args...]
            (执行 jar 文件)
其中选项包括:
    -d32            使用 32 位数据模型 (如果可用)
    -d64            使用 64 位数据模型 (如果可用)
    -server         选择 "server" VM
                    默认 VM 是 server,
                    因为您是在服务器类计算机上运行。
    -cp <目录和 zip/jar 文件的类搜索路径>
    -classpath <目录和 zip/jar 文件的类搜索路径>
                    用 : 分隔的目录, JAR 档案
                    和 ZIP 档案列表, 用于搜索类文件。
    -D<名称>=<值>
                    设置系统属性
    -verbose:[class|gc|jni]
                    启用详细输出
    -version        输出产品版本并退出
    -version:<值>
                    警告: 此功能已过时, 将在
                    未来发行版中删除。
                    需要指定的版本才能运行
    -showversion    输出产品版本并继续
    -jre-restrict-search | -no-jre-restrict-search
                    警告: 此功能已过时, 将在
                    未来发行版中删除。
                    在版本搜索中包括/排除用户专用 JRE
    -? -help        输出此帮助消息
    -X              输出非标准选项的帮助
    -ea[:<packagename>...|:<classname>]
    -enableassertions[:<packagename>...|:<classname>]
                    按指定的粒度启用断言
    -da[:<packagename>...|:<classname>]
    -disableassertions[:<packagename>...|:<classname>]
                    禁用具有指定粒度的断言
    -esa | -enablesystemassertions
                    启用系统断言
    -dsa | -disablesystemassertions
                    禁用系统断言
    -agentlib:<libname>[=<选项>]
                    加载本机代理库 <libname>, 例如 -agentlib:hprof
                    另请参阅 -agentlib:jdwp=help 和 -agentlib:hprof=help
    -agentpath:<pathname>[=<选项>]
                    按完整路径名加载本机代理库
    -javaagent:<jarpath>[=<选项>]
                    加载 Java 编程语言代理, 请参阅 java.lang.instrument
    -splash:<imagepath>
                    使用指定的图像显示启动屏幕
```

#### 2.2.1、实战
> 实战1：查看jvm版本

```shell script
[root@node01 ~]# java -version
java version "1.8.0_141"
Java(TM) SE Runtime Environment (build 1.8.0_141-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.141-b15, mixed mode)

# -showversion参数是表示，先打印版本信息，再执行后面的命令，在调试时非常有用，后面会使用到。
```

> 实战2：通过-D设置系统属性参数

```java
public class TestJVM {
    public static void main(String[] args) {
        /**
         * System.getProperty(); 获取系统参数
         *  在运行的时候可指定参数
         */
        String str = System.getProperty("str");
        if (str == null) {
            System.out.println("deriwotua");
        } else {
            System.out.println(str);
        }
    }
}
```

进行编译、测试：
```shell script
#编译
[root@node01 test]# javac TestJVM.java

#测试
[root@node01 test]# java TestJVM
deriwotua
# 使用-D指定参数
[root@node01 test]# java -Dstr=123 TestJVM
123
```

#### 2.2.2、-server与-client参数
可以通过`-server`或`-client`设置`jvm`的运行参数。
- 它们的区别是`Server VM`的**初始堆空间会大一些，默认使用的是并行垃圾回收器，启动慢运行快**。
- `Client VM`相对来讲会保守一些，初始堆空间会小一些，使用**串行的垃圾回收器**，它的目标是为了让JVM的启动速度更快，但运行速度会比`Serve VM`模式慢些。
- JVM在启动的时候会根据硬件和操作系统自动选择使用Server还是Client类型的JVM。
- 32位操作系统
  - 如果是Windows系统，不论硬件配置如何，都默认使用Client类型的JVM。
  - 如果是其他操作系统上，机器配置有2GB以上的内存同时有2个以上CPU的话默认使用server模式，否则使用client模式。
- 64位操作系统
  - 只有server类型，不支持client类型。

测试：
```shell script
# 指定client模式 
[root@node01 test]# java -client -showversion TestJVM
java version "1.8.0_141"
Java(TM) SE Runtime Environment (build 1.8.0_141-b15)
# 即使指定的是client但是64位系统是不支持client类型这里还是64-Bit Server VM
Java HotSpot(TM) 64-Bit Server VM (build 25.141-b15, mixed mode)

deriwotua

[root@node01 test]# java -server -showversion TestJVM
java version "1.8.0_141"
Java(TM) SE Runtime Environment (build 1.8.0_141-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.141-b15, mixed mode)

deriwotua

#由于机器是64位系统，所以不支持client模式
```

### 2.3、-X参数
`jvm`的`-X`参数是非标准参数，在不同版本的`jvm`中，参数可能会有所不同，可以通过`java -X`查看非标准参数。

```shell script
[root@node01 test]# java -X
    -Xmixed           混合模式执行 (默认)
    -Xint             仅解释模式执行
    -Xbootclasspath:<用 : 分隔的目录和 zip/jar 文件>
                      设置搜索路径以引导类和资源
    -Xbootclasspath/a:<用 : 分隔的目录和 zip/jar 文件>
                      附加在引导类路径末尾
    -Xbootclasspath/p:<用 : 分隔的目录和 zip/jar 文件>
                      置于引导类路径之前
    -Xdiag            显示附加诊断消息
    -Xnoclassgc       禁用类垃圾收集
    -Xincgc           启用增量垃圾收集
    -Xloggc:<file>    将 GC 状态记录在文件中 (带时间戳)
    -Xbatch           禁用后台编译
    -Xms<size>        设置初始 Java 堆大小
    -Xmx<size>        设置最大 Java 堆大小
    -Xss<size>        设置 Java 线程堆栈大小
    -Xprof            输出 cpu 配置文件数据
    -Xfuture          启用最严格的检查, 预期将来的默认值
    -Xrs              减少 Java/VM 对操作系统信号的使用 (请参阅文档)
    -Xcheck:jni       对 JNI 函数执行其他检查
    -Xshare:off       不尝试使用共享类数据
    -Xshare:auto      在可能的情况下使用共享类数据 (默认)
    -Xshare:on        要求使用共享类数据, 否则将失败。
    -XshowSettings    显示所有设置并继续
    -XshowSettings:all
                      显示所有设置并继续
    -XshowSettings:vm 显示所有与 vm 相关的设置并继续
    -XshowSettings:properties
                      显示所有属性设置并继续
    -XshowSettings:locale
                      显示所有与区域设置相关的设置并继续

-X 选项是非标准选项, 如有更改, 恕不另行通知。不同版本是不一样的
```

#### 2.3.1、-Xint、-Xcomp、-Xmixed
在解释模式(interpreted mode)下，`-Xint`标记会强制JVM执行所有的字节码，当然这会降低运行速度，通常低10倍或更多。

`-Xcomp`参数编译模式(compiled mode)与它（`-Xint`）正好相反，JVM在第一次使用时会把所有的字节码编译成本地代码，从而带来最大程度的优化。
- 然而，很多应用在使用`-Xcomp`也会有一些性能损失，当然这比使用`-Xint`损失的少，原因是`-xcomp`没有让JVM启用`JIT`编译器的全部功能。`JIT`编译器可以对是否需要编译做判断(是直接执行字节码还是需要编译成本地代码来执行)，如果所有代码都进行编译的话，对于一些只执行一次的代码就没有意义了。

`-Xmixed`是混合模式(mixed mode)，将解释模式与编译模式进行混合使用，由`jvm`自己决定，这是`jvm`默认的模式，也是推荐使用的模式。

示例：强制设置运行模式
- 命令中带上 `-showversion` 参数打印执行环境后继续以此查看执行的模式
```shell script
# -Xint强制设置为解释模式（interpreted mode） 
# 解释模式编译快执行慢
[root@node01 test]# java -showversion -Xint TestJVM
java version "1.8.0_141"
Java(TM) SE Runtime Environment (build 1.8.0_141-b15)
# interpreted mode 解释模式
Java HotSpot(TM) 64-Bit Server VM (build 25.141-b15, interpreted mode)

deriwotua

# -Xcomp强制设置为编译模式（compiled mode）
# 会把字节码文件代码编译成本地代码执行
# 编译模式编译慢执行快
[root@node01 test]# java -showversion -Xcomp TestJVM
java version "1.8.0_141"
Java(TM) SE Runtime Environment (build 1.8.0_141-b15)
# compiled mode 编译模式
# 执行时会稍稍卡顿一下，卡顿就是在编译字节码为本地代码的过程然后再执行
Java HotSpot(TM) 64-Bit Server VM (build 25.141-b15, compiled mode)

deriwotua
#注意：编译模式下，第一次执行会比解释模式下执行慢一些，注意观察。

# 不指定模式默认的混合模式
[root@node01 test]# java -showversion TestJVM
java version "1.8.0_141"
Java(TM) SE Runtime Environment (build 1.8.0_141-b15)
# mixed mode默认混合模式 自动判断是直接执行字节码文件还是编译成本地代码后执行
Java HotSpot(TM) 64-Bit Server VM (build 25.141-b15, mixed mode)

deriwotua
```

### 2.4、-XX非标准参数
-XX参数也是非标准参数，**主要用于`jvm`的调优和`debug`操作**。

-XX参数的使用有2种方式，一种是boolean类型，一种是非boolean类型
- boolean类型。`+`号就是启用；`-`号就是禁用
  - 格式：`-XX:[+-]<name>` 表示启用或禁用<name>属性
  - 如：`-XX:+DisableExplicitGC` 表示禁用手动调用gc操作，也就是说调用`System.gc()`无效
    ```java
    public class TestJVM {
        public static void main(String[] args) {
            /**
             * System.getProperty(); 获取系统参数
             *  在运行的时候可指定参数
             */
            String str = System.getProperty("str");
            if (str == null) {
                System.out.println("deriwotua");
            } else {
                System.out.println(str);
            }
            /**
             * 设置了-XX:+DisableExplicitGC禁用手动调用gc操作
             *   手动调用gc无效
             */
            System.gc();
        }
    }
    ```
- 非boolean类型
  - 格式：`-XX:<name>=<value>` 表示`<name>`属性的值为`<value>`
  - 如：`-XX:NewRatio=1` 表示新生代和老年代的比值

用法
```shell script
# -XX:+DisableExplicitGC 禁用手动调用gc操作 代码里存在 System.gc()调用无效
[root@node01 test]# java -showversion -XX:+DisableExplicitGC TestJVM
java version "1.8.0_141"
Java(TM) SE Runtime Environment (build 1.8.0_141-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.141-b15, mixed mode)

deriwotua
```

### 2.5、-Xms与-Xmx参数
> 属于 -XX非标准参数

`-Xms`与`-Xmx`分别是**设置`jvm`的堆内存的初始大小和最大大小**。
- `-Xmx2048m`：等价于`-XX:MaxHeapSize`，设置JVM最大堆内存为2048M。
- `-Xms512m`：等价于`-XX:InitialHeapSize`，设置JVM初始堆内存为512M。

适当的调整jvm的内存大小，可以充分利用服务器资源，让程序跑的更快。

示例
```shell script
# 设置JVM堆内存初始大小512m和最大大小2048m
[root@node01 test]# java -Xms512m -Xmx2048m TestJVM

deriwotua
```

### 2.6、查看jvm的运行参数
有些时候需要查看`jvm`的运行参数，这个需求可能会存在2种情况：
- 运行java命令时打印出运行参数；
- 查看正在运行的java进程的参数；

#### 2.6.1、运行java命令时打印参数

运行java命令时打印参数，需要添加`-XX:+PrintFlagsFinal`启用打印参数。
```shell script
# + 代表启用  -XX:+PrintFlagsFinal 启用打印参数
# 打印的参数格式：类型 参数名  <=><:=>参数值    {product}
[root@node01 test]# java -XX:+PrintFlagsFinal -version
[Global flags]
     intx ActiveProcessorCount                      = -1                                  {product}
    uintx AdaptiveSizeDecrementScaleFactor          = 4                                   {product}
    uintx AdaptiveSizeMajorGCDecayTimeScale         = 10                                  {product}
    uintx AdaptiveSizePausePolicy                   = 0                                   {product}
    uintx AdaptiveSizePolicyCollectionCostMargin    = 50                                  {product}
    uintx AdaptiveSizePolicyInitializingSteps       = 20                                  {product}
    uintx AdaptiveSizePolicyOutputInterval          = 0                                   {product}
    uintx AdaptiveSizePolicyWeight                  = 10                                  {product}
    uintx AdaptiveSizeThroughPutPolicy              = 0                                   {product}
    uintx AdaptiveTimeWeight                        = 25                                  {product}
     bool AdjustConcurrency                         = false                               {product}
     bool AggressiveHeap                            = false                               {product}
     bool AggressiveOpts                            = false                               {product}
     intx AliasLevel                                = 3                                   {C2 product}
     bool AlignVector                               = false                               {C2 product}
     intx AllocateInstancePrefetchLines             = 1                                   {product}
     intx AllocatePrefetchDistance                  = 192                                 {product}
#    …………………………略…………………………………………	 
     bool UseParallelGC                            := true                                {product}
     bool UseXmmI2D                                 = false                               {ARCH product}
     bool UseXmmI2F                                 = false                               {ARCH product}
     bool UseXmmLoadAndClearUpper                   = true                                {ARCH product}
     bool UseXmmRegToRegMoveAll                     = true                                {ARCH product}
     bool VMThreadHintNoPreempt                     = false                               {product}
     intx VMThreadPriority                          = -1                                  {product}
     intx VMThreadStackSize                         = 0                                   {pd product}
     intx ValueMapInitialSize                       = 11                                  {C1 product}
     intx ValueMapMaxLoopSize                       = 8                                   {C1 product}
     intx ValueSearchLimit                          = 1000                                {C2 product}
     bool VerifyMergedCPBytecodes                   = true                                {product}
     bool VerifySharedSpaces                        = false                               {product}
     intx WorkAroundNPTLTimedWaitHang               = 1                                   {product}
    uintx YoungGenerationSizeIncrement              = 20                                  {product}
    uintx YoungGenerationSizeSupplement             = 80                                  {product}
    uintx YoungGenerationSizeSupplementDecay        = 8                                   {product}
    uintx YoungPLABSize                             = 4096                                {product}
     bool ZeroTLAB                                  = false                               {product}
     intx hashCode                                  = 5                                   {product}
java version "1.8.0_141"
Java(TM) SE Runtime Environment (build 1.8.0_141-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.141-b15, mixed mode)
```

由上述的信息可以看出，参数有`boolean类型`和`数字类型`，值的操作符是`=`或`:=`
- 值的操作符是`=`代表默认值
- 值的操作符是`:=`被修改的值 即值已经被修改过

验证值的操作符是`:=`时值被修改过
- 查看上面倒数第二个`bool ZeroTLAB = false {product}` 默认值(=)是false 下面改为true

```shell script
# 格式： java -XX:<+-><name> -XX非标准参数 + 启用 -禁用 name 参数名
java -XX:+PrintFlagsFinal -XX:+ZeroTLAB TestJVM

# ...省略若干打印的参数
    uintx YoungGenerationSizeSupplementDecay        = 8                                   {product}
    uintx YoungPLABSize                             = 4096                                {product}
     bool ZeroTLAB                                 := true                                {product}
     intx hashCode                                  = 5                                   {product}
deriwotua
# 可以看到 bool ZeroTLAB := true {product}  这个参数已经被修改
```

#### 2.6.2、查看正在运行的jvm参数
如果想要查看正在运行的`jvm`就需要借助于`jinfo`命令查看。

首先，启动一个tomcat用于测试，来观察下运行的`jvm`参数。
```shell script
cd /tmp/
rz 上传
tar -xvf apache-tomcat-7.0.57.tar.gz
cd apache-tomcat-7.0.57
cd bin/
./startup.sh

#http://192.168.40.133:8080/ 进行访问
```

访问成功：
![运行中的tomcat](assets/运行中的tomcat.png)

查看所有的参数
- 用法：`jinfo -flags <进程id>`
  - 查找进程id 可以通过ps命令 `ps -ef | grep tomcat` 或者使用Java提供的 `jps` 或者 `jps -l` 查看java进程

  ```shell script
  #通过jps 或者 jps -l 查看java进程
  [root@node01 bin]# jps
  6346 Jps        # jps命令本身进程
  6219 Bootstrap  # tomcat 进程
  # 直接使用jps打印的名称不好区分
  # jps -l 打印时把完整类包带上便于区分 
  [root@node01 bin]# jps -l
  6358 sun.tools.jps.Jps                      # jps命令本身进程
  6219 org.apache.catalina.startup.Bootstrap  # tomcat 进程
  [root@node01 bin]#
  
  # 通过上面的进程id 使用 jinfo 查看正在运行的jvm参数
  [root@node01 bin]# jinfo -flags 6219
  Attaching to process ID 6219, please wait...
  Debugger attached successfully.
  Server compiler detected.
  JVM version is 25.141-b15
  # 上面学过的 初始堆内存大小-XX:InitialHeapSize=31457280 最大堆内存大小-XX:MaxHeapSize=488636416 
  Non-default VM flags: -XX:CICompilerCount=2 -XX:InitialHeapSize=31457280 -XX:MaxHeapSize=488636416 -XX:MaxNewSize=162529280 -XX:MinHeapDeltaBytes=524288 -XX:NewSize=10485760 -XX:OldSize=20971520 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps -XX:+UseParallelGC
  Command line: -Djava.util.logging.config.file=/tmp/apache-tomcat7.0.57/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djava.endorsed.dirs=/tmp/apache-tomcat-7.0.57/endorsed -Dcatalina.base=/tmp/apachetomcat-7.0.57 -Dcatalina.home=/tmp/apache-tomcat-7.0.57 -Djava.io.tmpdir=/tmp/apachetomcat-7.0.57/temp
  ```

查看运行Java程序某一参数的值
- 用法：`jinfo -flag <参数名> <进程id>`
  ```shell script
  #查看某一参数的值，用法：jinfo -flag <参数名> <进程id>
  [root@node01 bin]# jinfo -flag MaxHeapSize 6219
  -XX:MaxHeapSize=488636416
  ```

# 3、jvm的内存模型
JVM的内存模型在`1.7`和`1.8`有较大的区别，虽然这里是以`1.8`为例进行讲解，但是也是需要对`1.7`的内存模型有所了解，所以接下里，先学习`1.7`再学习`1.8`的内存模型。

### 3.1、jdk1.7的堆内存模型

![jdk1.7堆内存模型](assets/jdk1.7堆内存模型.png)

`Young` 年轻区（代）
- `Young`区被划分为三部分，**`Eden`区**和**两个大小严格相同的`Survivor`区**，其中，`Survivor`区间中，某一时刻只有其中一个是被使用的，另外一个留做垃圾收集时复制对象用，在`Eden`区间变满的时候， GC就会将**存活的对象移到空闲的`Survivor`区间中**，根据JVM的策略，在经过几次垃圾收集后，任然存活于`Survivor`的对象将被移动到`Tenured`区间。

`Tenured` 年老区
- `Tenured`区**主要保存生命周期长的对象**，一般是一些老的对象，当一些对象在`Young`复制转移一定的次数以后，对象就会被转移到`Tenured`区，一般如果系统中用了application级别的缓存，缓存中的对象往往会被转移到这一区间。

`Perm` 永久区
- `Perm`代主要保存`class`,`method`,`filed`对象，这部份的空间一般不会溢出，除非一次性加载了很多的类，不过在涉及到热部署的应用服务器的时候，有时候会遇到 `java.lang.OutOfMemoryError : PermGen space` 的错误，造成这个错误的很大原因就有可能是每次都重新部署，但是重新部署后，类的class没有被卸载掉，这样就造成了大量的class对象保存在了`perm`中，这种情况下，一般重新启动应用服务器可以解决问题。

`Virtual`区
- 最大内存和初始内存的差值，就是`Virtual`区。

### 3.2、jdk1.8的堆内存模型

![jdk1.8堆内存模型](assets/jdk1.8堆内存模型.png)

由上图可以看出，`jdk1.8`的堆内存模型是由2部分组成，年轻代 + 年老代。
- 年轻代：`Eden` + `2*Survivor`
- 年老代：`OldGen`

在`jdk1.8`中变化最大的`Perm`永久区，用`Metaspace`（元数据空间）进行了取代。

需要特别说明的是：`Metaspace`所占用的内存空间不是在虚拟机内部，而是在服务器本地内存空间中，这也是与1.7的永久代最大的区别所在。

`1.8`内存模型
![堆内存与非堆内存](assets/堆内存与非堆内存.png)

> CCS 压缩指针

### 3.3、为什么要废弃1.7中的永久区？
官网给出了解释：`http://openjdk.java.net/jeps/122`
> This is part of the JRockit and Hotspot convergence effort. JRockit customers do not need
> to configure the permanent generation (since JRockit does not have a permanent
> generation) and are accustomed to not configuring the permanent generation.

> 移除永久代是为融合`HotSpot JVM`与 `JRockit VM`而做出的努力，因为`JRockit`没有永久代，不需要配置永久代。

现实使用中，由于永久代内存经常不够用或发生内存泄露，爆出异常`java.lang.OutOfMemoryError: PermGen`。

基于此，将永久区废弃，而改用元空间，改为了使用本地内存空间。

### 3.4、通过jstat命令进行查看堆内存使用情况
`jstat`命令可以查看堆内存各部分的使用量，以及加载类的数量。
- 命令的格式如下
```shell script
# vmid 进程id
jstat [-命令选项] [vmid] [间隔时间/毫秒] [查询次数]
```

#### 3.4.1、查看class加载统计

以tomcat为例查看class加载统计
- 格式：`jstat -class [vmid]`
```shell script
[root@node01 ~]# jps
7080 Jps
6219 Bootstrap

# 查看tomcat class加载统计
[root@node01 ~]# jstat -class 6219
Loaded  Bytes  Unloaded Bytes Time
  3273 7122.3         0   0.0 3.98
# Loaded 3273 加载3273个class
#   Bytes 所占用空间7122.3
# Unloaded 未加载数量
#   Bytes未加载占用空间0.0
# Time 时间3.98
```

说明
- `Loaded`：加载class的数量
- `Bytes`：所占用空间大小
- `Unloaded`：未加载数量
- `Bytes`：未加载占用空间
- `Time`：时间

#### 3.4.2、查看编译统计

查看编译统计
- 格式：`jstat -compiler [vmid]`
```shell script
[root@node01 ~]# jstat -compiler 6219
Compiled Failed Invalid Time FailedType FailedMethod
  2376        1       0 8.04          1 org/apache/tomcat/util/IntrospectionUtils
setProperty
```

说明
- `Compiled`：编译数量。
- `Failed`：失败数量
- `Invalid`：不可用数量
- `Time`：时间
- `FailedType`：失败类型
- `FailedMethod`：失败的方法

#### 3.4.3、垃圾回收统计

```shell script
[root@node01 ~]# jstat -gc 6219
 S0C   S1C   S0U   S1U   EC      EU      OC      OU      MC     MU      CCSC   CCSU   YGC   YGCT   FGC   FGCT   GCT
9216.0 8704.0 0.0 6127.3 62976.0 3560.4 33792.0 20434.9 23808.0 23196.1 2560.0 2361.6  7   1.078    1   0.244 1.323

#也可以指定打印的间隔和次数，每1秒中打印一次，共打印5次
[root@node01 ~]# jstat -gc 6219 1000 5
 S0C S1C S0U S1U EC EU OC OU MC MU CCSC CCSU YGC YGCT FGC FGCT GCT
9216.0 8704.0 0.0 6127.3 62976.0 3917.3 33792.0 20434.9 23808.0 23196.1 2560.0 2361.6 7 1.078 1 0.244 1.323
9216.0 8704.0 0.0 6127.3 62976.0 3917.3 33792.0 20434.9 23808.0 23196.1 2560.0 2361.6 7 1.078 1 0.244 1.323
9216.0 8704.0 0.0 6127.3 62976.0 3917.3 33792.0 20434.9 23808.0 23196.1 2560.0 2361.6 7 1.078 1 0.244 1.323
9216.0 8704.0 0.0 6127.3 62976.0 3917.3 33792.0 20434.9 23808.0 23196.1 2560.0 2361.6 7 1.078 1 0.244 1.323
9216.0 8704.0 0.0 6127.3 62976.0 3917.3 33792.0 20434.9 23808.0 23196.1 2560.0 2361.6 7 1.078 1 0.244 1.323
```

说明
- `S0C`：第一个`Survivor`区的大小（KB）
- `S1C`：第二个`Survivor`区的大小（KB）
- `S0U`：第一个`Survivor`区的使用大小（KB）
- `S1U`：第二个`Survivor`区的使用大小（KB）
- `EC`：`Eden`区的大小（KB）
- `EU`：`Eden`区的使用大小（KB）
- `OC`：`Old`区大小（KB）
- `OU`：`Old`使用大小（KB）
- `MC`：方法区大小（KB）
- `MU`：方法区使用大小（KB）
- `CCSC`：压缩类空间大小（KB）
- `CCSU`：压缩类空间使用大小（KB）
- `YGC`：年轻代垃圾回收次数
- `YGCT`：年轻代垃圾回收消耗时间
- `FGC`：老年代垃圾回收次数
- `FGCT`：老年代垃圾回收消耗时间
- `GCT`：垃圾回收消耗总时间

## 4、jmap的使用以及内存溢出分析
前面通过`jstat`可以对jvm堆的内存进行统计分析，而`jmap`可以获取到更加详细的内容，如：内存使用情况的汇总、对内存溢出的定位与分析。

### 4.1、查看内存使用情况

查看内存使用情况
- 格式：`jmap -heap <vmid>`
```shell script
[root@node01 ~]# jmap -heap 6219
Attaching to process ID 6219, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.141-b15

using thread-local object allocation.
Parallel GC with 2 thread(s)

Heap Configuration: #堆内存配置信息
    MinHeapFreeRatio          = 0
    MaxHeapFreeRatio          = 100
    MaxHeapSize               = 488636416 (466.0MB)
    NewSize                   = 10485760 (10.0MB)  # 初始年轻代大小
    MaxNewSize                = 162529280 (155.0MB)
    OldSize                   = 20971520 (20.0MB)
    NewRatio                  = 2
    SurvivorRatio             = 8
    MetaspaceSize             = 21807104 (20.796875MB)
    CompressedClassSpaceSize  = 1073741824 (1024.0MB)
    MaxMetaspaceSize          = 17592186044415 MB
    G1HeapRegionSize          = 0 (0.0MB)

Heap Usage: # 堆内存的使用情况
PS Young Generation #年轻代
Eden Space:
    capacity  = 123731968 (118.0MB)
    used      = 1384736 (1.320587158203125MB)
    free      = 122347232 (116.67941284179688MB)
    1.1191416594941737% used
From Space:
    capacity  = 9437184 (9.0MB)
    used      = 0 (0.0MB)
    free      = 9437184 (9.0MB)
    0.0% used
To Space:
    capacity  = 9437184 (9.0MB)
    used      = 0 (0.0MB)
    free      = 9437184 (9.0MB)
    0.0% used
PS Old Generation #年老代
    capacity  = 28311552 (27.0MB)
    used      = 13698672 (13.064071655273438MB)
    free      = 14612880 (13.935928344726562MB)
    48.38545057508681% used

13648 interned Strings occupying 1866368 bytes.
```

### 4.2、查看内存中对象数量及大小

查看所有对象，包括活跃以及非活跃的
```shell script
jmap -histo <pid> | more

[root@node01 ~]# jmap -histo 6219 | more
 num     #instances         #bytes  class name
----------------------------------------------
   1:         20559       17384512  [B
   2:        111749       13785560  [C
   3:         18884        9265200  [I
   4:         60356        2414240  java.util.TreeMap$Entry
   5:         63113        1514712  java.lang.String
   6:         18306         732240  java.util.HashMap$KeyIterator
   7:         20000         640000  java.util.ArrayList$Itr
   8:         19421         621472  java.io.File
   9:         15442         617680  java.util.HashMap$ValueIterator
  10:          8339         595656  [Ljava.lang.Object;
  11:         17452         558464  java.util.HashMap$Node
  12:          6004         528352  java.lang.reflect.Method
  13:          4118         475088  java.lang.Class
  14:          8100         453600  java.util.concurrent.ConcurrentHashMap$KeyIterator
  15:         11545         390952  [Ljava.lang.String;
  16:         15428         370272  org.apache.catalina.LifecycleEvent
  17:         15428         311808  [Lorg.apache.catalina.Container;
  18:          2924         264640  [S
  19:          3669         264168  java.util.regex.Pattern
  20:          3655         233920  java.util.regex.Matcher
  21:          7288         233216  java.util.concurrent.ConcurrentHashMap$Node
  22:          1330         205616  [Ljava.util.HashMap$Node;
  23:          3655         204680  [Ljava.util.regex.Pattern$GroupHead;
  24:          3827         179448  [[C
  25:          2776         155456  java.util.concurrent.ConcurrentHashMap$ValueIterator
  26:          6356         152544  java.util.concurrent.CopyOnWriteArrayList$COWIterator
  27:          5744         137856  java.util.Collections$UnmodifiableCollection$1
- - More - - 
```

查看活跃对象
```shell script
jmap -histo:live <pid> | more
```


```shell script
[root@node01 ~]# jmap -histo:live 6219 | more

  num     #instances         #bytes  class name
 ----------------------------------------------
    1:         37124        6613080  [C  # [ 表示数组 C 表示字符 [C 字符数组空间占用最多
    2:          2086        3335360  [B
    3:         35391         849384  java.lang.String
    4:         15338         490816  java.util.HashMap$Node
    5:          4105         473736  java.lang.Class
    6:          4503         396264  java.lang.reflect.Method
    7:          4478         283552  [Ljava.lang.Object;
    8:          2218         244312  [I
    9:          6935         221920  java.util.concurrent.ConcurrentHashMap$Node
   10:           993         177928  [Ljava.util.HashMap$Node;
   11:            87         104592  [Ljava.util.concurrent.ConcurrentHashMap$Node;
   12:          6194          99104  java.lang.Object
   13:          1107          80856  [Ljava.lang.String;
   14:          1551          74448  java.util.HashMap
   15:           187          57776  [Ljava.util.WeakHashMap$Entry;
   16:          2557          53440  [Ljava.lang.Class;
   17:          1515          48480  java.util.Hashtable$Entry
   18:          1078          43120  java.lang.ref.Finalizer
   19:          1070          42800  java.lang.ref.SoftReference
   20:          1070          42800  java.util.LinkedHashMap$Entry
   21:          1021          40840  java.util.TreeMap$Entry
   22:           841          40368  org.apache.tomcat.util.modeler.AttributeInfo
   23:           476          38080  java.lang.reflect.Constructor
   24:            48          34944  [S
   25:           125          34416  [[C
   26:          1351          32424  java.util.ArrayList
   27:           987          31584  java.lang.ref.WeakReference
- - More - -

#对象说明
B byte
C char
D double
F float
I int
J long
Z boolean
[ 数组，如[I表示int[]
[L+类名 其他对象
```

### 4.3、将内存使用情况dump到文件中
有些时候需要将`jvm`当前内存中的情况`dump`到文件中即做快照，然后对它进行分析，`jmap`也是支持`dump`到文件中的。

用法
```shell script
# format=b 二进制格式
jmap -dump:format=b,file=dumpFileName <pid>

#示例
jmap -dump:format=b,file=/tmp/dump.dat 6219
```

![将内存使用情况dump到文件中](assets/将内存使用情况dump到文件中.png)

可以看到已经在/tmp下生成了dump.dat的文件。

### 4.4、通过jhat对dump文件进行分析
在上一小节中，我们将`jvm`的内存`dump`到文件中，这个文件是一个二进制的文件，不方便查看，这时可以借助于`jhat`工具进行查看。

用法
```shell script
jhat -port <port> <file>

#示例：
[root@node01 tmp]# jhat -port 9999 /tmp/dump.dat
Reading from /tmp/dump.dat...
Dump file created Mon Sep 10 01:04:21 CST 2018
Snapshot read, resolving...
Resolving 204094 objects...
Chasing references, expect 40 dots........................................
Eliminating duplicate references........................................
Snapshot resolved.
Started HTTP server on port 9999  # jhat地址
Server is ready.
```

打开浏览器进行访问：`http://192.168.40.133:9999/`
- 页面上按照包名进行分类
![通过jhat对dump文件进行分析](assets/通过jhat对dump文件进行分析.png)

在最后面有`OQL`查询功能
- 提供一种查询语句
- [语法示例](http://192.168.40.133:9999/oqlhelp/)
![OQL查询功能](assets/OQL查询功能.png)

![OQL查询功能2](assets/OQL查询功能2.png)

### 4.5、通过MAT工具对dump文件进行分析

#### 4.5.1、MAT工具介绍
`MAT`(Memory Analyzer Tool)，一个基于Eclipse的内存分析工具，是一个快速、功能丰富的`JAVA heap`分析工具，它可以帮助查找内存泄漏和减少内存消耗。使用内存分析工具从众多的对象中进行分析，快速的计算出在内存中对象的占用大小，看看是谁阻止了垃圾收集器的回收工作，并可以通过报表直观的查看到可能造成这种结果的对象。

[官网地址](https://www.eclipse.org/mat/)

![MAT内存分析工具](assets/MAT内存分析工具.png)

#### 4.5.2、下载安装
[下载地址](https://www.eclipse.org/mat/downloads.php)

![MAT下载地址](assets/MAT下载地址.png)

将下载得到的`MemoryAnalyzer-1.8.0.20180604-win32.win32.x86_64.zip`进行解压

![MAT启动](assets/MAT启动.png)

#### 4.5.3、使用

![MAT中打开dump文件](assets/MAT中打开dump文件.png)

选择第一个自动生成内存溢出报表
![MAT中打开dump文件2](assets/MAT中打开dump文件2.png)

![MAT中dump文件详情页](assets/MAT中dump文件详情页.png)

![MAT搜索查找](assets/MAT搜索查找.png)

查看对象以及它的依赖
![MAT查看对象依赖](assets/MAT查看对象依赖.png)

查看可能存在内存泄露的分析
![可能内存泄漏分析](assets/可能内存泄漏分析.png)

## 5、实战：内存溢出的定位与分析
内存溢出在实际的生产环境中经常会遇到，比如，不断的将数据写入到一个集合中，出现了死循环，读取超大的文
件等等，都可能会造成内存溢出。

如果出现了内存溢出，首先需要定位到发生内存溢出的环节，并且进行分析，是正常还是非正常情况，如果是正常的需求，就应该考虑加大内存的设置，如果是非正常需求，那么就要对代码进行修改，修复这个bug。

首先，得先学会如何定位问题，然后再进行分析。如何定位问题呢，需要借助于`jmap`与`MAT`工具进行定
位分析。

接下来，模拟内存溢出的场景。

### 5.1、模拟内存溢出
编写代码，向List集合中添加100万个字符串，每个字符串由1000个UUID组成。如果程序能够正常执行，最后打印ok。
```java
public class TestJvmOutOfMemory {
    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 10000000; i++) {
            String str = "";
            for (int j = 0; j < 1000; j++) {
                str += UUID.randomUUID().toString();
            }
            list.add(str);
        }
        System.out.println("ok");
    }
}
```

为了演示效果，设置执行的参数，这里使用的是Idea编辑器。
```shell script
# 设置初始堆内存大小为8m 最大也为8m
# 当发生内存溢出异常后把 heap dump到文件
-Xms8m -Xmx8m -XX:+HeapDumpOnOutOfMemoryError
```

![内存溢出演示](assets/内存溢出演示.png)


### 5.2、运行测试
测试结果如下
```shell script
java.lang.OutOfMemoryError: Java heap space
Dumping heap to java_pid5348.hprof ...
Heap dump file created [8137186 bytes in 0.032 secs]
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at java.util.Arrays.copyOf(Arrays.java:3332)
        at
java.lang.AbstractStringBuilder.ensureCapacityInternal(AbstractStringBuilder.java:124)
        at java.lang.AbstractStringBuilder.append(AbstractStringBuilder.java:448)
        at java.lang.StringBuilder.append(StringBuilder.java:136)
        at tk.deriwotua.jvm.TestJvmOutOfMemory.main(TestJvmOutOfMemory.java:14)
Process finished with exit code 1
```
可以看到，当发生内存溢出时，会dump文件到java_pid5348.hprof。
![内存溢出dump](assets/内存溢出dump.png)

### 5.3、导入到MAT工具中进行分析

![MAT中分析内存溢出](assets/MAT中分析内存溢出.png)

可以看到，有91.03%的内存由Object[]数组占有，所以比较可疑。

分析：这个可疑是正确的，因为已经有超过90%的内存都被它占有，这是非常有可能出现内存溢出的。

查看详情
![MAT可疑内存溢出详情页](assets/MAT可疑内存溢出详情页.png)

可以看到集合中存储了大量的uuid字符串。

![MAT可疑内存溢出详情页可展开每个对象详情](assets/MAT可疑内存溢出详情页可展开每个对象详情.png)

## 6、jstack的使用
有些时候需要查看下jvm中的线程执行情况，比如，发现服务器的CPU的负载突然增高了、出现了死锁、死循环等，该如何分析呢？

由于程序是正常运行的，没有任何的输出，从日志方面也看不出什么问题，所以就需要看下jvm的内部线程的执行情况，然后再进行分析查找出原因。

这个时候，就需要借助于`jstack`命令了，`jstack`的作用是**将正在运行的jvm的线程情况进行快照**，并且打印出来：
- 用法：`jstack <pid>`

```shell script
[root@node01 bin]# jstack 2203  # 打印每个线程执行情况
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.141-b15 mixed mode):

"Attach Listener" #24 daemon prio=9 os_prio=0 tid=0x00007fabb4001000 nid=0x906 waiting on condition [0x0000000000000000]
    java.lang.Thread.State: RUNNABLE
# http-bio-8080-exec-5 线程执行情况
"http-bio-8080-exec-5" #23 daemon prio=5 os_prio=0 tid=0x00007fabb057c000 nid=0x8e1 waiting on condition [0x00007fabd05b8000]
    java.lang.Thread.State: WAITING (parking)  # 线程处在等待状态
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for <0x00000000f8508360> (a
java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:104)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:32)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
        at java.lang.Thread.run(Thread.java:748)

"http-bio-8080-exec-4" #22 daemon prio=5 os_prio=0 tid=0x00007fab9c113800 nid=0x8e0 waiting on condition [0x00007fabd06b9000]
    java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for <0x00000000f8508360> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:104)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:32)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
        at java.lang.Thread.run(Thread.java:748)

"http-bio-8080-exec-3" #21 daemon prio=5 os_prio=0 tid=0x0000000001aeb800 nid=0x8df waiting on condition [0x00007fabd09ba000]
    java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for <0x00000000f8508360> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:104)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:32)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
        at java.lang.Thread.run(Thread.java:748)

"http-bio-8080-exec-2" #20 daemon prio=5 os_prio=0 tid=0x0000000001aea000 nid=0x8de waiting on condition [0x00007fabd0abb000]
    java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for <0x00000000f8508360> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:104)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:32)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
        at java.lang.Thread.run(Thread.java:748)

"http-bio-8080-exec-1" #19 daemon prio=5 os_prio=0 tid=0x0000000001ae8800 nid=0x8dd waiting on condition [0x00007fabd0bbc000]
    java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for <0x00000000f8508360> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:104)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:32)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
        at java.lang.Thread.run(Thread.java:748)

"ajp-bio-8009-AsyncTimeout" #17 daemon prio=5 os_prio=0 tid=0x00007fabe8128000 nid=0x8d0 waiting on condition [0x00007fabd0ece000]
    java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at org.apache.tomcat.util.net.JIoEndpoint$AsyncTimeout.run(JIoEndpoint.java:152)
        at java.lang.Thread.run(Thread.java:748)

"ajp-bio-8009-Acceptor-0" #16 daemon prio=5 os_prio=0 tid=0x00007fabe82d4000 nid=0x8cf runnable [0x00007fabd0fcf000]
    java.lang.Thread.State: RUNNABLE
        at java.net.PlainSocketImpl.socketAccept(Native Method)
        at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:409)
        at java.net.ServerSocket.implAccept(ServerSocket.java:545)
        at java.net.ServerSocket.accept(ServerSocket.java:513)
        at org.apache.tomcat.util.net.DefaultServerSocketFactory.acceptSocket(DefaultServerSocketFactory.java:60)
        at org.apache.tomcat.util.net.JIoEndpoint$Acceptor.run(JIoEndpoint.java:220)
        at java.lang.Thread.run(Thread.java:748)

"http-bio-8080-AsyncTimeout" #15 daemon prio=5 os_prio=0 tid=0x00007fabe82d1800 nid=0x8ce waiting on condition [0x00007fabd10d0000]
    java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at org.apache.tomcat.util.net.JIoEndpoint$AsyncTimeout.run(JIoEndpoint.java:152)
        at java.lang.Thread.run(Thread.java:748)

"http-bio-8080-Acceptor-0" #14 daemon prio=5 os_prio=0 tid=0x00007fabe82d0000 nid=0x8cd runnable [0x00007fabd11d1000]
    java.lang.Thread.State: RUNNABLE
        at java.net.PlainSocketImpl.socketAccept(Native Method)
        at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:409)
        at java.net.ServerSocket.implAccept(ServerSocket.java:545)
        at java.net.ServerSocket.accept(ServerSocket.java:513)
        at org.apache.tomcat.util.net.DefaultServerSocketFactory.acceptSocket(DefaultServerSocketFactory.java:60)
        at org.apache.tomcat.util.net.JIoEndpoint$Acceptor.run(JIoEndpoint.java:220)
        at java.lang.Thread.run(Thread.java:748)

"ContainerBackgroundProcessor[StandardEngine[Catalina]]" #13 daemon prio=5 os_prio=0 tid=0x00007fabe82ce000 nid=0x8cc waiting on condition [0x00007fabd12d2000]
    java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at org.apache.catalina.core.ContainerBase$ContainerBackgroundProcessor.run(ContainerBase.java:1513)
        at java.lang.Thread.run(Thread.java:748)

"GC Daemon" #10 daemon prio=2 os_prio=0 tid=0x00007fabe83b4000 nid=0x8b3 in Object.wait()[0x00007fabd1c2f000]
    java.lang.Thread.State: TIMED_WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000e315c2d0> (a sun.misc.GC$LatencyLock)
        at sun.misc.GC$Daemon.run(GC.java:117)
        - locked <0x00000000e315c2d0> (a sun.misc.GC$LatencyLock)
"Service Thread" #7 daemon prio=9 os_prio=0 tid=0x00007fabe80c3800 nid=0x8a5 runnable[0x0000000000000000]
    java.lang.Thread.State: RUNNABLE
"C1 CompilerThread1" #6 daemon prio=9 os_prio=0 tid=0x00007fabe80b6800 nid=0x8a4 waiting on condition [0x0000000000000000]
    java.lang.Thread.State: RUNNABLE
"C2 CompilerThread0" #5 daemon prio=9 os_prio=0 tid=0x00007fabe80b3800 nid=0x8a3 waiting on condition [0x0000000000000000]
    java.lang.Thread.State: RUNNABLE
"Signal Dispatcher" #4 daemon prio=9 os_prio=0 tid=0x00007fabe80b2000 nid=0x8a2 runnable[0x0000000000000000]
    java.lang.Thread.State: RUNNABLE

"Finalizer" #3 daemon prio=8 os_prio=0 tid=0x00007fabe807f000 nid=0x8a1 in Object.wait()[0x00007fabd2a67000]
    java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000e3162918> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
        - locked <0x00000000e3162918> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:164)
        at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:209)

"Reference Handler" #2 daemon prio=10 os_prio=0 tid=0x00007fabe807a800 nid=0x8a0 in Object.wait() [0x00007fabd2b68000]
    java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000e3162958> (a java.lang.ref.Reference$Lock)
        at java.lang.Object.wait(Object.java:502)
        at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
        - locked <0x00000000e3162958> (a java.lang.ref.Reference$Lock)
        at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

"main" #1 prio=5 os_prio=0 tid=0x00007fabe8009000 nid=0x89c runnable [0x00007fabed210000]
    java.lang.Thread.State: RUNNABLE
        at java.net.PlainSocketImpl.socketAccept(Native Method)
        at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:409)
        at java.net.ServerSocket.implAccept(ServerSocket.java:545)
        at java.net.ServerSocket.accept(ServerSocket.java:513)
        at org.apache.catalina.core.StandardServer.await(StandardServer.java:453)
        at org.apache.catalina.startup.Catalina.await(Catalina.java:777)
        at org.apache.catalina.startup.Catalina.start(Catalina.java:723)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at org.apache.catalina.startup.Bootstrap.start(Bootstrap.java:321)
        at org.apache.catalina.startup.Bootstrap.main(Bootstrap.java:455)

"VM Thread" os_prio=0 tid=0x00007fabe8073000 nid=0x89f runnable

"GC task thread#0 (ParallelGC)" os_prio=0 tid=0x00007fabe801e000 nid=0x89d runnable

"GC task thread#1 (ParallelGC)" os_prio=0 tid=0x00007fabe8020000 nid=0x89e runnable

"VM Periodic Task Thread" os_prio=0 tid=0x00007fabe80d6800 nid=0x8a6 waiting on condition

JNI global references: 43
```

### 6.1、线程的状态

![线程状态](assets/线程状态.png)

在Java中线程的状态一共被分成6种
- 初始态（NEW）
  - 创建一个Thread对象，但还未调用`start()`启动线程时，线程处于初始态。
- 运行态（RUNNABLE），在Java中，运行态包括 就绪态 和 运行态。
  - 就绪态
    - 该状态下的线程已经获得执行所需的所有资源，只要CPU分配执行权就能运行。
    - 所有就绪态的线程存放在就绪队列中。
  - 运行态
    - 获得CPU执行权，正在执行的线程。
    - 由于一个CPU同一时刻只能执行一条线程，因此每个CPU每个时刻只有一条运行态的线程。
- 阻塞态（BLOCKED）
  - 当一条正在执行的线程请求某一资源失败时，就会进入阻塞态。
  - 而在Java中，阻塞态专指请求锁失败时进入的状态。
  - 由一个阻塞队列存放所有阻塞态的线程。
  - 处于阻塞态的线程会不断请求资源，一旦请求成功，就会进入就绪队列，等待执行。
- 等待态（WAITING）
  - 当前线程中调用`wait`、`join`、`park`函数时，当前线程就会进入等待态。
  - 也有一个等待队列存放所有等待态的线程。
  - 线程处于等待态表示它需要等待其他线程的指示才能继续运行。
  - 进入等待态的线程会释放CPU执行权，并释放资源（如：锁）
- 超时等待态（TIMED_WAITING）
  - 当运行中的线程调用`sleep(time)`、`wait`、`join`、`parkNanos`、`parkUntil`时，就会进入该状态；
  - 它和等待态一样，并不是因为请求不到资源，而是主动进入，并且进入后需要其他线程唤醒；
  - 进入该状态后释放CPU执行权 和 占有的资源。
  - 与等待态的区别：到了超时时间后自动进入阻塞队列，开始竞争锁。
- 终止态（TERMINATED）
  - 线程执行结束后的状态。

### 6.2、实战：死锁问题
如果在生产环境发生了死锁，将看到的是部署的程序没有任何反应了，这个时候可以借助`jstack`进行分析，下面查找死锁的原因。

#### 6.2.1、构造死锁
编写代码，启动2个线程，Thread1拿到了`obj1`锁，准备去拿`obj2`锁时，`obj2`已经被Thread2锁定，所以发生了死锁。
```java
public class TestDeadLock {
    private static Object obj1 = new Object();
    private static Object obj2 = new Object();
    public static void main(String[] args) {
        new Thread(new Thread1()).start();
        new Thread(new Thread2()).start();
    }
    private static class Thread1 implements Runnable{
        @Override
        public void run() {
            synchronized (obj1){
                System.out.println("Thread1 拿到了 obj1 的锁！");
                try {
                    // 停顿2秒的意义在于，让Thread2线程拿到obj2的锁
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj2){
                    System.out.println("Thread1 拿到了 obj2 的锁！");
                }
            }
        }
    }
    private static class Thread2 implements Runnable{
        @Override
        public void run() {
            synchronized (obj2){
                System.out.println("Thread2 拿到了 obj2 的锁！");
                try {
                    // 停顿2秒的意义在于，让Thread1线程拿到obj1的锁
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj1){
                    System.out.println("Thread2 拿到了 obj1 的锁！");
                }
            }
        }
    }
}
```

#### 6.2.2、在linux上运行

```shell script
[root@node01 test]# javac TestDeadLock.java
[root@node01 test]# ll
总用量 28
-rw-r--r--. 1 root root 184 9月 11 10:39 TestDeadLock$1.class
-rw-r--r--. 1 root root 843 9月 11 10:39 TestDeadLock.class
-rw-r--r--. 1 root root 1567 9月 11 10:39 TestDeadLock.java
-rw-r--r--. 1 root root 1078 9月 11 10:39 TestDeadLock$Thread1.class
-rw-r--r--. 1 root root 1078 9月 11 10:39 TestDeadLock$Thread2.class
-rw-r--r--. 1 root root 573 9月 9 10:21 TestJVM.class
-rw-r--r--. 1 root root 261 9月 9 10:21 TestJVM.java

[root@node01 test]# java TestDeadLock
Thread1 拿到了 obj1 的锁！
Thread2 拿到了 obj2 的锁！
#这里发生了死锁，程序一直将等待下去
````

#### 6.2.3、使用jstack进行分析

```shell script
[root@node01 ~]# jstack 3256
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.141-b15 mixed mode):

"Attach Listener" #11 daemon prio=9 os_prio=0 tid=0x00007f5bfc001000 nid=0xcff waiting on condition [0x0000000000000000]
    java.lang.Thread.State: RUNNABLE

"DestroyJavaVM" #10 prio=5 os_prio=0 tid=0x00007f5c2c008800 nid=0xcb9 waiting on condition [0x0000000000000000]
    java.lang.Thread.State: RUNNABLE

"Thread-1" #9 prio=5 os_prio=0 tid=0x00007f5c2c0e9000 nid=0xcc5 waiting for monitor entry[0x00007f5c1c7f6000]
    java.lang.Thread.State: BLOCKED (on object monitor)
        at TestDeadLock$Thread2.run(TestDeadLock.java:47)
        - waiting to lock <0x00000000f655dc40> (a java.lang.Object)
        - locked <0x00000000f655dc50> (a java.lang.Object)
        at java.lang.Thread.run(Thread.java:748)

"Thread-0" #8 prio=5 os_prio=0 tid=0x00007f5c2c0e7000 nid=0xcc4 waiting for monitor entry[0x00007f5c1c8f7000]
    java.lang.Thread.State: BLOCKED (on object monitor)
        at TestDeadLock$Thread1.run(TestDeadLock.java:27)
        - waiting to lock <0x00000000f655dc50> (a java.lang.Object)
        - locked <0x00000000f655dc40> (a java.lang.Object)
        at java.lang.Thread.run(Thread.java:748)

"Service Thread" #7 daemon prio=9 os_prio=0 tid=0x00007f5c2c0d3000 nid=0xcc2 runnable [0x0000000000000000]
    java.lang.Thread.State: RUNNABLE

"C1 CompilerThread1" #6 daemon prio=9 os_prio=0 tid=0x00007f5c2c0b6000 nid=0xcc1 waiting on condition [0x0000000000000000]
    java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #5 daemon prio=9 os_prio=0 tid=0x00007f5c2c0b3000 nid=0xcc0 waiting on condition [0x0000000000000000]
    java.lang.Thread.State: RUNNABLE

"Signal Dispatcher" #4 daemon prio=9 os_prio=0 tid=0x00007f5c2c0b1800 nid=0xcbf runnable [0x0000000000000000]
    java.lang.Thread.State: RUNNABLE

"Finalizer" #3 daemon prio=8 os_prio=0 tid=0x00007f5c2c07e800 nid=0xcbe in Object.wait() [0x00007f5c1cdfc000]
    java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000f6508ec8> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
        - locked <0x00000000f6508ec8> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:164)
        at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:209)

"Reference Handler" #2 daemon prio=10 os_prio=0 tid=0x00007f5c2c07a000 nid=0xcbd in Object.wait() [0x00007f5c1cefd000]
    java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000f6506b68> (a java.lang.ref.Reference$Lock)
        at java.lang.Object.wait(Object.java:502)
        at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
        - locked <0x00000000f6506b68> (a java.lang.ref.Reference$Lock)
        at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

"VM Thread" os_prio=0 tid=0x00007f5c2c072800 nid=0xcbc runnable

"GC task thread#0 (ParallelGC)" os_prio=0 tid=0x00007f5c2c01d800 nid=0xcba runnable

"GC task thread#1 (ParallelGC)" os_prio=0 tid=0x00007f5c2c01f800 nid=0xcbb runnable

"VM Periodic Task Thread" os_prio=0 tid=0x00007f5c2c0d6800 nid=0xcc3 waiting on condition

JNI global references: 6


Found one Java-level deadlock:
=============================
"Thread-1": # Thread-1 等待锁定 0x00007f5c080062c8 这个资源
  waiting to lock monitor 0x00007f5c080062c8 (object 0x00000000f655dc40, a java.lang.Object), 
  which is held by "Thread-0" # 而资源被Thread-0这个线程占用
"Thread-0": # Thread-0 等待锁定 0x00007f5c08004e28 这个资源
  waiting to lock monitor 0x00007f5c08004e28 (object 0x00000000f655dc50, a java.lang.Object),
  which is held by "Thread-1" # 而资源被Thread-1这个线程占用

Java stack information for the threads listed above:
===================================================
"Thread-1":
        at TestDeadLock$Thread2.run(TestDeadLock.java:47)
        # Thread-1 等待锁定 0x00000000f655dc40 这个资源
        - waiting to lock <0x00000000f655dc40> (a java.lang.Object)
        # Thread-1 已锁定 0x00000000f655dc50 这个资源
        - locked <0x00000000f655dc50> (a java.lang.Object)
        at java.lang.Thread.run(Thread.java:748)
"Thread-0":
        at TestDeadLock$Thread1.run(TestDeadLock.java:27)
        # Thread-0 等待锁定 0x00000000f655dc50 这个资源
        - waiting to lock <0x00000000f655dc50> (a java.lang.Object)
        # Thread-0 已锁定 0x00000000f655dc40 这个资源
        - locked <0x00000000f655dc40> (a java.lang.Object)
        at java.lang.Thread.run(Thread.java:748)
# 发现一个死锁
Found 1 deadlock.
```

在输出的信息中，已经看到，发现了1个死锁，关键看这个
```shell script
"Thread-1":
        at TestDeadLock$Thread2.run(TestDeadLock.java:47)
        - waiting to lock <0x00000000f655dc40> (a java.lang.Object)
        - locked <0x00000000f655dc50> (a java.lang.Object)
        at java.lang.Thread.run(Thread.java:748)
"Thread-0":
        at TestDeadLock$Thread1.run(TestDeadLock.java:27)
        - waiting to lock <0x00000000f655dc50> (a java.lang.Object)
        - locked <0x00000000f655dc40> (a java.lang.Object)
        at java.lang.Thread.run(Thread.java:748)
# 发现一个死锁
Found 1 deadlock.
```

可以清晰的看到
- Thread2获取了 `<0x00000000f655dc50>` 的锁，等待获取 `<0x00000000f655dc40>` 这个锁
- Thread1获取了 `<0x00000000f655dc40>` 的锁，等待获取 `<0x00000000f655dc50>` 这个锁
- 由此可见，发生了死锁。

## 7、VisualVM工具的使用
> VisualVM涵盖了上面使用的所有命令

VisualVM，能够监控线程，内存情况，查看方法的CPU时间和内存中的对象，已被GC的对象，反向查看分配的堆栈(如100个String对象分别由哪几个对象分配出来的)。

VisualVM使用简单，几乎0配置，功能还是比较丰富的，几乎囊括了其它JDK自带命令的所有功能。
- 内存信息
- 线程信息
- Dump堆（本地进程）
- Dump线程（本地进程）
- 打开堆Dump。堆Dump可以用jmap来生成。
- 打开线程Dump
- 生成应用快照（包含内存信息、线程信息等等）
- 性能分析。CPU分析（各个方法调用时间，检查哪些方法耗时多），内存分析（各类对象占用的内存，检查哪些类占用内存多）
- ……

### 7.1、启动
在`jdk`的安装目录的`bin`目录下，找到`jvisualvm.exe`，双击打开即可。
![VisualVM启动](assets/VisualVM启动.png)

启动后会自动查找本地VM进程
![VisualVM页面](assets/VisualVM页面.png)

### 7.2、查看本地进程

![以idea为例双击查看idea进程](assets/以idea为例双击查看idea进程.png)

### 7.3、查看CPU、内存、类、线程运行信息

![查看CPU内存类线程运行信息](assets/查看CPU内存类线程运行信息.png)

### 7.4、查看线程详情

![查看线程详情](assets/查看线程详情.png)

也可以点击右上角Dump按钮，将线程的信息导出，其实就是执行的`jstack`命令。

![线程信息导出](assets/线程信息导出.png)

发现，显示的内容是一样的。

### 7.5、抽样器
抽样器可以对CPU、内存在一段时间内进行抽样，以供分析。

![抽样器](assets/抽样器.png)

### 7.6、监控远程的jvm

VisualJVM不仅是可以监控本地jvm进程，还可以监控远程的jvm进程，需要借助于`JMX`技术实现。

#### 7.6.1、什么是JMX？
`JMX`（Java Management Extensions，即Java管理扩展）是一个为应用程序、设备、系统等植入管理功能的框架。

`JMX`可以跨越一系列异构操作系统平台、系统体系结构和网络传输协议，灵活的开发无缝集成的系统、网络和服务管理应用。

#### 7.6.2、监控远程的tomcat
想要监控远程的tomcat，就需要在远程的tomcat进行对JMX配置，方法如下：
```yaml
#在tomcat的bin目录下，修改catalina.sh，添加如下的参数
JAVA_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
#这几个参数的意思是：
#-Dcom.sun.management.jmxremote ：允许使用JMX远程管理
#-Dcom.sun.management.jmxremote.port=9999 ：JMX远程连接端口
#-Dcom.sun.management.jmxremote.authenticate=false ：不进行身份认证，任何用户都可以连接
#-Dcom.sun.management.jmxremote.ssl=false ：不使用ssl

# 添加了一个JAVA_OPTS参数 启动时添加到jvm中
```
保存退出，重启tomcat。

#### 7.6.3、使用VisualJVM连接远程tomcat

首先添加远程主机
![添加远程主机](assets/添加远程主机.png)

然后接下来要在该主机上添加需要监控的jvm，在一个主机下可能会有很多的jvm需要监控
![添加需要监控的jvm](assets/添加需要监控的jvm.png)

![添加需要监控的jvm2](assets/添加需要监控的jvm2.png)

连接成功。使用方法和前面就一样了，就可以和监控本地jvm进程一样，监控远程的tomcat进程。

远程不能直接查看dump文件，还是需要dump后把dump文件下载到本地然后再在VisualJVM载入

## 7. 垃圾回收&相关算法&垃圾收集器&GC日志

[垃圾回收&相关算法&垃圾收集器&GC日志](./jvm-gc-gcalgorithm.md)

## 8. tomcat优化&底层代码指令&编码优化

[tomcat优化&底层代码指令&编码优化](./jvm-tomcat8-class.md)