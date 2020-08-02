package tk.deriwotua.consumer.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通过 `logging.level.xx=debug` 来设置日志级别。然而这个对Feign客户端而言不会产生效果。
 * 因为 `@FeignClient` 注解修改的客户端在被代理时，都会创建一个新的`Feign.Logger`实例。
 * 需要额外指定这个日志的级别才可以
 */
@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel(){
        //记录所有请求和响应的明细，包括头信息、请求体、元数据
        return Logger.Level.FULL;
        /**
         *   - 这里指定的Level级别是`FULL`，Feign支持4种级别
         *     - `NONE`：不记录任何日志信息，这是默认值。
         *     - `BASIC`：仅记录请求的方法，URL以及响应状态码和执行时间
         *     - `HEADERS`：在BASIC的基础上，额外记录了请求和响应的头信息
         *     - `FULL`：记录所有请求和响应的明细，包括头信息、请求体、元数据。
         */
    }
}
