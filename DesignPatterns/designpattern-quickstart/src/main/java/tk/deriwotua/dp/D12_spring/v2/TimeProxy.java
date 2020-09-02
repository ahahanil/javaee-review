package tk.deriwotua.dp.D12_spring.v2;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class TimeProxy {

    @Before("execution (void tk.deriwotua.dp.D12_spring.v2.Tank.move())")
    public void before() {
        System.out.println("method start.." + System.currentTimeMillis());
    }

    @After("execution (void tk.deriwotua.dp.D12_spring.v2.Tank.move())")
    public void after() {
        System.out.println("method stop.." + System.currentTimeMillis());
    }

}
