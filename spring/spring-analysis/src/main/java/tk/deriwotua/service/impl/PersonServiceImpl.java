package tk.deriwotua.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.deriwotua.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

    /**
     * 创建一个新的事务，并暂停当前事务（如果存在）
     * Spring默认情况下会对(RuntimeException)及其子类来进行回滚,在Exception及其子类的时候则不会进行回滚操作
     * @Transactional 注解应该只被应用到public方法上,这是由Spring AOP的本质决定的
     * @Transactional 注解要生效的话,需配置 @EnableTransactionManagement
     * 不过如果是使用SpringBoot的话,就可以不需要了
     * TransactionAutoConfiguration自动配置类里已经带有 @EnableTransactionManagement 注解
     *
     * TransactionAutoConfiguration自动配置类定义了很多与事务处理相关的bean,
     * 其中与@Transactional注解息息相关的是TransactionInterceptor这个类
     *
     * 每个带有@Transactional注解的方法都会创建一个切面,
     * 所有的事务处理逻辑就是由这个切面完成的,这个切面的具体实现就是TransactionInterceptor类
     *
     * TransactionInterceptor是个单例对象,所有带有@Transactional注解的方法都会经由此对象代理
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void getPerson() {

    }
}
