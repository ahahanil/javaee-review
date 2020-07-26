package tk.deriwotua.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import tk.deriwotua.dao.UserDao;

/**
 * @Author deriwotua
 * @Date 22:00 7/23/2020
 */
public class UserDemo {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserDao userDao = (UserDao) applicationContext.getBean("userDao");

        userDao.getUser();
    }

}
