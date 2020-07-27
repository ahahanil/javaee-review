package tk.deriwotua.service.impl;

import com.itheima.dao.UserDao;
import tk.deriwotua.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao;
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void save() {
        userDao.save();
    }
}
