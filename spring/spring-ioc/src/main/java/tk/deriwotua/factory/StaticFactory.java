package tk.deriwotua.factory;

import tk.deriwotua.dao.UserDao;
import tk.deriwotua.dao.impl.UserDaoImpl;

public class StaticFactory {

    public static UserDao getUserDao(){
        return new UserDaoImpl();
    }

}
