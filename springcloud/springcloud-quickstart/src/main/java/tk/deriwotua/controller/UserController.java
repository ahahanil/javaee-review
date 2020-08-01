package tk.deriwotua.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.deriwotua.pojo.User;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/{id}")
    public String getUser(@PathVariable String id){
        User user = new User();
        user.setId(Long.valueOf(id));
        user.setUserName("userName");
        user.setPassword("password");
        user.setName("name");
        user.setAge(18);
        user.setSex(2);
        user.setBirthday(new Date());
        user.setNote("note");
        user.setCreated(new Date());
        user.setUpdated(new Date());

        return JSONObject.toJSONString(user);
    }

}
