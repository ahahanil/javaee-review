package tk.deriwotua.test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import tk.deriwotua.dao.UserDao;

public class SpringTest {

    @Test
    //测试scope属性
    public void test1(){
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserDao userDao1 = (UserDao) app.getBean("userDao");
        System.out.println(userDao1);
        //app.close();
    }

}
