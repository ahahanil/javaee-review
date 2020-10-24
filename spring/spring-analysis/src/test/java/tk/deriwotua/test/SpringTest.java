package tk.deriwotua.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import tk.deriwotua.bean.Person;

/**
 * test
 */
public class SpringTest {

    /**
     * 测试Spring容器产生person对象
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        /**
         * ClassPathXmlApplicationContext用于加载CLASSPATH下的Spring配置文件
         */
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 这里就已经可以获取到Bean的实例了，那么必然第一行就已经完成了对所有Bean实例的加载
        // 因此可以通过ClassPathXmlApplicationContext作为入口
        Person person = app.getBean(Person.class);
        System.out.println(person.toString());
    }

}
