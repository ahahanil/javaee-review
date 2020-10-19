[TOC]

# 0. 学习目标

- 能够说出什么是消息中间件
- 能够安装RabbitMQ
- 能够编写RabbitMQ的入门程序
- 能够说出RabbitMQ的5种模式特征
- 能够使用Spring整合RabbitMQ

# 1. 消息中间件概述
## 1.1. 什么是消息中间件

`MQ`全称为`Message Queue`，消息队列是应用程序和应用程序之间的通信方法。

- 为什么使用MQ

  - 在项目中，可将一些无需即时返回且耗时的操作提取出来，进行**异步处理**，而这种异步处理的方式大大的节省了服务器的请求响应时间，从而**提高**了**系统**的**吞吐量**。

- 开发中消息队列通常有如下应用场景
  - 任务**异步**处理。将不需要同步处理的并且耗时长的操作由消息队列通知消息接收方进行异步处理。提高了应用程序的响应时间。
  - 应用程序**解耦合**。MQ相当于一个中介，生产方通过MQ与消费方交互，它将应用程序进行解耦合。
  - **削峰填谷**。如订单系统，在下单的时候就会往数据库写数据。但是数据库只能支撑每秒1000左右的并发写入，并发量再高就容易宕机。低峰期的时候并发也就100多个，但是在高峰期时候，并发量会突然激增到5000以上，这个时候数据库肯定卡死了。
  
  ![](assets/01.jpg)
  
    - 消息被MQ保存起来了，然后系统就可以按照自己的消费能力来消费，比如每秒1000个数据，这样慢慢写入数据库，这样就不会卡死数据库了。
  
    ![](assets/02.jpg)
  
    - 但是使用了MQ之后，限制消费消息的速度为1000，但是这样一来，高峰期产生的数据势必会被积压在MQ中，高峰就被“削”掉了。但是因为消息积压，在高峰期过后的一段时间内，消费消息的速度还是会维持在1000QPS，直到消费完积压的消息,这就叫做“填谷”
  
    ![](assets/03.jpg)

## 1.2. AMQP 和 JMS

MQ是消息通信的模型；实现MQ的大致有两种主流方式：`AMQP`、`JMS`。

### 1.2.1. AMQP

`AMQP`是一种协议，更准确的说是一种`binary wire-level protocol`（链接协议）。这是其和`JMS`的本质差别，`AMQP`不从API层进行限定，而是直接定义网络交换的数据格式。

### 1.2.2. JMS

`JMS`即Java消息服务（JavaMessage Service）应用程序接口，是一个Java平台中关于面向消息中间件（MOM）的API，用于在两个应用程序之间，或分布式系统中发送消息，进行异步通信。

### 1.2.3. AMQP 与 JMS 区别

- `JMS`是定义了统一的接口，来对消息操作进行统一；`AMQP`是通过规定协议来统一数据交互的格式
- `JMS`限定了必须使用Java语言；`AMQP`只是协议，不规定实现方式，因此是跨语言的。
- `JMS`规定了两种消息模式；而`AMQP`的消息模式更加丰富

## 1.3. 消息队列产品

市场上常见的消息队列有如下
- `ActiveMQ`：基于`JMS`
- `ZeroMQ`：基于C语言开发
- `RabbitMQ`：基于`AMQP`协议，`erlang`语言开发，稳定性好
- `RocketMQ`：基于`JMS`，阿里巴巴产品
- `Kafka`：类似`MQ`的产品；分布式消息系统，高吞吐量

## 1.4. RabbitMQ

`RabbitMQ`是由erlang语言开发，基于`AMQP`（Advanced Message Queue 高级消息队列协议）协议实现的消息队列，它是一种应用程序之间的通信方法，消息队列在分布式系统开发中应用非常广泛。

RabbitMQ官方地址：`http://www.rabbitmq.com/`

RabbitMQ提供了6种模式
- 简单模式
- work模式
- Publish/Subscribe发布与订阅模式
- Routing路由模式
- Topics主题模式
- RPC远程调用模式（远程调用，不太算MQ；暂不作介绍）

官网对应模式介绍：`https://www.rabbitmq.com/getstarted.html`

![1555988678324](assets/1555988678324.png)

# 2. 安装及配置RabbitMQ

详细查看 [RabbitMQ安装说明文档](./RabbitMQ安装说明文档.md)。

# 3. RabbitMQ入门

## 3.1. 搭建示例工程

### 3.1.1. 创建工程

![1555991321159](assets/1558319510387.png)



![1555991353704](assets/1558319546688.png)

### 3.1.2. 添加依赖

往`rabbitmq-quick`的`pom.xml`文件中添加如下依赖

```xml
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.6.0</version>
</dependency>
```

## 3.2. 编写生产者

编写消息生产者`Producer`
```java
package tk.deriwotua.mq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

    static final String QUEUE_NAME = "simple_queue";

    public static void main(String[] args) throws Exception {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //主机地址;默认为 localhost
        connectionFactory.setHost("localhost");
        //连接端口;默认为 5672
        connectionFactory.setPort(5672);
        //虚拟主机名称;默认为 /
        connectionFactory.setVirtualHost("/deriwotua");
        //连接用户名；默认为guest
        connectionFactory.setUsername("guest");
        //连接密码；默认为guest
        connectionFactory.setPassword("guest");

        //创建连接
        Connection connection = connectionFactory.newConnection();

        // 创建频道
        Channel channel = connection.createChannel();

        /**
         * 声明（创建）队列
         *   参数1：队列名称
         *   参数2：是否定义持久化队列
         *   参数3：是否独占本次连接
         *   参数4：是否在不使用的时候自动删除队列
         *   参数5：队列其它参数
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 要发送的信息
        String message = "你好；小兔子！";
        /**
         * 推送消息
         *   参数1：交换机名称，如果没有指定则使用默认Default Exchage
         *   参数2：路由key,简单模式可以传递队列名称
         *   参数3：消息其它属性
         *   参数4：消息内容
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("已发送消息：" + message);

        // 关闭资源
        channel.close();
        connection.close();
    }
}
```

在执行上述的消息发送之后；可以登录rabbitMQ的管理控制台，可以发现队列和其消息：

![1556006638979](assets/1556006638979.png)

![1556006647177](assets/1556006647177.png)

## 3.3. 编写消费者

抽取创建`connection`的工具类`ConnectionUtil`

```java
package tk.deriwotua.mq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtils {
    public static Connection getConnection() throws Exception {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //主机地址;默认为 localhost
        connectionFactory.setHost("localhost");
        //连接端口;默认为 5672
        connectionFactory.setPort(5672);
        //虚拟主机名称;默认为 /
        connectionFactory.setVirtualHost("/itcast");
        //连接用户名；默认为guest
        connectionFactory.setUsername("heima");
        //连接密码；默认为guest
        connectionFactory.setPassword("heima");

        //创建连接
        return connectionFactory.newConnection();
    }
}
```

编写消息的消费者`Consumer`

