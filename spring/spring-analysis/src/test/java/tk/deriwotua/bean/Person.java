package tk.deriwotua.bean;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * person
 */
@Scope("singleton")
@Lazy
public class Person implements InitializingBean, BeanNameAware, BeanClassLoaderAware {

    private String name;

    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public void initMethod(){
        System.out.println("Enter Person#initMethod()");
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("Enter Person#setBeanClassLoader()");
    }

    public void setBeanName(String s) {
        System.out.println("Enter Person#setBeanName()");
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("Enter Person#afterPropertiesSet()");
    }
}
