package tk.deriwotua.es.last.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Locale.ENGLISH;

public class OBeanUtils {

    private OBeanUtils() {
    }

    public final static Pattern PATTERN_SERIAL = Pattern.compile("[A-Za-z]{0,10}\\d+[-]*");

    public final static Pattern PATTERN_NUMBER = Pattern.compile("\\d+");

    private final static Logger logger = LoggerFactory.getLogger(OBeanUtils.class);

    private static ObjectMapper mapper = new ObjectMapper();

    private final static String STR_CLASS = "class";
    private final static String STR_EXTMAP = "extMap";

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        //mapper.setVisibilityChecker(mapper.getVisibilityChecker().with(JsonAutoDetect.Visibility.NONE));

    }

    /**
     * 对象转换
     *
     * @param srcObj
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T convertObj(Object srcObj, T t) {
        BeanUtils.copyProperties(srcObj, t);
        return t;
    }

    /**
     * 对象转换
     *
     * @param srcObj
     * @param t
     * @param ignoreProperties
     * @param <T>
     * @return
     */
    public static <T> T convertObj(Object srcObj, T t, String... ignoreProperties) {
        BeanUtils.copyProperties(srcObj, t, ignoreProperties);
        return t;
    }


    public static <T> T convertObj(Object srcObj, Class<T> tClass) {
        Object o = null;
        try {
            if (srcObj == null) {
                return null;
            }
            o = tClass.newInstance();
            convertObj(srcObj, o);

        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        return (T) o;
    }

    public static <T> T convertSerializeObj(Object srcObj, Class<T> tClass) {
        if (srcObj.getClass().equals(String.class)) {
            return SerializeUtils.fromJson(srcObj.toString(), tClass);
        }
        String json = SerializeUtils.toJson(srcObj);
        return SerializeUtils.fromJson(json, tClass);
    }

    public static <T> List<T> convertSerializeList(List list, Class<T> tClass) throws IOException {
        JavaType javaType1 = OBeanUtils.getJsonMapper().getTypeFactory().constructParametricType(List.class, tClass);
        String json = SerializeUtils.toJson(list);
        List<T> dest = null;
        dest = getJsonMapper().readValue(json, javaType1);
        return dest;

    }

    public static List<String> convertString(String input) {
        if (input == null) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<String>();
        Matcher m = PATTERN_SERIAL.matcher(input);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }


    /**
     * 序列号生产格式
     *
     * @param size
     * @param prefix
     * @return
     */
    public static String SNFormat(int size, String prefix) {
        String s = "%0" + size;
        return prefix + s + "d";
    }

    /**
     * 返回MD5字符串
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String toMD5String(String str) throws Exception {
        return EncryptUtils.encryptMD5(str);
    }

    /**
     * map转对象
     *
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null) {
            return null;
        }
        Object obj = beanClass.newInstance();

        org.apache.commons.beanutils.BeanUtils.populate(obj, map);

        return obj;
    }

    /**
     * 对象转Map
     *
     * @param obj
     * @return
     */
    public static Map<Object, Object> objectToMap(Object obj) {
        if (obj == null) {
            return new HashedMap();
        }
        if (obj instanceof Map) {
            return (Map) obj;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }

    public static Map<Object, Object> objectToMap2(Object obj) {
        if (obj instanceof Map) {
            return (Map) obj;
        }
        Map<Object, Object> map = objectToMap(obj);
        Map<Object, Object> result = new HashMap<Object, Object>();
        result.putAll(map);
        return result;
    }

    public static Map<String, Object> objectToMap3(Object obj) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (null == obj) {
            return result;
        }

        if (obj instanceof Map) {
            return (Map) obj;
        }
        Map<Object, Object> map = objectToMap(obj);
        Set keys = map.keySet();
        for (Object key : keys) {
            if (map.get(key) != null && !key.equals(STR_CLASS)) {
                result.put(key.toString(), map.get(key));
            }
        }
        return result;
    }

    public static Map<String, Object> objectToMapDateToString(Object obj) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
        Map<String, Object> result = new LinkedHashMap<>();
        if (null == obj) {
            return result;
        }

        if (obj instanceof Map) {
            return (Map) obj;
        }
        Map<Object, Object> map = objectToMap(obj);
        Set keys = map.keySet();
        for (Object key : keys) {
            if (map.get(key) != null && !key.equals(STR_CLASS)) {
                Object o = map.get(key);
                if (o instanceof Date) {
                    result.put(key.toString(), format.format(o));
                } else {
                    result.put(key.toString(), map.get(key));
                }

            }
        }
        return result;
    }

    /**
     * @param obj
     * @param isNull true  输出null值
     * @return
     */
    public static Map<String, Object> objectToMap3(Object obj, boolean isNull) {
        if (obj instanceof Map) {
            return (Map) obj;
        }
        Map<Object, Object> map = objectToMap(obj);

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        Set keys = map.keySet();
        for (Object key : keys) {
            if (isNull) {
                if (!key.equals(STR_CLASS)) {
                    result.put(key.toString(), map.get(key));
                }
            } else {
                if (map.get(key) != null && !key.equals(STR_CLASS)) {
                    result.put(key.toString(), map.get(key));
                }
            }

        }
        return result;
    }


    /**
     * Collection 转 List<Map>
     *
     * @param obj
     * @return
     */
    public static List<Map<String, Object>> listObjectToMap3(Collection obj) {
        if (obj == null) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object o1 : obj) {
            result.add(objectToMap3(o1));
        }

        return result;
    }


    public static MultiValueMap<String, Object> objectToMultValueMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<Object, Object> m = new org.apache.commons.beanutils.BeanMap(obj);
        Set<Object> set = m.keySet();
        MultiValueMap mvm = new LinkedMultiValueMap<String, Object>();
        for (Object o : set) {
            if (!o.equals(STR_CLASS)) {
                mvm.add(o.toString(), m.get(o));
            }
        }
        return mvm;
    }

    public static String capitalize(String methodName, Type type) {
        int index = methodName.indexOf("get");
        String name = methodName.substring(index, methodName.length());
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }

    public static String capitalizeMethod(String methodName, Type type) {
        int index = methodName.indexOf("get");
        String name = methodName.substring(index, methodName.length());
        String setMethodName = name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
        return setMethodName;
    }

    /**
     * 字符串递增
     *
     * @param str
     * @return
     */

    public static String addNumber(String str) {
        Matcher m = PATTERN_NUMBER.matcher(str);
        String number = "";
        if (m.find()) {
            number = m.group();
        }
        String prefix = str.substring(0, str.length() - number.length());
        int n = Integer.parseInt(number);
        n++;
        String format = OBeanUtils.SNFormat(number.length(), prefix);
        return String.format(format, n);
    }

    /**
     * 获取对象的值
     *
     * @param obj
     * @param propertyName name,number,cateogry.name,storeState.name
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object getRelValue(Object obj, String propertyName) throws InvocationTargetException, IllegalAccessException {
        Object result = null;
        Object destObj = obj;
        String[] n = propertyName.split("\\.");
        PropertyDescriptor pd = null;
        for (String pname : n) {
            pd = BeanUtils.getPropertyDescriptor(obj.getClass(), pname);
            destObj = pd.getReadMethod().invoke(destObj);
            result = destObj;
        }
        return result;
    }

    public static ObjectMapper getJsonMapper() {
        return mapper;
    }


/*

    public static <T>Pagination<T> convertPagination(com.taimeitech.framework.common.dto.Pagination pagination, Class<T> tClass){
        com.taimeitech.framework.common.dto.Pagination<T> dest=new com.taimeitech.framework.common.dto.Pagination<T>();
        JavaType javaType1 = OBeanUtils.getJsonMapper().getTypeFactory().constructParametricType(com.taimeitech.framework.common.dto.Pagination.class, tClass);
        String json= SerializeUtils.toJson(pagination);
        try {
            dest=OBeanUtils.getJsonMapper().readValue(json,javaType1);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        return dest;
    }
*/


}
