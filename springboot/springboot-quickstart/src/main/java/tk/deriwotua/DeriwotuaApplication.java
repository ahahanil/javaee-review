package tk.deriwotua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// mybatis官方starter提供的MapperSan
//@org.mybatis.spring.annotation.MapperScan("tk.deriwotua.mybatis")
// 如果引入github通用mapper就需要使用通用mapper实现的MapperScan
@tk.mybatis.spring.annotation.MapperScan("tk.deriwotua.mybatis")
public class DeriwotuaApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeriwotuaApplication.class, args);
    }
}
