package tk.deriwotua.quickstart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author deriwotua
 * @Date 18:52 7/30/2020
 */
@RestController
@RequestMapping("/helloworld")
public class HelloWorldController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello World!!";
    }

}
