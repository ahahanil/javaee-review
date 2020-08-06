package tk.deriwotua.dashboard.mapper;

import org.apache.ibatis.annotations.Param;

public interface DAUMapper {
    Integer queryCountByDate(@Param("start") String var1, @Param("end") String var2);
}