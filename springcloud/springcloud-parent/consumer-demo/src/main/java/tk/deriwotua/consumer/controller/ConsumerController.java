package tk.deriwotua.consumer.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/consumer")
@Slf4j
//类上声明默认的降级逻辑为defaultFallback()方法
// defaultFallback()方法返回类型要与处理失败的方法的返回类型
@DefaultProperties(defaultFallback = "defaultFallback")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    /*@GetMapping("{id}")
    public User queryById(@PathVariable Long id){
        String url = "http://localhost:9091/user/" + id;
        return restTemplate.getForObject(url, User.class);
    }*/

    /*@Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("{id}")
    public User queryById(@PathVariable Long id){
        //String url = "http://localhost:9091/user/" + id;
        *//**
         * 通过org.springframework.cloud.client.discovery.DiscoveryClient#getInstances(java.lang.String)获取eureka中注册的user-service实例列表
         *//*
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("user-service");
        ServiceInstance serviceInstance = serviceInstanceList.get(0);
        // 动态获取实例的地址和端口
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/user/" + id;
        return restTemplate.getForObject(url, User.class);
    }*/

    /*@GetMapping("{id}")
    public User queryById(@PathVariable Long id){
        String url = "http://user-service/user/" + id;
        return restTemplate.getForObject(url, User.class);
    }*/

    @GetMapping("{id}")
    /**
     * 声明一个降级逻辑的方法
     */
    @HystrixCommand(fallbackMethod = "queryByIdFallback")
    public String queryById(@PathVariable Long id){
        String url = "http://user-service/user/" + id;
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * 熔断的降级逻辑方法必须跟正常逻辑方法保证：相同的参数列表和返回值声明
     * @param id
     * @return
     */
    public String queryByIdFallback(Long id){
        log.error("查询用户信息失败。id：{}", id);
        return "对不起，网络太拥挤了！";
    }

    @GetMapping("/2/{id}")
    /**
     * 不指定时使用类上声明的默认降级逻辑的方法
     */
    @HystrixCommand
    public String queryById2(@PathVariable Long id){
        String url = "http://user-service/user/" + id;
        return restTemplate.getForObject(url, String.class);
    }

    public String defaultFallback(){
        return "默认提示：对不起，网络太拥挤了！";
    }
}