```java
package tk.deriwotua.mq.simple;

import com.rabbitmq.client.*;
import tk.deriwotua.mq.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 消费者
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();

        /**
         * 声明（创建）队列
         *   参数1：队列名称
         *   参数2：是否定义持久化队列
         *   参数3：是否独占本次连接
         *   参数4：是否在不使用的时候自动删除队列
         *   参数5：队列其它参数
         */
        channel.queueDeclare(Producer.QUEUE_NAME, true, false, false, null);

        //创建消费者；并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            /**
             * consumerTag 消息者标签，在channel.basicConsume时候可以指定
             * envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * properties 属性信息
             * body 消息
             */
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //路由key
                System.out.println("路由key为：" + envelope.getRoutingKey());
                //交换机
                System.out.println("交换机为：" + envelope.getExchange());
                //消息id
                System.out.println("消息id为：" + envelope.getDeliveryTag());
                //收到的消息
                System.out.println("接收到的消息为：" + new String(body, "utf-8"));
            }
        };
        //监听消息
        /**
         * 参数1：队列名称
         * 参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动确认
         * 参数3：消息接收到后回调
         */
        channel.basicConsume(Producer.QUEUE_NAME, true, consumer);

        //不关闭资源，应该一直监听消息
        //channel.close();
        //connection.close();
    }
}
```



## 3.4. 小结

上述的入门案例中中其实使用的是如下的简单模式

![1555991074575](assets/1555991074575.png)

在上图的模型中，有以下概念
- `P`：生产者，也就是要发送消息的程序
- `C`：消费者：消息的接受者，会一直等待消息到来。
- `queue`：消息队列，图中红色部分。类似一个邮箱，可以缓存消息；生产者向其中投递消息，消费者从其中取出消息。


# 4. AMQP

## 4.1. 相关概念介绍

`AMQP`  一个提供统一消息服务的应用层标准高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计。

`AMQP`是一个二进制协议，拥有一些现代化特点：`多信道`、`协商式`，`异步`，`安全`，`扩平台`，`中立`，`高效`。

RabbitMQ是`AMQP`协议的`Erlang`的实现。

| 概念           | 说明                                                         |
| -------------- | ------------------------------------------------------------ |
| 连接Connection | 一个网络连接，比如TCP/IP套接字连接。                         |
| 会话Session    | 端点之间的命名对话。在一个会话上下文中，保证“恰好传递一次”。 |
| 信道Channel    | 多路复用连接中的一条独立的双向数据流通道。为会话提供物理传输介质。 |
| 客户端Client   | AMQP连接或者会话的发起者。AMQP是非对称的，客户端生产和消费消息，服务器存储和路由这些消息。 |
| 服务节点Broker | 消息中间件的服务节点；一般情况下可以将一个RabbitMQ Broker看作一台RabbitMQ 服务器。 |
| 端点           | AMQP对话的任意一方。一个AMQP连接包括两个端点（一个是客户端，一个是服务器）。 |
| 消费者Consumer | 一个从消息队列里请求消息的客户端程序。                       |
| 生产者Producer | 一个向交换机发布消息的客户端应用程序。                       |

## 4.2. RabbitMQ运转流程

在入门案例中

- 生产者发送消息
  1. 生产者创建连接`Connection`，开启一个信道`Channel`，连接到服务节点`RabbitMQ Broker`；
  2. 声明队列并设置属性；如是否排它，是否持久化，是否自动删除；
  3. 将路由键（空字符串）与队列绑定起来；
  4. 发送消息至`RabbitMQ Broker`；
  5. 关闭信道；
  6. 关闭连接；
- 消费者接收消息
  1. 消费者创建连接`Connection`，开启一个信道`Channel`，连接到`RabbitMQ Broker`
  2. 向`Broker` 请求消费相应队列中的消息，设置相应的回调函数；
  3. 等待`Broker`回应闭关投递响应队列中的消息，消费者接收消息；
  4. 确认（`ack，自动确认`）接收到的消息；
  5. RabbitMQ从队列中删除相应已经被确认的消息；
  6. 关闭信道；
  7. 关闭连接；

![1565105223969](assets/1565105223969.png)

## 4.3. 生产者流转过程说明

1. 客户端与代理服务器`Broker`建立连接。会调用`newConnection()`方法,这个方法会进一步封装`Protocol Header 0-9-1` 的报文头发送给`Broker` ，以此通知`Broker` 本次交互采用的是`AMQPO-9-1` 协议，紧接着`Broker` 返回`Connection.Start` 来建立连接，在连接的过程中涉及`Connection.Start/.Start-OK` 、`Connection.Tune/.Tune-Ok` ，`Connection.Open/ .Open-Ok` 这6 个命令的交互。
2. 客户端调用`connection.createChannel`方法。此方法开启信道，其包装的`channel.open`命令发送给`Broker`,等待`channel.basicPublish`方法，对应的`AMQP`命令为`Basic.Publish`,这个命令包含了`content Header` 和`content Body()`。`content Header` 包含了消息体的属性，例如:投递模式，优先级等，`content Body` 包含了消息体本身。
3. 客户端发送完消息需要关闭资源时，涉及到`Channel.Close`和`Channl.Close-Ok` 与`Connetion.Close`和`Connection.Close-Ok`的命令交互。

![生产者流转过程图](assets/生产者流转过程图.bmp)

## 4.4. 消费者流转过程说明

