package tk.deriwtotua.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.deriwtotua.user.pojo.User;
import tk.deriwtotua.user.service.UserService;

@RestController
@RequestMapping("/user")
//自动刷新配置 与配置中心配置同步
@RefreshScope
public class UserController {

    @Autowired
    private UserService userService;

    @Value("test.name")
    private String name;

    @GetMapping("/{id}")
    public User queryById(@PathVariable Long id){
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        System.out.println("配置中心参数 test.name: " + name);
        return userService.queryById(id);
    }
}
