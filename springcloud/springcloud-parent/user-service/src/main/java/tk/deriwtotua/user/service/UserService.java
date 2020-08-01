package tk.deriwtotua.user.service;

import org.springframework.stereotype.Service;
import tk.deriwtotua.user.pojo.User;

import java.util.Date;

@Service
public class UserService {

    /*@Autowired
    private UserMapper userMapper;

    /**
     * 根据主键查询用户
     * @param id 用户id
     * @return 用户
     */
    public User queryById(Long id){

        //return userMapper.selectByPrimaryKey(id);

        //模仿上面数据库查询
        User user = new User();
        user.setId(Long.valueOf(id));
        user.setUserName("userName");
        user.setPassword("password");
        user.setName("name");
        user.setAge(18);
        user.setSex(2);
        user.setBirthday(new Date());
        user.setNote("note");
        user.setCreated(new Date());
        user.setUpdated(new Date());
        return user;
    }
}
