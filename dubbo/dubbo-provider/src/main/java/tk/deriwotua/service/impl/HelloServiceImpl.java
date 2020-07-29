package tk.deriwotua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import tk.deriwotua.service.HelloService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 发布服务必须使用Dubbo提供的Service注解
 */
@Service(interfaceClass = HelloService.class,protocol = "dubbo")
@Transactional
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "8083 hello " + name;
    }
}