1. 消费者客户端与代理服务器`Broker`建立连接。会调用`newConnection()` 方法,这个方法会进一步封装`Protocol Header 0-9-1` 的报文头发送给`Broker` ，以此通知`Broker` 本次交互采用的是`AMQPO-9-1` 协议，紧接着`Broker` 返回`Connection.Start` 来建立连接，在连接的过程中涉及`Connection.Start/.Start-OK` 、`Connection.Tune/.Tune-Ok` ，`Connection.Open/.Open-Ok` 这6 个命令的交互。
2. 消费者客户端调用`connection.createChannel`方法。和生产者客户端一样，协议涉及`Channel . Open/Open-Ok`命令。
3. 在真正消费之前，消费者客户端需要向`Broker` 发送`Basic.Consume` 命令(即调用`channel.basicConsume` 方法〉将`Channel` 置为接收模式，之后`Broker` 回执`Basic . Consume - Ok` 以告诉消费者客户端准备好消费消息。
4. `Broker` 向消费者客户端推送(Push) 消息，即`Basic.Deliver` 命令，这个命令和`Basic.Publish` 命令一样会携带`Content Header` 和`Content Body`。
5. 消费者接收到消息并正确消费之后，向`Broker` 发送确认，即`Basic.Ack` 命令。
6. 客户端发送完消息需要关闭资源时，涉及到`Channel.Close`和`Channl.Close-Ok `与`Connetion.Close`和`Connection.Close-Ok`的命令交互。

![消费者流转过程图](assets/消费者流转过程图.bmp)

# 5. RabbitMQ工作模式

## 4.1. Work queues工作队列模式

### 4.1.1. 模式说明

![1556009144848](assets/1556009144848.png)

`Work Queues`与入门程序的`简单模式`相比，多了一个或一些消费端，多个消费端共同消费同一个队列中的消息。

**应用场景**：对于 任务过重或任务较多情况使用工作队列可以提高任务处理的速度。

### 4.1.2. 代码

`Work Queues`与入门程序的`简单模式`的代码是几乎一样的；可以完全复制，并复制多一个消费者进行多个消费者同时消费消息的测试。

#### 1）生产者

```java
package tk.deriwotua.mq.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import tk.deriwotua.mq.utils.ConnectionUtils;

/**
 * 生产者
 */
public class Producer {

    static final String QUEUE_NAME = "simple_queue";

    public static void main(String[] args) throws Exception {
        //创建连接
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();

        /**
         * 声明（创建）队列
         *   参数1：队列名称
         *   参数2：是否定义持久化队列
         *   参数3：是否独占本次连接
         *   参数4：是否在不使用的时候自动删除队列
         *   参数5：队列其它参数
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        for (int i = 1; i <= 30; i++) {
            // 发送信息
            String message = "你好；小兔子！work模式--" + i;
            /**
             * 参数1：交换机名称，如果没有指定则使用默认Default Exchage
             * 参数2：路由key,简单模式可以传递队列名称
             * 参数3：消息其它属性
             * 参数4：消息内容
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("已发送消息：" + message);
        }

        // 关闭资源
        channel.close();
        connection.close();
    }
}
```

#### 2）消费者1

```java
package tk.deriwotua.mq.work;

import com.rabbitmq.client.*;
import tk.deriwotua.mq.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 消费者
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        final Channel channel = connection.createChannel();

        /**
         * 声明（创建）队列
         *   参数1：队列名称
         *   参数2：是否定义持久化队列
         *   参数3：是否独占本次连接
         *   参数4：是否在不使用的时候自动删除队列
         *   参数5：队列其它参数
         */
        channel.queueDeclare(Producer.QUEUE_NAME, true, false, false, null);

        //一次只能接收并处理一个消息
        channel.basicQos(1);

        //创建消费者；并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            /**
             * consumerTag 消息者标签，在channel.basicConsume时候可以指定
             * envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * properties 属性信息
             * body 消息
             */
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    //路由key
                    System.out.println("路由key为：" + envelope.getRoutingKey());
                    //交换机
                    System.out.println("交换机为：" + envelope.getExchange());
                    //消息id
                    System.out.println("消息id为：" + envelope.getDeliveryTag());
                    //收到的消息
                    System.out.println("消费者1-接收到的消息为：" + new String(body, "utf-8"));
                    Thread.sleep(1000);

                    //确认消息
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        
        /**
         * 监听消息
         * 参数1：队列名称
         * 参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动确认
         * 参数3：消息接收到后回调
         */
        channel.basicConsume(Producer.QUEUE_NAME, false, consumer);
    }
}
```

#### 3）消费者2

```java
package tk.deriwotua.mq.work;

import com.rabbitmq.client.*;
import tk.deriwotua.mq.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 消费者
 */
public class ConsumerTwo {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        final Channel channel = connection.createChannel();

        // 声明（创建）队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(Producer.QUEUE_NAME, true, false, false, null);

        //一次只能接收并处理一个消息
        channel.basicQos(1);

        //创建消费者；并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            /**
             * consumerTag 消息者标签，在channel.basicConsume时候可以指定
             * envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * properties 属性信息
             * body 消息
             */
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    //路由key
                    System.out.println("路由key为：" + envelope.getRoutingKey());
                    //交换机
                    System.out.println("交换机为：" + envelope.getExchange());
                    //消息id
                    System.out.println("消息id为：" + envelope.getDeliveryTag());
                    //收到的消息
                    System.out.println("消费者2-接收到的消息为：" + new String(body, "utf-8"));
                    Thread.sleep(1000);

                    //确认消息
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //监听消息
        /**
         * 参数1：队列名称
         * 参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动确认
         * 参数3：消息接收到后回调
         */
        channel.basicConsume(Producer.QUEUE_NAME, false, consumer);
    }
}
```



### 4.1.3. 测试

启动两个消费者，然后再启动生产者发送消息；到IDEA的两个消费者对应的控制台查看是否竞争性的接收到消息。

![1556014310859](assets/1556014310859.png)

![1556014318106](assets/1556014318106.png)

### 4.1.4. 小结

在一个队列中如果有多个消费者，那么消费者之间对于同一个消息的关系是**竞争**的关系。

## 4.2. 订阅模式类型

订阅模式示例图：

![1556014499573](assets/1556014499573.png)

前面2个案例中，只有3个角色
- `P`：生产者，也就是要发送消息的程序
- `C`：消费者：消息的接受者，会一直等待消息到来。
- `queue`：消息队列，图中红色部分

而在订阅模型中，多了一个`exchange`角色，而且过程略有变化
- `P`：生产者，也就是要发送消息的程序，但是不再发送到队列中，而是发给`X`（交换机）
- `C`：消费者，消息的接受者，会一直等待消息到来。
- `Queue`：消息队列，接收消息、缓存消息。
- `Exchange`：交换机，图中的`X`。一方面，接收生产者发送的消息。另一方面，知道如何处理消息，例如`递交给某个特别队列`、`递交给所有队列`、`或是将消息丢弃`。到底如何操作，取决于`Exchange`的类型。`Exchange`有常见以下3种类型
  - `Fanout`：广播，将消息交给所有绑定到交换机的队列
  - `Direct`：定向，把消息交给符合指定`routing key` 的队列
  - `Topic`：通配符，把消息交给符合`routing pattern`（路由模式） 的队列

**Exchange（交换机）只负责转发消息，不具备存储消息的能力**，因此如果没有任何队列与`Exchange`绑定，或者没有符合路由规则的队列，那么消息会丢失！

## 4.3. Publish/Subscribe发布与订阅模式

### 4.3.1. 模式说明

![1556010329032](assets/1556010329032.png)

发布订阅模式
1、每个消费者监听自己的队列。
2、生产者将消息发给`broker`，由`交换机`将消息转发到绑定此交换机的每个队列，每个绑定交换机的队列都将接收到消息

### 4.3.2. 代码

#### 1）生产者

```java
package tk.deriwotua.mq.publishandsubscribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import tk.deriwotua.mq.utils.ConnectionUtils;

/**
 * 发布与订阅使用的交换机类型为：fanout
 */
public class Producer {

    //交换机名称
    static final String FANOUT_EXCHANGE = "fanout_exchange";
    //队列名称
    static final String FANOUT_QUEUE_1 = "fanout_queue_1";
    //队列名称
    static final String FANOUT_QUEUE_2 = "fanout_queue_2";

    public static void main(String[] args) throws Exception {

        //创建连接
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();

        /**
         * 声明交换机
         *   参数1：交换机名称
         *   参数2：交换机类型，fanout、topic、direct、headers
         */
        channel.exchangeDeclare(FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);

        // 声明（创建）队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(FANOUT_QUEUE_1, true, false, false, null);
        channel.queueDeclare(FANOUT_QUEUE_2, true, false, false, null);

        //队列绑定交换机
        channel.queueBind(FANOUT_QUEUE_1, FANOUT_EXCHANGE, "");
        channel.queueBind(FANOUT_QUEUE_2, FANOUT_EXCHANGE, "");

        for (int i = 1; i <= 10; i++) {
            // 发送信息
            String message = "你好；小兔子！发布订阅模式--" + i;
            /**
             * 参数1：交换机名称，如果没有指定则使用默认Default Exchage
             * 参数2：路由key,简单模式可以传递队列名称
             * 参数3：消息其它属性
             * 参数4：消息内容
             */
            channel.basicPublish(FANOUT_EXCHANGE, "", null, message.getBytes());
            System.out.println("已发送消息：" + message);
        }

        // 关闭资源
        channel.close();
        connection.close();
    }
}
```

#### 2）消费者1

```java
package tk.deriwotua.mq.publishandsubscribe;

import com.rabbitmq.client.*;
import tk.deriwotua.mq.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 消费者
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();

        //声明交换机
        channel.exchangeDeclare(Producer.FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);

        // 声明（创建）队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(Producer.FANOUT_QUEUE_1, true, false, false, null);

        //队列绑定交换机
        channel.queueBind(Producer.FANOUT_QUEUE_1, Producer.FANOUT_EXCHANGE, "");

        //创建消费者；并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            /**
             * consumerTag 消息者标签，在channel.basicConsume时候可以指定
             * envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * properties 属性信息
             * body 消息
             */
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //路由key
                System.out.println("路由key为：" + envelope.getRoutingKey());
                //交换机
                System.out.println("交换机为：" + envelope.getExchange());
                //消息id
                System.out.println("消息id为：" + envelope.getDeliveryTag());
                //收到的消息
                System.out.println("消费者1-接收到的消息为：" + new String(body, "utf-8"));
            }
        };
        //监听消息
        /**
         * 参数1：队列名称
         * 参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动确认
         * 参数3：消息接收到后回调
         */
        channel.basicConsume(Producer.FANOUT_QUEUE_1, true, consumer);
    }
}
```

#### 3）消费者2

```java
package tk.deriwotua.mq.publishandsubscribe;

import com.rabbitmq.client.*;
import tk.deriwotua.mq.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 消费者
 */
public class ConsumerTwo {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();

        //声明交换机
        channel.exchangeDeclare(Producer.FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);

        // 声明（创建）队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(Producer.FANOUT_QUEUE_2, true, false, false, null);

        //队列绑定交换机
        channel.queueBind(Producer.FANOUT_QUEUE_2, Producer.FANOUT_EXCHANGE, "");

        //创建消费者；并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            /**
             * consumerTag 消息者标签，在channel.basicConsume时候可以指定
             * envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * properties 属性信息
             * body 消息
             */
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //路由key
                System.out.println("路由key为：" + envelope.getRoutingKey());
                //交换机
                System.out.println("交换机为：" + envelope.getExchange());
                //消息id
                System.out.println("消息id为：" + envelope.getDeliveryTag());
                //收到的消息
                System.out.println("消费者2-接收到的消息为：" + new String(body, "utf-8"));
            }
        };
        //监听消息
        /**
         * 参数1：队列名称
         * 参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动确认
         * 参数3：消息接收到后回调
         */
        channel.basicConsume(Producer.FANOUT_QUEUE_2, true, consumer);
    }
}
```

### 4.3.3. 测试

启动所有消费者，然后使用生产者发送消息；在每个消费者对应的控制台可以查看到生产者发送的所有消息；到达**广播**的效果。

在执行完测试代码后，其实到RabbitMQ的管理后台找到`Exchanges`选项卡，点击 `fanout_exchange` 的交换机，可以查看到如下的绑定：

![1556015006220](assets/1556015006220.png)

### 4.3.4. 小结

交换机需要与队列进行绑定，绑定之后；一个消息可以被多个消费者都收到。

**发布订阅模式与工作队列模式的区别**

1、工作队列模式不用定义交换机，而发布/订阅模式需要定义交换机。 

2、发布/订阅模式的生产方是面向交换机发送消息，工作队列模式的生产方是面向队列发送消息(底层使用默认交换机)。

3、发布/订阅模式需要设置队列和交换机的绑定，工作队列模式不需要设置，实际上工作队列模式会将队列绑 定到默认的交换机 。

## 4.4. Routing路由模式

### 4.4.1. 模式说明

路由模式特点
- 队列与交换机的绑定，不能是任意绑定了，而是要指定一个`RoutingKey`（路由key）
- 消息的发送方在 向 `Exchange`发送消息时，也必须指定消息的 `RoutingKey`。
- `Exchange`不再把消息交给每一个绑定的队列，而是根据消息的`Routing Key`进行判断，只有队列的`Routingkey`与消息的 `Routing key`完全一致，才会接收到消息

![1556029284397](assets/1556029284397.png)

图解
- `P`：生产者，向`Exchange`发送消息，发送消息时，会指定一个`routing key`。
- `X`：`Exchange`（交换机），接收生产者的消息，然后把消息递交给 与`routing key`完全匹配的队列
- `C1`：消费者，其所在队列指定了需要`routing key` 为 `error` 的消息
- `C2`：消费者，其所在队列指定了需要`routing key` 为 `info`、`error`、`warning` 的消息

### 4.4.2. 代码

在编码上与 `Publish/Subscribe发布与订阅模式` 的区别是交换机的类型为：`Direct`，还有队列绑定交换机的时候需要指定`routing key`。

#### 1）生产者

```java
package tk.deriwotua.mq.routing;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import tk.deriwotua.mq.utils.ConnectionUtils;

/**
 * 路由模式的交换机类型为：direct
 */
public class Producer {

    //交换机名称
    static final String DIRECT_EXCHANGE = "direct_exchange";
    //队列名称
    static final String DIRECT_QUEUE_INSERT = "direct_queue_insert";
    //队列名称
    static final String DIRECT_QUEUE_UPDATE = "direct_queue_update";

    public static void main(String[] args) throws Exception {
        //创建连接
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();

        /**
         * 声明交换机
         * 参数1：交换机名称
         * 参数2：交换机类型，fanout、topic、direct、headers
         */
        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明（创建）队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(DIRECT_QUEUE_INSERT, true, false, false, null);
        channel.queueDeclare(DIRECT_QUEUE_UPDATE, true, false, false, null);

        //队列绑定交换机
        channel.queueBind(DIRECT_QUEUE_INSERT, DIRECT_EXCHANGE, "insert");
        channel.queueBind(DIRECT_QUEUE_UPDATE, DIRECT_EXCHANGE, "update");

        // 发送信息
        String message = "新增了商品。路由模式；routing key 为 insert " ;
        /**
         * 参数1：交换机名称，如果没有指定则使用默认Default Exchage
         * 参数2：路由key,简单模式可以传递队列名称
         * 参数3：消息其它属性
         * 参数4：消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE, "insert", null, message.getBytes());
        System.out.println("已发送消息：" + message);

        // 发送信息
        message = "修改了商品。路由模式；routing key 为 update" ;
        /**
         * 参数1：交换机名称，如果没有指定则使用默认Default Exchage
         * 参数2：路由key,简单模式可以传递队列名称
         * 参数3：消息其它属性
         * 参数4：消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE, "update", null, message.getBytes());
        System.out.println("已发送消息：" + message);

        // 关闭资源
        channel.close();
        connection.close();
    }
}
```
#### 2）消费者1

```java
package tk.deriwotua.mq.routing;


import com.rabbitmq.client.*;
import tk.deriwotua.mq.utils.ConnectionUtils;

import java.io.IOException;

public class Consumer {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();

        //声明交换机
        channel.exchangeDeclare(Producer.DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明（创建）队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(Producer.DIRECT_QUEUE_INSERT, true, false, false, null);

        //队列绑定交换机
        channel.queueBind(Producer.DIRECT_QUEUE_INSERT, Producer.DIRECT_EXCHANGE, "insert");

        //创建消费者；并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            /**
             * consumerTag 消息者标签，在channel.basicConsume时候可以指定
             * envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * properties 属性信息
             * body 消息
             */
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //路由key
                System.out.println("路由key为：" + envelope.getRoutingKey());
                //交换机
                System.out.println("交换机为：" + envelope.getExchange());
                //消息id
                System.out.println("消息id为：" + envelope.getDeliveryTag());
                //收到的消息
                System.out.println("消费者1-接收到的消息为：" + new String(body, "utf-8"));
            }
        };
        //监听消息
        /**
         * 参数1：队列名称
         * 参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动确认
         * 参数3：消息接收到后回调
         */
        channel.basicConsume(Producer.DIRECT_QUEUE_INSERT, true, consumer);
    }
}
```
#### 3）消费者2

```java
package tk.deriwotua.mq.routing;

import com.rabbitmq.client.*;
import tk.deriwotua.mq.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 消费者
 */
public class ConsumerTwo {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();

        //声明交换机
        channel.exchangeDeclare(Producer.DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明（创建）队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(Producer.DIRECT_QUEUE_UPDATE, true, false, false, null);

        //队列绑定交换机
        channel.queueBind(Producer.DIRECT_QUEUE_UPDATE, Producer.DIRECT_EXCHANGE, "update");

        //创建消费者；并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            /**
             * consumerTag 消息者标签，在channel.basicConsume时候可以指定
             * envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * properties 属性信息
             * body 消息
             */
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //路由key
                System.out.println("路由key为：" + envelope.getRoutingKey());
                //交换机
                System.out.println("交换机为：" + envelope.getExchange());
                //消息id
                System.out.println("消息id为：" + envelope.getDeliveryTag());
                //收到的消息
                System.out.println("消费者2-接收到的消息为：" + new String(body, "utf-8"));
            }
        };
        //监听消息
        /**
         * 参数1：队列名称
         * 参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动确认
         * 参数3：消息接收到后回调
         */
        channel.basicConsume(Producer.DIRECT_QUEUE_UPDATE, true, consumer);
    }
}
```

### 4.4.3. 测试

启动所有消费者，然后使用生产者发送消息；在消费者对应的控制台可以查看到生产者发送对应routing key对应队列的消息；到达**按照需要接收**的效果。

在执行完测试代码后，其实到RabbitMQ的管理后台找到`Exchanges`选项卡，点击 `direct_exchange` 的交换机，可以查看到如下的绑定：

![1556031175744](assets/1556031175744.png)

### 4.4.4. 小结

Routing模式要求队列在绑定交换机时要指定`routing key`，消息会转发到符合`routing key`的队列。

## 4.5. Topics通配符模式

### 4.5.1. 模式说明

`Topic`类型与`Direct`相比，都是可以根据`RoutingKey`把消息路由到不同的队列。只不过`Topic`类型`Exchange`可以让队列在绑定`Routing key` 的时候**使用通配符**！

`Routingkey` 一般都是有一个或多个单词组成，多个单词之间以”.”分割，例如： `item.insert`

通配符规则
- `#`：匹配一个或多个词
  - `item.#`：能够匹配`item.insert.abc` 或者 `item.insert`
- `*`：匹配不多不少恰好1个词
  - `item.*`：只能匹配`item.insert`

![1556031362048](assets/1556031362048.png)

![1556031519931](assets/1556031519931.png)

图解
- `红色Queue`：绑定的是`usa.#` ，因此凡是以 `usa.`开头的`routing key` 都会被匹配到
- `黄色Queue`：绑定的是`#.news` ，因此凡是以 `.news`结尾的 `routing key` 都会被匹配



### 4.5.2. 代码

#### 1）生产者

使用`topic`类型的`Exchange`，发送消息的`routing key`有3种： `item.insert`、`item.update`、`item.delete`：

```java
package tk.deriwotua.mq.topics;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 通配符Topic的交换机类型为：topic
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {

        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2. 设置参数
        factory.setHost("localhost");//ip  默认值 localhost
        factory.setPort(5672); //端口  默认值 5672
        factory.setVirtualHost("/deriwotua");//虚拟机 默认值/
        factory.setUsername("guest");//用户名 默认 guest
        factory.setPassword("guest");//密码 默认值 guest
        //3. 创建连接 Connection
        Connection connection = factory.newConnection();
        //4. 创建Channel
        Channel channel = connection.createChannel();
       /*
        * exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean
        *        internal, Map<String, Object> arguments)
        * 参数：
        *  1. exchange:交换机名称
        *  2. type:交换机类型
        *      DIRECT("direct"),：定向
        *      FANOUT("fanout"),：扇形（广播），发送消息到每一个与之绑定队列。
        *      TOPIC("topic"),通配符的方式
        *      HEADERS("headers");参数匹配
 *
        *  3. durable:是否持久化
        *  4. autoDelete:自动删除
        *  5. internal：内部使用。 一般false
        *  6. arguments：参数
        */
        String exchangeName = "test_topic";
        //5. 创建交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC,true,false,false,null);
        //6. 创建队列
        String queue1Name = "test_topic_queue1";
        String queue2Name = "test_topic_queue2";
        channel.queueDeclare(queue1Name,true,false,false,null);
        channel.queueDeclare(queue2Name,true,false,false,null);
        //7. 绑定队列和交换机
        /*
         * queueBind(String queue, String exchange, String routingKey)
         * 参数：
         *     1. queue：队列名称
         *     2. exchange：交换机名称
         *     3. routingKey：路由键，绑定规则
         *         如果交换机的类型为fanout ，routingKey设置为""
         */
        // routing key  系统的名称.日志的级别。
        //=需求： 所有error级别的日志存入数据库，所有order系统的日志存入数据库
        channel.queueBind(queue1Name,exchangeName,"#.error");
        channel.queueBind(queue1Name,exchangeName,"order.*");
        channel.queueBind(queue2Name,exchangeName,"*.*");

        String body = "日志信息：张三调用了findAll方法...日志级别：info...";
        //8. 发送消息
        channel.basicPublish(exchangeName,"goods.error",null,body.getBytes());
        channel.basicPublish(exchangeName,"order.goods",null,body.getBytes());

        //9. 释放资源
        channel.close();
        connection.close();

    }
}
```
#### 2）消费者1

将日志信息存入数据库
```java
package tk.deriwotua.mq.topics;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_Topic1 {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2. 设置参数
        factory.setHost("localhost");//ip  默认值 localhost
        factory.setPort(5672); //端口  默认值 5672
        factory.setVirtualHost("/deriwotua");//虚拟机 默认值/
        factory.setUsername("guest");//用户名 默认 guest
        factory.setPassword("guest");//密码 默认值 guest
        //3. 创建连接 Connection
        Connection connection = factory.newConnection();
        //4. 创建Channel
        Channel channel = connection.createChannel();

        String queue1Name = "test_topic_queue1";
        String queue2Name = "test_topic_queue2";

        // 接收消息
        Consumer consumer = new DefaultConsumer(channel){
            /*
             *   回调方法，当收到消息后，会自动执行该方法
             *   1. consumerTag：标识
             *   2. envelope：获取一些信息，交换机，路由key...
             *   3. properties:配置信息
             *   4. body：数据
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
              /*  System.out.println("consumerTag："+consumerTag);
                System.out.println("Exchange："+envelope.getExchange());
                System.out.println("RoutingKey："+envelope.getRoutingKey());
                System.out.println("properties："+properties);*/
                System.out.println("body："+new String(body));
                System.out.println("将日志信息存入数据库.......");
            }
        };

        /*
         * basicConsume(String queue, boolean autoAck, Consumer callback)
         * 参数：
         *     1. queue：队列名称
         *     2. autoAck：是否自动确认
         *     3. callback：回调对象
         */
        channel.basicConsume(queue1Name,true, consumer);

        //关闭资源？不要
    }
}
```
#### 3）消费者2

将日志信息打印控制台

```java
package tk.deriwotua.mq.topics;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_Topic2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2. 设置参数
        factory.setHost("localhost");//ip  默认值 localhost
        factory.setPort(5672); //端口  默认值 5672
        factory.setVirtualHost("/deriwotua");//虚拟机 默认值/
        factory.setUsername("guest");//用户名 默认 guest
        factory.setPassword("guest");//密码 默认值 guest
        //3. 创建连接 Connection
        Connection connection = factory.newConnection();
        //4. 创建Channel
        Channel channel = connection.createChannel();

        String queue1Name = "test_topic_queue1";
        String queue2Name = "test_topic_queue2";

        // 接收消息
        Consumer consumer = new DefaultConsumer(channel){
            /*
                回调方法，当收到消息后，会自动执行该方法

                1. consumerTag：标识
                2. envelope：获取一些信息，交换机，路由key...
                3. properties:配置信息
                4. body：数据

             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
              /*  System.out.println("consumerTag："+consumerTag);
                System.out.println("Exchange："+envelope.getExchange());
                System.out.println("RoutingKey："+envelope.getRoutingKey());
                System.out.println("properties："+properties);*/
                System.out.println("body："+new String(body));
                System.out.println("将日志信息打印控制台.......");
            }
        };

        /*
         * basicConsume(String queue, boolean autoAck, Consumer callback)
         * 参数：
         *     1. queue：队列名称
         *     2. autoAck：是否自动确认
         *     3. callback：回调对象
         */
        channel.basicConsume(queue2Name,true,consumer);

        //关闭资源？不要
    }
}
```

### 4.5.3. 测试

启动所有消费者，然后使用生产者发送消息；在消费者对应的控制台可以查看到生产者发送对应`routing key`对应队列的消息；到达**按照需要接收**的效果；并且这些`routing key`可以使用通配符。

在执行完测试代码后，其实到RabbitMQ的管理后台找到`Exchanges`选项卡，点击 `topic_exchange` 的交换机，可以查看到如下的绑定：

![1556032433333](assets/1556032433333.png)

### 4.5.4. 小结

Topic主题模式可以实现 `Publish/Subscribe发布与订阅模式` 和 ` Routing路由模式` 的功能；只是`Topic`在配置`routing key` 的时候可以使用通配符，显得更加灵活。

## 4.6. 模式总结

RabbitMQ工作模式
**1、简单模式 `HelloWorld`**
- 一个生产者、一个消费者，不需要设置交换机（使用默认的交换机）

**2、工作队列模式 `Work Queue`**
- 一个生产者、多个消费者（竞争关系），不需要设置交换机（使用默认的交换机）

**3、发布订阅模式 `Publish/subscribe`**
- 需要设置类型为`fanout`的交换机，并且交换机和队列进行绑定，当发送消息到交换机后，交换机会将消息发送到绑定的队列

**4、路由模式 `Routing`**
- 需要设置类型为`direct`的交换机，交换机和队列进行绑定，并且指定`routing key`，当发送消息到交换机后，交换机会根据`routing key`将消息发送到对应的队列

**5、通配符模式 `Topic`**
- 需要设置类型为`topic`的交换机，交换机和队列进行绑定，并且指定通配符方式的`routing key`，当发送消息到交换机后，交换机会根据`routing key`将消息发送到对应的队列


# 5. Spring 整合RabbitMQ

## 5.1. 搭建生产者工程

### 5.1.1. 创建工程

![1565149342994](assets/1565149342994.png)

![1565144326498](assets/1565144326498.png)

### 5.1.2. 添加依赖

修改pom.xml文件内容为如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>tk.deriwotua</groupId>
    <artifactId>spring-rabbitmq-producer</artifactId>
    <version>1.0-SNAPSHOT</version>


    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.7.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.amqp</groupId>
            <artifactId>spring-rabbit</artifactId>
            <version>2.1.8.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.1.7.RELEASE</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.0</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```



### 5.1.3. 配置整合

创建`rabbitmq.properties`连接参数等配置文件
```properties
rabbitmq.host=127.0.0.1
rabbitmq.port=5672
rabbitmq.username=guest
rabbitmq.password=guest
rabbitmq.virtual-host=/deriwotua
```

创建 `spring-rabbitmq-producer.xml` 整合配置文件；
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:rabbit="http://www.springframework.org/schema/rabbit"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/rabbit
       http://www.springframework.org/schema/rabbit/spring-rabbit.xsd">
    <!--加载配置文件-->
    <context:property-placeholder location="classpath:rabbitmq.properties"/>

    <!-- 定义rabbitmq connectionFactory -->
    <rabbit:connection-factory id="connectionFactory" host="${rabbitmq.host}"
                               port="${rabbitmq.port}"
                               username="${rabbitmq.username}"
                               password="${rabbitmq.password}"
                               virtual-host="${rabbitmq.virtual-host}"/>
    <!--定义管理交换机、队列-->
    <rabbit:admin connection-factory="connectionFactory"/>

    <!--定义持久化队列，不存在则自动创建；不绑定到交换机则绑定到默认交换机
        默认交换机类型为direct，名字为：""，路由键为队列的名称-->
    <!--
        id：bean的名称
        name：queue的名称
        auto-declare:自动创建
        auto-delete:自动删除。 最后一个消费者和该队列断开连接后，自动删除队列
        exclusive:是否独占
        durable：是否持久化
    -->
    <rabbit:queue id="spring_queue" name="spring_queue"    auto-declare="true"/>

    <!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~广播；所有队列都能收到消息~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_fanout_queue_1" name="spring_fanout_queue_1" auto-declare="true"/>

    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_fanout_queue_2" name="spring_fanout_queue_2" auto-declare="true"/>

    <!--定义广播类型交换机；并绑定上述两个队列-->
    <rabbit:fanout-exchange id="spring_fanout_exchange" name="spring_fanout_exchange"  auto-declare="true">
        <rabbit:bindings>
            <rabbit:binding  queue="spring_fanout_queue_1"  />
            <rabbit:binding queue="spring_fanout_queue_2"/>
        </rabbit:bindings>
    </rabbit:fanout-exchange>

    <!--定义路由类型交换机；并绑定上述两个队列-->
    <!--<rabbit:direct-exchange name="aa" >
        <rabbit:bindings>
            &lt;!&ndash;direct 类型的交换机绑定队列  key ：路由key  queue：队列名称&ndash;&gt;
            <rabbit:binding queue="spring_queue" key="xxx"></rabbit:binding>
        </rabbit:bindings>
    </rabbit:direct-exchange>-->

    <!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~通配符；*匹配一个单词，#匹配多个单词 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_topic_queue_star" name="spring_topic_queue_star"  auto-declare="true"/>
    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_topic_queue_well" name="spring_topic_queue_well" auto-declare="true"/>
    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_topic_queue_well2" name="spring_topic_queue_well2" auto-declare="true"/>

    <rabbit:topic-exchange id="spring_topic_exchange"  name="spring_topic_exchange" auto-declare="true">
        <rabbit:bindings>
            <rabbit:binding pattern="heima.*"  queue="spring_topic_queue_star"/>
            <rabbit:binding pattern="heima.#" queue="spring_topic_queue_well"/>
            <rabbit:binding pattern="deriwotua.#" queue="spring_topic_queue_well2"/>
        </rabbit:bindings>
    </rabbit:topic-exchange>

    <!--定义rabbitTemplate对象操作可以在代码中方便发送消息-->
    <rabbit:template id="rabbitTemplate" connection-factory="connectionFactory"/>
</beans>
```

### 5.1.4. 发送消息

创建测试文件 `ProducerTest.java`
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-rabbitmq.xml")
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 只发队列消息
     * 默认交换机类型为 direct
     * 交换机的名称为空，路由键为队列的名称
     */
    @Test
    public void queueTest(){
        //路由键与队列同名
        rabbitTemplate.convertAndSend("spring_queue", "只发队列spring_queue的消息。");
    }

    /**
     * 发送广播
     * 交换机类型为 fanout
     * 绑定到该交换机的所有队列都能够收到消息
     */
    @Test
    public void fanoutTest(){
        /**
         * 参数1：交换机名称
         * 参数2：路由键名（广播设置为空）
         * 参数3：发送的消息内容
         */
        rabbitTemplate.convertAndSend("spring_fanout_exchange", "", "发送到spring_fanout_exchange交换机的广播消息");
    }

    /**
     * 通配符
     * 交换机类型为 topic
     * 匹配路由键的通配符，*表示一个单词，#表示多个单词
     * 绑定到该交换机的匹配队列能够收到对应消息
     */
    @Test
    public void topicTest(){
        /**
         * 参数1：交换机名称
         * 参数2：路由键名
         * 参数3：发送的消息内容
         */
        rabbitTemplate.convertAndSend("spring_topic_exchange", "heima.bj", "发送到spring_topic_exchange交换机heima.bj的消息");
        rabbitTemplate.convertAndSend("spring_topic_exchange", "heima.bj.1", "发送到spring_topic_exchange交换机heima.bj.1的消息");
        rabbitTemplate.convertAndSend("spring_topic_exchange", "heima.bj.2", "发送到spring_topic_exchange交换机heima.bj.2的消息");
        rabbitTemplate.convertAndSend("spring_topic_exchange", "deriwotua.cn", "发送到spring_topic_exchange交换机deriwotua.cn的消息");
    }
}
```

## 5.2. 搭建消费者工程

### 5.2.1. 创建工程

![1565149374831](assets/1565149374831.png)

![1565144775216](assets/1565144775216.png)

### 5.2.2. 添加依赖
修改`pom.xml`文件内容为如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>tk.deriwotua</groupId>
    <artifactId>spring-rabbitmq-consumer</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.7.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.amqp</groupId>
            <artifactId>spring-rabbit</artifactId>
            <version>2.1.8.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.1.7.RELEASE</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.0</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### 5.2.3. 配置整合
创建`rabbitmq.properties`连接参数等配置文件；
```properties
rabbitmq.host=127.0.0.1
rabbitmq.port=5672
rabbitmq.username=guest
rabbitmq.password=guest
rabbitmq.virtual-host=/deriwotua
```

创建 `spring-rabbitmq-consumer.xml` 整合配置文件；
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:rabbit="http://www.springframework.org/schema/rabbit"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/rabbit
       http://www.springframework.org/schema/rabbit/spring-rabbit.xsd">
    <!--加载配置文件-->
    <context:property-placeholder location="classpath:rabbitmq.properties"/>

    <!-- 定义rabbitmq connectionFactory -->
    <rabbit:connection-factory id="connectionFactory" host="${rabbitmq.host}"
                               port="${rabbitmq.port}"
                               username="${rabbitmq.username}"
                               password="${rabbitmq.password}"
                               virtual-host="${rabbitmq.virtual-host}"/>

    <bean id="springQueueListener" class="tk.deriwotua.mq.listener.SpringQueueListener"/>
    <!--<bean id="fanoutListener1" class="tk.deriwotua.mq.listener.FanoutListener1"/>
    <bean id="fanoutListener2" class="tk.deriwotua.mq.listener.FanoutListener2"/>
    <bean id="topicListenerStar" class="tk.deriwotua.mq.listener.TopicListenerStar"/>
    <bean id="topicListenerWell" class="tk.deriwotua.mq.listener.TopicListenerWell"/>
    <bean id="topicListenerWell2" class="tk.deriwotua.mq.listener.TopicListenerWell2"/>-->

    <rabbit:listener-container connection-factory="connectionFactory" auto-declare="true">
        <rabbit:listener ref="springQueueListener" queue-names="spring_queue"/>
       <!-- <rabbit:listener ref="fanoutListener1" queue-names="spring_fanout_queue_1"/>
        <rabbit:listener ref="fanoutListener2" queue-names="spring_fanout_queue_2"/>
        <rabbit:listener ref="topicListenerStar" queue-names="spring_topic_queue_star"/>
        <rabbit:listener ref="topicListenerWell" queue-names="spring_topic_queue_well"/>
        <rabbit:listener ref="topicListenerWell2" queue-names="spring_topic_queue_well2"/>-->
    </rabbit:listener-container>
</beans>
```

