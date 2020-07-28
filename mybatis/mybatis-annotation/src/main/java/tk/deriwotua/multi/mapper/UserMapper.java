package tk.deriwotua.multi.mapper;

import tk.deriwotua.multi.domain.User;

import java.util.List;

public interface UserMapper {

    public List<User> findAll();

    public List<User> findUserAndRoleAll();

}
