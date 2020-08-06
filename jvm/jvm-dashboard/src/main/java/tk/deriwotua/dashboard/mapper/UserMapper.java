package tk.deriwotua.dashboard.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    List<Map<String, Object>> queryUserCityGroupByCity(@Param("start") String var1, @Param("end") String var2);
}
