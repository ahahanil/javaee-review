package tk.deriwotua.rpc.client;

/**
 * 服务调用方的接口必须跟服务提供方的接口保持一致（包路径可以不一致）
 */
public interface HelloNetty {
    String hello();
}
