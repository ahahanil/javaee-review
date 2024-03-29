package tk.deriwotua.dao.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import tk.deriwotua.service.UserService;

public class UserController {

    public static void main(String[] args) {
        //ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApplicationContext app = new FileSystemXmlApplicationContext("C:\\ProgramCode\\Git\\Review\\spring\\spring-ioc\\src\\main\\resources\\applicationContext.xml");
        //UserService userService = (UserService) app.getBean("userService");
        UserService userService = app.getBean(UserService.class);
        userService.save();
    }

}
