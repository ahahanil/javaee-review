package tk.deriwotua.consumer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tk.deriwotua.consumer.client.fallback.UserClientFallback;
import tk.deriwotua.consumer.config.FeignConfig;
import tk.deriwotua.consumer.pojo.User;

/**
 * 声明当前类是一个Feign客户端，指定服务名为user-service
 *      feign内部已实现 ribbon 负载均衡不需要再额外实现负载均衡
 *          使用时直接配置 ribbon 相关配置项即可
 *      feign内部也实现了 hystrix 使用上需要编写熔断时服务降级fallback实现类并feign客户端注解声明该实现类 然后就可以开启feign熔断
 *      需要注意的时feign客户端日志功能单单配置中 `logging.level.xx=debug` 来设置日志级别是不够的
 *          需要额外定义feign客户端代理后Feign.Logger实例类的日志级别 然后注解中使用 configuration 声明feign配置类
 *  value   声明代理服务名
 *  fallback    声明熔断后的服务降级实现类
 *  configuration   声明feign客户端配置类
 */
@FeignClient(value = "user-service", fallback = UserClientFallback.class, configuration = FeignConfig.class)
public interface UserClient {

    //http://user-service/user/123
    @GetMapping("/user/{id}")
    User queryById(@PathVariable Long id);
}
