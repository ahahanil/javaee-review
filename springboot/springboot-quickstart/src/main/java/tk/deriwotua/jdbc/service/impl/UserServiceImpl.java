package tk.deriwotua.jdbc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.deriwotua.jdbc.service.UserService;
import tk.deriwotua.mybatis.UserMapper;
import tk.deriwotua.pojo.User;

@Service
public class UserServiceImpl implements UserService {
    /*@Override
    public User queryById(Long id) {
        //根据id查询
        return new User();
    }

    @Override
    @Transactional
    public void saveUser(User user){
        System.out.println("新增用户...");
    }*/

    @Autowired
    private UserMapper userMapper;

    public User queryById(Long id){
        //根据id查询
        return userMapper.selectByPrimaryKey(id);
    }
    @Transactional
    public void saveUser(User user){
        System.out.println("新增用户...");
        userMapper.insertSelective(user);
    }
}
