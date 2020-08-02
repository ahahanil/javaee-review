package tk.deriwotua.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义局部过滤器
 *      自定义过滤器的命名应该为 ***GatewayFilterFactory
 *          ***     即自定义过滤器的名称 在配置 spring.cloud.routes.filters 配置时需要用到
 */
@Component
public class MyParamGatewayFilterFactory extends AbstractGatewayFilterFactory<MyParamGatewayFilterFactory.Config> {

    static final String PARAM_NAME = "param";

    public MyParamGatewayFilterFactory() {
        super(Config.class);
    }

    public List<String> shortcutFieldOrder() {
        return Arrays.asList(PARAM_NAME);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            /**
             * 配置文件中配置自定义过滤器 tk.deriwotua.gateway.filter.MyParamGatewayFilterFactory 值为 name
             *  spring.cloud.gateway.routes.filters[{MyParam=name}]
             *  访问时 http://localhost:10010/api/user/8?name=deriwotua   config.param ==> name
             */
            //获取请求参数中param对应的参数名 的参数值
            ServerHttpRequest request = exchange.getRequest();
            // 访问 http://localhost:10010/api/user/8?name2=deriwotua     name2 与 config.param值name 不匹配 条件不满足
            if(request.getQueryParams().containsKey(config.param)){
                // 访问时 http://localhost:10010/api/user/8?name=deriwotua 时参数中包含配置文件中配置自定义过滤器值 name
                // 满足这里的 if 条件
                request.getQueryParams().get(config.param).
                        forEach(value -> System.out.printf("------------局部过滤器--------%s = %s------", config.param, value));
            }
            return chain.filter(exchange);
        };
    }

    /**
     *  配置文件中对应配置对象
     *      配置文件中配置自定义过滤器 tk.deriwotua.gateway.filter.MyParamGatewayFilterFactory 值为 name
     *          spring.cloud.gateway.routes.filters[{MyParam=name}]
     */
    public static class Config{
        /**
         * 当配置为 spring.cloud.gateway.routes.filters[{MyParam=name}]
         *      这里的 param 值就为 name
         */
        //对应在配置过滤器的时候指定的MyParam参数名
        private String param;

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }
    }
}
