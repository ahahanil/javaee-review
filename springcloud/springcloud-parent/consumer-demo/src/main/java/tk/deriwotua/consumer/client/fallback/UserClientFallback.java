package tk.deriwotua.consumer.client.fallback;

import org.springframework.stereotype.Component;
import tk.deriwotua.consumer.client.UserClient;
import tk.deriwotua.consumer.pojo.User;

/**
 * 开启Feign的熔断功能
 *  首先需要配置中开启熔断功能
 *      feign.hystrix.enabled=true
 *  然后需要实现 标注@FeignClient的代理接口 作为熔断后降级的`fallback`的处理类
 *      比如：tk.deriwotua.consumer.client.UserClient
 *  最后在 标注@FeignClient的代理接口 注解中指定熔断后降级的`fallback`的处理类
 *      比如：@FeignClient(value = "user-service", fallback = UserClientFallback.class)
 */
@Component
public class UserClientFallback implements UserClient {
    @Override
    public User queryById(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("用户异常");
        return user;
    }
}
