package tk.deriwotua.dao.dao;


import tk.deriwotua.dao.domain.User;

import java.io.IOException;
import java.util.List;

public interface UserMapper {

    public List<User> findAll() throws IOException;

    public User findById(int id);

}
