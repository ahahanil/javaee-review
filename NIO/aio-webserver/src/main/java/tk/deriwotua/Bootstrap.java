package tk.deriwotua;

import tk.deriwotua.connector.Connector;

/**
 * 启动服务器
 */
public final class Bootstrap {

    public static void main(String[] args) {
//        ConnectorBIOSocket connector = new ConnectorBIOSocket();
        Connector connector = new Connector();
        connector.start();
    }
}