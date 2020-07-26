package tk.deriwotua.service.impl;

import tk.deriwotua.dao.UserDao;
import tk.deriwotua.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserServiceImpl() {
    }

    /*public void setUserDao(UserDao userDao) {
                this.userDao = userDao;
            }*/
    public void save() {
        userDao.save();
    }
}
