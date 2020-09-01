package tk.deriwotua.dp.D03_factorymethod.simplefactory;

/**
 * 简单工厂
 * 简单工厂模式是类的创建模式，又叫做静态工厂方法（Static Factory Method）模式。
 * 简单工厂模式是由一个工厂对象决定创建出哪一种产品类的实例。
 *
 *  @see java.text.DateFormat 类作为简单工厂模式的典型例子
 *
 * 假如应用系统需要支持多种登录方式如：口令认证、域认证（口令认证通常是去数据库中验证用户，而域认证则是需要到微软的域中验证用户）。
 * 那么自然的做法就是建立一个各种登录方式都适用的接口
 *
 * 可以设想一下真实的场景，如果把Main当做一个servlet的话，
 * 当客户端发起登录请求——>
 *      请求交给服务端的Servlet——>
 *          Servlet根据客户端传递的loginType调用工厂类LoginManager的factory()方法——>
 *              factory()方法根据参数loginType创建相应的登录验证类(DomainLogin或PasswordLogin)并返回——>
 *                  登录验证类调用方法verify()验证用户名密码是否正确
 *
 * 简单工厂模式的优点
 *      模式的核心是工厂类。这个类含有必要的逻辑判断，可以决定在什么时候创建哪一个登录验证类的实例，而调用者则可以免除直接创建对象的责任。
 *      简单工厂模式通过这种做法实现了对责任的分割，当系统引入新的登录方式的时候无需修改调用者。
 * 简单工厂模式的缺点
 *      这个工厂类集中了所有的创建逻辑，当有复杂的多层次等级结构时，所有的业务逻辑都在这个工厂类中实现。
 *      什么时候它不能工作了，整个系统都会受到影响。
 */
public class Main {
    public static void main(String[] args) {
        String loginType = "password";
        String name = "name";
        String password = "password";
        Login login = LoginManager.factory(loginType);
        boolean bool = login.verify(name, password);
        if (bool) {
            /**
             * 业务逻辑
             */
        } else {
            /**
             * 业务逻辑
             */
        }
    }

    /**
     * 假如不使用简单工厂模式则验证登录Servlet代码如下
     *  假设test为一个Servlet，变量loginType、name、password表示从客户端传递过来的参数
     */
    public void test(){
        String loginType = "password";
        String name = "name";
        String password = "password";
        //处理口令认证
        if(loginType.equals("password")){
            PasswordLogin passwordLogin = new PasswordLogin();
            boolean bool = passwordLogin.verify(name, password);
            if (bool) {
                /**
                 * 业务逻辑
                 */
            } else {
                /**
                 * 业务逻辑
                 */
            }
        }
        //处理域认证
        else if(loginType.equals("passcode")){
            DomainLogin domainLogin = new DomainLogin();
            boolean bool = domainLogin.verify(name, password);
            if (bool) {
                /**
                 * 业务逻辑
                 */
            } else {
                /**
                 * 业务逻辑
                 */
            }
        }else{
            /**
             * 业务逻辑
             */
        }
    }
}
