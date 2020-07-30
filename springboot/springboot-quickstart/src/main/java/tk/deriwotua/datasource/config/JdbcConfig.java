package tk.deriwotua.datasource.config;

/*// Configuration声明是一个配置类
@Configuration
// 指定属性文件的路径
@PropertySource("classpath:jdbc.properties")
public class JdbcConfig {
    *//**
     * 属性注入值
     *//*
    @Value("${jdbc.url}")
    String url;
    @Value("${jdbc.driverClassName}")
    String driverClassName;
    @Value("${jdbc.username}")
    String username;
    @Value("${jdbc.password}")
    String password;

    */

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
     * 方法声明为一个注册Bean的方法
     * Spring会自动调用该方法，将方法的返回值加入Spring容器中。然后就可以在任意位置通过 `@Autowired` 注入`DataSource`了
     *//*
    @Bean
    public DataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        return druidDataSource;
    }
}*/

/**
 * 第二种方式
 */
/*// 采用ConfigurationProperties注入
@Configuration
// 声明JdbcProperties配置类
@EnableConfigurationProperties(JdbcProperties.class)
public class JdbcConfig {

    *//**
     * dataSource
     * @param jdbcProperties Bean 参数注入
     * @return dataSource
     *//*
    @Bean
    public DataSource dataSource(JdbcProperties jdbcProperties){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(jdbcProperties.getUrl());
        druidDataSource.setDriverClassName(jdbcProperties.getDriverClassName());
        druidDataSource.setUsername(jdbcProperties.getUsername());
        druidDataSource.setPassword(jdbcProperties.getPassword());
        return druidDataSource;
    }

    *//**
     * ConfigurationProperties VS @value 优势
     * - `Relaxed binding`：松散绑定
     *   - 不严格要求属性文件中的属性名与成员变量名一致。支持驼峰，中划线，下划线等等转换，甚至支持对象引导。比如：`user.friend.name`代表的是user对象中的friend属性中的name属性，显然friend也是对象。`@value`注解就难以完成这样的注入方式。
     *   - `meta-data support`元数据支持，帮助IDE生成属性提示（写开源框架会用到）。
     *//*

}*/

/**
 * 第三种方式
 * 属性只有一个Bean需要使用，无需将其注入到一个类（JdbcProperties，将该类上的所有注解去掉）中。
 * 而是直接在需要的地方声明即可
 */
@Configuration
public class JdbcConfig {

    /**
     * 直接把 `@ConfigurationProperties(prefix = "jdbc")` 声明在需要使用的 `@Bean` 的方法上，
     * 然后SpringBoot就会自动调用这个`Bean`（此处是DataSource）的`set方法`，然后完成注入。
     * 使用的前提是该类必须有对应属性的`set方法`
     * @return
     */
    @Bean("jdbcDataSource")
    // 声明要注入的属性前缀，Spring Boot会自动把相关属性通过set方法注入到DataSource中
    @ConfigurationProperties(prefix = "jdbc")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

}
