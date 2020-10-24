package tk.deriwotua.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.deriwotua.service.HelloService;
import tk.deriwotua.service.impl.HelloServiceImpl;

/**
 * auto config
 *
 * 运行 mvn:install 打包安装
 */
// 相当于一个普通的 java 配置类
@Configuration
// 当 HelloWorldService 在类路径的条件下
@ConditionalOnClass({HelloService.class})
// 将 application.properties 的相关的属性字段与该类一一对应，并生成 Bean
@EnableConfigurationProperties(HelloWorldProperties.class)
public class HelloWorldAutoConfiguration {

    private final HelloWorldProperties helloWorldProperties;

    // 注入属性类
    @Autowired
    public HelloWorldAutoConfiguration(HelloWorldProperties helloWorldProperties){
        this.helloWorldProperties = helloWorldProperties;
    }

    @Bean
    // 当容器没有这个 Bean 的时候才创建这个 Bean
    @ConditionalOnMissingBean(HelloService.class)
    // 当配置文件中 hello.world.enabled=true 时
    @ConditionalOnProperty(prefix = "hello.world", value = "enabled", havingValue = "true")
    public HelloService helloWorldService() {
        HelloService helloService = new HelloServiceImpl();
        helloService.setWords(helloWorldProperties.getWords());
        return helloService;
    }
}
