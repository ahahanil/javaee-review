package tk.deriwotua.cofig;

import org.springframework.context.annotation.*;

//标志该类是Spring的核心配置类
@Configuration
//<context:component-scan base-package="tk.deriwotua"/>
@ComponentScan("tk.deriwotua")
//<import resource=""/>
@Import({DataSourceConfiguration.class})
public class SpringCofiguration {

}
