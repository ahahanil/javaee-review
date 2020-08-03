package tk.deriwotua.es.last.utils;

import org.apache.commons.collections.map.HashedMap;
import tk.deriwotua.es.last.dto.BaseQueryDto;
import tk.deriwotua.es.last.dto.ElkQueryParamDto;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class ElkUtils {

    public static Map<String, String> extMap2Param(Map<String, String> extMap) {
        Map<String, String> param = new HashedMap();
        if (null == extMap) return param;

        for (Map.Entry<String, String> entry : extMap.entrySet()) {
            param.put("extMap." + entry.getKey(), entry.getValue());
        }
        return param;
    }

    public static Map<String, String> bean2Map(ElkQueryParamDto obj) {
        if (obj == null) {
            return new HashedMap();
        }
        Map<String, String> map = new HashedMap();
        Set<String> parentFields = OBeanUtils.objectToMap3(new BaseQueryDto()).keySet();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性 RealIndex
                if (!key.equals("class")
                        && !key.equals("extMap")
                        && !key.equals("realIndex")
                        && !parentFields.contains(key)
                        && !key.equals("operTimeFrom")
                        && !key.equals("operTimeTo")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object o = getter != null ? getter.invoke(obj) : null;
                    if (null != o) {
                        map.put(key, String.valueOf(o));
                    }
                }
            }
        } catch (Exception e) {
        }
        return map;
    }

    /**
     * 索引(库)
     * @return
     */
    public static String getIndex(String appId, String propertiesEnv){return new StringBuffer(appId).append("_").append(propertiesEnv).append("_").append("index").toString().toLowerCase();}

    /**
     * 类型(表)
     * @return
     */
    public static String getType(String appId, String propertiesEnv){return new StringBuffer(appId).append("_").append(propertiesEnv).append("_").append("type").toString().toLowerCase();}

}