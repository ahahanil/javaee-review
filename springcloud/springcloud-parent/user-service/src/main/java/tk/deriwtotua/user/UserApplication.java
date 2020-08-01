package tk.deriwtotua.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@MapperScan("tk.deriwotua.user.mapper")  //不启用扫描包就需要在每个mapper上添加@Mapper注解
@EnableDiscoveryClient // 开启Eureka客户端发现功能 加载配置文件后工具配置自动注册到 eureka注册中心
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
