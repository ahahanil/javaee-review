package tk.deriwotua.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import tk.deriwotua.service.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class HelloController {
    /**
     * 使用的是Dubbo提供的@Reference注解
     */
    @Reference
    private HelloService helloService;

    @RequestMapping("/sayHello")
    @ResponseBody
    public String sayHello(String name){
        return helloService.sayHello(name);
    }
}
