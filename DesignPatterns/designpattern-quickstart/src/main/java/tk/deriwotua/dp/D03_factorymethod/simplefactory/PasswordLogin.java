package tk.deriwotua.dp.D03_factorymethod.simplefactory;

/**
 * 口令认证
 */
public class PasswordLogin implements Login {

    @Override
    public boolean verify(String name, String password) {
        /**
         * 业务逻辑
         */
        return true;
    }

}