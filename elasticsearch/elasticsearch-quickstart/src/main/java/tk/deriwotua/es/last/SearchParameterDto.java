package tk.deriwotua.es.last;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.StringUtils;
import tk.deriwotua.es.last.utils.ElasticUtil;

import java.util.Map;

/**
 * Search Parameter
 *
 * @param <T>
 * @author Cookie
 */
public class SearchParameterDto<T> {
    private String[] indexes;
    private String[] types;
    private Integer size;
    private Integer from;
    private Map<String, String> mustParams;
    private Map<String, Boolean> sorts;
    private Class<T> clazz;

    /**
     * 设置了这个参数之后，所有mustParams将会失效
     * 在这个参数里面可以设置查询，range等等
     */
    private String queryString;


    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public StringBuilder getPathString(String restPath) {
        return ElasticUtil.buildPath(indexes, types, restPath);
    }

    protected ObjectNode baseBuild() {
        ObjectNode req = JsonNodeFactory.instance.objectNode();

        if (size != null) {
            req.put("size", size);
        }
        if (from != null) {
            req.put("from", from);
        }
        if (sorts != null && !sorts.isEmpty()) {
            ArrayNode orderBy = req.putArray("sort");
            for (String propertyName : sorts.keySet()) {
                orderBy.addObject().putObject(propertyName).put("order", sorts.get(propertyName) ? "asc" : "desc");
            }
        }
        return req;
    }

    public ObjectNode buildRequestNode() {
        ObjectNode req = baseBuild();
        if (queryString != null && !StringUtils.isEmpty(queryString)) {
            req.putObject("query").putObject("query_string").put("query", queryString);
        } else {
            if (mustParams != null && !mustParams.isEmpty()) {
                ArrayNode must = req.putObject("query").putObject("bool").putArray("must");
                for (String key : mustParams.keySet()) {
                    must.addObject().putObject("match_phrase").put(key, mustParams.get(key));
                }
            }
        }
        return req;
    }

    public String[] getIndexes() {
        return indexes;
    }

    public void setIndexes(String[] indexes) {
        this.indexes = indexes;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Map<String, String> getMustParams() {
        return mustParams;
    }

    public void setMustParams(Map<String, String> mustParams) {
        this.mustParams = mustParams;
    }

    public Map<String, Boolean> getSorts() {
        return sorts;
    }

    public void setSorts(Map<String, Boolean> sorts) {
        this.sorts = sorts;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
}