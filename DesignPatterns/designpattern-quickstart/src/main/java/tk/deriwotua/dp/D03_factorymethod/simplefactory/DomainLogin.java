package tk.deriwotua.dp.D03_factorymethod.simplefactory;

/**
 * 域认证
 */
public class DomainLogin implements Login {

    @Override
    public boolean verify(String name, String password) {
        /**
         * 业务逻辑
         */
        return true;
    }

}