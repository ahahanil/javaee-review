package tk.deriwotua.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.deriwotua.jdbc.service.UserService;
import tk.deriwotua.pojo.User;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void queryById() {
        User user = userService.queryById(1L);
        System.out.println("user = " + user);
    }

    @Test
    public void saveUser() {
        User user = new User();
        user.setUserName("test");
        user.setName("test");
        user.setPassword("123456");
        user.setSex(1);
        user.setAge(20);
        user.setCreated(new Date());
        userService.saveUser(user);
    }
}