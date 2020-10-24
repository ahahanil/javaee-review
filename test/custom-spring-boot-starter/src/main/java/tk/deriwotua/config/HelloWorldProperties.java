package tk.deriwotua.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
// 自动获取配置文件中前缀为hello.world的属性，把值传入对象参数
@ConfigurationProperties(prefix = "hello.world")
public class HelloWorldProperties {

    public static final String DEFAULT_WORDS = "world";

    private String words = DEFAULT_WORDS;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

}
