package tk.deriwotua.mybatis;

/**
 * 没有配置mapper接口扫描包，因此需要给每一个Mapper接口添加 `@Mapper` 注解，才能被识别
 * 也可以不加注解，而是在启动类上添加扫描包注解 @MapperScan("tk.deriwotua.mybatis")
 */
/*@Mapper
public interface UserMapper {
}*/

import tk.deriwotua.pojo.User;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
}
