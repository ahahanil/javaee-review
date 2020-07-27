package tk.deriwotua.service;

import tk.deriwotua.domain.User;

import java.util.List;

public interface UserService {
    List<User> list();

    void save(User user, Long[] roleIds);

    void del(Long userId);
}