### 5.2.4. 消息监听器

#### 1）队列监听器

创建 `SpringQueueListener.java`
```java
package tk.deriwotua.mq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class SpringQueueListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            String msg = new String(message.getBody(), "utf-8");

            System.out.printf("接收路由名称为：%s，路由键为：%s，队列名为：%s的消息：%s \n",
                    message.getMessageProperties().getReceivedExchange(),
                    message.getMessageProperties().getReceivedRoutingKey(),
                    message.getMessageProperties().getConsumerQueue(),
                    msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 2）广播监听器1
创建 `FanoutListener1.java`

```java
package tk.deriwotua.mq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class FanoutListener1 implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            String msg = new String(message.getBody(), "utf-8");
            System.out.printf("广播监听器1：接收路由名称为：%s，路由键为：%s，队列名为：%s的消息：%s \n",
                    message.getMessageProperties().getReceivedExchange(),
                    message.getMessageProperties().getReceivedRoutingKey(),
                    message.getMessageProperties().getConsumerQueue(),
                    msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 3）广播监听器2
创建 `FanoutListener2.java`

```java
public class FanoutListener2 implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            String msg = new String(message.getBody(), "utf-8");

            System.out.printf("广播监听器2：接收路由名称为：%s，路由键为：%s，队列名为：%s的消息：%s \n",
                    message.getMessageProperties().getReceivedExchange(),
                    message.getMessageProperties().getReceivedRoutingKey(),
                    message.getMessageProperties().getConsumerQueue(),
                    msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 4）星号通配符监听器
创建 `TopicListenerStar.java`

```java
public class TopicListenerStar implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            String msg = new String(message.getBody(), "utf-8");

            System.out.printf("通配符*监听器：接收路由名称为：%s，路由键为：%s，队列名为：%s的消息：%s \n",
                    message.getMessageProperties().getReceivedExchange(),
                    message.getMessageProperties().getReceivedRoutingKey(),
                    message.getMessageProperties().getConsumerQueue(),
                    msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 5）井号通配符监听器
创建 `TopicListenerWell.java`
```java
public class TopicListenerWell implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            String msg = new String(message.getBody(), "utf-8");

            System.out.printf("通配符#监听器：接收路由名称为：%s，路由键为：%s，队列名为：%s的消息：%s \n",
                    message.getMessageProperties().getReceivedExchange(),
                    message.getMessageProperties().getReceivedRoutingKey(),
                    message.getMessageProperties().getConsumerQueue(),
                    msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 6）井号通配符监听器2
创建 `TopicListenerWell2.java`
```java
public class TopicListenerWell2 implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            String msg = new String(message.getBody(), "utf-8");

            System.out.printf("通配符#监听器2：接收路由名称为：%s，路由键为：%s，队列名为：%s的消息：%s \n",
                    message.getMessageProperties().getReceivedExchange(),
                    message.getMessageProperties().getReceivedRoutingKey(),
                    message.getMessageProperties().getConsumerQueue(),
                    msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

# 6. Spring Boot整合RabbitMQ

## 6.1. 简介

在Spring项目中，可以使用`Spring-Rabbit`去操作RabbitMQ `https://github.com/spring-projects/spring-amqp`

尤其是在spring boot项目中只需要引入对应的amqp启动器依赖即可，方便的使用`RabbitTemplate`发送消息，使用注解接收消息。


*一般在开发过程中*
- **生产者工程**
  - `application.yml`文件配置RabbitMQ相关信息
  - 在生产者工程中编写配置类，用于创建交换机和队列，并进行绑定
  - 注入`RabbitTemplate`对象，通过`RabbitTemplate`对象发送消息到交换机

- **消费者工程：**
  - `application.yml`文件配置RabbitMQ相关信息
  - 创建消息处理类，用于接收队列中的消息并进行处理

## 5.2. 搭建生产者工程

### 5.2.1. 创建工程

创建生产者工程springboot-producer

![1556072078816](assets/1558321303612.png)

![1556072084653](assets/1558321417385.png)

### 5.2.2. 添加依赖

修改pom.xml文件内容为如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
        <relativePath/>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>springboot-producer</artifactId>
    <groupId>tk.deriwotua</groupId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!--2. rabbitmq-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>

</project>
```

### 5.2.3. 启动类

```java
@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class);
    }
}
```

### 5.2.4. 配置RabbitMQ

#### 1）配置文件

创建`application.yml`
```yml
# 配置RabbitMQ的基本信息  ip 端口 username  password..
spring:
  rabbitmq:
    host: 127.0.0.1# ip
    port: 5672
    username: guest
    password: guest
    virtual-host: /
