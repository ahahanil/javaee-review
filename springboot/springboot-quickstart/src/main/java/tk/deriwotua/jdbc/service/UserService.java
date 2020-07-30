package tk.deriwotua.jdbc.service;

import tk.deriwotua.pojo.User;

public interface UserService {
     User queryById(Long id);

    void saveUser(User user);
}
