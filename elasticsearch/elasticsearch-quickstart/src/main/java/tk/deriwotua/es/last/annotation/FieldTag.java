package tk.deriwotua.es.last.annotation;

import tk.deriwotua.es.last.enums.ElasticSearchIndexType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ElasticSearch 属性参数
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldTag {
    /**
     * 分析器
     */
    String analyzer() default "";

    /**
     * 映射类型
     */
    String type();

    /**
     * 索引类型
     */
    ElasticSearchIndexType index() default ElasticSearchIndexType.analyzed;

    boolean fieldData() default false;
}