```

#### 2）绑定交换机和队列

创建RabbitMQ队列与交换机绑定的配置类`RabbitMQConfig`
```java
package tk.deriwotua.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置EXCHANGE_NAME、QUEUE_NAME 相当于给rabbitTemplate指定了默认交换机和队列
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "boot_topic_exchange";
    public static final String QUEUE_NAME = "boot_queue";

    //1.交换机
    @Bean("bootExchange")
    public Exchange bootExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    //2.Queue 队列
    @Bean("bootQueue")
    public Queue bootQueue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    /* 3. 队列和交互机绑定关系 Binding
     *   1. 知道哪个队列
     *   2. 知道哪个交换机
     *   3. routing key
     */
    @Bean
    public Binding bindQueueExchange(@Qualifier("bootQueue") Queue queue, 
                                     @Qualifier("bootExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }

}
```

## 5.3. 搭建消费者工程

### 5.3.1. 创建工程

创建消费者工程springboot-consumer

![1556073553841](assets/1558322109359.png)

![1556073567786](assets/1558322192435.png)

### 5.3.2. 添加依赖

修改`pom.xml`文件内容为如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
        <relativePath/>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <groupId>tk.deriwotua</groupId>
    <artifactId>springboot-consumer</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!--RabbitMQ 启动依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

### 5.3.3. 启动类

```java
@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class);
    }
}
```

### 5.3.4. 配置RabbitMQ

创建`application.yml`

```yml
spring:
  rabbitmq:
    host: 127.0.0.1 #主机ip
    port: 5672 #端口
    username: guest
    password: guest
    virtual-host: /
```

### 5.3.5. 消息监听处理类

编写消息监听器`MyListener`

```java
package tk.deriwotua.consumerspringboot.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    @RabbitListener(queues = "boot_queue")
    public void ListenerQueue(Message message){
        //System.out.println(message);
        System.out.println(new String(message.getBody()));
    }

}
```

## 5.4. 测试

在生产者工程springboot-producer中创建测试类，发送消息：

```java
package tk.deriwotua;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.deriwotua.mq.config.RabbitMQConfig;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTest {

    //1.注入RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSend(){

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,"boot.haha","boot mq hello~~~");
    }
}
```

先运行上述测试程序（交换机和队列才能先被声明和绑定），然后启动消费者；在消费者工程`springboot-consumer`中控制台查看是否接收到对应消息。

另外；也可以在RabbitMQ的管理控制台中查看到交换机与队列的绑定

![1556074827222](assets/1556074827222.png)

# 6. rabbitmq 高级特性 

[rabbitmq进阶](./rabbitmq高级特性.md)

